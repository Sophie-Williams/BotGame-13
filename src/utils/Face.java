package utils;

import static utils.Direction.*;

public enum Face implements Action {
	FACE_N(NORTH), FACE_NE(NORTH_EAST), FACE_SE(SOUTH_EAST), FACE_S(SOUTH), FACE_SW(SOUTH_WEST), FACE_NW(NORTH_WEST);
	
	private final Direction direction;
	
	private Face(Direction direction) {
		this.direction = direction;
	}
	
	@Override
	public int actionCost() {
		return 1;
	}
	
	@Override
	public ActionType type() {
		return ActionType.FACE;
	}
	
	public Direction direction() {
		return direction;
	}
}
