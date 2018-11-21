package utils;

public class VisibleTileData {
	private final boolean hasBot;
	
	private final Direction botFacing;
	private final int botArmor;
	private final int botAp;
	private final int botMetabolism;
	private final boolean[] botPermanentFlag;
	private final boolean[] botFlag;
	
	private final int actionPeriod;
	private final int actionPeriodStep;

	public VisibleTileData(boolean hasBot, Direction botFacing, int botArmor, int botAp, int botMetabolism,
			boolean[] botPermanentFlag, boolean[] botFlag, int actionPeriod,
			int actionPeriodStep) {
		this.hasBot = hasBot;
		this.botFacing = botFacing;
		this.botArmor = botArmor;
		this.botAp = botAp;
		this.botMetabolism = botMetabolism;
		
		botPermanentFlag = botPermanentFlag == null? null: botPermanentFlag.clone();
		botFlag = botFlag == null? null: botFlag.clone();
		if(botPermanentFlag != null && botPermanentFlag.length != Bot.PERMANENT_FLAG_LENGTH) {
			throw new IllegalArgumentException("botPermanentFlag.length: " + botPermanentFlag.length);
		}
		if(botFlag != null && botFlag.length != Bot.FLAG_LENGTH) {
			throw new IllegalArgumentException("botFlag.length: " + botFlag.length);
		}
		this.botPermanentFlag = botPermanentFlag;
		this.botFlag = botFlag;
		
		this.actionPeriod = actionPeriod;
		this.actionPeriodStep = actionPeriodStep;
	}

	public boolean isHasBot() {
		return hasBot;
	}

	public Direction getBotFacing() {
		return botFacing;
	}

	public int getBotArmor() {
		return botArmor;
	}

	public int getBotAp() {
		return botAp;
	}
	
	public int getBotMetabolism() {
		return botMetabolism;
	}
	
	public boolean[] getBotPermanentFlag() {
		return botPermanentFlag.clone();
	}

	public boolean[] getBotFlag() {
		return botFlag.clone();
	}

	public int getActionPeriod() {
		return actionPeriod;
	}

	public int getActionPeriodStep() {
		return actionPeriodStep;
	}
	
}
