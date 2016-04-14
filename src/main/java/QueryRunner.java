import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class QueryRunner {

	// search Pokemon by national number
	public List<Map<String, String>> searchByNumber(String number) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT " +
			"      ?number" +		
			"      ?name" +
			"      (GROUP_CONCAT(?typeName;separator=\"|\") as ?types)" + // aggregate types (seperated by semicolon) since each Pokemon can have 1-2 types
			"      ?color" +
			"      ?height" +
			"      ?weight" +
			"      (str(?image) as ?imageUrl)" +  // get url string from image RDF node
			"      ?attack" +
			"      ?defense" +
			"      ?spAttack" +
			"      ?spDefense" +
			"      ?speed" +
			"      ?hp" +
			"      ?description " +
			"WHERE {" +
			"      ?pokemon pkm:nationalNumber ?number. " + 	// find Pokemon with number
			"      ?pokemon <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
			"      ?pokemon pkm:type ?type. " +
			"      ?type <http://www.w3.org/2000/01/rdf-schema#label> ?typeDescription. " +
			"	   BIND(REPLACE(?typeDescription, \" Type\", \"\") AS ?typeName). " +
			"      ?pokemon pkm:colour ?color. " +
			"      ?pokemon pkm:description ?description. " +
			"      ?pokemon pkm:length ?height. " +
			"      ?pokemon pkm:weight ?weight. " +
			"      ?pokemon <http://xmlns.com/foaf/0.1/depiction> ?image. " +  // image of Pokemon
			"      ?pokemon pkm:baseAttack ?attack. " +
			"      ?pokemon pkm:baseDefense ?defense. " +
			"      ?pokemon pkm:baseSpAtk ?spAttack. " +
			"      ?pokemon pkm:baseSpDef ?spDefense. " +
			"      ?pokemon pkm:baseSpeed ?speed. " +
			"      ?pokemon pkm:baseHP ?hp. " +
			"      FILTER strStarts(str(?number), \"" + number + "\" )" +
			"	   FILTER (langMatches(lang(?description), \"EN\"))" +	// only return English description
			"	   FILTER contains(str(?image), \"legendarypokemon.net\")" +  // only return url of image from legendarypokemon.net
			"	   FILTER (?attack > 70)" +  // only return url of image from legendarypokemon.net
			"}" +
			"GROUP BY ?number ?name ?color ?description ?height ?weight ?image ?attack ?defense ?spAttack ?spDefense ?speed ?hp " +
			"ORDER BY ?number LIMIT 10";  // return 10 results ordered by number
		return runQuery(queryString);
	}
	
	// search Pokemon by English name
	public List<Map<String, String>> searchByName(String name) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT " +
			"      ?number" +
			"      ?name" +
			"      (GROUP_CONCAT(?typeName;separator=\"|\") as ?types)" +
			"      ?color" +
			"      ?height" +
			"      ?weight" +
			"      (str(?image) as ?imageUrl)" +  // get url string from image RDF node
			"      ?attack" +
			"      ?defense" +
			"      ?spAttack" +
			"      ?spDefense" +
			"      ?speed" +
			"      ?hp" +
			"      ?description " +
			"WHERE {" +
			"      ?pokemon <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
			"      ?pokemon pkm:nationalNumber ?number. " +
			"      ?pokemon pkm:type ?type. " +
			"      ?type <http://www.w3.org/2000/01/rdf-schema#label> ?typeDescription. " +
			"	   BIND(REPLACE(?typeDescription, \" Type\", \"\") AS ?typeName). " +
			"      ?pokemon pkm:colour ?color. " +
			"      ?pokemon pkm:description ?description. " +
			"      ?pokemon pkm:length ?height. " +
			"      ?pokemon pkm:weight ?weight. " +
			"      ?pokemon <http://xmlns.com/foaf/0.1/depiction> ?image. " +
			"      ?pokemon pkm:baseAttack ?attack. " +
			"      ?pokemon pkm:baseDefense ?defense. " +
			"      ?pokemon pkm:baseSpAtk ?spAttack. " +
			"      ?pokemon pkm:baseSpDef ?spDefense. " +
			"      ?pokemon pkm:baseSpeed ?speed. " +
			"      ?pokemon pkm:baseHP ?hp. " +
			"      FILTER strStarts(?name, \"" + name + "\" )" +
			"	   FILTER (langMatches(lang(?description), \"EN\"))" +	// only return English description
			"	   FILTER contains(str(?image), \"legendarypokemon.net\")" +  // only return url of image from legendarypokemon.net
			"}" +
			"GROUP BY ?number ?name ?color ?description ?height ?weight ?image ?attack ?defense ?spAttack ?spDefense ?speed ?hp " +
			"ORDER BY ?name LIMIT 10";  // return 10 results ordered by name
		return runQuery(queryString);
	}

	public List<Map<String, String>> advancedSearch(String type, String color, String heightFilter, String weightFilter,
			String attackFilter, String defenseFilter, String spAttackFilter, String spDefenceFilter,
			String speedFilter, String hpFilter, String sortBy, String sortOrder) throws IOException {
//		String queryString = 
//				"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
//				"SELECT " +
//				"      ?number" +
//				"      ?name" +
//				"      (GROUP_CONCAT(?typeName;separator=\"|\") as ?types)" +
//				"      ?color" +
//				"      ?height" +
//				"      ?weight" +
//				"      (str(?image) as ?imageUrl)" +  // get url string from image RDF node
//				"      ?attack" +
//				"      ?defense" +
//				"      ?spAttack" +
//				"      ?spDefense" +
//				"      ?speed" +
//				"      ?hp" +
//				"      ?description " +
//				"WHERE {" +
//				"      ?pokemon <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
//				"      ?pokemon pkm:nationalNumber ?number. " +
//				"      ?pokemon pkm:type ?type. " +
//				"      ?type <http://www.w3.org/2000/01/rdf-schema#label> ?typeDescription. " +
//				"	   BIND(REPLACE(?typeDescription, \" Type\", \"\") AS ?typeName). " +
//				"      ?pokemon pkm:colour ?color. " +
//				"      ?pokemon pkm:description ?description. " +
//				"      ?pokemon pkm:length ?height. " +
//				"      ?pokemon pkm:weight ?weight. " +
//				"      ?pokemon <http://xmlns.com/foaf/0.1/depiction> ?image. " +
//				"      ?pokemon pkm:baseAttack ?attack. " +
//				"      ?pokemon pkm:baseDefense ?defense. " +
//				"      ?pokemon pkm:baseSpAtk ?spAttack. " +
//				"      ?pokemon pkm:baseSpDef ?spDefense. " +
//				"      ?pokemon pkm:baseSpeed ?speed. " +
//				"      ?pokemon pkm:baseHP ?hp. " +
//				"	   FILTER (langMatches(lang(?description), \"EN\"))" +	// only return English description
//				"	   FILTER contains(str(?image), \"legendarypokemon.net\")" +  // only return url of image from legendarypokemon.net
//				"}" +
//				"GROUP BY ?number ?name ?color ?description ?height ?weight ?image ?attack ?defense ?spAttack ?spDefense ?speed ?hp " +
//				"ORDER BY ?name LIMIT 10";  // return 10 results ordered by name
		return null;
	}
	
	public static List<Map<String, String>> runQuery(String queryString) throws IOException {
		InputStream in = new FileInputStream(new File("Pokedex.rdf"));
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "N-TRIPLES");
		in.close();

		System.out.println("Query:" + queryString);
		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// uncomment the line below to print output in console instead
		ResultSetFormatter.out(System.out, results, query);

		List<Map<String, String>> resultsList = new LinkedList<Map<String, String>>();
		while (results.hasNext()) {
			Map<String, String> map = new HashMap<String, String>();
			QuerySolution result = results.next();
			Iterator<String> varNames = result.varNames();
			while (varNames.hasNext()){
				String varName = varNames.next();
				map.put(varName, result.getLiteral(varName).getString());
			}
			resultsList.add(map);
		}
		
		qe.close();
		
		return resultsList;
	}
	
	public static void main(String[] args) throws IOException {
		QueryRunner queryRunner = new QueryRunner();
//		queryRunner.searchByName("Pi");
		queryRunner.searchByNumber("1");
	}
}
