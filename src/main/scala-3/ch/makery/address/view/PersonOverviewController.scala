package ch.makery.address.view
import ch.makery.address.model.Person
import ch.makery.address.MainApp
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
@FXML
class PersonOverviewController():
  @FXML
  private var personTable: TableView[Person] = null
  @FXML
  private var firstNameColumn: TableColumn[Person, String] = null
  @FXML
  private var lastNameColumn: TableColumn[Person, String] = null
  @FXML
  private var firstNameLabel: Label = null
  @FXML
  private var lastNameLabel: Label = null
  @FXML
  private var streetLabel: Label = null
  @FXML
  private var postalCodeLabel: Label = null
  @FXML
  private var cityLabel: Label = null
  @FXML
  private var birthdayLabel: Label = null
  // initialize Table View display contents model
  def initialize() =
    personTable.items = MainApp.personData
    // initialize columns's cell values
    firstNameColumn.cellValueFactory = {_.value.firstName}
    lastNameColumn.cellValueFactory  = {_.value.lastName}
    showPersonDetails(None)
    personTable.selectionModel().selectedItem.onChange(
      (_, _, newValue) => showPersonDetails(Option(newValue))
    )

  private def showPersonDetails(person: Option[Person]): Unit =
    person match
      case Some(person) =>
        import ch.makery.address.util.DateUtil.*
        // Fill the labels with info from the person object.
        firstNameLabel.text <== person.firstName
        lastNameLabel.text <== person.lastName
        streetLabel.text <== person.street
        cityLabel.text <== person.city;
        postalCodeLabel.text <== person.postalCode.delegate.asString()
        birthdayLabel.text <== Bindings.createStringBinding(
          () => {
            person.date.value.asString
          }, person.date
        )

      case None =>
        // Person is null, remove all the text.
        firstNameLabel.text.unbind()
        lastNameLabel.text.unbind()
        streetLabel.text.unbind()
        postalCodeLabel.text.unbind()
        cityLabel.text.unbind()
        birthdayLabel.text.unbind()

        firstNameLabel.text = ""
        lastNameLabel.text = ""
        streetLabel.text = ""
        postalCodeLabel.text = ""
        cityLabel.text = ""
        birthdayLabel.text = ""
  @FXML
  def handleDeletePerson(action: ActionEvent): Unit =
    val selectedIndex = personTable.selectionModel().selectedIndex.value
    if (selectedIndex >= 0) then
      MainApp.personData.remove(selectedIndex)
    else
      val alert = new Alert(AlertType.Information):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      alert.showAndWait()

  def handleNewPerson(action: ActionEvent) =
    val person = new Person("", "")
    val okClicked = MainApp.showPersonEditDialog(person);
    if (okClicked) then {
      MainApp.personData += person
      val id = person.save()
      id.map(x => person.id.value = x)
    }

  def handleEditPerson(action: ActionEvent) =
    val selectedPerson = personTable.selectionModel().selectedItem.value
    if (selectedPerson != null) then
      val okClicked = MainApp.showPersonEditDialog(selectedPerson)

      if (okClicked) then showPersonDetails(Some(selectedPerson))
      selectedPerson.save()
    else
      // Nothing selected.
      val alert = new Alert(Alert.AlertType.Warning):
        initOwner(MainApp.stage)
        title = "No Selection"
        headerText = "No Person Selected"
        contentText = "Please select a person in the table."
      .showAndWait()
