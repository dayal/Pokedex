import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class SPARQLQueryRunner {

	public ResultSet findPokemonByIdQuery(int id) throws IOException {
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
	
	public static ResultSet runSPARQLQuery(String queryString) throws IOException {
		InputStream in = new FileInputStream(new File("Pokedex.rdf"));
		Model model = ModelFactory.createDefaultModel();
		model.read(in, null, "N-TRIPLES");
		in.close();

		System.out.println(queryString);
		Query query = QueryFactory.create(queryString);

		// Execute the query and obtain results
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();

		// Output query results	
		ResultSetFormatter.out(System.out, results, query);

		// Important - free up resources used running the query
		qe.close();
		
		return results;
	}
	
	public static void main(String[] args) throws IOException {
		SPARQLQueryRunner queryRunner = new SPARQLQueryRunner();
		queryRunner.findPokemonByIdQuery(1);
	}
}
