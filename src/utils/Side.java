package utils;

import java.util.function.Supplier;

public final class Side {
	private final int id;
	private final String name;
	private final Supplier<BotController> botControllerSupplier;
	
	public Side(int id, String name, Supplier<BotController> botControllerSupplier) {
		this.id = id;
		this.name = name;
		this.botControllerSupplier = botControllerSupplier;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public BotController makeBotController() {
		return botControllerSupplier.get();
	}
	
	@Override
	public String toString() {
		return "(" + id + ", \"" + name + "\")"; 
	}
}
