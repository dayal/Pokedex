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

public class SPARQLQueryRunner {

	// return data given Pokemon's national number
	public List<Map<String, String>> findPokemonByNumber(int number) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT " +
			"      ?name" +
			"      (GROUP_CONCAT(?typeName;separator=\"|\") as ?types)" + // aggregate types (seperated by semicolon) since each Pokemon can have 1-2 types
			"      ?color" +
			"      ?height" +
			"      ?weight" +
			"      ?depiction" +
			"      ?attack" +
			"      ?defense" +
			"      ?spAttack" +
			"      ?spDefense" +
			"      ?speed" +
			"      ?hp" +
			"      ?description " +
			"WHERE {" +
			"      ?pokemon pkm:nationalNumber " + number + ". " + 	// find Pokemon with number
			"      ?pokemon <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
			"      ?pokemon pkm:type ?type. " +
			"      ?type <http://www.w3.org/2000/01/rdf-schema#label> ?typeDescription. " +
			"	   BIND(REPLACE(?typeDescription, \" Type\", \"\") AS ?typeName). " +
			"      ?pokemon pkm:colour ?color. " +
			"      ?pokemon pkm:description ?description. " +
			"      ?pokemon pkm:length ?height. " +
			"      ?pokemon pkm:weight ?weight. " +
			"      ?pokemon <http://xmlns.com/foaf/0.1/depiction> ?depiction. " +  // image of Pokemon
			"      ?pokemon pkm:baseAttack ?attack. " +
			"      ?pokemon pkm:baseDefense ?defense. " +
			"      ?pokemon pkm:baseSpAtk ?spAttack. " +
			"      ?pokemon pkm:baseSpDef ?spDefense. " +
			"      ?pokemon pkm:baseSpeed ?speed. " +
			"      ?pokemon pkm:baseHP ?hp. " +
			"	   FILTER (langMatches(lang(?description), \"EN\"))" +	// only return English description
			"	   FILTER regex(str(?depiction), \"veekun.com\", \"i\")" +  // only return url of image from veekun.com
			"}" +
			"GROUP BY ?name ?color ?description ?height ?weight ?depiction ?attack ?defense ?spAttack ?spDefense ?speed ?hp";
		return runSPARQLQuery(queryString);
	}
	
	// return data given Pokemon's English name
	public List<Map<String, String>> findPokemonByName(String name) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT " +
					"      ?nationalNumber" +
					"      (GROUP_CONCAT(?typeName;separator=\"|\") as ?types)" +
					"      ?color" +
					"      ?height" +
					"      ?weight" +
					"      ?depiction" +
					"      ?attack" +
					"      ?defense" +
					"      ?spAttack" +
					"      ?spDefense" +
					"      ?speed" +
					"      ?hp" +
					"      ?description " +
			"WHERE {" +
			"      ?pokemon <http://www.w3.org/2000/01/rdf-schema#label> \"" + name + "\". " +
			"      ?pokemon pkm:nationalNumber ?nationalNumber. " +
			"      ?pokemon pkm:type ?type. " +
			"      ?type <http://www.w3.org/2000/01/rdf-schema#label> ?typeDescription. " +
			"	   BIND(REPLACE(?typeDescription, \" Type\", \"\") AS ?typeName). " +
			"      ?pokemon pkm:colour ?color. " +
			"      ?pokemon pkm:description ?description. " +
			"      ?pokemon pkm:length ?height. " +
			"      ?pokemon pkm:weight ?weight. " +
			"      ?pokemon <http://xmlns.com/foaf/0.1/depiction> ?depiction. " +
			"      ?pokemon pkm:baseAttack ?attack. " +
			"      ?pokemon pkm:baseDefense ?defense. " +
			"      ?pokemon pkm:baseSpAtk ?spAttack. " +
			"      ?pokemon pkm:baseSpDef ?spDefense. " +
			"      ?pokemon pkm:baseSpeed ?speed. " +
			"      ?pokemon pkm:baseHP ?hp. " +
			"	   FILTER (langMatches(lang(?description), \"EN\"))" +
			"	   FILTER regex(str(?depiction), \"veekun.com\", \"i\")" +
			"}";
		return runSPARQLQuery(queryString);
	}
	
	public static List<Map<String, String>> runSPARQLQuery(String queryString) throws IOException {
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
		SPARQLQueryRunner queryRunner = new SPARQLQueryRunner();
		queryRunner.findPokemonByNumber(1);
	}
}
