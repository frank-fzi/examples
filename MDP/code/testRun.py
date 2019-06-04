from state import State
from mdp import MDP
from random import random, randrange, choice
import trial
import numpy as np
from time import gmtime, strftime
separator = "==========================================================="

class TestRun():
    # train rewards and actions
    def raTraining(self, mdp, state, subjects, targetStateId) :
        print(separator)
        print ("training reward and actions for target state %i..." % targetStateId)
        rTrainingTimes = np.zeros((len(subjects), len(mdp.actionIds)), np.float)
        for subject in subjects :
            currentStateId = state.getId(subject)
            print(separator)
            print("starting from state %i:" % currentStateId)
            aTrainingTimes = mdp.trainReward(targetStateId, state, subject)
            aTrainingTime = np.sum(aTrainingTimes)
            rTrainingTimes[currentStateId] = aTrainingTimes.tolist()
            print("Training S%i to S%i took %f seconds." % (currentStateId, targetStateId, aTrainingTime))
        return rTrainingTimes

    # train quality
    def qTraining(self, mdp):
        print(separator)
        print("The following arrays are used to train quality (Q):")
        print("Reward (R)")
        print(mdp.reward)
        print("Action (A)")
        print(mdp.action)
        print(separator)
        print("training quality...")
        qTrainingTime = mdp.trainQuality()
        print("Quality (Q) [trained in %f seconds]:" % (qTrainingTime))
        print(np.multiply(mdp.quality, 100).astype("int"))
        return qTrainingTime

    # infer policy from R, A,
    def pInfer(self, mdp):
        pInferTime = mdp.inferPolicy()
        print("Policy (P) [inferred in %f seconds]:" % (pInferTime))
        print(mdp.policy)
        return pInferTime

    # execute test run with trained policy (based on R, A, Q)
    def run(self, mdp, targetStateId, state, subjects):
        trialRuns = []
        mdpRuns = []
        for subject in subjects :
            initialStateId = state.getId(subject)

            #trial and error
            trialRun = mdp.trial(initialStateId, targetStateId, state, subject)
            trialRuns.append(trialRun)    

            # solve with MDP
            mdpRun = mdp.solve(initialStateId, targetStateId, state, subject)
            mdpRuns.append(mdpRun)
        return (trialRuns, mdpRuns)