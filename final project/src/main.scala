import akka.actor._
// import akka.actor.{Actor, ActorRef, Terminated}
import scala.collection.mutable.ArrayBuffer
import akka.routing.RoundRobinPool
// import akka.util.{Duration}
import scala.concurrent.duration.Duration
// import akka.util.duration._
import scala.concurrent.duration._
// import scala.concurrent.ExecutionContext.Implicits.global

object multi extends App {
  println("WELCOME EXTENDING \"APP\" OBJECT ACTS AS MAIN")
  calculate(nrOfWorkers = 8, nrOfElements = 100000, nrOfMessages = 10000)

  sealed trait Message
  case object Calculate extends Message
  case class Work(start: Int, nrOfElements: Int) extends Message
  case class Result(value: Double) extends Message
  case class PiApproximation(pi: Double, duration: Duration)

  class Worker extends Actor {

   override def preStart() {
    println("%s is running".format(self.path.name))
  }

  override def postStop() {
    println("%s has stopped".format(self.path.name))
  }

  def calculatePiFor(start: Int, nrOfElements: Int): Double = {
    var acc = 0.0
    for (i ← start until (start + nrOfElements))
    acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
    acc
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
        Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

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
          // system.scheduler.scheduleOnce(1 second) {
          //   self ! PoisonPill
          // }
        }
      }
    }

    class Listener extends Actor {
      def receive = {
        case PiApproximation(pi, duration) ⇒
        println("\n\tPi approximation: \t%s\n\tCalculation time: \t%s"
          .format(pi, duration))
        context.system.terminate()
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
