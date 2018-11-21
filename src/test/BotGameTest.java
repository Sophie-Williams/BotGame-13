package test;

import javax.swing.JFrame;

//import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Map;
//import java.util.Scanner;
import java.util.Set;

import org.python.util.PythonInterpreter;

import utils.BasicAction;
import utils.BasicGameRunner;
import utils.Bot;
import utils.Direction;
import utils.GameRunner;
import utils.GameState;
import utils.HexCoord;
import utils.Maps;
import utils.MetabolismPeriodGetters;
import utils.Side;
import utils.Tile;
import utils.TileData; 

public final class BotGameTest {
	
	private static void pythonTest() {
		PythonInterpreter.initialize(System.getProperties(),  
				 System.getProperties(),
				 new String[0]);

		PythonInterpreter pi = new PythonInterpreter();
		
		//File pyFile = new File("pytest.py");
		//Scanner pyScanner = new Scanner(pyFile);
		
		//pi.exec("print(\'Hello World!\')");
		pi.execfile("src\\test\\pytest.py");
		pi.close();
	}
	
	private static void gameTest() {
		JFrame frame = new JFrame("Bot Game");
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static GameRunner testRunner() {
		Side s = new Side(0, "Only Side", () -> TestBotControllers.makeConstantActionController(BasicAction.MOVE));
		Bot b = new Bot(new HexCoord(0, 0),
						Direction.NORTH_EAST,
						MetabolismPeriodGetters.constantMetabolismPeriodGetter(0),
						TestBotControllers.makeConstantActionController(BasicAction.MOVE),
						s);
		Set<Bot> bots = new HashSet<>();
		bots.add(b);
		
		Set<HexCoord> coords = Maps.fullHex(5);
		
		
		Set<HexCoord> smallPatch = Maps.fullHex(1);
		Map<HexCoord, Integer> periods = Maps.yieldPatch(smallPatch, 1);
		Set<HexCoord> smallShell = Maps.shell(2);
		periods.putAll(Maps.yieldPatch(smallShell, 2));
		Set<HexCoord> largeShell = Maps.shell(3);
		largeShell.addAll(Maps.shell(4));
		periods.putAll(Maps.yieldPatch(largeShell, 4));
		
		//Map<HexCoord, Integer> periods = Maps.yieldPatch(coords, 1);
		
		Set<Tile> tiles = Maps.makeMap(coords, periods, bots);
		
		GameState startState = new GameState(0, bots, tiles);
		GameRunner runner = new BasicGameRunner(startState);
		return runner;
	}
	
	private static void runnerTest() {
		Side s = new Side(0, "Only Side", () -> TestBotControllers.makeConstantActionController(BasicAction.LOOK));
		Bot b = new Bot(new HexCoord(0, 0),
						Direction.NORTH_EAST,
						MetabolismPeriodGetters.constantMetabolismPeriodGetter(3),
						TestBotControllers.makeConstantActionController(BasicAction.LOOK),
						s);
		Set<Bot> bots = new HashSet<>();
		bots.add(b);
		
		Set<HexCoord> coords = Maps.fullHex(5);
		Map<HexCoord, Integer> periods = Maps.yieldPatch(coords, 1);
		Set<Tile> tiles = Maps.makeMap(coords, periods, bots);
		
		GameState startState = new GameState(0, bots, tiles);
		GameRunner runner = new BasicGameRunner(startState);
		
		for(int i = 0; i < 10; i++) {
			System.out.println("Action points: " + b.getActionPoints());
			//System.out.println("Location: " + b.getLocation());
			//System.out.println("");
			runner.advance();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		//gameTest();
		runnerTest();
	}

}
