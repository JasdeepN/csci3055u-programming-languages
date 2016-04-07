package ActorUI
import threads._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.BooleanProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.{ Scene, Group, Node }

/**
 * Example of using mouse event filters.
 * Most important part is in `makeDraggable(Node)` method.
 *
 * Based on example from JavaFX tutorial [[http://docs.oracle.com/javafx/2/events/filters.htm Handling JavaFX Events]].
 */
object ScalaFXUI extends JFXApp {

  private val dragModeActiveProperty = new BooleanProperty(this, "dragModeActive", true)
  private val borderStyle = "" +
    "-fx-background-color: white;" +
    "-fx-border-color: black;" +
    "-fx-border-width: 1;" +
    "-fx-border-radius: 6;" +
    "-fx-padding: 6;"

  stage = new JFXApp.PrimaryStage() {

    // val newGroup = new Group() {
    //   // x = 100
    //   // y = 100
    //   // padding = Insets(20)
    //   // newGroup.opacity_ = 0
    //   val loginPanel = createLoginPanel()
    //   val confirmationPanel = createConfirmationPanel()
    //   val progressPanel = createProgressPanel()

    //   loginPanel.relocate(0, 0)
    //   confirmationPanel.relocate(0, 67)
    //   progressPanel.relocate(0, 106)

    //   children = Seq(loginPanel, confirmationPanel, progressPanel)
    // alignmentInParent = Pos.Center
    // }

    val mainButtons = new Group() {
      margin = Insets(5)
      val buttons = createMainButtonPanel()
      children = Seq(buttons)
      alignmentInParent = Pos.Center
    }

    val msgPanelGroup = new Group() {
      margin = Insets(5)
      val msgPanel = createMsgPanel()
      children = Seq(msgPanel)
      alignmentInParent = Pos.TopCenter
    }

    // dragModeActiveProperty <== dragModeCheckbox.selected

    title = "Draggable Panels"
    scene = new Scene(600, 300) {
      root = new BorderPane() {
        top = msgPanelGroup
        center = mainButtons
        // bottom = dragModeCheckbox
      }
    }

    // val panelsPane = new Pane() {
    //   // val loginPanel = makeDraggable(createLoginPanel())
    //   // val confirmationPanel = makeDraggable(createConfirmationPanel())
    //   // val progressPanel = makeDraggable(createProgressPanel())

    //   val loginPanel = createLoginPanel()
    //   val confirmationPanel = createConfirmationPanel()
    //   val progressPanel = createProgressPanel()

    //   loginPanel.relocate(0, 0)
    //   confirmationPanel.relocate(0, 67)
    //   progressPanel.relocate(0, 106)

    //   children = Seq(loginPanel, confirmationPanel, progressPanel)
    // alignmentInParent = Pos.TopLeft
    // }

    // val dragModeCheckbox = new CheckBox("Drag mode") {
    //   margin = Insets(6)
    //   selected = dragModeActiveProperty()
    // }

    // dragModeActiveProperty <== dragModeCheckbox.selected

    // title = "Draggable Panels"
    // scene = new Scene(400, 300) {
    //   root = new BorderPane() {
    //     center = panelsPane
    //     bottom = dragModeCheckbox
    //   }
    // }
  }

  // private def makeDraggable(node: Node): Node = {

  //   val dragContext = new DragContext()

  //   new Group(node) {
  //     filterEvent(MouseEvent.Any) {
  //       (me: MouseEvent) =>
  //         if (dragModeActiveProperty()) {
  //           me.eventType match {
  //             case MouseEvent.MousePressed =>
  //               dragContext.mouseAnchorX = me.x
  //               dragContext.mouseAnchorY = me.y
  //               dragContext.initialTranslateX = node.translateX()
  //               dragContext.initialTranslateY = node.translateY()
  //             case MouseEvent.MouseDragged =>
  //               node.translateX = dragContext.initialTranslateX + me.x - dragContext.mouseAnchorX
  //               node.translateY = dragContext.initialTranslateY + me.y - dragContext.mouseAnchorY
  //             case _ =>
  //           }
  //           me.consume()
  //         }
  //     }
  //   }
  // }

  private def createLoginPanel(): Node = {
    val toggleGroup1 = new ToggleGroup()

    val textField = new TextField() {
      prefColumnCount = 10
      promptText = "Your name"
    }

    val passwordField = new PasswordField() {
      prefColumnCount = 10
      promptText = "Your password"
    }

    // val choiceBox = new ChoiceBox[String](
    //   ObservableBuffer(
    //     "English", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
    //     "Fran\u00E7ais")) {
    //   tooltip = Tooltip("Your language")
    //   selectionModel().select(0)
    // }

    new HBox(6) {
      children = Seq(
        new VBox(2) {
          children = Seq(
            new RadioButton("High") {
              toggleGroup = toggleGroup1
              selected = true
            },
            new RadioButton("Medium") {
              toggleGroup = toggleGroup1
              selected = false
            },
            new RadioButton("Low") {
              toggleGroup = toggleGroup1
              selected = false
            })
        },
        new VBox(2) {
          children = Seq(textField, passwordField)
        } //,
        /*choiceBox*/ )

      // alignment = Pos.BottomLeft
      style = borderStyle
    }
  }

  private def createMsgPanel(): Node = {
    new VBox() {
    margin = Insets(5)
      val msgLabel = new Label("")
      val text_f = new TextArea("Text Area")
      children = Seq(
      	text_f,
        new HBox(2) {
          alignment = Pos.CenterRight
          children = Seq(
            new Button("clear") { onAction = handle { msgLabel.text = "" } },
            new Button("show") { onAction = handle { msgLabel.text = "text and things" } })
        })
    }
  }

  private def createMainButtonPanel(): Node = new HBox(2) {
    margin = Insets(5)
    children = Seq(
      new Button("1") {
        onAction = handle { calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000) }
      },
      new Button("2") {
        onAction = handle { print_something(nrOfWorkers = 2, nrOfMessages = 5, "This is a Thread") }
      })
    // alignment = Pos.CenterRight
    // style = borderStyle
  }

  private def createProgressPanel(): Node = new HBox(6) {
    val slider = new Slider()
    val progressIndicator = new ProgressIndicator() {
      progress <== slider.value / slider.max
    }
    children = Seq(new Label("Progress:"), slider, progressIndicator)
    style = borderStyle
  }

  private final class DragContext {
    var mouseAnchorX: Double = 0d
    var mouseAnchorY: Double = 0d
    var initialTranslateX: Double = 0d
    var initialTranslateY: Double = 0d
  }

}
