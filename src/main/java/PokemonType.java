
public enum PokemonType {

	NORMAL(0, "normal"),
	FIGHT(1, "fighting"),
	FLYING(2, "flying"),
	POISON(3, "poison"),
	GROUND(4, "ground"),
	ROCK(5, "rock"),
	BUG(6, "bug"),
	GHOST(7, "ghost"),
	STEEL(8, "steel"),
	FIRE(9, "fire"),
	WATER(11, "water"),
	GRASS(11, "grass"),
	ELECTRIC(12, "electric"),
	PSYCHIC(13, "psychic"),
	ICE(14, "ice"),
	DRAGON(15, "dragon"),
	DARK(16, "dark"),
	FAIRY(17, "fairy");
	
	
	private int typeId;
	private String text;
	// This matrix defines the effectiveness of all types against each other
	// For example, typeEffectivenessMatrix[9][11] = 2 means that fire does 2x damage to grass
	private static double[][] typeEffectivenessMatrix = new double[][] {
		  { 1, 1, 1, 1, 1, 0.5, 1, 0, 0.5, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
		  { 2, 1, 0.5, 0.5, 1, 2, 0.5, 0, 2, 1, 1, 1, 1, 0.5, 2, 1, 2, 0.5 },
		  { 1, 2, 1, 1, 1, 0.5, 2, 1, 0.5, 1, 1, 2, 0.5, 1, 1, 1, 1, 1 },
		  { 1, 1, 1, 0.5, 0.5, 0.5, 1, 0.5, 0, 1, 1, 2, 1, 1, 1, 1, 1, 2 },
		  { 1, 1, 0, 2, 1, 2, 0.5, 1, 2, 2, 1, 0.5, 2, 1, 1, 1, 1, 1 },
		  { 1, 0.5, 2, 1, 0.5, 1, 2, 1, 0.5, 2, 1, 1, 1, 1, 2, 1, 1, 1 },
		  { 1, 0.5, 0.5, 0.5, 1, 1, 1, 0.5, 0.5, 0.5, 1, 2, 1, 2, 1, 1, 2, 0.5 },
		  { 0, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 0.5, 1 },
		  { 1, 1, 1, 1, 1, 2, 1, 1, 0.5, 0.5, 0.5, 1, 0.5, 1, 2, 1, 1, 2 },
		  { 1, 1, 1, 1, 1, 0.5, 2, 1, 2, 0.5, 0.5, 2, 1, 1, 2, 0.5, 1, 1 },
		  { 1, 1, 1, 1, 2, 2, 1, 1, 1, 2, 0.5, 0.5, 1, 1, 1, 0.5, 1, 1 },
		  { 1, 1, 0.5, 0.5, 2, 2, 0.5, 1, 0.5, 0.5, 2, 0.5, 1, 1, 1, 0.5, 1, 1 },
		  { 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 2, 0.5, 0.5, 1, 1, 0.5, 1, 1 },
		  { 1, 2, 1, 2, 1, 1, 1, 1, 0.5, 1, 1, 1, 1, 0.5, 1, 1, 0, 1 },
		  { 1, 1, 2, 1, 2, 1, 1, 1, 0.5, 0.5, 0.5, 2, 1, 1, 0.5, 2, 1, 1 },
		  { 1, 1, 1, 1, 1, 1, 1, 1, 0.5, 1, 1, 1, 1, 1, 1, 2, 1, 0 },
		  { 1, 0.5, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 1, 0.5, 0.5 },
		  { 1, 2, 1, 0.5, 1, 1, 1, 1, 0.5, 0.5, 1, 1, 1, 1, 1, 2, 2, 1 },
	};
	
	private PokemonType(int typeId, String text) {
		this.typeId = typeId;
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
	public int getId() {
		return this.typeId;
	}
	
	public static PokemonType fromString(String text) {
		if (text != null) {
			for (PokemonType type : PokemonType.values()) {
				if (text.equalsIgnoreCase(type.text)) {
					return type;
				}
			}
		}
		return null;
	}
	
	public static PokemonType[] parsePokemonTypes(String types) {
		PokemonType[] pokemonTypes = new PokemonType[2];
		int index = types.indexOf("|");
		if (index == -1) {
			// pokemon has only 1 type
			pokemonTypes[0] = fromString(types);
			pokemonTypes[1] = null;
		} else {
			pokemonTypes[0] = fromString(types.substring(0, index));
			pokemonTypes[1] = fromString(types.substring(index + 1));
		}
		
		return pokemonTypes;
	}
	
	public static double calculateEffectivenessModifier(PokemonType[] pokemon1Types, PokemonType[] pokemon2Types) {
		if (pokemon1Types[1] == null && pokemon2Types[1] == null) {  // both only have 1 type
			return typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[0].getId()];
		} else if (pokemon1Types[1] == null) { // pokemon 2 has 2 types
			return typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[0].getId()]
					* typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[1].getId()];
		} else if (pokemon2Types[1] == null) { // pokemon 1 has 2 types
			return typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[0].getId()]
					* typeEffectivenessMatrix[pokemon1Types[1].getId()][pokemon2Types[0].getId()];
		} else // both have 2 types {
			return typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[0].getId()]
					* typeEffectivenessMatrix[pokemon1Types[0].getId()][pokemon2Types[1].getId()]
					* typeEffectivenessMatrix[pokemon1Types[1].getId()][pokemon2Types[0].getId()]
					* typeEffectivenessMatrix[pokemon1Types[1].getId()][pokemon2Types[1].getId()];
	}
}
