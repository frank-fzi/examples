from rdflib import Graph

class Schema(Graph) :
    __instance = None

    @staticmethod
    def instance():
        if Schema.__instance == None:
            Schema()
        return Schema.__instance

    def __init__(self):
        if Schema.__instance != None:
            raise Exception("Schema is a singleton!")
        super(Schema, self).__init__()
        Schema.__instance = self
        print ("preparing schema")
        self.parse("../data/schema.ttl", format="turtle")