package utils;

public final class SetPermanentFlag implements Action {

	private final boolean[] permanentFlag;
	
	public SetPermanentFlag(boolean[] permanentFlag) {
		permanentFlag = permanentFlag.clone();
		if (permanentFlag.length != Bot.PERMANENT_FLAG_LENGTH) {
			throw new IllegalArgumentException("permanentFlag.length: " + permanentFlag.length);
		}
		
		this.permanentFlag = permanentFlag;
	}
	
	@Override
	public int actionCost() {
		return 1;
	}

	@Override
	public ActionType type() {
		return ActionType.SET_FLAG;
	}
	
	public boolean[] getPermanentFlag() {
		return permanentFlag.clone();
	}

}
