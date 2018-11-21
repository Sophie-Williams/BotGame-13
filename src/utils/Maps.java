package utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Maps {
	private Maps() {
		throw new AssertionError("Uninstantiable");
	}
	
	public static Set<HexCoord> shell(int radius) {
		Set<HexCoord> result = new HashSet<>();
		if(radius == 0) {
			result.add(new HexCoord(0, 0));
			return result;
		}
		
		for(int i = 0; i < radius; i++) {
			result.add(new HexCoord(radius, i));//S up to SE
			result.add(new HexCoord(radius - i, radius)); //SE to NE
			result.add(new HexCoord(-i, radius - i)); //NE to N
			result.add(new HexCoord(-radius, -i)); //N to NW
			result.add(new HexCoord(i - radius, -radius)); //NW to SW
			result.add(new HexCoord(i, i - radius)); //SW to S
		}
		
		return result;
	}
	
	public static Set<HexCoord> fullHex(int radius) {
		Set<HexCoord> result = new HashSet<>();
		for(int i = 0; i <= radius; i++) {
			result.addAll(shell(i));
		}
		return result;
	}
	
	public static Map<HexCoord, Integer> yieldPatch(Set<HexCoord> coords, int period) {
		Map<HexCoord, Integer> result = new HashMap<>();
		for(HexCoord hc: coords) {
			result.put(hc, period);
		}
		return result;
	}
	
	public static Tile makeTile(HexCoord hc, boolean hasBot, Bot bot, int actionPointPeriod) {
		return new Tile(new TileData(hc, hasBot, bot, actionPointPeriod, 0));
	}
	
	public static Map<HexCoord, Bot> getBotsFromCoords(Set<Bot> bots) {
		Map<HexCoord, Bot> result = new HashMap<>();
		for(Bot b: bots) {
			result.put(b.getLocation(), b);
		}
		return result;
	}
	
	public static Set<Tile> makeMap(Set<HexCoord> coords, Map<HexCoord, Integer> periods, Set<Bot> bots) {
		Set<Tile> result = new HashSet<>();
		Map<HexCoord, Bot> botsFromCoords = getBotsFromCoords(bots);
		for(HexCoord hc: coords) {
			if(botsFromCoords.containsKey(hc)) {
				result.add(makeTile(hc, true, botsFromCoords.get(hc), periods.getOrDefault(hc, 0)));
			}
			else {
				result.add(makeTile(hc, false, null, periods.getOrDefault(hc, 0)));
			}
		}
		return result;
	}
	
}
