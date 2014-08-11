package kr.ac.jejuuniv.domain.model;

public enum ItemType {
	FOOD(0), COMMODITY(1), DAILY_NECESSITIES(2), INDUSTRIAL_PRODUCTS(3);

	private int id;

	private ItemType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
