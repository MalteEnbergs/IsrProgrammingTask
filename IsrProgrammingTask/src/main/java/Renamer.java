
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Renamer {
	private static final Logger logger = LogManager.getLogger(Renamer.class);

	//variable declarations and defaults
	private Path inputPath= Paths.get(System.getProperty("user.dir") + "/input");   
	private Path importPath= Paths.get(System.getProperty("user.dir") + "/import");
	private String forbiddenSymbols = "!&";
	
	//logging messages to have a single point of truth with tests
	private Map <String, String> loggingMessages = Stream.of(new String[][] {
		{"defaultForbiddenSymbols","Using default forbidden symbols."},
		{"defaultPaths","Using path defaults."},
		{"importPathError","Import path does not exist."},
		{"inputPathError","Input path does not exist."},
		{"pathSuccess","Paths confirmed."},
		{"fileNotMovable", "File %s could not be moved."}
	}).collect(Collectors.toMap(data -> data[0], data -> data[1]));
	
	public Renamer() {}
	
	/**
	 * Checks the given parameters and if successful the renaming and moving process
	 * @param paths
	 */
	public void start(String[] parameters) {
		logger.info("Starting.");
		if(this.readGivenParameters(parameters)) {
			this.startMoveAndRename();
			logger.info("Done.");
		}else
			logger.info("Process aborted.");
	}
	
	/**
	 * Takes the parameters given upon execution of the jar file and stores them in variables, 
	 * if none are provided the defaults remain
	 * @param parameters string parameters given to the main method
	 *  - parameters[0] = forbiddenSymbols (if "default" the defaults are kept)
	 *  - parameters[1] = InputPath
	 *  - parameters[2] = ImportPath
	 * @return true, if paths exist and program can start running
	 */
	public boolean readGivenParameters(String[] parameters) {
		boolean symbolsProvided = false;
		try { 
			if(parameters[0].compareToIgnoreCase("default")!=0) {
				forbiddenSymbols = parameters[0];
				symbolsProvided = true;
			}
			inputPath = Paths.get(System.getProperty("user.dir") + parameters[1]).normalize();
			importPath = Paths.get(System.getProperty("user.dir") + parameters[2]).normalize();
		}catch(ArrayIndexOutOfBoundsException e){
			logger.info(loggingMessages.get("defaultPaths"));
		}
		if(!symbolsProvided)
			logger.info(loggingMessages.get("defaultForbiddenSymbols"));
		
		boolean pathError = false;
		if(!Files.exists(importPath)) {
			logger.error(loggingMessages.get("importPathError"));
			pathError = true;
		}
		if(!Files.exists(inputPath)) {
			logger.error(loggingMessages.get("inputPathError"));
			pathError = true;
		}
		if(!pathError)
			logger.info(loggingMessages.get("pathSuccess"));
		return !pathError;
	}
	
	/**
	 * Method, which calls the moveAndRename method for every file in the folder
	 */
	public void startMoveAndRename() {
		try {
			Files.list(inputPath).forEach(file -> this.moveAndRename(file));
		} catch (IOException e) {
			logger.error(loggingMessages.get("inputPathError"));
		}
	}
	
	/**
	 * Renames the file of the given filePath and tries to move it to the import directory (logs error message, if not possible)
	 * @param filePath
	 */
	private void moveAndRename(Path filePath) {
		String fileName = filePath.getFileName().toString();
		String newFileName = fileName.replaceAll("[" + regexProofSymbols(forbiddenSymbols) + "]", "_");
		try {
			Files.move(filePath, importPath.resolve(newFileName));
		} catch (IOException e) {
			logger.error(String.format(loggingMessages.get("fileNotMovable"),fileName));
		}
	}
	
	/**
	 * Makes sure, that all symbols have a '\' in front, as this way they are only used as symbols in the regex
	 * @param stringWithSymbols
	 * @return
	 */
	public String regexProofSymbols(String stringWithSymbols) {
		String returnString = "";
		for(char symbol : stringWithSymbols.toCharArray())
			returnString += "\\" + symbol;
		return returnString;
	}

	// Getters and setters
	public Path getInputPath() {
		return inputPath;
	}

	public void setInputPath(Path inputPath) {
		this.inputPath = inputPath;
	}

	public Path getImportPath() {
		return importPath;
	}

	public void setImportPath(Path importPath) {
		this.importPath = importPath;
	}

	public String getForbiddenSymbols() {
		return forbiddenSymbols;
	}

	public void setForbiddenSymbols(String forbiddenSymbols) {
		this.forbiddenSymbols = forbiddenSymbols;
	}

	public Map<String, String> getLoggingMessages() {
		return loggingMessages;
	}
	
	
	
	
	
}
