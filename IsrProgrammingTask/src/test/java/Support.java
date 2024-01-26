import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Support {
	/**
	 * Tries to create a file and fails the test, if that is not possible
	 * @param path
	 */
	public static void createFile(Path path){
		try {
			if(Files.isDirectory(path))
				Files.createDirectory(path);
			else
				Files.createFile(path);
		}catch(IOException e) {
			fail("File for path " + path.toString() + " could not be created.");
		}
	}
	
	/**
	 * Tries to create a directory and fails the test, if that is not possible
	 * @param path
	 */
	public static void createDirectory(Path path){
		try {
			Files.createDirectory(path);
		}catch(IOException e) {
			fail("Directory for path " + path.toString() + " could not be created.");
		}
	}
	
	/**
	 * Tries to remove a file or directory (depending on the given path) and fails the test, if that is not possible
	 * @param path
	 */
	public static void removeFileOrDirectory(Path path){
		try {
			if(Files.isDirectory(path))
				Files.list(path).forEach(filePath -> Support.removeFileOrDirectory(filePath));
			Files.deleteIfExists(path);
		}catch(IOException e) {
			fail("File or directory for path " + path.toString() + " could not be removed.");
		}
	}
	
	/**
	 * Creates all directories contained in the given List
	 * @param paths
	 */
	public static void createDirectories(List<Path> paths) {
		paths.forEach(path -> Support.createDirectory(path));
	}
	
	/**
	 * Removes all directories contained in the given List
	 * @param paths
	 */
	public static void removeDirectories(List<Path> paths) {
		paths.forEach(path -> Support.removeFileOrDirectory(path));
	}
	
	/**
	 * Creates the files, whose names are contained in the list fileNames in the given directory
	 * @param fileNames
	 * @param directoryPath
	 */
	public static void createFilesInDirectory(List<String> fileNames,Path directoryPath) {
		fileNames.forEach(
				fileName -> 
				Support.createFile(
						directoryPath.resolve(fileName)
						)
				);
	}
}
