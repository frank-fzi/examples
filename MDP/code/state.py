from rdflib import Graph, URIRef
from rdflib.namespace import Namespace
from queries import Queries
from qudt import Qudt
from schema import Schema

class State(Graph) :

    # constructor for triples (construct query results)
    def __init__(self, triples={}):
        super(State, self).__init__()
        for triple in triples:
            self.add(triple)
        qudt = Namespace("http://qudt.org/schema/qudt/")
        unit = Namespace("http://qudt.org/vocab/unit/")
        ex = Namespace("http://example.org/schema#")
        self.namespace_manager.bind("qudt", qudt)
        self.namespace_manager.bind("unit", unit)
        self.namespace_manager.bind("ex", ex)
        self.namespace_manager.bind("quantity", "http://qudt.org/vocab/quantity/")
        self.namespace_manager.bind("quantitykind", "http://qudt.org/vocab/quantitykind/")        
        self.queries = Queries.instance()
        self.qudt = Qudt.instance()
        self.schema = Schema.instance()
        self.__action = []
        self.__action.append("perform a0 (does not change anything)")
        self.__action.append("perform a1 (float2string)") # quantity.unit = float -> quantity = "float unit"
        self.__action.append("perform a2 (float2qudt)")   # quantity.unit = float -> {QUDT}
        self.__action.append("perform a3 (string2float)") # quantity = "float unit" -> quantity.unit = float
        self.__action.append("perform a4 (string2qudt)")  # quantity = "float unit" -> {QUDT}
        self.__action.append("perform a5 (qudt2float)")   # {QUDT} -> quantity.unit = float
        self.__action.append("perform a6 (qudt2string)")  # {QUDT} -> quantity = "float unit"
        self.__action.append("perform a7 (qudtBase)")     # {QUDT} -> {QUDT'} (with base unit)
        self.__action.append("perform a8 (qudtSiblings)") # {QUDT} -> {QUDT'} (with siblings)
        self.targetState = 9    
        # quantity = (quantityClass, baseUnit, targetUnit)    
        self.Length = (qudt.LengthUnit, unit.M, unit.MilliM)
        self.Temperature = (qudt.TemperatureUnit, unit.K, unit.DEG_C)
        self.Mass = (qudt.MassUnit, unit.KG, unit.GRAM)

    def action(self, number, debug=False):
        if debug :
            print (self.__action[number])
        if number == 0:
            return self
        graph = self.qudt + self.schema + self
        return State(graph.query(self.queries.query[number]))

    def getId(self, subject) :
        graph = self.qudt + self.schema + self
        isString = bool(graph.query(self.queries.state["string"], initBindings={'subject': subject}))
        isFloat = bool(graph.query(self.queries.state["float"], initBindings={'subject': subject}))
        isQudt = bool(graph.query(self.queries.state["qudt"], initBindings={'subject': subject}))

        if isString :            
            baseString = graph.query(self.queries.state["string"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[1]})
            targetString = graph.query(self.queries.state["string"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[2]})
            return 6 if targetString else 3 if baseString else 0

        if isFloat :
            baseFloat = graph.query(self.queries.state["float"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[1]})
            targetFloat = graph.query(self.queries.state["float"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[2]})
            return 7 if targetFloat else 4 if baseFloat else 1

        if isQudt :
            baseQudt = graph.query(self.queries.state["qudt"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[1]})
            targetQudt = graph.query(self.queries.state["qudt"], initBindings={'subject': subject, 'quantityClass': self.Length[0], 'unit' : self.Length[2]})
            return 8 if targetQudt else 5 if baseQudt else 2

    def print(self) :
        print ("print state")
        for node in self.query(self.queries.individuals):
            print ("node: ", node[0])
            print ("state: ", self.getId(node[0]))
            
