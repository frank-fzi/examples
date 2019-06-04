from random import random, randrange, choice

def trial(initialStateId, targetStateId, state, subject):
    log = []
    currentStateId = initialStateId

    # run trial and error
    while currentStateId != targetStateId :
        # perform action
        actionId = randrange(0,9)
        newState = state.action(actionId)
        newStateId = newState.getId(subject)

        # log entry
        logEntry = (currentStateId, actionId, newStateId)
        log.append(logEntry)
        print("%i: %i -%i-> %s" % (len(log), logEntry[0], logEntry[1], logEntry[2]))

        # prepare next iteration
        state = newState if len(newState) > 0 and newStateId != None else state            
        currentStateId = state.getId(subject)

    # plot results
    print ("result: %i iterations from S%i to S%i" % (len(log), initialStateId, targetStateId))
    return log