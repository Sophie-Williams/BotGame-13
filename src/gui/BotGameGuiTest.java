package gui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.util.concurrent.TimeUnit;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import test.BotGameTest;
import utils.GameRunner;

public final class BotGameGuiTest {

	private final static GameRunner runner = BotGameTest.testRunner();
	
	public static void main(String[] args) throws InterruptedException {
		JFrame frame = new JFrame("Bots");
		BotGamePanel panel = new BotGamePanel(runner);

		Container pane = frame.getContentPane();
		BoxLayout layout = new BoxLayout(pane, BoxLayout.Y_AXIS);
		frame.setLayout(layout);
		
		JButton advanceButton = new JButton("Advance");
		pane.add(advanceButton);
		advanceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel.advance();
			}
		});
		
		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.X_AXIS));
		pane.add(gamePanel);
		
		TileDataViewer td = new TileDataViewer();
		panel.addTileDataReceiver(td);
		gamePanel.add(td);
		
		JScrollPane scrollPane = new JScrollPane(panel);
		//pane.add(scrollPane);
		gamePanel.add(scrollPane);
		
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		//while(true) {
		//	TimeUnit.MILLISECONDS.sleep(1000);
		//	panel.advance();
		//}
	}

}
