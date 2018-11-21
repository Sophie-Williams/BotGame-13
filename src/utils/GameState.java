package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class GameState {
	private final int turnNumber;
	private final Set<Bot> bots = new HashSet<>();
	private final Set<Tile> tiles = new HashSet<>();
	private final Map<HexCoord, Tile> tilesFromCoords = new HashMap<>();
	
	public GameState(int turnNumber, Set<Bot> bots, Set<Tile> tiles) {
		this.turnNumber = turnNumber;
		this.bots.addAll(bots);
		
		for(Tile t: tiles) {
			this.tiles.add(t);
			tilesFromCoords.put(t.getData().getCoord(), t);
		}
	}

	public int getTurnNumber() {
		return turnNumber;
	}
	
	public Set<Bot> getBots() {
		return bots;
	}

	public Set<Tile> getTiles() {
		Set<Tile> result = new HashSet<>();
		result.addAll(tiles);
		return result;
	}
	
	public Tile tileFromCoord(HexCoord coord) {
		return tilesFromCoords.get(coord);
	}
	
	public GameState nextTurn() {
		return new GameState(turnNumber + 1, new HashSet<Bot>(bots), new HashSet<Tile>(tiles));
	}
}
