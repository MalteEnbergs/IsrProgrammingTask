import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import nl.altindag.log.LogCaptor;

public class RenamingCheck {
	
	private final static LogCaptor logCaptor = LogCaptor.forClass(Renamer.class);
	private Renamer renamer = new Renamer();
	private final Path defaultInputPath = renamer.getInputPath();
	private final Path defaultImportPath = renamer.getImportPath();
	private final List<Path> defaultPaths = Arrays.asList(defaultInputPath, defaultImportPath);
	private final Map<String,String> loggingMessages = renamer.getLoggingMessages();

	
	/**
	 * Cleanup directories and logs
	 */
	@After
	public void clearDirectoriesAndLogs() {
		Support.removeDirectories(defaultPaths);
		logCaptor.clearLogs();
	}
	
	/**
	 * Create directories
	 */
	@Before
	public void createDirectories() {
		Support.createDirectories(defaultPaths);
	}
	
	/**
	 * Tries a successful renaming and moving of three files
	 * @throws IOException
	 */
	@Test
	public void SuccessfulMovingAndRenamingOfThreeFiles() throws IOException{
		List<String> testFilesInput = Arrays.asList(
				"testFile123.txt", "testFile!&.txt", "testFile1!3.txt");
		Support.createFilesInDirectory(testFilesInput, defaultInputPath);
		
		renamer.startMoveAndRename();
		
		List<String> testFilesOutputNames = Arrays.asList(
				"testFile123.txt", "testFile__.txt", "testFile1_3.txt");
		
		assertEquals("Input folder not Empty.", 0, Files.list(defaultInputPath).count());
		Files.list(defaultImportPath).forEach(file -> assertTrue(file.getFileName() + " File not present.",
				testFilesOutputNames.contains(file.getFileName().toString())));
	}
	
	/**
	 * Contains an error, as two files will result in the same name after renaming
	 * checks if 
	 * - the error is logged
	 * - the file stays in input
	 * - the process continues
	 * @throws IOException
	 */
	@Test
	public void sameNameAfterRenameError() throws IOException {
		List<String> testFilesInput = Arrays.asList(
				"!.txt", "&.txt", "also_to_rename!.txt");
		Support.createFilesInDirectory(testFilesInput, defaultInputPath);
		
		renamer.startMoveAndRename();
		
		assertTrue("Error file still deleted", Files.exists(defaultInputPath.resolve("&.txt")));
		assertTrue("No error log", logCaptor.getErrorLogs().contains(String.format(loggingMessages.get("fileNotMovable"),"&.txt")));
		assertTrue("First file not moved", Files.exists(defaultImportPath.resolve("_.txt")));
		assertTrue("First file not moved", Files.exists(defaultImportPath.resolve("also_to_rename_.txt")));
		assertEquals("Wrong File count in input folder.", 1, Files.list(defaultInputPath).count());
		assertEquals("Wrong file count in import folder.", 2, Files.list(defaultImportPath).count());
	}
	
	/**
	 * Same scenario as SuccessfulMovingAndRenamingOfThreeFiles, but as a full run
	 * @throws IOException
	 */
	@Test
	public void fullRun() throws IOException{
		List<String> testFilesInput = Arrays.asList(
				"testFile123.txt", "testFile!&.txt", "testFile1!3.txt");
		Support.createFilesInDirectory(testFilesInput, defaultInputPath);
		
		Starter.main(new String[0]);
		
		List<String> testFilesOutputNames = Arrays.asList(
				"testFile123.txt", "testFile__.txt", "testFile1_3.txt");
		
		assertEquals("Input folder not Empty.", 0, Files.list(defaultInputPath).count());
		Files.list(defaultImportPath).forEach(file -> assertTrue(file.getFileName() + " File not present.",
				testFilesOutputNames.contains(file.getFileName().toString())));
	}
	
	/**
	 * Test if the regex proofing works as expected
	 */
	@Test
	public void testRegexProofing() {
		String input = "&[]}{";
		String expectedOutput = "\\&\\[\\]\\}\\{";
		
		String actualOutput = renamer.regexProofSymbols(input);
		
		assertEquals(expectedOutput, actualOutput);
		
		String testFilename = "123&}[.test";
		String expectedNewFileName = "123___.test";
		String actualNewFileName = testFilename.replaceAll("[" + renamer.regexProofSymbols(actualOutput) + "]", "_");
		
		assertEquals(expectedNewFileName, actualNewFileName);
		
	}

}
