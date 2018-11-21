package utils;

public enum Direction {
	NORTH(0), NORTH_EAST(1), SOUTH_EAST(2), SOUTH(3), SOUTH_WEST(4), NORTH_WEST(5);
	
	private final int index;
	
	private Direction(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
}
