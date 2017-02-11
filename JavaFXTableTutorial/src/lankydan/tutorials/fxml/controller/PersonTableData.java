package lankydan.tutorials.fxml.controller;

import java.util.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import lankydan.tutorials.fxml.data.Person;

public class PersonTableData {

	private SimpleStringProperty firstName;
	private SimpleStringProperty surname;
	private SimpleObjectProperty<Date> dateOfBirth;
	private SimpleStringProperty occupation;
	private SimpleDoubleProperty salary;

	public PersonTableData(Person person) {
		this.firstName = new SimpleStringProperty(person.getFirstName());
		this.surname = new SimpleStringProperty(person.getSurname());
		this.dateOfBirth = new SimpleObjectProperty<Date>(
				person.getDateOfBirth());
		this.occupation = new SimpleStringProperty(person.getOccupation());
		this.salary = new SimpleDoubleProperty(person.getSalary());
	}

	public PersonTableData(final String firstName, final String surname,
			final Date dateOfBirth, final String occupation,
			final double salary) {
		this.firstName = new SimpleStringProperty(firstName);
		this.surname = new SimpleStringProperty(surname);
		this.dateOfBirth = new SimpleObjectProperty<Date>(dateOfBirth);
		this.occupation = new SimpleStringProperty(occupation);
		this.salary = new SimpleDoubleProperty(salary);
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(final String firstName) {
		this.firstName.set(firstName);
	}

	public String getSurname() {
		return surname.get();
	}

	public void setSurname(final String surname) {
		this.surname.set(surname);
	}

	public Date getDateOfBirth() {
		return dateOfBirth.get();
	}

	public void setDateOfBirth(final Date dateOfBirth) {
		this.dateOfBirth.set(dateOfBirth);
	}

	public String getOccupation() {
		return occupation.get();
	}

	public void setOccupation(final String occupation) {
		this.occupation.set(occupation);
	}

	public double getSalary() {
		return salary.get();
	}

	public void setSalary(final double salary) {
		this.salary.set(salary);
	}

}
