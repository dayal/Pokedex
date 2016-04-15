import static spark.Spark.*;

import java.util.Map;

public class RequestHandler {
	
	public static void main(String[] args) {
		QueryRunner queryRunner = new QueryRunner();
		
		get("/pokemon/:searchString", (req, res) -> {
			String searchString = req.params(":searchString");
			
			if (searchString.matches("^\\d+$")) {  // check whether search string is number or name
				return queryRunner.searchByNumber(searchString);
			} else {
				searchString = searchString.substring(0, 1).toUpperCase()+ searchString.substring(1);  // Capitalize the first letter
				return queryRunner.searchByName(searchString);
			}
		}, new JsonTransformer());
		
		get("/discover", (req, res) -> {
			String type = req.queryParams("type");
			String color = req.queryParams("color");
			String heightFilter = req.queryParams("heightFilter");
			String weightFilter = req.queryParams("weightFilter");
			String attackFilter = req.queryParams("attackFilter");
			String defenseFilter = req.queryParams("defenseFilter");
			String spAttackFilter = req.queryParams("spAttackFilter");
			String spDefenseFilter = req.queryParams("spDefenseFilter");
			String speedFilter = req.queryParams("speedFilter");
			String hpFilter = req.queryParams("hpFilter");
			String sortBy = req.queryParams("sortBy");
			String sortOrder = req.queryParams("sortOrder");

			return queryRunner.advancedSearch(type, color, heightFilter, weightFilter, attackFilter, defenseFilter,
					spAttackFilter, spDefenseFilter, speedFilter, hpFilter, sortBy, sortOrder);
		}, new JsonTransformer());

		get("/battle", (req, res) -> {
			Map<String, String> pokemon1Attrs = queryRunner.searchByNumber(req.queryParams("pokemon1")).get(0);
			Map<String, String> pokemon2Attrs = queryRunner.searchByNumber(req.queryParams("pokemon2")).get(0);
			BattlePokemon pokemon1 = new BattlePokemon(
					pokemon1Attrs.get("name"),
					Integer.parseInt(pokemon1Attrs.get("attack")),
					Integer.parseInt(pokemon1Attrs.get("defense")),
					Integer.parseInt(pokemon1Attrs.get("spAttack")),
					Integer.parseInt(pokemon1Attrs.get("spDefense")),
					Integer.parseInt(pokemon1Attrs.get("speed")),
					Integer.parseInt(pokemon1Attrs.get("hp")),
					PokemonType.parsePokemonTypes(pokemon1Attrs.get("types")));
			BattlePokemon pokemon2 = new BattlePokemon(
					pokemon2Attrs.get("name"),
					Integer.parseInt(pokemon2Attrs.get("attack")),
					Integer.parseInt(pokemon2Attrs.get("defense")),
					Integer.parseInt(pokemon2Attrs.get("spAttack")),
					Integer.parseInt(pokemon2Attrs.get("spDefense")),
					Integer.parseInt(pokemon2Attrs.get("speed")),
					Integer.parseInt(pokemon2Attrs.get("hp")),
					PokemonType.parsePokemonTypes(pokemon2Attrs.get("types")));

			BattleResult result = Battle.battle(pokemon1, pokemon2);
			
			return result;
		}, new JsonTransformer());
		
		get("/findSimilar", (req, res) -> {
			Map<String, String> similarPokemon = queryRunner.findSimilar(req.queryParams("number")).get(0);
			String similarPokemonNumber = similarPokemon.get("number");
			
			return queryRunner.searchByNumber(similarPokemonNumber);
		}, new JsonTransformer());

		after((req, res) -> {
			res.status(200);
			res.header("Access-Control-Allow-Origin", "*");
			res.type("application/json");
		});
	}
	

}
