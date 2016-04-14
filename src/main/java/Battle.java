public class Battle {
	
	public static int LEVEL = 30;  // Set level to 30 for simplicity
	public static int BASE_ATTACK_POWER = 50;  // Base power described in the damage formula
	
	// Pokemon Battle
	// Damage formula derived from http://bulbapedia.bulbagarden.net/wiki/Damage#Damage_formula
	public static BattleResult battle(BattlePokemon pokemon1, BattlePokemon pokemon2) {
		
		boolean goesFirst = (pokemon1.getSpeed() >= pokemon2.getSpeed()) ? true : false; // Pokemon with faster speed goes first
		boolean hasWon = false;
		double hpLeft1 = pokemon1.getHp();
		double hpLeft2 = pokemon2.getHp();
		double effectivenessModifier1 = PokemonType.calculateEffectivenessModifier(pokemon1.getTypes(), pokemon2.getTypes());  // how effective are pokemon1's types against pokemon2's (eg. water vs fire)
		double effectivenessModifier2 = PokemonType.calculateEffectivenessModifier(pokemon2.getTypes(), pokemon1.getTypes());;
		StringBuilder battleLog = new StringBuilder();
		
		while (hpLeft1 > 0 && hpLeft2 > 0) {
			if (goesFirst) {
				hpLeft2 -= calculateDamage(pokemon1.getAttack(), pokemon2.getDefense(), pokemon1.getSpAttack(), pokemon2.getSpDefense(), effectivenessModifier1);
				battleLog.append(pokemon1.getName() + " attacked! ");
				battleLog.append(pokemon2.getName() + " has " + Math.max(0, hpLeft2) + " hp left.\n");
				if (hpLeft2 <= 0) {
					hasWon = true;
					battleLog.append(pokemon1.getName() + " has won!\n");
					break;
				}
				hpLeft1 -= calculateDamage(pokemon2.getAttack(), pokemon1.getDefense(), pokemon2.getSpAttack(), pokemon1.getSpDefense(), effectivenessModifier2);
				battleLog.append(pokemon2.getName() + " attacked! ");
				battleLog.append(pokemon1.getName() + " has " + Math.max(0, hpLeft1) + " hp left.\n");
				if (hpLeft1 <= 0) {
					hasWon = false;
					battleLog.append(pokemon2.getName() + " has won!\n");
					break;
				}
			} else {
				hpLeft1 -= calculateDamage(pokemon2.getAttack(), pokemon1.getDefense(), pokemon2.getSpAttack(), pokemon1.getSpDefense(), effectivenessModifier2);
				battleLog.append(pokemon2.getName() + " attacked! ");
				battleLog.append(pokemon1.getName() + " has " + Math.max(0, hpLeft1) + " hp left.\n");
				if (hpLeft1 <= 0) {
					hasWon = false;
					battleLog.append(pokemon2.getName() + " has won!\n");
					break;
				}
				hpLeft2 -= calculateDamage(pokemon1.getAttack(), pokemon2.getDefense(), pokemon1.getSpAttack(), pokemon2.getSpDefense(), effectivenessModifier1);
				battleLog.append(pokemon1.getName() + " attacked! ");
				battleLog.append(pokemon2.getName() + " has " + Math.max(0, hpLeft2) + " hp left.\n");
				if (hpLeft2 <= 0) {
					hasWon = true;
					battleLog.append(pokemon1.getName() + " has won!\n");
					break;
				}
			}
		}
		
		System.out.println(battleLog.toString());
		return new BattleResult(hasWon, pokemon1, pokemon2, battleLog.toString());
	}

	// http://bulbapedia.bulbagarden.net/wiki/Damage#Damage_formula
	public static double calculateDamage(int attack, int opponentDefense, int spAttack,
			int opponentSpDefense, double modifier) {
		return (((2 * LEVEL + 10) / 250d) * ((double)(attack + spAttack) / (double)(opponentDefense + opponentSpDefense))
				* BASE_ATTACK_POWER + 2) * modifier * (1 - Math.random() * 0.15);
	}
	
}
