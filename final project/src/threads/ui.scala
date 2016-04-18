package ActorUI
import threads._
import pi._

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.beans.property.BooleanProperty
import scalafx.collections.ObservableBuffer
import scalafx.geometry.{ Insets, Pos }
import scalafx.scene.control._
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout._
import scalafx.scene.{ Scene, Group, Node }
import scalafx.stage.Stage
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.Alert
import scala.Console
import scalafx.event.{ ActionEvent, Event }

object ScalaFXUI extends JFXApp {

  var numberOfThreads = 1

   stage = new JFXApp.PrimaryStage() {

      val mainButtons = new Group() {
         margin = Insets(5)
         val buttons = createMainButtonPanel()
         children = Seq(buttons)
         alignmentInParent = Pos.Center
      }

      title = "Scala FX UI"
      scene = new Scene(265, 60) {
         root = new BorderPane() {
            center = mainButtons
         }
      }
   }

   private def createMainButtonPanel(): Node = {
      val styles = Seq(
         "spinner")

      val intSpinners = new Spinner[Integer](1, 64, 2) {
         prefWidth = 75
      }

      val aler =  new Alert(AlertType.Information) {
                     initOwner(stage)
                     title = "Help"
                     headerText = "Uses 1-64 threads"
                     contentText = "Select the number of threads"

                  }

      new HBox(2) {
         margin = Insets(5)
         children = Seq(
            new Button("Pi") {
               onAction = handle {
                  pi.calculate(intSpinners.getValue(), nrOfElements = 10000, nrOfMessages = 10000)
               }
            },
            new Button("Threads") {
               onAction = handle {
                  threads.start(intSpinners.getValue(), ("This button called the Akka system and used " + intSpinners.getValue() + " threads to print this"))
               }
            },
            new Button("help") {
               onAction = handle {
                 aler.showAndWait()
               }
            },
            new HBox(30, intSpinners))
      }

   }
}
