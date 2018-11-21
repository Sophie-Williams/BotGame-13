package gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class BotGamePanelMouseListener implements MouseListener {

	private final BotGamePanel panel;
	
	public BotGamePanelMouseListener(BotGamePanel panel) {
		this.panel = panel;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		panel.registerClick(e.getX(), e.getY());
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
	
}
