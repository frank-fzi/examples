import numpy as np, time
from random import choice 

separator = "==========================================================="

class MDP () :
    def __init__ (self) :
        self.stateIds = range(9)
        self.actionIds = range(9)
        statesCount = len(self.stateIds)
        self.reward = np.full((statesCount, statesCount), -1, np.int)
        self.action = np.full_like(self.reward, -1, np.int)
        self.quality = np.zeros_like(self.reward, np.float)
        self.policy = np.full((statesCount), -1, np.int)
        

    def trainReward(self, targetStateId, state, subject) :
        currentStateId = state.getId(subject)
        duration = np.zeros(len(self.actionIds), np.float)

        # perform all available actions for current state
        for actionId in self.actionIds :
            startTime = time.time()
            result = state.action(actionId)
            newStateId = result.getId(subject)            

            # update reward and action matrix according to the result of the last action
            if len(result) > 0 and newStateId != None :         
                self.reward[currentStateId][newStateId] = 100 if newStateId == targetStateId else 0
                self.action[currentStateId][newStateId] = actionId
            
            # print result of last action
            endTime = time.time()   
            duration[actionId] = endTime - startTime
            print("%i: %i -(%i)-> %s [%f seconds]" % (actionId, currentStateId, actionId, newStateId, (duration[actionId])))
        return duration

    def updateQ(self, currentStateId, newStateId, actionId, alpha, gamma):
        rsa = self.reward[currentStateId, actionId]
        qsa = self.quality[currentStateId, actionId]
        new_q = qsa + alpha * (rsa + gamma * max(self.quality[newStateId, :]) - qsa)
        self.quality[currentStateId, actionId] = new_q
        # renormalize row to be between 0 and 1
        rn = self.quality[currentStateId][self.quality[currentStateId] > 0] / np.sum(self.quality[currentStateId][self.quality[currentStateId] > 0])
        self.quality[currentStateId][self.quality[currentStateId] > 0] = rn
        # print(quality)
        return self.reward[currentStateId, actionId]

    def trainQuality(self) :
        startTime = time.time()
        gamma = 0.8
        alpha = 1.
        n_episodes = 1E4
        n_states = len(self.stateIds)
        n_actions = len(self.actionIds)
        epsilon = 0.05
        random_state = np.random.RandomState(1999)
        for e in range(int(n_episodes)):
            states = list(range(n_states))
            random_state.shuffle(states)
            current_state = states[0]
            goal = False
            if e % int(n_episodes / 10.) == 0 and e > 0:
                pass
            while not goal:
                # epsilon greedy
                valid_moves = self.reward[current_state] >= 0
                if random_state.rand() < epsilon:
                    actions = np.array(list(range(n_actions)))
                    actions = actions[valid_moves == True]
                    if type(actions) is int:
                        actions = [actions]
                    random_state.shuffle(actions)
                    action = actions[0]
                    next_state = action
                else:
                    if np.sum(self.quality[current_state]) > 0:
                        action = np.argmax(self.quality[current_state])
                    else:
                        # Don't allow invalid moves at the start
                        # Just take a random move
                        actions = np.array(list(range(n_actions)))
                        actions = actions[valid_moves == True]
                        random_state.shuffle(actions)
                        action = actions[0]
                    next_state = action
                reward = self.updateQ(current_state, next_state, action,
                                alpha=alpha, gamma=gamma)
                # Goal state has reward 100
                if reward > 1:
                    goal = True
                current_state = next_state
        endTime = time.time()
        duration = endTime - startTime
        return duration

    def inferPolicy (self):
        startTime = time.time()
        targetStates = np.argmax(self.quality, axis=1)
        stateId = 0
        print(separator)
        print("Infer policy:")
        for nextStateId in targetStates :
            actionId = self.action[stateId][targetStates[stateId]]
            print("* in state %i perform action %i to reach next state %i" % (stateId, actionId, nextStateId))
            self.policy[stateId] = actionId
            stateId+=1
        endTime = time.time()
        duration = endTime - startTime
        return duration

    def trial(self, initialStateId, targetStateId, state, subject):
        return self.perform(initialStateId, targetStateId, state, subject, True)

    def solve(self, initialStateId, targetStateId, state, subject):
        return self.perform(initialStateId, targetStateId, state, subject, False)

    def perform(self, initialStateId, targetStateId, state, subject, random):
        info = "performing random actions" if random else "using MDP policy"
        print("==========================================================")
        print ("trying to reach S%i from S%i "  % (targetStateId, initialStateId) + info)
        
        log = []
        currentStateId = initialStateId
        while currentStateId != targetStateId :
            startTime = time.time()
            # perform action
            actionId = choice(self.actionIds) if random else self.policy[currentStateId]
            newState = state.action(actionId)
            newStateId = newState.getId(subject)
            logId = newStateId if newStateId != None else -1
            logEntry = [int(currentStateId), int(actionId), int(logId)]

            # prepare next iteration
            state = newState if len(newState) > 0 and newStateId != None else state            
            currentStateId = state.getId(subject)
            endTime = time.time()
            aDuration = endTime - startTime
            logEntry.append(aDuration)
            log.append(logEntry)
            print("%i: %i -(%i)-> %s [%f seconds]" % (len(log), logEntry[0], logEntry[1], newStateId, (logEntry[3])))

        # plot results
        print ("result: %i iterations from S%i to S%i" % (len(log), initialStateId, targetStateId))
        return log