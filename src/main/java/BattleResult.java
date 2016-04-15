public class BattleResult {

	boolean hasWon;
	BattlePokemon pokemon1;
	BattlePokemon pokemon2;
	String battlelog;

	public boolean isHasWon() {
		return hasWon;
	}

	public BattlePokemon getPokemon1() {
		return pokemon1;
	}

	public BattlePokemon getPokemon2() {
		return pokemon2;
	}

	public String getBattlelog() {
		return battlelog;
	}

	public BattleResult(boolean hasWon, BattlePokemon pokemon1, BattlePokemon pokemon2, String battleLog) {

		this.hasWon = hasWon;
		this.pokemon1 = pokemon1;
		this.pokemon2 = pokemon2;
		this.battlelog = battleLog;
	}
}