package utils;

public final class Tile {
	private TileData data;
	
	public Tile(TileData data) {
		if(data == null) {
			throw new NullPointerException("data: " + data);
		}
		this.data = data;
	}

	public TileData getData() {
		return data;
	}

	public void setData(TileData data) {
		if(data == null) {
			throw new NullPointerException("data: " + data);
		}
		this.data = data;
	}
	
	public void setBot(Bot b) {
		if(b == null) {
			setData(new TileData(data.getCoord(), false, null, data.getActionPointPeriod(), data.getActionPointPeriodStep()));
		} else {
			setData(new TileData(data.getCoord(), true, b, data.getActionPointPeriod(), data.getActionPointPeriodStep()));
		}
	}
}
