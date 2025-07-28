package ch.makery.address

import ch.makery.address.model.Person
import ch.makery.address.util.Database
import ch.makery.address.view.{PersonEditDialogController, PersonOverviewController}
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.{Scene, control}
import scalafx.Includes.*
import javafx.scene as jfxs
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

object MainApp extends JFXApp3:
  Database.setupDB()
  // Window Root Pane
  var roots: Option[scalafx.scene.layout.BorderPane] = None
  //stylesheet
  var cssResource = getClass.getResource("view/DarkTheme.css")
  var overviewControl: Option[PersonOverviewController] = None

  val personData = new ObservableBuffer[Person]()
  /**
   * Constructor
   */
  personData ++= Person.getAllPersons

  override def start(): Unit =
    // Fix: Correct path
    val rootResource = getClass.getResource("/ch/makery/address/view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()

    // Fix: use global `roots`, not shadowed val
    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "AddressApp"
      icons += new Image(getClass.getResource("/images/book.png").toExternalForm)
      scene = new Scene():
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots.get

    // Display PersonOverview
    showPersonOverview()

  // Fix: avoid val roots which shadows global `roots`
  def showPersonOverview(): Unit =
    val resource = getClass.getResource("/ch/makery/address/view/PersonOverview.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val personOverview = loader.getRoot[jfxs.layout.AnchorPane]
    val ctrl = loader.getController[PersonOverviewController]
    overviewControl = Option(ctrl)
    this.roots.foreach(_.setCenter(personOverview))

  def showPersonEditDialog(person: Person): Boolean =
    val resource = getClass.getResource("view/PersonEditDialog.fxml")
    val loader = new FXMLLoader(resource)
    loader.load();
    val roots2 = loader.getRoot[jfxs.Parent]
    val control = loader.getController[PersonEditDialogController]

    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      scene = new Scene:
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots2

    control.dialogStage = dialog
    control.person = person
    dialog.showAndWait()

    control.okClicked
  // Optional: Testing property bindings (unchanged)
  val stringA = new StringProperty("sunway")
  val stringB = new StringProperty("monash")
  val stringC = new StringProperty("taylor")
  stringA.onChange((a, b, c) => {
    println("a has changed value from " + b + " to " + c)
  })

  stringB <== stringA
  stringC <==> stringA
  stringA.value = "segi"
  stringC.value = "utm"
  println("string a value: " + stringA.value)
  println("string b value: " + stringB.value)
  println("string c value: " + stringC.value)

  val add: (Int, Int) => Int = (a: Int, b: Int) => (a + b)
  print(add(1, 2))

  extension (value: Int)
    def area: Double = 3.142 * value * value

  println(56.area)