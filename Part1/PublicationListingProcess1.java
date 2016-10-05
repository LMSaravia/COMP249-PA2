package Part1;
//---------- ---------------------------------------------------------------------
//Assignment 2
//Questions: 1
//Written by: Luis Saravia Patron ID 26800505
//For COMP249 Section: U Winter 2016
//------------------------------------------------------------------------------

/**
 * This program reads a Publication inventory from a text file, allows the user to 
 * correct the repeated publication codes and then outputs the corrected inventory
 * to another text file.
 */

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Luis
 */
public class PublicationListingProcess1 {

	private static final String INPUT_FILE_NAME = "PublicationData_Input.txt";

	static Publication [] publicationArray;

	enum PublicationTypes {
		PUBLICATIONCODE,	
		PUBLICATIONNAME,
		PUBLICATIONYEAR,
		PUBLICATIONAUTHORNAME,
		PUBLICATIONCOST,
		PUBLICATIONNBPAGES
	}


	public static void main(String[] args) {	

		int numberOfRecords = 0;
		String outputFileName;
		FileReader inputStream = null;
		PrintWriter outputStream = null;

		System.out.println("Welcome to Publication Listing Process 1");			 
		System.out.println("Program written by Luis Saravia Patron");
		
		//	Get output file name from user
		System.out.println("Please enter a new file name to store the corrected"
				+ " inventory:");
		Scanner keyboard = new Scanner(System.in);
		outputFileName = keyboard.next(); // e.g. "PublicationData_Output.txt"
		
		// Create file object
		File outputFile = new File(outputFileName);

		// 	If the file exists reject and prompt again
		while(outputFile.exists()) {
			System.out.println(outputFile + " already exists.");
			System.out.println(outputFile + " size is " + outputFile.getTotalSpace() +
					" bytes.");
			System.out.println("Please enter a new file name.");

			keyboard.nextLine();	//	clear junk from line of input
			outputFileName = keyboard.next();
			outputFile = new File(outputFileName);
		}

		//	Establish input and output streams
		try {
			inputStream = new FileReader(INPUT_FILE_NAME);
			outputStream = new PrintWriter(outputFileName);
			
			// Get number of records from input file
			numberOfRecords = countNumberOfRecords(inputStream);
			inputStream.close();
		} 
		catch (FileNotFoundException e) {
			System.err.println("File not found");
			System.err.println(e.getMessage());
			System.out.println("System will close now");
			System.exit(0);
		}	
		catch (IOException e) {
			System.err.println("IO Error");
			System.err.println(e.getMessage());
		}

		//	If 0 or 1 records found there's nothing to correct
		if(numberOfRecords == 0) {	
			System.out.println(numberOfRecords + " record found.");
			System.out.println("End of program.");
			System.exit(0);
		}
		
		//	Establish a new input stream
		try {
			inputStream = new FileReader(INPUT_FILE_NAME);
		} 
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
			System.out.println("System will close.");
			System.exit(0);
		}
		
		// If number of records is 1, display it.
		if(numberOfRecords == 1) {		
			try {
				System.out.println(numberOfRecords + " record found.");
				printFileItems(inputStream);
				inputStream.close();
			} 
			catch (IOException e) {
					System.err.println("IO Error");
			}
			System.out.println("End of program.");
			System.exit(0);
		}

		// More than 1 record
		
		// initialize array to number of records size
		publicationArray = new Publication[numberOfRecords];

		// Copy inventory records to publicationArray, correct repeated codes
		// and output corrected inventory file
		correctListOfItems(inputStream, outputStream);

		// close input and output streams
		try {
			inputStream.close();
			outputStream.close();
		} 
		catch (IOException e) {
			System.err.println("IO error closing file/s.");
			System.err.println(e.getMessage());
		}

		// Print original file
		System.out.println("Original data:");
		
			// Establish a new input stream for original file		
		try {
			inputStream = new FileReader(INPUT_FILE_NAME);
			printFileItems(inputStream);
			System.out.println("");
			inputStream.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
		}
		catch (IOException e) {
			System.err.println("IO error.");
		}

		// Print corrected file
		System.out.println("Corrected data:");
		
			// Establish a new input stream for corrected file
		FileReader inputStreamNew = null;
		try {
			inputStreamNew = new FileReader(outputFileName);
			printFileItems(inputStreamNew);
			inputStreamNew.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
		}
		catch (IOException e) {
			System.err.println("IO error.");
		}
			
		keyboard.close();
		System.out.println("End of program.");
		System.exit(0);

	}	// End of main()

	
	// Required methods

	/**
	 * This method takes a FileReader input stream, reads each line of the file and
	 * if it has the format of a Publication it creates a publication with its data
	 * and keeps it in a Publication array. 
	 * Then it searches the array for any publication with a duplicate code and 
	 * fixes it by prompting the user to enter a new code for it. 
	 * @param FileReader inputStream
	 * @param PrintWrinter outputStream
	 */
	public static void correctListOfItems(FileReader inputStream, 
			PrintWriter outputStream) {

		Scanner fileScanner = new Scanner(inputStream);
		Scanner lineScanner = null;
		String line = fileScanner.nextLine();
		PrintWriter output = outputStream;

		// 	create objects and copy into the array
		for(int i = 0; i < publicationArray.length; i++) {	// for each space in the array
			if(checkRecordFormat(line)) {	// if the line is a valid record create the object.
				lineScanner = new Scanner(line);
				publicationArray[i] = new Publication(lineScanner.nextLong(), 
						lineScanner.next(), lineScanner.nextInt(), lineScanner.next(),
						lineScanner.nextDouble(), lineScanner.nextInt());

				// if there are more lines go to the next one
				if (fileScanner.hasNextLine())
					line = fileScanner.nextLine();
				continue;
			}
			else if (fileScanner.hasNextLine()) { // if not a valid record skip 
				line = fileScanner.nextLine();	// read next line
				i--;	// re-use index
			}		
			else	// no more lines to read
				break;	
		}
		fileScanner.close();

		// create a code array of same size
		long[] codeArray = new long[publicationArray.length]; 

		// copy each publication_code in publicationArray into codeArray
		for(int i = 0; i < publicationArray.length; i++) {
			codeArray[i] = publicationArray[i].getPublication_code();
		}

		// Search for duplicate codes
		for(int i = 0; i < codeArray.length; i++) { // check each code in the array to be unique.
			for(int j = 0; j < publicationArray.length; j++){
				if(codeArray[i] == publicationArray[j].getPublication_code() 
						&& i != j) {
					System.out.println("Copy detected at pos. " + j + ": " + publicationArray[j]);

					// fix duplicate code in publicationArray[j]
					long codeReplacement = fixDuplicateCode(j);
					publicationArray[j].setPublication_code(codeReplacement);
					codeArray[j] = codeReplacement;
				}
			}
			// Print to file
			output.println(publicationArray[i]);
		}
		output.close();
	} // end of correctListOfItems() 

	
	/**
	 * Prints the Publication records in a text file to screen
	 * @param input file stream name
	 * @throws IOException 
	 */
	public static void printFileItems(FileReader inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(inputStream);
		String line;
		String next = reader.readLine();
		while(next != null) {
			line = next;
			if(checkRecordFormat(line))
				System.out.println(line);
			next = reader.readLine();
		}
		reader.close();
	}

	// Other methods
	
	/**
	 * This method accepts an input stream name, reads it and return the number of
	 * publication records on it.
	 * @param FileReader stream in
	 * @return int recordNum
	 */
		public static int countNumberOfRecords(FileReader in) {
			int recordNum = 0;
	
			Scanner reader = new Scanner(in);
	
			// Scan all the file and count possible records
			String line;
			while(reader.hasNextLine()) {
				line = reader.nextLine();
				if(checkRecordFormat(line)) // if it is a record increase recordNum
					recordNum++;
			}
			reader.close();		
			return recordNum;	
		}

	
	/**
	 * This method reads a String line and checks if it has a valid publication format.
	 * @param String line
	 * @return true for a valid format, false for a not matching format.
	 */
		private static boolean checkRecordFormat(String line) {
			if (line != null) {
				Scanner verifier = new Scanner(line);
				//	if this line has the following sequence: long String int String Double int.
				//	return true, else return false
				if(verifier.hasNextLong()) {
					verifier.nextLong();
					if(verifier.hasNext()) {
						verifier.next();
						if(verifier.hasNextInt()) {
							verifier.nextInt();
							if(verifier.hasNext()) {
								verifier.next();
								if(verifier.hasNextDouble()) {
									verifier.nextDouble();
									if(verifier.hasNextInt()) {
										verifier.close();
										return true;
									}
								}
							}
						}
					}
				}
				verifier.close();
			}
			return false;
		}


	/**
	 * This method takes the index of a duplicate code in PublicationArray and prompts the
	 * user to enter a new publication_code. It verifies it is non-existing code and 
	 * returns the new publication_code. If the new code is still a copy it informs the
	 * the user and asks for a new code again.
	 * @param int j position of a duplicate code in PublicationArray
	 * @return long publication code
	 */
	private static long fixDuplicateCode(int j) {
		boolean done = false;
		Scanner kb = new Scanner(System.in);
		long newCode = 0;
		
		while(!done) {
			System.out.println("Please enter a new publication code");

			try {
				newCode = kb.nextLong();	// Read newCode from user
				for(int i = 0; i < publicationArray.length; i++) {	// check duplicates
					if ((publicationArray[i].getPublication_code() == newCode)
							&& (i != j))						// skip current index
						throw new CopyCodeException(newCode);				
				}
				// if it is a new code
				done = true;
			}
			catch (CopyCodeException e) {
				System.out.println(e.getCode_number() + " is still a copy.");
			}
			catch (InputMismatchException e) {
				System.out.println("Invalid entry. Only integer numbers are allowed.");
				kb.nextLine(); 		// Discard invalid entry
			}
		}
		return newCode;
	} 

}	// End of class
