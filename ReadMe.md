To access the code you can open the directory IsrProgrammingTask in Eclipse. 
To run all tests run AllTests.java jUnit TestSuite.
The main method will not run, unless a input and import directory are created in the IsrProgrammingsTask folder.

The runnable jar file can be found in RunnableExample.
When it is run without parameters it will try to perform the task on the directories called "input" and "import" in the same directory as it. 
Thereby the Symbols '!' and '&' will be replaced with '_'.
To change the paths or forbidden symbols run via command line and pass parameters.

Run possibilities:
	no parameters:

		java -jar IsrProgrammingTask.jar

	set custom forbidden symbols:

		java -jar IsrProgrammingTask.jar forbiddenSymbols
		Example:
			java -jar IsrProgrammingTask.jar !^&[]{} 
			(Note, that the '&' has to be lead by a '^', as it would be interpreted by the console otherwise)

	set custom forbidden symbols and custom relative paths 
	(if default forbidden symbols should be used, use keyword default)

		java -jar IsrProgrammingTask.jar forbiddenSymbols /inputPath /importPath
		Example:
			java -jar IsrProgrammingTask.jar !^&[]{} /../input /../import
			java -jar IsrProgrammingTask.jar default /../input /../import
 	