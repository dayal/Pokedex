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
			"      FILTER strStarts(str(?number), \"" + number + "\" ) " +
			"	   FILTER (langMatches(lang(?description), \"EN\")) " +	// only return English description
			"	   FILTER contains(str(?image), \"legendarypokemon.net\") " +  // only return url of image from legendarypokemon.net
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
			"      FILTER strStarts(?name, \"" + name + "\" ) " +
			"	   FILTER (langMatches(lang(?description), \"EN\")) " +	// only return English description
			"	   FILTER contains(str(?image), \"legendarypokemon.net\") " +  // only return url of image from legendarypokemon.net
			"}" +
			"GROUP BY ?number ?name ?color ?description ?height ?weight ?image ?attack ?defense ?spAttack ?spDefense ?speed ?hp " +
			"ORDER BY ?name LIMIT 10";  // return 10 results ordered by name
		return runQuery(queryString);
	}

	public List<Map<String, String>> advancedSearch(String type, String color, String heightFilter, String weightFilter,
			String attackFilter, String defenseFilter, String spAttackFilter, String spDefenseFilter,
			String speedFilter, String hpFilter, String sortBy, String sortOrder) throws IOException {
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
				(type == null ? "" : // skip if filter is null(undefined)
				"      FILTER (?typeName = \"" + type + "\") ") +
				(color == null ? "" :
				"      FILTER (?color = \"" + color + "\") ") +
				(heightFilter == null ? "" :
				"      FILTER (?height " + heightFilter + ") ") +  // unit of height is 1/10 meter (in RDF data)
				(weightFilter == null ? "" :
				"      FILTER (?weight " + weightFilter + ") ") +  // unit of weight is 1/10 kg (in RDF data)
				(attackFilter == null ? "" :
				"      FILTER (?attack " + attackFilter + ") ") +
				(defenseFilter == null ? "" :
				"      FILTER (?defense " + defenseFilter + ") ") +
				(spAttackFilter == null ? "" :
				"      FILTER (?weight " + spAttackFilter + ") ") +
				(spDefenseFilter == null ? "" :
				"      FILTER (?spDefense " + spDefenseFilter + ") ") +
				(speedFilter == null ? "" :
				"      FILTER (?speed " + speedFilter + ") ") +
				(hpFilter == null ? "" :
				"      FILTER (?hp " + hpFilter + ") ") +
				"	   FILTER (langMatches(lang(?description), \"EN\")) " +	// only return English description
				"	   FILTER contains(str(?image), \"legendarypokemon.net\") " +  // only return url of image from legendarypokemon.net
				"}" +
				"GROUP BY ?number ?name ?color ?description ?height ?weight ?image ?attack ?defense ?spAttack ?spDefense ?speed ?hp " +
				(sortBy != null && sortOrder != null ?  // sort by which attribute and whether the order is ASC or DESC
				"ORDER BY " + sortOrder + "(?" + sortBy + ") " :
				"ORDER BY ?name ") +
				"LIMIT 20";
		return runQuery(queryString);
	}
	
	// find a similar pokemon
	// Only returns number and name. Run search query to retrieve full data. 
	public List<Map<String, String>> findSimilar(String number) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT DISTINCT " +
			"      ?number ?name " +
			"WHERE {" +
			"      ?pokemon1 pkm:nationalNumber " + number + ". " + 	// find first Pokemon with number
			"      ?pokemon1 pkm:type ?type1. " +
			"      ?pokemon1 pkm:colour ?color1. " +
			"      ?pokemon1 pkm:length ?height1. " +
			"      ?pokemon1 pkm:weight ?weight1. " +
			"      ?pokemon2 pkm:type ?type2. " +
			"      ?pokemon2 pkm:colour ?color2. " +
			"      ?pokemon2 pkm:length ?height2. " +
			"      ?pokemon2 pkm:weight ?weight2. " +
			"      ?pokemon2 pkm:nationalNumber ?number. " +
			"      ?pokemon2 <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
			"      FILTER (?pokemon2 != ?pokemon1) " +  // pokemon2 is different than pokemon 1
			"      FILTER (?weight2 < ?weight1 * 1.5) " +
			"      FILTER (?weight2 > ?weight1 / 1.5) " +
			"      FILTER (?color2 = ?color1 || ?type2 = ?type1) " +  // either color or type matches
			"} " +
			"ORDER BY RAND() LIMIT 1";  // return a random result
		return runQuery(queryString);
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
			while (varNames.hasNext()) {
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
		// queryRunner.searchByName("Pi");
//		 queryRunner.searchByNumber("1");
		// queryRunner.advancedSearch("Grass", null, "> 5", null, null, null,
		// null, null, null, null, "weight", "DESC");

		queryRunner.findSimilar("6");
//		Map<String, String> pokemon1Attrs = queryRunner.searchByNumber("1").get(0);
//		Map<String, String> pokemon2Attrs = queryRunner.searchByNumber("2").get(0);
//		BattlePokemon pokemon1 = new BattlePokemon(
//				pokemon1Attrs.get("name"),
//				Integer.parseInt(pokemon1Attrs.get("attack")),
//				Integer.parseInt(pokemon1Attrs.get("defense")),
//				Integer.parseInt(pokemon1Attrs.get("spAttack")),
//				Integer.parseInt(pokemon1Attrs.get("spDefense")),
//				Integer.parseInt(pokemon1Attrs.get("speed")),
//				Integer.parseInt(pokemon1Attrs.get("hp")),
//				PokemonType.parsePokemonTypes(pokemon1Attrs.get("types")));
//		BattlePokemon pokemon2 = new BattlePokemon(
//				pokemon2Attrs.get("name"),
//				Integer.parseInt(pokemon2Attrs.get("attack")),
//				Integer.parseInt(pokemon2Attrs.get("defense")),
//				Integer.parseInt(pokemon2Attrs.get("spAttack")),
//				Integer.parseInt(pokemon2Attrs.get("spDefense")),
//				Integer.parseInt(pokemon2Attrs.get("speed")),
//				Integer.parseInt(pokemon2Attrs.get("hp")),
//				PokemonType.parsePokemonTypes(pokemon2Attrs.get("types")));
//		
//		BattleResult result = Battle.battle(pokemon1, pokemon2);

	}
}
