import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class SupportTest {

	@Test
	public void createAndRemoveFile() {
		Path filePath = Paths.get(System.getProperty("user.dir") + "test.txt");
		
		assertFalse("File existed before beginning.", Files.exists(filePath));
		
		Support.createFile(filePath);
		
		assertTrue("File was not created.", Files.exists(filePath));
		
		Support.removeFileOrDirectory(filePath);
		
		assertFalse("File was not removed", Files.exists(filePath));
	}
	
	@Test
	public void createAndRemoveDirectory() {
		Path directoryPath = Paths.get(System.getProperty("user.dir")).resolve("test");
		
		assertFalse("Directory existed before beginning.", Files.isDirectory(directoryPath));
		
		Support.createDirectory(directoryPath);
		
		assertTrue("Directory was not created.", Files.isDirectory(directoryPath));
		
		Support.removeFileOrDirectory(directoryPath);
		
		assertFalse("Directory was not removed", Files.isDirectory(directoryPath));
	}
	
	
	@Test
	public void createAndRemoveDirectoryLists() {
		Path firstPath= Paths.get(System.getProperty("user.dir") + "/first");   
		Path secoundPath= Paths.get(System.getProperty("user.dir") + "/secound");
		List<Path> paths = Arrays.asList(firstPath, secoundPath);
		
		assertFalse("Directories existed before beginning.", Files.exists(firstPath) || Files.exists(secoundPath));
		
		Support.createDirectories(paths);
		
		assertTrue("Directories were not created.", Files.exists(firstPath) && Files.exists(secoundPath));
		
		Support.removeDirectories(paths);
		
		assertFalse("Directories were not removed", Files.exists(firstPath) || Files.exists(secoundPath));
	}
	
	@Test
	public void createFilesInDirectory() {
		Path directoryPath = Paths.get(System.getProperty("user.dir"));
		String fileName = "test.txt";
		Path filePath = directoryPath.resolve(fileName);

		assertFalse("File existed before beginning.", Files.exists(filePath));
		
		Support.createFilesInDirectory(Arrays.asList(fileName), directoryPath);
		
		assertTrue("File was not created.", Files.exists(filePath));
		
		Support.removeFileOrDirectory(filePath);
		
		assertFalse("File was not removed", Files.exists(filePath));
	}

}
