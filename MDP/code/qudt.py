from rdflib import Graph

class Qudt(Graph) :
    __instance = None

    @staticmethod
    def instance():
        if Qudt.__instance == None:
            Qudt()
        return Qudt.__instance

    def __init__(self):
        if Qudt.__instance != None:
            raise Exception("Qudt is a singleton!")
        super(Qudt, self).__init__()
        Qudt.__instance = self
        print ("preparing qudt")
        self.parse("../data/QUDTv2.ttl", format="turtle")