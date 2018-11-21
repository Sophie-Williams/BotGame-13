package utils;

public enum BasicAction implements Action {
	TERMINATE(1, ActionType.TERMINATE),
	INCREASE_ARMOR(1, ActionType.INCREASE_ARMOR),
	//BOMBARD(1, ActionType.BOMBARD),
	MOVE(1, ActionType.MOVE),
	MAKE_BOT(1, ActionType.MAKE_BOT),
	GIVE_AP(0, ActionType.GIVE_AP), //The action doesn't have a cost, but the executing robot still loses an AP if there's a bot to receive it
	LOOK(0, ActionType.LOOK);

	private final int actionCost;
	private final ActionType type;
	
	private BasicAction(int actionCost, ActionType type) {
		this.actionCost = actionCost;
		this.type = type;
	}

	@Override
	public int actionCost() {
		return actionCost;
	}
	
	@Override
	public ActionType type() {
		return type;
	}
}
