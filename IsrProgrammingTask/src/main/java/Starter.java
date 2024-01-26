
public class Starter {
	
	/**
	 * Main method, creates a Renamer and starts the renaming process
	 * @param args parameters given to jar file (if none are provided the defaults are kept)
	 *  - args[0] = forbiddenSymbols (if "default" the defaults are kept)
	 *  - args[1] = InputPath
	 *  - args[2] = ImportPath
	 */
	public static void main(String [] args) {
		Renamer renamer = new Renamer();
		renamer.start(args);
		
	}
		
}
