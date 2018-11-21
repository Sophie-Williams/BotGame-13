package test;

import utils.Action;
import utils.Bot;
import utils.BotController;
import utils.Surroundings;

public abstract class TestBotControllers {
	private TestBotControllers() {
		throw new AssertionError("Uninstantiable");
	}
	
	public static BotController makeConstantActionController(Action a) {
		return new BotController() {

			@Override
			public Action getAction(Bot bot, Surroundings surroundings) {
				return a;
			}
			
		};
	}
}
