from state import State
from mdp import MDP
from testRun import TestRun
from random import random, randrange, choice
import trial, json, numpy as np, time

# config
trainRA = True
targetStateIds = [6,7,8]
separator = "==========================================================="

# get data
print ("read file \"states.ttl\"")
state=State()
state.parse("../data/states.ttl", format="turtle")
subjects = []
for node in state.query(state.queries.individuals):
    subjects.append(node[0])

# prepare tests
mdp = MDP()
testRun = TestRun()

def executeTest(mdp, targetStateId, state, subjects):
    results = {}
    results["date"] = time.strftime("%a, %d %b %Y %H:%M:%S +0000", time.gmtime())
    results["timestamp"] = time.time()
    results["targetStateId"] = targetStateId
    results["qTrainingTime"] = testRun.qTraining(mdp)
    results["pInferTime"] = testRun.pInfer(mdp)
    results["reward"] = mdp.reward.tolist()
    results["action"] = mdp.action.tolist()
    results["quality"] = mdp.quality.tolist()
    results["policy"] = mdp.policy.tolist()
    result = testRun.run(mdp, targetStateId, state, subjects)
    results["trialRuns"] = result[0]
    results["mdpRuns"] = result[1]    
    return results

def log(results, mdp) :    
    file = open("../data/trials.txt", "a")
    characters = file.write(json.dumps(results))
    file.write(",\n")
    print("wrote %i characters to logfile" % characters)
    file.close()

# run tests forever
while True:
    print(separator)
    date = time.strftime("%a, %d %b %Y %H:%M:%S +0000", time.gmtime())
    print("start new test at %s (reward training=%s)" % (date, str(trainRA)))
    if trainRA :
        for targetStateId in targetStateIds: 
            rTrainingTimes = testRun.raTraining(mdp, state, subjects, targetStateId)
            results = executeTest(mdp, targetStateId, state, subjects)
            results["pretrained"] = False
            results["rTrainingTimes"] = rTrainingTimes.tolist()
            log(results, mdp)
    else :
        targetStateId = 6
        print ("using pretrained reward and actions for target state %i" % targetStateId)
        mdp.reward = np.array([
        [  0,   0,   0,  -1,  -1,  -1,  -1,  -1,  -1],
        [  0,   0,   0,  -1,  -1,  -1,  -1,  -1,  -1],
        [  0,   0,   0,  -1,  -1,   0,  -1,  -1,  -1],
        [ -1,  -1,  -1,   0,   0,   0,  -1,  -1,  -1],
        [ -1,  -1,  -1,   0,   0,   0,  -1,  -1,  -1],
        [ -1,  -1,  -1,   0,   0,   0,  -1,  -1,   0],
        [ -1,  -1,  -1,  -1,  -1,  -1, 100,   0,   0],
        [ -1,  -1,  -1,  -1,  -1,  -1, 100,   0,   0],
        [ -1,  -1,  -1,  -1,  -1,   0, 100,   0,   0],
        ]).astype("int")
        mdp.action = np.array([
        [ 0,  3,  4, -1, -1, -1, -1, -1, -1],
        [ 1,  0,  2, -1, -1, -1, -1, -1, -1],
        [ 6,  5,  0, -1, -1,  7, -1, -1, -1],
        [-1, -1, -1,  0,  3,  4, -1, -1, -1],
        [-1, -1, -1,  1,  0,  2, -1, -1, -1],
        [-1, -1, -1,  6,  5,  7, -1, -1,  8],
        [-1, -1, -1, -1, -1, -1,  0,  3,  4],
        [-1, -1, -1, -1, -1, -1,  1,  0,  2],
        [-1, -1, -1, -1, -1,  7,  6,  5,  0],
        ]).astype("int")
        results = executeTest(mdp, targetStateId, state, [subjects[0]])
        results["pretrained"] = True
        log(results, mdp)