package utils;

public final class TileData {
	private final HexCoord coord;
	private final boolean hasBot;
	private final Bot bot;
	private final int actionPointPeriod;
	private final int actionPointPeriodStep;
	
	private final VisibleTileData visibleData;
	
	public TileData(HexCoord coord, boolean hasBot, Bot bot, int actionPointPeriod, int actionPointPeriodStep) {
		this.coord = coord;
		this.hasBot = hasBot;
		this.bot = bot;
		this.actionPointPeriod = actionPointPeriod;
		this.actionPointPeriodStep = actionPointPeriodStep;
		
		//System.out.println("hasBot: " + hasBot);
		
		visibleData = new VisibleTileData(hasBot,
										  hasBot? bot.getFacing(): null,
										  hasBot? bot.getArmor(): 0,
										  hasBot? bot.getActionPoints(): 0,
										  hasBot? bot.getMetabolism(): 0,
										  //hasBot? bot.getMetobolismStep(): 0,
										  hasBot? bot.getPermanentFlag(): null,
										  hasBot? bot.getFlag(): null,
										  actionPointPeriodStep,
										  actionPointPeriodStep);
	}

	public VisibleTileData getVisibleData() {
		return visibleData;
	}
	
	public HexCoord getCoord() {
		return coord;
	}
	
	public boolean hasBot() {
		return hasBot;
	}

	public Bot getBot() {
		return bot;
	}
	
	public int getActionPointPeriod() {
		return actionPointPeriod;
	}

	public int getActionPointPeriodStep() {
		return actionPointPeriodStep;
	}
	
	public TileData nextStep() {
		if (actionPointPeriod != -1) {
			int nextStep = actionPointPeriodStep + 1;
			if (nextStep >= actionPointPeriod) {
				nextStep = 0;
			}
			return new TileData(coord, hasBot, bot, actionPointPeriod, nextStep);
		} 
		else {
			return this;
		}
	}
}
