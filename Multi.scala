import akka.actor._
import akka.routing.RoundRobinRouter
import akka.util.Duration
import akka.util.duration._
// import scalafx.application.JFXApp
// import scalafx.application.JFXApp.PrimaryStage
// import scalafx.geometry.Insets
// import scalafx.scene.Scene
// import scalafx.scene.control.Label
// import scalafx.scene.layout.BorderPane

// object HelloSBT extends JFXApp {
//   stage = new PrimaryStage {
//     scene = new Scene {
//       root = new BorderPane {
//         padding = Insets(25)
//         center = new Label("Hello SBT")
//       }
//     }
//   }
// }

object multi extends App {

// object multi extends JFXApp {

  calculate(nrOfWorkers = 8, nrOfElements = 100000, nrOfMessages = 10000)

  sealed trait Message
  case object Calculate extends Message
  case class Work(start: Int, nrOfElements: Int) extends Message
  case class Result(value: Double) extends Message
  case class PiApproximation(pi: Double, duration: Duration)

  class Worker extends Actor {

    //runs before worker starts
    override def preStart() {
      println("%s is running".format(self.path.name))
    }


  //runs after worker stops
  override def postStop() {
    println("%s has stopped".format(self.path.name))
  }

  //example method
  def calculatePiFor(start: Int, nrOfElements: Int): Double = {
    var acc = 0.0
    for (i ← start until (start + nrOfElements))
    acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
  }

  def doSomething() {
    println("Do something")
    "return this"
  }

  def receive = {
    case Work(start, nrOfElements) =>
        sender ! Result(calculatePiFor(start, nrOfElements)) // perform the work
      }
    }



    class Master(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef)
    extends Actor {

      var pi: Double = _
      var nrOfResults: Int = _
      val start: Long = System.currentTimeMillis

      val workerRouter = context.actorOf(
        Props[Worker].withRouter(RoundRobinRouter(nrOfWorkers)), name = "workerRouter")

      def receive = {
        case Calculate ⇒
        for (i ← 0 until nrOfMessages) workerRouter ! Work(i * nrOfElements, nrOfElements)
        case Result(value) ⇒
        pi += value
        nrOfResults += 1
        if (nrOfResults == nrOfMessages) {
          // Send the result to the listener
          listener ! PiApproximation(pi, duration = (System.currentTimeMillis - start).millis)
          // Stops this actor and all its supervised children
          context.stop(self)
        }
      }
    }

    class Listener extends Actor {
      def receive = {
        case PiApproximation(pi, duration) ⇒
        println("\n\tPi approximation: \t%s\n\tCalculation time: \t%s"
          .format(pi, duration))
        context.system.shutdown()
      }
    }


    def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    // Create an Akka system
    val system = ActorSystem("AkkaSystem")

    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new Master(
      nrOfWorkers, nrOfMessages, nrOfElements, listener)),
    name = "master")

    // start the calculation
    master ! Calculate

  }
}
