package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class BasicGameRunner implements GameRunner {
	
	private GameState state;
	private final Map<Bot, Surroundings> surroundingsFromBots = new HashMap<>();
	
	public BasicGameRunner(GameState state) {
		this.state = state;
	}
	
	private void processTileActionPoints() {
		for (Tile t: state.getTiles()) {
			if(t.getData().getActionPointPeriod() > 0) { //A period of zero means a tile doesn't yield
				if (t.getData().hasBot() && t.getData().getActionPointPeriodStep() == t.getData().getActionPointPeriod() - 1) {
					//System.out.println("Tile yielding");
					t.getData().getBot().changeActionPoints(1); //The tile yields an action point
				}
				t.setData(t.getData().nextStep());
			}
		}
	}
	
	private void processBotMetabolism() {
		for (Bot b: state.getBots()) {
			b.advanceMetabolism();
			if(b.getActionPoints() < 0) { //The bot starved
				terminateBot(b);
			}
		}
	}
	
	private Map<Bot, Action> getActionsFromBots() {
		Map<Bot, Action> result = new HashMap<>();
		
		for(Bot b: state.getBots()) {
			if (b.getActionPoints() > 0) {
				Surroundings s = surroundingsFromBots.get(b);
				Action a = b.getAction(s);
				result.put(b, a);
			} else {
				result.put(b, BasicAction.LOOK); //Looking is the only thing that can be done with no action points
			}
		}
		
		return result;
	}
	
	private void deductActionPoints(Map<Bot, Action> actions) {
		for (Bot b: actions.keySet()) {
			Action a = actions.get(b);
			if (a.actionCost() > b.getActionPoints()) {
				a = BasicAction.LOOK;
				actions.put(b, BasicAction.LOOK);
			}
			b.changeActionPoints(-a.actionCost());
		}
	}
	
	private Map<ActionType, Set<Bot>> getBotsFromActions(Map<Bot, Action> actions) {
		Map<ActionType, Set<Bot>> result = new HashMap<>();
		for(ActionType type: ActionType.values()) {
			result.put(type, new HashSet<Bot>());
		}
		for (Bot b: actions.keySet()) {
			ActionType t = actions.get(b).type();
			result.get(t).add(b);
		}
		
		return result;
	}
	
	private void terminateBot(Bot b) {
		HexCoord coord = b.getLocation();
		
		//Remove robot from tile
		Tile t = state.tileFromCoord(coord);
		t.setBot(null);
		//TileData oldData = t.getData();
		//TileData newData = new TileData(coord, false, null, oldData.getActionPointPeriod(), oldData.getActionPointPeriodStep());
		//t.setData(newData);
		
		//Remove robot from record
		state.getBots().remove(b);
	}
	
	private void terminateBots(Set<Bot> bots) {
		for(Bot b: bots) {
			terminateBot(b);
		}
	}
	
	private void increaseArmor(Set<Bot> bots) {
		for(Bot b: bots) {
			b.boostArmor();
		}
	}
	
	private void setFacing(Set<Bot> bots, Map<Bot, Action> actions) {
		for(Bot b: bots) {
			Action a = actions.get(b);
			Face f = (Face) a;
			b.setFacing(f.direction());
		}
	}
	
	private Tile facedTile(Bot b) {
		HexCoord coord = b.getLocation();
		HexCoord targetCoord = coord.getNeighbors().get(b.getFacing().ordinal()); //Sketchy - uses ordinal
		Tile targetTile = state.tileFromCoord(targetCoord);
		//System.out.println("targetTile: " + targetTile);
		return targetTile;
	}
	
	private void bombard(Set<Bot> bots, Map<Bot, Action> actions) {
		Map<Bot, Integer> oldArmors = new HashMap<>();
		for(Bot b: bots) {
			oldArmors.put(b, b.getArmor());
		}
		
		for(Bot b: bots) {
			Action a = actions.get(b);
			Bombard bombard = (Bombard) a;
			
			Tile targetTile = facedTile(b);
			if (targetTile.getData().hasBot()) {
				Bot target = targetTile.getData().getBot();
				
				//Bombarding happens simultaneously, so bombardings use the armor they had before bombarding started
				int bombardAmount = Math.min(oldArmors.get(b), bombard.getAmount());
				
				int newTargetArmor = target.getArmor() - bombardAmount;
				b.changeArmor(-bombardAmount);
				
				if (newTargetArmor < 0) {
					terminateBot(target); //The armor was reduced to less than zero, so the bot was destroyed
				} else {
					target.changeArmor(-bombardAmount);
				}
			}
		}
	}
	
	private void absorbInto(Bot victim, Bot recipient) {
		terminateBot(victim);
		//No special absorption rules now
	}
	
	private void setLocation(Bot b, HexCoord c) {
		Tile oldTile = state.tileFromCoord(b.getLocation());
		if(oldTile.getData().getBot() == b) {
			oldTile.setBot(null); //Remove the bot only if this is the current bot
		}
		
		Tile newTile = state.tileFromCoord(c);
		newTile.setBot(b);
		b.setLocation(newTile.getData().getCoord());
	}
	
	private void move(Set<Bot> moving) {
		Set<Bot> newMoving = new HashSet<>(moving);
		
		do { //See which bots will move by iteratively setting stationary bots that won't
			moving = new HashSet<Bot>(newMoving);
			//newMoving = new HashSet<Bot>(moving); //Not necessary
			
			Map<Tile, Set<Bot>> botsFromTiles = new HashMap<>();
			Set<Tile> checkTiles = new HashSet<>(); //Tiles that could have a movement conflict
			for(Bot b: state.getBots()) {
				Tile targetTile = moving.contains(b) ? facedTile(b): state.tileFromCoord(b.getLocation());
				if(!botsFromTiles.containsKey(targetTile)) {
					botsFromTiles.put(targetTile, new HashSet<Bot>());
				}
				botsFromTiles.get(targetTile).add(b);
				if(moving.contains(b)) {
					checkTiles.add(targetTile);
				}
			}

			//Conflicts where multiple bots try to end up in the same tile
			for(Tile t: checkTiles) {
				Set<Bot> tryingBots = botsFromTiles.get(t);
				if(tryingBots.size() > 1) { //Multiple bots trying to enter the same tile
					
					boolean isStillBotWithArmor = false;
					for(Bot b: tryingBots) {
						if(!moving.contains(b) && b.getArmor() > 0) {
							isStillBotWithArmor = true;
						}
					}
					if(isStillBotWithArmor) {
						newMoving.removeAll(tryingBots); //The moving bots won't be able to move, and the other isn't moving anyways
					}
					//The robot with highest armor (tiebreaker of highest AP) gets to move into the tile (possible absorbing a zero armor bot)
					else { 
						Set<Bot> movingBots = new HashSet<>();
						for(Bot b: tryingBots) {
							if(moving.contains(b)) {
								movingBots.add(b);
							}
						}
						
						if(movingBots.size() > 1) { //If there's only one bot moving, it'll get to move
							List<Bot> strongestBots = new ArrayList<>();
							for(Bot b: movingBots) {
								Bot compBot = strongestBots.get(0);
								if(b.getArmor() >= compBot.getArmor()) {
									if(b.getArmor() > compBot.getArmor()) { //Stronger
										strongestBots.clear();
										strongestBots.add(b);
									} else if (b.getActionPoints() >= compBot.getActionPoints()) {
										if(b.getActionPoints() > compBot.getActionPoints()) {
											strongestBots.clear();
											strongestBots.add(b);
										}
										else {
											strongestBots.add(b);
										}
									}
								}
							}
							
							if(strongestBots.size() > 1) { //Multiple bots that tie for strongest
								newMoving.removeAll(tryingBots); //None can move
							} else if (strongestBots.size() == 1) { //It should always be 1 or greater, but I want to be sure
								Set<Bot> removeBots = new HashSet<>(movingBots);
								removeBots.remove(strongestBots.get(0));
								newMoving.removeAll(removeBots); //The strongest moves in
							}
						}
					}
				}
			}
			
			//Conflicts where two bots try to move through each other
			for(Bot b: moving) {
				Tile target = facedTile(b);
				if(target == null) {
					newMoving.remove(b); //Going against the wall
				} else if(target.getData().hasBot()) {
					Bot b2 = target.getData().getBot();
					HexCoord target2 = facedTile(b2).getData().getCoord();
					if(target2.equals(b.getLocation())) { //Both bots are trying to move into each other
						boolean bothArmored = true;
						if(b.getArmor() == 0) {
							bothArmored = false;
							//If neither is armored, it will sit still, if the other is armored, it will move in and destroy b
							newMoving.remove(b);
						}
						if(b2.getArmor() == 0) {
							bothArmored = false;
							//Ditto for b2
							newMoving.remove(b2);
						}
						if(bothArmored) { //Both are armored so neither can move
							newMoving.remove(b);
							newMoving.remove(b2);
						}
					}
				}
			}
			
		} while(!newMoving.equals(moving));
		
		Map<Tile, Bot> stationaryNoArmorBotsFromTiles = new HashMap<>();
		for(Bot b: state.getBots()) {
			if(!moving.contains(b) && b.getArmor() == 0) {
				stationaryNoArmorBotsFromTiles.put(state.tileFromCoord(b.getLocation()), b);
			}
		}
		
		for(Bot b: moving) { //Move the bots that should move
			Tile targetTile = facedTile(b);
			setLocation(b, facedTile(b).getData().getCoord());
			
			if(stationaryNoArmorBotsFromTiles.containsKey(targetTile)) {
				Bot victim = stationaryNoArmorBotsFromTiles.get(targetTile);
				absorbInto(victim, b); //Moving into a bot with no armor
			}
		}
	}
	
	private void setFlags(Set<Bot> bots, Map<Bot, Action> actions) {
		for(Bot b: bots) {
			Action a = actions.get(b);
			SetFlag sf = (SetFlag) a;
			boolean[] newFlag = sf.getFlag();
			b.setFlag(newFlag);
		}
	}
	
	private void setPermanentFlags(Set<Bot> bots, Map<Bot, Action> actions) {
		for(Bot b: bots) {
			Action a = actions.get(b);
			SetPermanentFlag sf = (SetPermanentFlag) a;
			boolean[] newPermanentFlag = sf.getPermanentFlag();
			b.setPermanentFlag(newPermanentFlag);
		}
	}
	
	private Surroundings getSurroundings(Bot b) {
		HexCoord center = b.getLocation();
		List<HexCoord> neighbors = center.getNeighbors();
		VisibleTileData[] surroundingTiles = new VisibleTileData[6];
		int i = 0;
		for(HexCoord hc: neighbors) {
			Tile t = state.tileFromCoord(hc);
			if(t == null) {
				surroundingTiles[i] = null;
			}
			else {
				TileData td = t.getData();
				VisibleTileData vtd = td.getVisibleData();
				surroundingTiles[i] = vtd;
			}
		}
		Surroundings s = new Surroundings(surroundingTiles);
		return s;
	}
	
	private Set<Bot> makeBots(Set<Bot> bots) {
		Set<Bot> newBots = new HashSet<>();
		Map<Bot, HexCoord> startLocations = new HashMap<>();
		for(Bot b: bots) {
			Bot newBot = new Bot(b.getLocation(),
								 b.getFacing(),
								 MetabolismPeriodGetters.defaultStepwiseMetabolismPeriodGetter(),
								 b.getSide().makeBotController(),
								 b.getSide());
			setLocation(newBot, newBot.getLocation()); //Move it onto the current bot
			newBots.add(newBot);
			startLocations.put(newBot, newBot.getLocation());
		}
		move(newBots);
		Set<Bot> survived = new HashSet<>();
		for(Bot b: newBots) {
			if(b.getLocation().equals(startLocations.get(b))) { //The bot is still in its maker's hex
				terminateBot(b);
			}
			else {
				survived.add(b);
			}
		}
		
		for(Bot b: bots) { //Re-introduce creating bots
			setLocation(b, b.getLocation());
		}
		
		return survived;
	}
	
	private void giveAp(Set<Bot> bots) {
		Map<Bot, Integer> oldActionPoints = new HashMap<>();
		//Giving action points is done simultaneously,
		//so a bot with no AP that tries to give an action point will fail even if another bot is giving it an AP
		for(Bot b: state.getBots()) {
			oldActionPoints.put(b, b.getActionPoints());
		}
		
		for(Bot b: bots) {
			if(oldActionPoints.get(b) > 0) {
				Tile target = facedTile(b);
				if(target.getData().hasBot()) {
					Bot recipient = target.getData().getBot();
					if(recipient.changeActionPoints(1)) { //If the recipient is already at max points, no transfer takes place
						b.changeActionPoints(-1);
					}
				}
			}
		}
	}
	
	private void look(Set<Bot> bots) {
		for(Bot b: bots) {
			//HexCoord center = b.getLocation();
			//List<HexCoord> neighbors = center.getNeighbors();
			//VisibleTileData[] surroundingTiles = new VisibleTileData[6];
			//int i = 0;
			//for(HexCoord hc: neighbors) {
			//	surroundingTiles[i] = state.tileFromCoord(hc).getData().getVisibleData();
			//}
			//Surroundings s = new Surroundings(surroundingTiles);
			Surroundings s = getSurroundings(b);
			surroundingsFromBots.put(b, s);
		}
	}
	
	@Override
	public void advance() {
		//Preliminary parts
		processTileActionPoints(); //See which tiles yield action points and give the points to bots
		processBotMetabolism(); //Bots lose action points from hunger
		Map<Bot, Action> actionsFromBots = getActionsFromBots(); //See what actions bots take (don't execute them yet)
		deductActionPoints(actionsFromBots); //Make bots lose action points for their actions (change action to LOOK if necessary)
		
		Map<ActionType, Set<Bot>> botsFromActions = getBotsFromActions(actionsFromBots);
		//Process actions
		terminateBots(botsFromActions.get(ActionType.TERMINATE));
		increaseArmor(botsFromActions.get(ActionType.INCREASE_ARMOR));
		setFacing(botsFromActions.get(ActionType.FACE), actionsFromBots);
		bombard(botsFromActions.get(ActionType.BOMBARD), actionsFromBots);
		move(botsFromActions.get(ActionType.MOVE));
		setFlags(botsFromActions.get(ActionType.SET_FLAG), actionsFromBots);
		setPermanentFlags(botsFromActions.get(ActionType.SET_PERMANENT_FLAG), actionsFromBots);
		Set<Bot> newBots = makeBots(botsFromActions.get(ActionType.MAKE_BOT));
		giveAp(botsFromActions.get(ActionType.GIVE_AP));
		
		Set<Bot> lookingBots = new HashSet<>(botsFromActions.get(ActionType.LOOK));
		lookingBots.addAll(newBots);
		look(lookingBots);
		
		state = state.nextTurn();
	}

	@Override
	public GameState getGameState() {
		return state;
	}

}
