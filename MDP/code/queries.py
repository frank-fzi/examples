from rdflib.plugins.sparql import prepareQuery

class Queries() :
    __instance = None

    @staticmethod
    def instance():
        if Queries.__instance == None:
            Queries()
        return Queries.__instance

    def __init__(self):
        if Queries.__instance != None:
            raise Exception("Queries is a singleton!")
        Queries.__instance = self
        print ("preparing queries")
        
        self.query = []
        self.state = {}

        # query 0: neutral
        self.query.append(prepareQuery("""
        CONSTRUCT   { ?subject ?predicate ?object }
        WHERE       { ?subject ?predicate ?object }
        """))

        # query 1: quantity.unit = float -> quantity = "float unit"
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT {
            ?subject a owl:NamedIndividual .
            ?subject ?property ?value .
        }
        WHERE  {
            ?subject a owl:NamedIndividual .
            ?subject ?propertyUnit ?number .
            ?propertyUnit rdfs:subPropertyOf ex:quantity.unit
            BIND(STRAFTER(str(?propertyUnit), "#") AS ?quantityUnit) 
            BIND(STRAFTER(?quantityUnit, ".") AS ?abbr) 
            BIND(STRBEFORE(?quantityUnit, ".") AS ?quantity) 
			?unit qudt:abbreviation ?abbr .
			?unit qudt:symbol ?symbol .
            BIND(CONCAT(str(?number), " ", ?symbol) AS ?value)
            BIND(IRI(CONCAT(str(ex:), ?quantity)) AS ?property)
        }"""))

        # query 2: quantity.unit = float -> {QUDT}
        self.query.append(prepareQuery("""       
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity  ?quantityIri .
            ?quantityIri rdf:type qudt:Quantity . 
            ?quantityIri qudt:hasQuantityKind ?quantityKind .
			?quantityIri ex:rangeQuantity ?rangeQuantity .
            ?quantityIri qudt:quantityValue _:value .
            _:value rdf:type qudt:QuantityValue .
            _:value qudt:unit ?unit .
            _:value qudt:numericValue ?number .
        }
        WHERE  {
            ?subject a owl:NamedIndividual .
            ?subject ?propertyUnit ?number .
            ?propertyUnit rdfs:subPropertyOf ex:quantity.unit .
			?propertyUnit ex:rangeUnit ?unit .
			?propertyUnit ex:rangeQuantity ?rangeQuantity .
	        BIND(STRAFTER(str(?propertyUnit), "#") AS ?quantityUnit) 
            BIND(STRBEFORE(?quantityUnit, ".") AS ?quantity) 
			BIND(STRAFTER(str(?subject), "#") AS ?name)
			BIND(CONCAT(?name, ".", ?quantity) AS ?quantityName)
			BIND(IRI(CONCAT(str(ex:), ?quantityName)) AS ?quantityIri)
            OPTIONAL { ?unit qudt:hasQuantityKind ?quantityKind }
			OPTIONAL { ?unit qudt:quantityKind ?quantityKind} 
        }"""))

        # query 3: quantity = "float unit" -> quantity.unit = float
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject ?propertyUnit ?number .
        }
        WHERE {
            ?subject a owl:NamedIndividual .
            ?subject ?property ?value .
            ?property rdfs:subPropertyOf ex:quantity .
			?property ex:rangeQuantity ?quantityClass .
            BIND(xsd:decimal(STRBEFORE(?value, " ")) AS ?number)
            BIND(STRAFTER(?value, " ") AS ?symbol) 
			BIND(STRAFTER(str(?property), "#") AS ?quantity)
			?unit qudt:symbol ?symbol .
			?unit qudt:abbreviation ?abbr .
			?unit a ?quantityClass .
            BIND(CONCAT(?quantity, ".", ?abbr) AS ?quantityUnit)
            BIND(IRI(CONCAT(str(ex:), ?quantityUnit)) AS ?propertyUnit)
        }"""))

        # query 4: quantity = "float unit" -> {QUDT}
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity?quantityIri .
            ?quantityIri rdf:type qudt:Quantity . 
            ?quantityIri qudt:hasQuantityKind ?quantityKind .
			?quantityIri ex:rangeQuantity ?quantity .  
            ?quantityIri qudt:quantityValue _:value .
            _:value rdf:type qudt:QuantityValue .
            _:value qudt:unit ?unit .
            _:value qudt:numericValue ?number .
        }
        WHERE  {
            ?subject a owl:NamedIndividual .
            ?subject ?property ?value .
            ?property rdfs:subPropertyOf ex:quantity.
            ?property ex:rangeQuantity ?quantity .    
            BIND(xsd:decimal(STRBEFORE(?value, " ")) AS ?number)
            BIND(STRAFTER(?value, " ") AS ?symbol)     
            ?unit a ?quantity .
            ?unit qudt:symbol ?symbol .
			BIND(STRAFTER(str(?property), "#") AS ?quantityString) 
			BIND(STRAFTER(str(?subject), "#") AS ?name)
			BIND(CONCAT(?name, ".", ?quantityString) AS ?quantityName)
			BIND(IRI(CONCAT(str(ex:), ?quantityName)) AS ?quantityIri)
            OPTIONAL { ?unit qudt:hasQuantityKind ?quantityKind }
			OPTIONAL { ?unit qudt:quantityKind ?quantityKind} 
        }"""))

        # query 5: {QUDT} -> quantity.unit = float
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject ?propertyUnit ?number .
        }
        WHERE {
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity ?quantity .
            ?quantity a qudt:Quantity .
            ?quantity qudt:quantityValue ?value .
            ?value qudt:unit ?unit .
            ?value qudt:numericValue ?number .
            ?unit qudt:abbreviation ?abbr .
            BIND(STRAFTER(str(?quantity), "#") AS ?quantityName) 
            BIND(STRAFTER(?quantityName, ".") AS ?quantityString)
            BIND(CONCAT(?quantityString, ".", ?abbr) AS ?quantityUnit)
            BIND(IRI(CONCAT(str(ex:), ?quantityUnit)) AS ?propertyUnit)
        }"""))

        # query 6: {QUDT} -> quantity = "float unit"
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject ?property ?value .
        }     
        WHERE {
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity ?quantity .
            ?quantity qudt:quantityValue ?quantityValue .
            ?quantityValue qudt:unit ?unit .
            ?quantityValue qudt:numericValue ?number .
            ?unit qudt:symbol ?symbol .
			BIND(CONCAT(str(?number), " ", ?symbol) AS ?value)
			BIND(STRAFTER(str(?quantity), "#") AS ?quantityName) 
			BIND(STRAFTER(?quantityName, ".") AS ?quantityString)
			BIND(IRI(CONCAT(str(ex:), ?quantityString)) AS ?property)
        }"""))

        # query 7: {QUDT} -> {QUDT'} (with base unit)
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity?quantity .
            ?quantity rdf:type qudt:Quantity . 
            ?quantity qudt:hasQuantityKind ?quantityKind .
            ?quantity ex:rangeQuantity ?quantityClass .
            ?quantity qudt:quantityValue _:value .
            _:value rdf:type qudt:QuantityValue .
            _:value qudt:unit ?baseUnit .
            _:value qudt:numericValue ?baseNumber .
        }      
        WHERE {
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity ?quantity .
            ?quantity qudt:quantityValue ?quantityValue .
			?quantity ex:rangeQuantity ?quantityClass .
            ?quantityValue qudt:unit ?unit .
            ?quantityValue qudt:numericValue ?number .
			?unit qudt:conversionMultiplier ?multiplier .
			?unit qudt:conversionOffset ?offset .
			?baseUnit a ?quantityClass .
			?baseUnit qudt:conversionMultiplier 1e+00 .
			?baseUnit qudt:conversionOffset 0e+00 .
			BIND(?number * ?multiplier + ?offset AS ?baseNumber)
        }"""))

        # query 8: {QUDT} -> {QUDT'} (with siblings)
        self.query.append(prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        CONSTRUCT { 
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity?quantity .
            ?quantity rdf:type qudt:Quantity . 
            ?quantity qudt:hasQuantityKind ?quantityKind .
			?quantity ex:rangeQuantity ?quantityClass . 
            ?quantity qudt:quantityValue _:value .
            _:value rdf:type qudt:QuantityValue .
            _:value qudt:unit ?unit .
            _:value qudt:numericValue ?number .
        }       
        WHERE {
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity ?quantity .
            ?quantity qudt:quantityValue ?quantityValue .
			?quantity ex:rangeQuantity ?quantityClass .
            ?quantityValue qudt:unit ?baseUnit .
            ?quantityValue qudt:numericValue ?baseNumber .
			?baseUnit a ?quantityClass .
			?baseUnit qudt:conversionMultiplier 1e+00 .
			?baseUnit qudt:conversionOffset 0e+00 .
			?unit a ?quantityClass .
			?unit qudt:conversionMultiplier ?multiplier .
			?unit qudt:conversionOffset ?offset .
			BIND((?baseNumber - ?offset ) / ?multiplier AS ?number)
        }"""))

        # is state a valid string?
        self.state["string"] = prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        ASK {
            ?subject a owl:NamedIndividual .
            ?subject ?property ?value .
            ?property rdfs:subPropertyOf ex:quantity .
			?property ex:rangeQuantity ?quantityClass .
            BIND(xsd:decimal(STRBEFORE(?value, " ")) AS ?number)
            BIND(STRAFTER(?value, " ") AS ?symbol) 
            ?unit qudt:symbol ?symbol .
			?unit qudt:abbreviation ?abbr .
			?unit a ?quantityClass .
        }""")

        # is state a valid float?
        self.state["float"] = prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        ASK {
            ?subject a owl:NamedIndividual .
            ?subject ?propertyUnit ?number .
            ?propertyUnit rdfs:subPropertyOf ex:quantity.unit .
			?propertyUnit ex:rangeUnit ?unit .
			?propertyUnit ex:rangeQuantity ?rangeQuantity . 
        }""")

        # is state a valid qudt?
        self.state["qudt"] = prepareQuery("""
        PREFIX ex:   <http://example.org/schema#>
        PREFIX qudt:   <http://qudt.org/schema/qudt/>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        ASK {
            ?subject a owl:NamedIndividual .
            ?subject qudt:hasQuantity ?quantity .
            ?quantity qudt:quantityValue ?quantityValue .
			?quantity ex:rangeQuantity ?quantityClass .
            ?quantityValue qudt:unit ?unit .
            ?quantityValue qudt:numericValue ?number .
			?unit qudt:conversionMultiplier ?multiplier .
			?unit qudt:conversionOffset ?offset .
        }""")

        # list states
        self.individuals = prepareQuery("""
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        SELECT ?subject
		WHERE  {
            ?subject a owl:NamedIndividual
        }""")
        

        