package Part1;
// -------------------------------------------------------------------------------
// Assignment 2
// Questions: 1
// Written by: Luis Saravia Patron ID 26800505
// For COMP249 Section: U Winter 2016
// ------------------------------------------------------------------------------
/**
 * Exception class CopyCodeException, extends the Exception class. 
 * Should a copy publication code is detected at any point, an object from this class
 * will be thrown. 
 * @author Luis
 * @version 1.0
 */

public class CopyCodeException extends Exception {

	private long code_number;

	/**
	 * Default constructor with no arguments
	 */
	public CopyCodeException() {
		super("Copy of a publication code detected!");
		code_number = 0;
	}

	
	/**
	 * Parameterized constructor
	 * @param long type code number
	 */
	public CopyCodeException(long code)	{
		super("Copy of publication code " + code + " detected");
		code_number = code;
	}

	/**
	 * Parameterized constructor
	 * @param String object message
	 */
	public CopyCodeException(String message) {
		super(message);
		code_number = 0;
	}

	/**
	 * @return the code_number
	 */
	public long getCode_number() {
		return code_number;
	}
	
}
