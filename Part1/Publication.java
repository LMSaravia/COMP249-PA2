package Part1;
// -------------------------------------------------------------------------------
// Assignment 2
// Questions: 1
// Written by: Luis Saravia Patron ID 26800505
// For COMP249 Section: U Winter 2016
// ------------------------------------------------------------------------------

/**
 * Asssumptions:
 * It is assumed that both the publication name and the author(s) name are recorded as a
 * continuous string with “_” to separate the different words if any (i.e. a publication
 * name can be Absolute_Java, and author name can be Walter_Savitch).
 * @author Luis
 * @version 1.0
 */

public class Publication {

	private long publication_code;
	private String publication_name;
	private int publication_year;
	private String publication_authorname;
	private double publication_cost;
	private int publication_nbpages;

	
	/**
	 * Default constructor
	 * Initializes all numbers to zero and Strings to null string;
	 */
	public Publication() {
		this.publication_code = 0;
		this.publication_name = "";
		this.publication_year = 0000;
		this.publication_authorname = "";
		this.publication_cost = 0.00;
		this.publication_nbpages = 0;
	}


	/**
	 * Parameterized constructor
	 * @param long code for publication_code
	 * @param String publication_name 
	 * @param int publication_year 
	 * @param String publication_authorname 
	 * @param double publication_cost 
	 * @param int publication_nbpages 
	 */
	public Publication(long code, String p_name, int year, String a_name, 
			double cost, int nbPages) {
		publication_code = code;
		publication_name = p_name;
		publication_year = year;
		publication_authorname = a_name;
		publication_cost = cost;
		publication_nbpages = nbPages;		
	}

	
	/**
	 * @param Publication object p
	 */
	public Publication(Publication p) {
		publication_code = p.getPublication_code();
		publication_name = p.getPublication_name();
		publication_year = p.getPublication_year();
		publication_authorname = p.getPublication_authorname();
		publication_cost = p.getPublication_cost();
		publication_nbpages = p.getPublication_nbpages();
	}


	/**
	 * @return the publication_code
	 */
	public long getPublication_code() {
		return publication_code;
	}

	/**
	 * @return the publication_name
	 */
	public String getPublication_name() {
		return publication_name;
	}

	/**
	 * @return the publication_year
	 */
	public int getPublication_year() {
		return publication_year;
	}

	/**
	 * @return the publication_authorname
	 */
	public String getPublication_authorname() {
		return publication_authorname;
	}

	/**
	 * @return the publication_cost
	 */
	public double getPublication_cost() {
		return publication_cost;
	}

	/**
	 * @return the publication_nbpages
	 */
	public int getPublication_nbpages() {
		return publication_nbpages;
	}

	/**
	 * @param publication_code the publication_code to set
	 */
	public void setPublication_code(long publication_code) {
		this.publication_code = publication_code;
	}

	/**
	 * @param publication_name the publication_name to set
	 */
	public void setPublication_name(String publication_name) {
		this.publication_name = publication_name;
	}

	/**
	 * @param publication_year the publication_year to set
	 */
	public void setPublication_year(int publication_year) {
		this.publication_year = publication_year;
	}

	/**
	 * @param publication_authorname the publication_authorname to set
	 */
	public void setPublication_authorname(String publication_authorname) {
		this.publication_authorname = publication_authorname;
	}

	/**
	 * @param publication_cost the publication_cost to set
	 */
	public void setPublication_cost(double publication_cost) {
		this.publication_cost = publication_cost;
	}

	/**
	 * @param publication_nbpages the publication_nbpages to set
	 */
	public void setPublication_nbpages(int publication_nbpages) {
		this.publication_nbpages = publication_nbpages;
	}

	
	/**
	 * @param Object obj
	 * @return boolean true if the two objects belong to the same class and their
	 * attributes have equivalent values.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @Override Object equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publication otherObj = (Publication) obj;
		if (publication_authorname == null) {
			if (otherObj.publication_authorname != null)
				return false;
		} else if (!publication_authorname.equals(otherObj.publication_authorname))
			return false;
		if (publication_code != otherObj.publication_code)
			return false;
		if (Double.doubleToLongBits(publication_cost) != Double.doubleToLongBits(otherObj.publication_cost))
			return false;
		if (publication_name == null) {
			if (otherObj.publication_name != null)
				return false;
		} else if (!publication_name.equals(otherObj.publication_name))
			return false;
		if (publication_nbpages != otherObj.publication_nbpages)
			return false;
		if (publication_year != otherObj.publication_year)
			return false;
		return true;
	}

	
	/**
	 * @return String showing all attributes
	 * Format: publication_code, publication_name, publication_year, 
	 * publication_authorname, publication_cost, publication_nbpages
	 * @Override Object toString
	 */
	@Override
	public String toString() {
		return (publication_code + " " + publication_name + " " + publication_year +
				" " + publication_authorname + " " + publication_cost + " " +
				publication_nbpages);
	}

}
