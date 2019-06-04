from state import State
from random import random, randrange, choice
import numpy as np

def train(initialState, targetState, state, subject) :
    r = np.full((9,9), -1, np.int)
    a = np.full_like(r, -1, np.int)
    actions = []
    currentState = initialState
    i = 0
    while i < 9 :
        result = state.action(i)
        length = len(result)
        newState = result.getId(subject)
        valid = True if length > 0 and newState != None else False
        x = state.getId(subject)
        
        if valid :         
            y = result.getId(subject)
            r[x][y] = 100 if y == targetState else 0
            a[x][y] = i

        action = (currentState, i, newState)
        actions.append(action)
        currentState = state.getId(subject)
        print("%i: %i -%i-> %s" % (len(actions), action[0], action[1], action[2]))
        i += 1

    print("R")
    print(r)
    print("A")
    print(a)
    return actions