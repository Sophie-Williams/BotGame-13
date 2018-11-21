package utils;

public class Bombard implements Action {

	private final int amount;
	
	public Bombard(int amount) {
		this.amount = amount;
	}
	
	@Override
	public int actionCost() {
		return 1;
	}

	@Override
	public ActionType type() {
		return ActionType.BOMBARD;
	}
	
	public int getAmount() {
		return amount;
	}
}
