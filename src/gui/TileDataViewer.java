package gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.Bot;
import utils.TileData;

public final class TileDataViewer extends JPanel implements TileDataReceiver {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7666512380684705049L;

	//private final JLabel tileLabelT = new JLabel("Tile");
	//private final JLabel tileLabel = new JLabel("Tile");
	private final JLabel coordinateLabelT = new JLabel("Tile: ");
	private final JLabel coordinateLabel = new JLabel("");
	private final JLabel periodLabelT = new JLabel("Period: ");
	private final JLabel periodLabel = new JLabel("");
	private final JLabel stepLabelT = new JLabel("Step: ");
	private final JLabel stepLabel = new JLabel("");
	
	private final JLabel botLabelT = new JLabel("Bot");
	private final JLabel botLabel = new JLabel("");
	private final JLabel sideLabelT = new JLabel("Side: ");
	private final JLabel sideLabel = new JLabel("");
	private final JLabel directionLabelT = new JLabel("Direction: ");
	private final JLabel directionLabel = new JLabel("");
	private final JLabel apLabelT = new JLabel("Action Points: ");
	private final JLabel apLabel = new JLabel("");
	private final JLabel metabolismLabelT = new JLabel("Metabolism: ");
	private final JLabel metabolismLabel = new JLabel("");
	private final JLabel metabolismStepLabelT = new JLabel("Metabolism Step: ");
	private final JLabel metabolismStepLabel = new JLabel("");
	private final JLabel armorLabelT = new JLabel("Armor: ");
	private final JLabel armorLabel = new JLabel("");
	private final JLabel flagLabelT = new JLabel("Flag: ");
	private final JLabel flagLabel = new JLabel("");
	private final JLabel permFlagSetLabelT = new JLabel("Permanent Flag Set: ");
	private final JLabel permFlagSetLabel = new JLabel("");
	private final JLabel permFlagLabelT = new JLabel("Permanent Flag: ");
	private final JLabel permFlagLabel = new JLabel("");
	
	private final List<JLabel> tileTLabels = new ArrayList<>();
	private final List<JLabel> tileLabels = new ArrayList<>();
	
	private final List<JLabel> botTLabels = new ArrayList<>();
	private final List<JLabel> botLabels = new ArrayList<>();
	
	public TileDataViewer() {
		//tileTLabels.add(tileLabel);
		tileTLabels.add(coordinateLabelT);
		tileTLabels.add(periodLabelT);
		tileTLabels.add(stepLabelT);
		
		tileLabels.add(coordinateLabel);
		tileLabels.add(periodLabel);
		tileLabels.add(stepLabel);

		botTLabels.add(botLabelT);
		botTLabels.add(sideLabelT);
		botTLabels.add(directionLabelT);
		botTLabels.add(apLabelT);
		botTLabels.add(metabolismLabelT);
		botTLabels.add(metabolismStepLabelT);
		botTLabels.add(armorLabelT);
		botTLabels.add(flagLabelT);
		botTLabels.add(permFlagSetLabelT);
		botTLabels.add(permFlagLabelT);

		botLabels.add(botLabel);
		botLabels.add(sideLabel);
		botLabels.add(directionLabel);
		botLabels.add(apLabel);
		botLabels.add(metabolismLabel);
		botLabels.add(metabolismStepLabel);
		botLabels.add(armorLabel);
		botLabels.add(flagLabel);
		botLabels.add(permFlagSetLabel);
		botLabels.add(permFlagLabel);
		/*
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		add(tileLabel);
		
		JPanel tilePanel = new JPanel();
		add(tilePanel);
		tilePanel.setLayout(new BoxLayout(tilePanel, BoxLayout.X_AXIS));
		
		JPanel tilePanelC1 = new JPanel();
		tilePanel.add(tilePanelC1);
		tilePanelC1.setLayout(new BoxLayout(tilePanelC1, BoxLayout.Y_AXIS));
		tilePanelC1.add(coordinateLabelT);
		tilePanelC1.add(periodLabelT);
		tilePanelC1.add(stepLabelT);
		
		coordinateLabelT.setAlignmentX(RIGHT_ALIGNMENT);
		periodLabelT.setAlignmentX(RIGHT_ALIGNMENT);
		stepLabelT.setAlignmentX(RIGHT_ALIGNMENT);
		
		JPanel tilePanelC2 = new JPanel();
		tilePanel.add(tilePanelC2);
		tilePanelC2.setLayout(new BoxLayout(tilePanelC2, BoxLayout.Y_AXIS));
		tilePanelC2.add(coordinateLabel);
		tilePanelC2.add(periodLabel);
		tilePanelC2.add(stepLabel);
		
		add(botLabel);
		
		JPanel botPanel = new JPanel();
		add(botPanel);
		botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.X_AXIS));
		
		JPanel botPanelC1 = new JPanel();
		botPanelC1.setLayout(new BoxLayout(botPanelC1, BoxLayout.Y_AXIS));
		botPanel.add(botPanelC1);
		
		JPanel botPanelC2 = new JPanel();
		botPanelC2.setLayout(new BoxLayout(botPanelC2, BoxLayout.Y_AXIS));
		botPanel.add(botPanelC2);
		
		for(JLabel l: botTLabels) {
			botPanelC1.add(l);
			l.setAlignmentX(RIGHT_ALIGNMENT);
		}
		for(JLabel l: botLabels) {
			botPanelC2.add(l);
		}
		*/
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel c1 = new JPanel();
		add(c1);
		JPanel c2 = new JPanel();
		add(c2);
		
		c1.setLayout(new BoxLayout(c1, BoxLayout.Y_AXIS));
		c2.setLayout(new BoxLayout(c2, BoxLayout.Y_AXIS));
		
		for(JLabel l: tileTLabels) {
			c1.add(l);
			l.setAlignmentX(RIGHT_ALIGNMENT);
		}
		for(JLabel l: botTLabels) {
			c1.add(l);
			l.setAlignmentX(RIGHT_ALIGNMENT);
		}
		for(JLabel l: tileLabels) {
			c2.add(l);
		}
		for(JLabel l: botLabels) {
			c2.add(l);
		}
	}
	
	private String flagToString(boolean[] flag) {
		String result = "";
		for(int i = 0; i < flag.length; i++) {
			result += flag[i]? "1": "0";
		}
		return result;
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 800);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return new Dimension(310, 800);
	}
	
	@Override
	public void giveData(TileData td) {
		coordinateLabel.setText("" + td.getCoord());
		periodLabel.setText("" + td.getActionPointPeriod());
		stepLabel.setText("" + td.getActionPointPeriodStep());
		
		if(td.hasBot()) {
			botLabelT.setText("Bot: ");
			
			Bot b = td.getBot();
			botLabel.setText("True");
			sideLabel.setText("" + b.getSide());
			directionLabel.setText("" + b.getFacing());
			apLabel.setText("" + b.getActionPoints());
			metabolismLabel.setText("" + b.getMetabolism());
			metabolismStepLabel.setText("" + b.getMetabolismStep());
			armorLabel.setText("" + b.getArmor());
			flagLabel.setText("" + flagToString(b.getFlag()));
			permFlagSetLabel.setText("" + b.getPermanentFlagSet());
			permFlagLabel.setText("" + flagToString(b.getPermanentFlag()));
		}
		else {
			botLabelT.setText("Bot: ");
			for(JLabel l: botLabels) {
				l.setText("N/A");
			}
			//botLabel.setText("False");
		}
	}

}
