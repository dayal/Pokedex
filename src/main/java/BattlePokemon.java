public class BattlePokemon {

	private String name;
	private int attack;
	private int defense;
	private int spAttack;
	private int spDefense;
	private int speed;
	private int hp;
	private PokemonType[] types;

	public String getName() {
		return name;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefense() {
		return defense;
	}

	public int getSpAttack() {
		return spAttack;
	}

	public int getSpDefense() {
		return spDefense;
	}

	public int getSpeed() {
		return speed;
	}

	public int getHp() {
		return hp;
	}

	public PokemonType[] getTypes() {
		return types;
	}

	public BattlePokemon(String name, int attack, int defense, int spAttack, int spDefense, int speed, int hp,
			PokemonType[] types) {
		super();
		this.name = name;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		this.hp = hp;
		this.types = types;
	}

}