package lankydan.tutorials.fxml.controller;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import lankydan.tutorials.fxml.cell.EditCell;
import lankydan.tutorials.fxml.converter.MyDateStringConverter;
import lankydan.tutorials.fxml.converter.MyDoubleStringConverter;
import lankydan.tutorials.fxml.data.Person;

public class TableAppController implements Initializable {

	@FXML
	private TableView<PersonTableData> table;

	@FXML
	private TextField firstNameTextField;

	@FXML
	private TextField surnameTextField;

	@FXML
	private TextField dateOfBirthTextField;

	@FXML
	private TextField occupationTextField;

	@FXML
	private TextField salaryTextField;

	@FXML
	private Button submitButton;

	private ObservableList<PersonTableData> data = FXCollections
			.observableArrayList();

	private static final String DATE_PATTERN = "dd/MM/yyyy";

	private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(
			DATE_PATTERN);

	@FXML
	private TableColumn<PersonTableData, Date> dateOfBirthColumn;

	@FXML
	private TableColumn<PersonTableData, Double> salaryColumn;

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		DATE_FORMATTER.setLenient(false);
		table.setItems(data);
		populate(retrieveData());
		// createColumnManually();
		setupDateOfBirthColumn();
		setupSalaryColumn();
		setTableEditable();

	}

	private List<Person> retrieveData() {
		try {
			return Arrays.asList(
					new Person("Dan", "Newton",
							DATE_FORMATTER.parse("06/01/1994"),
							"Java Developer", 22000),
					new Person("George", "Newton",
							DATE_FORMATTER.parse("24/01/1995"), "Bro", 15021),
					new Person("Laura", "So",
							DATE_FORMATTER.parse("24/04/1995"), "Student", 0),
					new Person("Jamie", "Harwood",
							DATE_FORMATTER.parse("15/12/9999"),
							"Java Developer", 30000),
					new Person("Michael", "Collins",
							DATE_FORMATTER.parse("01/01/0001"), "Developer",
							299),
					new Person("Stuart", "Kerrigan",
							DATE_FORMATTER.parse("06/10/1894"),
							"Teaching Fellow", 100000));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return new ArrayList<Person>();
	}

	private void populate(final List<Person> people) {
		people.forEach(p -> data.add(new PersonTableData(p)));
	}

	private void setupDateOfBirthColumn() {
		// formats the display value to display dates in the form of dd/MM/yyyy
		dateOfBirthColumn
				.setCellFactory(EditCell.<PersonTableData, Date>forTableColumn(
						new MyDateStringConverter(DATE_PATTERN)));
		// updates the dateOfBirth field on the PersonTableData object to the
		// committed value
		dateOfBirthColumn.setOnEditCommit(event -> {
			final Date value = event.getNewValue() != null ? event.getNewValue()
					: event.getOldValue();
			((PersonTableData) event.getTableView().getItems()
					.get(event.getTablePosition().getRow()))
							.setDateOfBirth(value);
			table.refresh();
		});
	}

	private void setupSalaryColumn() {
		salaryColumn.setCellFactory(
				EditCell.<PersonTableData, Double>forTableColumn(
						new MyDoubleStringConverter()));
		// updates the salary field on the PersonTableData object to the
		// committed value
		salaryColumn.setOnEditCommit(event -> {
			final Double value = event.getNewValue() != null
					? event.getNewValue() : event.getOldValue();
			((PersonTableData) event.getTableView().getItems()
					.get(event.getTablePosition().getRow())).setSalary(value);
			table.refresh();
		});
	}

	private void setTableEditable() {
		table.setEditable(true);
		// allows the individual cells to be selected
		table.getSelectionModel().cellSelectionEnabledProperty().set(true);
		// when character or numbers pressed it will start edit in editable
		// fields
		table.setOnKeyPressed(event -> {
			if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
				editFocusedCell();
			} else if (event.getCode() == KeyCode.RIGHT
					|| event.getCode() == KeyCode.TAB) {
				table.getSelectionModel().selectNext();
				event.consume();
			} else if (event.getCode() == KeyCode.LEFT) {
				// work around due to
				// TableView.getSelectionModel().selectPrevious() due to a bug
				// stopping it from working on
				// the first column in the last row of the table
				selectPrevious();
				event.consume();
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void editFocusedCell() {
		final TablePosition<PersonTableData, ?> focusedCell = table
				.focusModelProperty().get().focusedCellProperty().get();
		table.edit(focusedCell.getRow(), focusedCell.getTableColumn());
	}

	@SuppressWarnings("unchecked")
	private void selectPrevious() {
		if (table.getSelectionModel().isCellSelectionEnabled()) {
			// in cell selection mode, we have to wrap around, going from
			// right-to-left, and then wrapping to the end of the previous line
			TablePosition<PersonTableData, ?> pos = table.getFocusModel()
					.getFocusedCell();
			if (pos.getColumn() - 1 >= 0) {
				// go to previous row
				table.getSelectionModel().select(pos.getRow(),
						getTableColumn(pos.getTableColumn(), -1));
			} else if (pos.getRow() < table.getItems().size()) {
				// wrap to end of previous row
				table.getSelectionModel().select(pos.getRow() - 1,
						table.getVisibleLeafColumn(
								table.getVisibleLeafColumns().size() - 1));
			}
		} else {
			int focusIndex = table.getFocusModel().getFocusedIndex();
			if (focusIndex == -1) {
				table.getSelectionModel().select(table.getItems().size() - 1);
			} else if (focusIndex > 0) {
				table.getSelectionModel().select(focusIndex - 1);
			}
		}
	}

	private TableColumn<PersonTableData, ?> getTableColumn(
			final TableColumn<PersonTableData, ?> column, int offset) {
		int columnIndex = table.getVisibleLeafIndex(column);
		int newColumnIndex = columnIndex + offset;
		return table.getVisibleLeafColumn(newColumnIndex);
	}

	private void createColumnManually() {
		TableColumn<PersonTableData, Date> dateOfBirthColumn = new TableColumn<>(
				"Date of Birth");
		dateOfBirthColumn.setCellValueFactory(person -> {
			SimpleObjectProperty<Date> property = new SimpleObjectProperty<>();
			property.setValue(person.getValue().getDateOfBirth());
			return property;
		});
		table.getColumns().add(2, dateOfBirthColumn);
	}

	@FXML
	private void submit(final ActionEvent event) {
		if (allFieldsValid()) {
			final String firstName = firstNameTextField.getText();
			final String surname = surnameTextField.getText();
			Date dateOfBirth = null;
			try {
				dateOfBirth = DATE_FORMATTER
						.parse(dateOfBirthTextField.getText());
			} catch (final ParseException e) {
			}
			final String occupation = occupationTextField.getText();
			final double salary = Double.parseDouble(salaryTextField.getText());
			data.add(new PersonTableData(firstName, surname, dateOfBirth,
					occupation, salary));
		}
	}

	private boolean allFieldsValid() {
		return !firstNameTextField.getText().isEmpty()
				&& !surnameTextField.getText().isEmpty()
				&& dateOfBirthFieldValid()
				&& !occupationTextField.getText().isEmpty()
				&& !salaryTextField.getText().isEmpty();
	}

	private boolean dateOfBirthFieldValid() {
		if (!dateOfBirthTextField.getText().isEmpty()) {
			try {
				DATE_FORMATTER.parse(dateOfBirthTextField.getText());
				return true;
			} catch (ParseException e) {
				return false;
			}
		}
		return false;
	}
}
