import static spark.Spark.*;

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
			String spDefenceFilter = req.params("spDefenceFilter");
			String speedFilter = req.params("speedFilter");
			String hpFilter = req.params("hpFilter");
			String sortBy = req.params("sortBy");
			String sortOrder = req.params("sortOrder");

			return queryRunner.advancedSearch(type, color, heightFilter, weightFilter, attackFilter, defenseFilter,
					spAttackFilter, spDefenceFilter, speedFilter, hpFilter, sortBy, sortOrder);
		}, new JsonTransformer());
	}
}
