from state import State

# test MDP state
state=State()
print ("read file")
state.parse("../data/states.ttl", format="turtle")

# Ausführung der Tests
print ("infering and merging states")
floats = state.action(2).action(7).action(8).action(6)
strings = state.action(4).action(7).action(8).action(6)
qudts = state.action(7).action(8).action(6)
graph = floats + strings + qudts
print ("write file")
graph.serialize("../data/result.ttl", format='turtle')
floats.print()
strings.print()
qudts.print()
print ("done")