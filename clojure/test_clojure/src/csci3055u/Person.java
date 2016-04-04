package csci3055u;

public class Person {
// instance variables
	// private int age;
	// private String lastName;
	private String firstName;

// defined constructor
	// public Person (String _firstName, String _lastName, int _age) {
	public Person (String _firstName) {

		this.firstName = _firstName;
		// this.lastName = _lastName;
		// this.age = _age;
	}


	public int getAge() {
		return age;
	}

	public void printAge(int _age) {
		this.age = age;
	}

// method to get first name
	public String getFirstName() {
		return firstName; // show first name
	} // end method getFirstName

// method to set first name
	public void setFirstName(String _firstName)

	{
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
}
