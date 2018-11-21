package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Scanner;

import org.python.core.PyCode;
import org.python.core.PyInstance;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import utils.Action;
import utils.Bot;
import utils.BotController;
import utils.Surroundings;

public final class PythonBotController implements BotController {

	private final PyObject controller;
	
	private final static PythonInterpreter interpreter;
	
	static {
		PythonInterpreter.initialize(System.getProperties(), System.getProperties(), new String[0]);
		interpreter = new PythonInterpreter();
	}
	
	public PythonBotController(File f) {
		//Scanner fileInput = new Scanner(f);
		Reader reader = null;
		try {
			reader = new FileReader(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		PyCode moduleCode = interpreter.compile(reader);
		interpreter.eval(moduleCode);
		controller = interpreter.get("control_def").__getattr__("make_control");
	}
	
	@Override
	public Action getAction(Bot bot, Surroundings surroundings) {
		// TODO Auto-generated method stub
		return null;
	}

}
