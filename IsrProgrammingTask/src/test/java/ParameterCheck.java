import static org.junit.Assert.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import nl.altindag.log.LogCaptor;

public class ParameterCheck {
	
	private final static LogCaptor logCaptor = LogCaptor.forClass(Renamer.class);
	private Renamer renamer = new Renamer();
	/**
	 * Importing default values from the reader
	 * Therefore not hardcoded, so only confirming that the values not change, but not that there are right.
	 */
	private final Path defaultInputPath = renamer.getInputPath();
	private final Path defaultImportPath = renamer.getImportPath();
	private final List<Path> defaultPaths = Arrays.asList(defaultInputPath, defaultImportPath);
	private final String defaultForbiddenSymbols = renamer.getForbiddenSymbols();
	private final Map<String,String> loggingMessages = renamer.getLoggingMessages();
	
	
	/**
	 * Creates new Renamer to reset default values
	 */
	@Before
	public void resetRenamer() {
		renamer = new Renamer();
	}
		
	/**
	 * Reset the logs after each test
	 */
	@After
	public void resetLogs() {
		logCaptor.clearLogs();
		System.out.println();
	}
	
	
	/**
	 * When no parameters are given
	 * Tests if the defaults are kept and the appropriate logs are shown, that the defaults are kept
	 * Check correct return value (true for Successful)
	 */
	@Test
	public void checkIfDefaultsAreKept(){
		//given
		Support.createDirectories(defaultPaths);
		String[] parameters = new String[0];
		
		//when
		boolean returnValue = renamer.readGivenParameters(parameters);	
		
		//then
		try {
		assertTrue("No default forbidden symbols log", logCaptor.getInfoLogs().contains(loggingMessages.get("defaultForbiddenSymbols")));
		assertTrue("No default paths log",logCaptor.getInfoLogs().contains(loggingMessages.get("defaultPaths")));
		assertTrue("No success log",logCaptor.getInfoLogs().contains(loggingMessages.get("pathSuccess")));
		
		assertEquals("Input Path changed", defaultInputPath, renamer.getInputPath());
		assertEquals("Import Path changed", defaultImportPath, renamer.getImportPath());
		assertEquals("Forbidden Symbols changed", defaultForbiddenSymbols, renamer.getForbiddenSymbols());
		
		assertTrue("Wrong return value", returnValue);
		//cleanup
		}finally {
			Support.removeDirectories(defaultPaths);
		}
	}
	
	/**
	 * Check if "default" keyword works
	 * Check if wrong paths are detected and appropriate errors are displayed 
	 * Check correct return value (false for Failure)
	 */
	@Test
	public void checkIfDefaultKewordWorksAndWrongPathsAreDetected(){
		//given
		String wrongInputPath = "not/a/real/input/path";
		String wrongImportPath = "not/a/real/import/path";
		String[] parameters = {"default", wrongInputPath, wrongImportPath};
		
		//when
		boolean returnValue = renamer.readGivenParameters(parameters);

		//then
		assertTrue("No default forbidden symbols log", logCaptor.getInfoLogs().contains(loggingMessages.get("defaultForbiddenSymbols")));
		assertEquals("Forbidden Symbols changed", defaultForbiddenSymbols, renamer.getForbiddenSymbols());
		
		assertTrue("No input path error log", logCaptor.getErrorLogs().contains(loggingMessages.get("inputPathError")));
		assertTrue("No import path error log", logCaptor.getErrorLogs().contains(loggingMessages.get("importPathError")));
		assertFalse("Faulty Success log", logCaptor.getInfoLogs().contains(loggingMessages.get("pathSuccess")));
	
		assertFalse("Wrong return value", returnValue);
	}
	
	/**
	 * Test if given parameters are taken
	 * Check correct return value (true for Successful)
	 */
	@Test
	public void checkIfCorrectParametersAreTaken(){
		//given
		String inputPathChange = "/../input";
		String importPathChange = "/../import";
		Path newInputPath = Paths.get(System.getProperty("user.dir") + inputPathChange).normalize();
		Path newImportPath = Paths.get(System.getProperty("user.dir") + importPathChange).normalize();
		List<Path> newPaths = Arrays.asList(newInputPath, newImportPath);
		Support.createDirectories(newPaths);
		
		String newForbiddenSymbols = "&%\"";
		String[] parameters = {newForbiddenSymbols, inputPathChange, importPathChange};
		
		//when
		boolean returnValue = renamer.readGivenParameters(parameters);	
		
		//then
		try {
		assertTrue("No success log", logCaptor.getInfoLogs().contains(loggingMessages.get("pathSuccess")));
		
		assertEquals("Input path not set",newInputPath, renamer.getInputPath());
		assertEquals("Import path not set",newImportPath, renamer.getImportPath());
		assertEquals("Forbidden symbols not set",newForbiddenSymbols, renamer.getForbiddenSymbols());

		assertTrue("Wrong return value", returnValue);
		//cleanup
		}finally{
			Support.removeDirectories(newPaths);
		}
	}
	
	
}
