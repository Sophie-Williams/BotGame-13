package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.GeneralPath;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;

import utils.Bot;
import utils.GameRunner;
import utils.HexCoord;
import utils.Tile;

public final class BotGamePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4274054140085157230L;
	
	private GameRunner runner;
	private Point normPoint;
	private final Set<TileDataReceiver> tileDataReceivers = new HashSet<>();
	
	private static final int WIDTH = 2500;
	private static final int HEIGHT = 2500;
	private static final int HEX_RADIUS = 30;
	private static final int HEX_WIDTH = 2* HEX_RADIUS;
	private static final int HEX_HEIGHT = (int) (Math.sqrt(3) * HEX_RADIUS);
	
	private static final int BOT_RADIUS = 15;
	
	private static final int X_HEX_BUFFER = 30;
	private static final int Y_HEX_BUFFER = 30;
	
	public BotGamePanel(GameRunner runner) {
		this.runner = runner;
		
		this.addMouseListener(new BotGamePanelMouseListener(this));
		
		repaint();
	}
	
	public void addTileDataReceiver(TileDataReceiver tdr) {
		this.tileDataReceivers.add(tdr);
	}
	
	public void removeTileDataReceiver(TileDataReceiver tdr) {
		this.tileDataReceivers.remove(tdr);
	}
	
	public void advance() {
		runner.advance();
		
		repaint();
	}
	
	private HexCoord coordFromClick(int x, int y) {
		x += normPoint.x - 60;
		y += normPoint.y - 80;
		
		double dx = x;
		double dy = y;
		
		double ne = dx / (HEX_WIDTH * 0.75);
		double s = dy / HEX_HEIGHT + ne / 2;
		ne = Math.round(ne);
		s = Math.round(s);
		
		return new HexCoord((int)ne, (int)s);
	}
	
	public void registerClick(int x, int y) {
		HexCoord hc = coordFromClick(x, y);
		Tile t = runner.getGameState().tileFromCoord(hc);
		for(TileDataReceiver tdr: tileDataReceivers) {
			tdr.giveData(t.getData());
		}
	}
	
	private Point getPointFromHexCoords(HexCoord hc) { //hc normalized so that (0, 0) is upper-left most hex
		int x = (int) (X_HEX_BUFFER + HEX_RADIUS + 0.75 * hc.getNorthEast() * HEX_WIDTH);
		int y = (int) (Y_HEX_BUFFER + (1 + hc.getSouth() - 0.5 * hc.getNorthEast()) * HEX_HEIGHT);
		return new Point(x, y);
	}
	
	private void drawHex(int x, int y, Color fill, Graphics2D g) {
		GeneralPath hexPath = new GeneralPath();
		
		int hw = HEX_WIDTH / 2;
		int hh = HEX_HEIGHT / 2;
		hexPath.moveTo(x - hw / 2, y - hh); //Make hexagon
		hexPath.lineTo(x + hw / 2, y - hh);
		hexPath.lineTo(x + hw, y);
		hexPath.lineTo(x + hw / 2, y + hh);
		hexPath.lineTo(x - hw / 2, y + hh);
		hexPath.lineTo(x - hw, y);
		
		hexPath.closePath();
		
		g.setColor(fill);
		g.fill(hexPath);
		
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g.draw(hexPath);
	}
	
	private Set<HexCoord> coordsFromTiles(Set<Tile> tiles) {
		Set<HexCoord> result = new HashSet<>();
		for(Tile t: tiles) {
			result.add(t.getData().getCoord());
		}
		return result;
	}
	
	private Point normalizePoint(Set<HexCoord> hexes) {
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		
		for(HexCoord hc: hexes) {
			Point p = getPointFromHexCoords(hc);
			if(p.x < minX) {
				minX = p.x;
			}
			if(p.y < minY) {
				minY = p.y;
			}
		}
		
		return new Point(minX - X_HEX_BUFFER - HEX_WIDTH / 2, minY - Y_HEX_BUFFER - HEX_HEIGHT / 2);
	}
	
	private Color hexColor(int period) {
		if(period == 0) {
			return Color.WHITE;
		}
		float hue = 1.0f / 3;
		float saturation = (float) Math.sqrt(1.0 / period);
		float brightness = 1.0f;
		return Color.getHSBColor(hue, saturation, brightness);
	}
	
	private void paintHexes(Graphics g) {
		Set<Tile> tiles = runner.getGameState().getTiles();
		for(Tile t: tiles) {
			Point p = getPointFromHexCoords(t.getData().getCoord());
			int x = p.x;
			int y = p.y;
			drawHex(x, y, hexColor(t.getData().getActionPointPeriod()), (Graphics2D)g);
		}
	}
	
	private void paintBot(Bot b, Graphics2D g) {
		HexCoord hc = b.getLocation();
		Point point = getPointFromHexCoords(hc);
		int x = point.x;
		int y = point.y;
		
		g.setColor(Color.WHITE);
		g.fillOval(x - BOT_RADIUS, y - BOT_RADIUS, BOT_RADIUS * 2, BOT_RADIUS * 2);

		g.setStroke(new BasicStroke(3.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
		g.setColor(Color.BLACK);
		g.drawOval(x - BOT_RADIUS, y - BOT_RADIUS, BOT_RADIUS * 2, BOT_RADIUS * 2);
	}
	
	private void paintBots(Graphics g) {
		for(Bot b: runner.getGameState().getBots()) {
			paintBot(b, (Graphics2D)g);
		}
	}
	
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.BLACK);

		Set<Tile> tiles = runner.getGameState().getTiles();
		Set<HexCoord> hexes = coordsFromTiles(tiles);
		normPoint = normalizePoint(hexes);
		g.translate(-normPoint.x, -normPoint.y);
		
		paintHexes(g);
		paintBots(g);

		g.translate(normPoint.x, normPoint.y);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
}
