import json, numpy,  matplotlib.pyplot as plt
from pylab import *
import seaborn as sns
import pandas as pd

# labels for plots
separator = "==========================================================="
timeLabel = "time in s"
tStateLabel = "target state"
sStateLabel = "start state"
actionLabel = "action"
iterLabel = "iterations"
typeLabel = "type"
repLabel = "repetition"

# count iterations per run
def countIterations(log, type, runs) :
    observations = []    
    for run in runs :
        iterations = len(run)
        time = 0
        for entry in run :
            time += entry[3]
        stateId = run[0][0] if iterations > 0 else log["targetStateId"]
        observation = {}
        observation[typeLabel] = type
        observation[sStateLabel] = stateId  
        observation[tStateLabel] = log["targetStateId"]        
        observation[iterLabel] = iterations    
        observation[timeLabel] = time               
        observations.append(observation)    
    return observations

# read data
file = open("../data/trials.txt", "r")
data = json.load(file)
dataSet = data[0]

# all fields of the data set incl. length of value
print(separator)
print("all fields of the data set incl. length of value")
for key, value in dataSet.items() :
    print ("* %s [%i]" % (key, len(str(value))))

# timing of reward training [state][action]
print(separator)
print("timing of reward training [state][action]")
rTrainingTimes = pd.DataFrame(data=dataSet["rTrainingTimes"])
print(rTrainingTimes)

# iterate all quality training times in data
print(separator)
print("iterate all quality training times in data")
qTrainingTimes = []
pInferTimes = []
trialTimes = []
mdpTimes = []
targets = numpy.zeros((9,1))
for log in data :
    qTrainingTimes.append(log["qTrainingTime"])
    pInferTimes.append(log["pInferTime"])

    # mdp times
    mdpTime = 0
    for mdpRun in log["mdpRuns"] :
        for state in mdpRun :
            if len(state) > 0 :
                mdpTime += state[2]
    mdpTimes.append(mdpTime)

    #trial times
    trialTime = 0
    for trialRun in log["trialRuns"] :
        for state in trialRun :
            if len(state) > 0 :
                trialTime += state[2]
    trialTimes.append(trialTime)

print("average qTrainingTime: %f" %  numpy.average(qTrainingTimes))
print("average pInferTime: %f" %  numpy.average(pInferTimes))
print("average mdpTime: %f" %  numpy.average(mdpTimes))
print("average trialTime: %f" %  numpy.average(trialTimes))

# iterate all runs in data
print(separator)
print("iterate all trial runs in data")

# get observations for runs
observations = []
for log in data :
    observations += countIterations(log, "trial", log["trialRuns"])
    observations += countIterations(log, "mdp", log["mdpRuns"])
runs = pd.DataFrame(observations)

# get reward training times
observations = []
counter = 0
rTrainingTime = 0
for log in data :
    rTrainingTimes = log["rTrainingTimes"] 
    for state in range(len(rTrainingTimes)) :
        for action in range(len(rTrainingTimes[state])) :
            observation = {}
            observation[tStateLabel] = log["targetStateId"]   
            observation[sStateLabel] = state 
            observation[actionLabel] = action
            observation[timeLabel] = rTrainingTimes[state][action]                     
            observations.append(observation)             
            rTrainingTime += rTrainingTimes[state][action] 
        counter += 1
rTrainings = pd.DataFrame(observations)
rTrainings6 = rTrainings.loc[rTrainings[tStateLabel] == 6]
rTrainings7 = rTrainings.loc[rTrainings[tStateLabel] == 7]
rTrainings8 = rTrainings.loc[rTrainings[tStateLabel] == 8]

# repetition time
observations = []
rTrainingTime = rTrainingTime/counter
qTrainingTime = numpy.average(qTrainingTimes)
pInferTime = numpy.average(pInferTimes)
trainingTime = rTrainingTime + qTrainingTime + pInferTime
mdpTime = numpy.average(mdpTimes)
trialTime= numpy.average(trialTimes)

for repetition in range(3) :

    # mdp time
    observation = {}
    observation[repLabel] = str(repetition)
    observation[timeLabel] = trainingTime + repetition * mdpTime
    observation[typeLabel] = "mdp"
    observations.append(observation)

    # trial time
    observation = {}
    observation[repLabel] = str(repetition)
    observation[timeLabel] = repetition * trialTime
    observation[typeLabel] = "trial"
    observations.append(observation)

repTimes = pd.DataFrame(observations)

# print data frames for debugging
print(runs)
print(rTrainings)
print(repTimes)

# plot repetition time
sns.set(style="darkgrid")
plot = sns.lineplot(
    x=repLabel,
    y=timeLabel,
    hue=typeLabel,
    data=repTimes)
plt.show()

# plot reward training times
sns.catplot(
    x=sStateLabel,
    y=timeLabel,
    hue=actionLabel,
    col=tStateLabel,
    data=rTrainings7,
    kind="bar")
sns.set(style="darkgrid")
plt.show()

sns.catplot(
    x=sStateLabel,
    y=timeLabel,
    hue=actionLabel,
    col=tStateLabel,
    data=rTrainings8,
    kind="bar")
sns.set(style="darkgrid")
plt.show()

# plot iterations
sns.catplot(
    x=sStateLabel,
    y=iterLabel,
    hue=typeLabel,
    col=tStateLabel,
    data=runs,
    kind="bar")
sns.set(style="darkgrid")
plt.show()

# plot iteration times
sns.catplot(
    x=sStateLabel,
    y=timeLabel,
    hue=typeLabel,
    col=tStateLabel,
    data=runs,
    kind="bar")
sns.set(style="darkgrid")
plt.show()