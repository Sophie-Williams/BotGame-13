package utils;

public final class SetFlag implements Action {

	private final boolean[] flag;
	
	public SetFlag(boolean[] flag) {
		flag = flag.clone();
		if (flag.length != Bot.FLAG_LENGTH) {
			throw new IllegalArgumentException("flag.length: " + flag.length);
		}
		
		this.flag = flag;
	}
	
	@Override
	public int actionCost() {
		return 1;
	}

	@Override
	public ActionType type() {
		return ActionType.SET_FLAG;
	}
	
	public boolean[] getFlag() {
		return flag.clone();
	}
	
}
