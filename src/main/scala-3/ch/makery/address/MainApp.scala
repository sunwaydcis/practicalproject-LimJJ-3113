package ch.makery.address

import ch.makery.address.model.Person
import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes.*
import javafx.scene as jfxs
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer

object MainApp extends JFXApp3:

  // Window Root Pane
  var roots: Option[scalafx.scene.layout.BorderPane] = None

  val personData = new ObservableBuffer[Person]()
  /**
   * Constructor
   */
  personData += new Person("Hans", "Muster")
  personData += new Person("Ruth", "Mueller")
  personData += new Person("Heinz", "Kurz")
  personData += new Person("Cornelia", "Meier")
  personData += new Person("Werner", "Meyer")
  personData += new Person("Lydia", "Kunz")
  personData += new Person("Anna", "Best")
  personData += new Person("Stefan", "Meier")
  personData += new Person("Martin", "Mueller")

  override def start(): Unit =
    // Fix: Correct path
    val rootResource = getClass.getResource("/ch/makery/address/view/RootLayout.fxml")
    val loader = new FXMLLoader(rootResource)
    loader.load()

    // Fix: use global `roots`, not shadowed val
    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "AddressApp"
      scene = new Scene():
        root = roots.get

    // Display PersonOverview
    showPersonOverview()

  // Fix: avoid val roots which shadows global `roots`
  def showPersonOverview(): Unit =
    val resource = getClass.getResource("/ch/makery/address/view/PersonOverview.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val personOverview = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.foreach(_.setCenter(personOverview))

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