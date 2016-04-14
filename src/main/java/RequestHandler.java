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
		
		get("/pokemon/discover", (req, res) -> {
			String type = req.params("type");
			String color = req.params("color");
			String heightFilter = req.params("heightFilter");
			String weightFilter = req.params("weightFilter");
			String attackFilter = req.params("attackFilter");
			String defenseFilter = req.params("defenseFilter");
			String spAttackFilter = req.params("spAttackFilter");
			String spDefenseFilter = req.params("spDefenseFilter");
			String speedFilter = req.params("speedFilter");
			String hpFilter = req.params("hpFilter");
			String sortBy = req.params("sortBy");
			String sortOrder = req.params("sortOrder");

			return queryRunner.advancedSearch(type, color, heightFilter, weightFilter, attackFilter, defenseFilter,
					spAttackFilter, spDefenseFilter, speedFilter, hpFilter, sortBy, sortOrder);
		}, new JsonTransformer());

		get("/pokemon/battle", (req, res) -> {
			Map<String, String> pokemon1Attrs = queryRunner.searchByNumber(req.params("pokemon1")).get(0);
			Map<String, String> pokemon2Attrs = queryRunner.searchByNumber(req.params("pokemon2")).get(0);
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
	}
	

}
