package utils;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public final class HexCoord {
	private final int northEast;
	private final int south;
	private final List<HexCoord> neighbors = new ArrayList<>();
	
	public HexCoord(int northEast, int south) {
		this.northEast = northEast;
		this.south = south;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o.getClass() != HexCoord.class) {
			return false;
		}
		HexCoord hc = (HexCoord) o;
		return northEast == hc.northEast && south == hc.south;
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result += south;
		result *= 31;
		result += northEast;
		
		return result;
	}
	
	@Override
	public String toString() {
		return "(" + south + ", " + northEast + ")";
	}
	
	public int getNorthEast() {
		return northEast;
	}

	public int getSouth() {
		return south;
	}
	
	public List<HexCoord> getNeighbors() {
		if(neighbors.size() == 0) {
			neighbors.add(new HexCoord(northEast, south - 1));     //North
			neighbors.add(new HexCoord(northEast + 1, south));     //Northeast
			neighbors.add(new HexCoord(northEast + 1, south + 1)); //Southeast
			neighbors.add(new HexCoord(northEast, south + 1));     //South
			neighbors.add(new HexCoord(northEast - 1, south));     //Southwest
			neighbors.add(new HexCoord(northEast - 1, south -1));  //Northwest
		}
		return new ArrayList<HexCoord>(neighbors);
	}
	
	public int taxiDistance(HexCoord hc) {
		int d1 = hc.south - south;
		int d2 = hc.northEast - northEast;
		/*int result = Math.abs(d1) + Math.abs(d2);
		
		if(Math.signum(d1) == Math.signum(d2)) {
			result -= Math.min(Math.abs(d1), Math.abs(d2));
		}
		
		return result;*/
		int d3 = -d1 - d2; //Get last cube coordinate
		int cubeDistance = Math.abs(d1) + Math.abs(d2) + Math.abs(d3);
		return cubeDistance / 2;
	}
}
