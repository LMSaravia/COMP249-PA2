package Part2;
//---------- ---------------------------------------------------------------------
//Assignment 2
//Questions: 2
//Written by: Luis Saravia Patron ID 26800505
//For COMP249 Section: U Winter 2016
//------------------------------------------------------------------------------

/**
 * This program reads a Publication inventory from a text file, allows the user to 
 * append more publications in the text file and saves a copy as a binary file.
 * It also searches in the inventory for a publication code entered by the user.
 * 
 * Asssumptions:
 * - The user will always keep the file sorted; that is, no new record will have a 
 * publication code that is smaller than any of the previous records in the file. 
 * - PublicationData_Ouput.txt. includes information about publications list of items
 * in a Publication store. The records in this file are sorted by publication code,
 * and the information in this file is assumed to always be correct.
 * - It is assumed that both the publication name and the author(s) name are recorded as a
 * continuous string with “_” to separate the different words if any (i.e. a publication
 * name can be Absolute_Java, and author name can be Walter_Savitch).
 * 
 * @author Luis
 * @version 1.0
 */


import java.io.FileReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * @author Luis
 */
public class PublicationListingProcess2 {

	private static final String FILE_NAME = "PublicationData_Output.txt";
	private static final String BINARY_OUTPUT = "Publications.dat";

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

		boolean done = false;
		int numberOfRecords = 0;
		PrintWriter textStream = null;
		FileReader in = null;
		ObjectOutputStream out = null;

		System.out.println("Welcome to Publication Listing Process 2");	
		System.out.println("Program written by Luis Saravia Patron \n");

		// Open file to append records to it 
		try {
			textStream = new PrintWriter(new FileOutputStream(FILE_NAME, true));

			// Call insertRowsToFile() to insert x number of records entered by user
			// user has a stopping condition throwing Exception
			while(!done) {	
				insertRowsToFile(textStream); // throws Exception to exit loop	
			}
		} 
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
			System.out.println("System will close now.");
			System.exit(0);
		}
		catch (Exception exitLoop){
			System.out.println(exitLoop.getMessage());
			System.out.println("");
		}
		finally {
			textStream.close();
		}

		// Show the contents after modifications.
		System.out.println("Contents of file after modifications:");
		try {
			in = new FileReader(FILE_NAME);
			printFileItems(in);
			System.out.println("");
			in.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
		}
		catch (IOException e) {
			System.out.println("IO Error");
		}		

		// Get number of records in PublicationData_Output.txt
		try {
			in = new FileReader(FILE_NAME);
			numberOfRecords = countNumberOfRecords(in);
			in.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
			System.out.println("System will close now.");
			System.exit(0);
		}
		catch (IOException e) {
			System.err.println("IO Error");
		}		

		// Initialize array to number of records size
		publicationArray = new Publication[numberOfRecords];

		// Fill publicationArray with data from records in 
		// PublicationData_Output.txt
		try {
			in = new FileReader(FILE_NAME);
			copyRecordsToArray(in);

			// Get a publication code to search from user.
			System.out.println("Enter the publication code you want to search");
			Scanner keyboard = new Scanner(System.in);
			long target = keyboard.nextLong();

			// Use binaryPublicationSearch() to search the publication code.
			System.out.println("Using binary search:");
			binaryPublicationSearch(publicationArray, 0, publicationArray.length - 1, target);
			System.out.println();

			//  Search using sequentialPublicationSearch()
			System.out.println("Using sequential search:");
			sequentialPublicationSearch(publicationArray, 0, publicationArray.length - 1, target);

			// Store objects on PublicationArray[] in a binary file
			out = new ObjectOutputStream(new FileOutputStream(BINARY_OUTPUT));
			for(int i = 0; i < publicationArray.length; i++) {
				out.writeObject(publicationArray[i]);
			}
			out.close();
			System.out.println("Publication array saved to " + BINARY_OUTPUT);
		} 
		catch(FileNotFoundException e) {
			System.err.println("File not found.");
			System.err.println(e.getMessage());
			System.out.println("System will close now.");
			System.exit(0);
		}
		catch(InputMismatchException e) {
			System.err.println("Error. Invalid publication code entry.");
			System.out.println("No search to perform.");
		}
		catch(IOException e) {
			System.err.println("IO Error");
			System.err.println(e.getMessage());
		}
		System.out.println("End of program.");
		System.exit(0);
		
	}	// End of main()


	// Required methods

/**
 * This method searches publication code in a Publication array using sequential
 * search. It also keeps track of the number of iterations needed to perform the
 * search.
 * @param array Publication[]
 * @param from int start index
 * @param to int end index
 * @param targetCode long publication code to search
 */
	public static void sequentialPublicationSearch(Publication[] array, int from,
			int to, long targetCode) {
		int min = from; // min = 0
		int max = to;	// max = n-1
		int guess;
		int count = 0; // iteration counter

		while(min <= max) { // If max < min, target is not there
			count++;
			guess = min;
			//If array[guess] equals target, found it.
			if(array[guess].getPublication_code() == targetCode) { 
				System.out.println("Number of iterations = " + count);
				System.out.println("Code " + targetCode + " found at index " + guess);
				return;
			}
			//If array[guess] != target, increase min by 1
			else if(array[guess].getPublication_code() != targetCode) { 
				min ++;
			}
		}
		System.out.println("Code not found");
	}

	/**
	 * This method searches publicationArray using binary search. It also keeps track
	 * of how many iterations it needs to perform the search.
	 * @param array of Publications
	 * @param from int start index
	 * @param to int end index
	 * @param targetCode long publication code to search
	 */
	public static void binaryPublicationSearch(Publication[] array, int from,
			int to,	long targetCode) {
		int min = from; // min = 0
		int max = to;	// max = n-1.
		int guess;
		int count = 0; // iteration counter

		while(min <= max) { // If max < min, target is not there
			count++;
			guess = (int) Math.floor((max + min) / 2); 	// average of max and min,
														// rounded down
			
			// If array[guess] equals target, found it.
			if(array[guess].getPublication_code() == targetCode) { 
				System.out.println("Number of iterations = " + count);
				System.out.println("Code " + targetCode + " found at index " + guess);
				return;
			}
			// Else if array[guess] < target, set min = guess + 1
			else if(array[guess].getPublication_code() < targetCode) { 
				min = guess + 1;
			}
			// Else guess was too high. Set max = guess - 1.
			else { 
				max = guess - 1;
			}
		}
		System.out.println("Code not found");
	}

/**
 * This method takes a PrintWriter output stream name, prompts the user to
 * enter publication's data and writes the publication to the file. The user has the 
 * choice to enter more publications or exit. 
 * @param textStream PrintWriter output stream name
 * @throws Exception
 */
	private static void insertRowsToFile(PrintWriter textStream) throws Exception {
		long p_code;
		String p_name;
		int p_year;
		String p_authorName;
		double p_cost;
		int p_nbPages;

		Scanner kb = new Scanner(System.in);

		try {
		// Prompt user to get Publication data
		System.out.println("Please enter a new publication code.");
		p_code = kb.nextLong();
		System.out.println("Please enter the publication name.");
		p_name = kb.next();
		System.out.println("Please enter the publication year.");
		p_year = kb.nextInt();
		System.out.println("Please enter the publication author name.");
		p_authorName = kb.next();
		System.out.println("Please enter the publication cost.");
		p_cost = kb.nextDouble();
		System.out.println("Please enter the publication number of pages.");
		p_nbPages = kb.nextInt();
		
		kb.nextLine(); // discard any additional input if any.

		// Create Publication instance
		Publication tempPub = new Publication(p_code, p_name, p_year, p_authorName, p_cost, p_nbPages);
		textStream.println(tempPub);
		}
		catch (InputMismatchException e){
			System.err.println("Invalid entry.");
			kb.nextLine();	// Discard the invalid entry
		}
				
		// Give user a chance to exit or continue adding publications
		System.out.println("Press 0 to stop adding publications or any number to continue");
		try {
			int choice = kb.nextInt();
			if(choice == 0) {
				textStream.flush();
				throw new Exception("Adding publications completed.");
			}
		}
		catch(InputMismatchException e) {
			System.err.println("Invalid choice.");
			textStream.flush();
			throw new Exception("Adding publications stopped.");
		}
	}


	/**
	 * Prints contents of a text file to screen
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


	//	Other Helper Methods

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

	private static void copyRecordsToArray(FileReader inputStream) {

		Scanner fileScanner = new Scanner(inputStream);
		Scanner lineScanner = null;
		String line = fileScanner.nextLine();

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
			else if (fileScanner.hasNextLine()) { // if not a valid record 
				line = fileScanner.nextLine();	// read next line
				i--;	// re-use index
			}		
			else	// no more lines to read
				break;	
		}
		fileScanner.close();
		lineScanner.close();
	} // end of copyRecordsToArray() 


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

}	// End of class