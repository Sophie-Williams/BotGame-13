package utils;

public final class Surroundings {
	private final VisibleTileData[] tiles;
	
	public Surroundings(VisibleTileData[] tiles) {
		tiles = tiles.clone();
		if (tiles.length != 6) {
			throw new IllegalArgumentException("tiles.length: " + tiles.length);
		}
		
		this.tiles = tiles;
	}
	
	public VisibleTileData getTile(Direction direction) {
		return tiles[direction.getIndex()];
	}
	
	public VisibleTileData getNorthEastTile() {
		return tiles[1];
	}
	
	public VisibleTileData getSouthEastTile() {
		return tiles[2];
	}
	
	public VisibleTileData getSouthTile() {
		return tiles[3];
	}
	
	public VisibleTileData getSouthWestTile() {
		return tiles[4];
	}
	
	public VisibleTileData getNorthWestTile() {
		return tiles[5];
	}
}
