package animation;

public class Ani {
	String resource, description, name, cost, vital, attack;
	boolean hasmonster;

	public Ani(String cardstring) {

		String[] tmp = cardstring.split(";");

		hasmonster = true;
		if (Integer.parseInt(tmp[3]) == 0) {
			hasmonster = false;
		}
		name = tmp[0];
		description = tmp[1];
		resource = tmp[2];
		cost = tmp[5];
		attack = tmp[6];
		vital = tmp[7];

		if (tmp[3].equals(0)) {
			attack = "";
			vital = "";
		}
	}

	public String getResource() {
		return resource;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public String getCost() {
		return cost;
	}

	public String getVital() {
		return vital;
	}

	public String getAttack() {
		return attack;
	}

	public boolean hasMonster() {
		return hasmonster;
	}

}
