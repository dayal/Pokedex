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

	public List<Map<String, String>> findPokemonById(int id) throws IOException {
		String queryString = 
			"PREFIX pkm: <http://pokedex.dataincubator.org/pkm/> " +
			"SELECT ?name ?color ?attack " +
			"WHERE {" +
			"      <http://pokedex.dataincubator.org/pokemon/" + id + "> <http://www.w3.org/2000/01/rdf-schema#label> ?name. " +
			"      <http://pokedex.dataincubator.org/pokemon/" + id + "> pkm:colour ?color. " +
			"      <http://pokedex.dataincubator.org/pokemon/" + id + "> pkm:baseAttack ?attack. " +
			"}";
		return runSPARQLQuery(queryString);
	}
	
	public static List<Map<String, String>> runSPARQLQuery(String queryString) throws IOException {
		InputStream in = new FileInputStream(new File("Pokedex.rdf"));
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "N-TRIPLES");
		in.close();

		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

//		ResultSetFormatter.out(System.out, results, query);

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
		queryRunner.findPokemonById(1);
	}
}
