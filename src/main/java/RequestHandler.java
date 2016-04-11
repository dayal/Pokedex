import static spark.Spark.*;

import java.util.List;
import java.util.Map;

public class RequestHandler {
	
	public static void main(String[] args) {
		SPARQLQueryRunner queryRunner = new SPARQLQueryRunner();
		
		get("/pokemon/:id", (req, res) -> {
//			StringBuilder output = new StringBuilder();
//			List<Map<String, String>> results = queryRunner.findPokemonById(Integer.parseInt(req.params(":id")));
//			for (Map<String, String> result : results) {
//				for (String attr : result.keySet()) {
//					output.append(attr + ": " + result.get(attr) + "\n");
//				}
//			}
//			return output.toString();
//		});
			return queryRunner.findPokemonById(Integer.parseInt(req.params(":id")));
		}, new JsonTransformer());
	}
}
