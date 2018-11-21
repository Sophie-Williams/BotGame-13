package utils;

public final class Bot {
	private HexCoord location;
	private Direction facing;
	private int actionPoints = 1;
	private int armor = 0;
	private boolean permanentFlagSet = false;
	private boolean[] permanentFlag = {false, false, false, false, false, false};
	private boolean[] flag = new boolean[FLAG_LENGTH];
	private MetabolismPeriodGetter metabolismPeriodGetter;
	private int metabolismPeriodStep = 0;
	
	private final BotController control;
	private final Side side;

	public final static int PERMANENT_FLAG_LENGTH = 6;
	public final static int FLAG_LENGTH = 32;
	
	public final static int MAX_ACTION_POINTS = 8;
	
	public Bot(HexCoord location, Direction facing, MetabolismPeriodGetter metabolismPeriodGetter, BotController control, Side side) {
		this.location = location;
		this.facing = facing;
		if(!(permanentFlag.length == PERMANENT_FLAG_LENGTH)) {
			throw new IllegalArgumentException("permanentFlag.length: " + permanentFlag.length);
		}
		this.metabolismPeriodGetter = metabolismPeriodGetter;
		this.control = control;
		this.side = side;
	}

	public HexCoord getLocation() {
		return location;
	}

	public void setLocation(HexCoord location) {
		this.location = location;
	}

	public Direction getFacing() {
		return facing;
	}

	public void setFacing(Direction facing) {
		this.facing = facing;
	}

	public int getActionPoints() {
		return actionPoints;
	}

	public void setActionPoints(int actionPoints) {
		this.actionPoints = actionPoints;
	}
	
	public boolean changeActionPoints(int increase) {
		actionPoints += increase;
		if(actionPoints > MAX_ACTION_POINTS) {
			actionPoints = MAX_ACTION_POINTS;
			return false;
		}
		return true;
	}

	private void checkArmor() {
		if (armor < 0) {
			armor = 0;
		}
		if (armor > 5) {
			armor = 5;
		}
	}
	
	public int getArmor() {
		return armor;
	}
	
	public void boostArmor() {
		armor += 1;
		checkArmor();
	}
	
	public void changeArmor(int change) {
		armor += change;
		checkArmor();
	}

	public boolean[] getFlag() {
		return flag;
	}

	public void setFlag(boolean[] flag) {
		flag = flag.clone();
		if (flag.length != FLAG_LENGTH) {
			throw new IllegalArgumentException("flag.length: " + flag.length);
		}
		
		this.flag = flag;
	}
	public boolean getPermanentFlagSet() {
		return permanentFlagSet;
	}
	
	public boolean[] getPermanentFlag() {
		return permanentFlag;
	}

	public boolean setPermanentFlag(boolean[] permanentFlag) {
		if(permanentFlagSet) {
			return false;
		}
		
		permanentFlag = permanentFlag.clone();
		if (flag.length != PERMANENT_FLAG_LENGTH) {
			throw new IllegalArgumentException("permanentFlag.length: " + permanentFlag.length);
		}
		this.permanentFlag = permanentFlag;
		return true;
	}
	
	public int getMetabolism() {
		return metabolismPeriodGetter.metabolismPeriod();
	}

	public int getMetabolismStep() {
		return metabolismPeriodStep;
	}

	public void setActionPointPeriodStep(int actionPointPeriodStep) {
		this.metabolismPeriodStep = actionPointPeriodStep;
	}

	public void advanceMetabolism() { //Bots lose AP other n turns
		if(getMetabolism() > 0) { //A metabolism period of zero means that robot doesn't have metabolism
			metabolismPeriodStep += 1;
			if (metabolismPeriodStep >= getMetabolism()) {
				//System.out.println("AP for metabolism");
				actionPoints--;
				metabolismPeriodStep = 0;
				metabolismPeriodGetter.advanceStep();
			}
		}
	}
	
	public Action getAction(Surroundings surroundings) {
		return control.getAction(this, surroundings);
	}

	public Side getSide() {
		return side;
	}
}
