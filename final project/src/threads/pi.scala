package ActorUI

import akka.actor._
import scala.collection.mutable.ArrayBuffer
import akka.routing.RoundRobinPool
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

/**
  * Creates a thread object which contains everything needed to start
  * a multi threaded operation in this case calculating pi
  */

  object pi {

    sealed trait Message

  // Task 1 variables
  case object Calculate extends Message
  case class Work(start: Int, nrOfElements: Int) extends Message
  case class Result(value: Double) extends Message
  case class PiApproximation(pi: Double, duration: Duration)

/**
  * Generic class (basically a blank template) for what messages are
  * sent between workers
  */

  class Worker() extends Actor {
    // could put class variables here

    override def preStart() {
      println("\to Thread %s is running".format(self.path.name))
    }

    override def postStop() {
      println("\tx Thread %s has stopped".format(self.path.name))
    }

    def calculatePiFor(start: Int, nrOfElements: Int): Double = {
      var acc = 0.0
      for (i ← start until (start + nrOfElements))
      acc += 4.0 * (1 - (i % 2) * 2) / (2 * i + 1)
      acc
    }

    //work has been assigned to a worker
    def receive = {
      //calls the "work" methods
      case Work(start, nrOfElements) =>
        // perform the work
        sender ! Result(calculatePiFor(start, nrOfElements))
      }
    }

    class piCalculator(nrOfWorkers: Int, nrOfMessages: Int, nrOfElements: Int, listener: ActorRef) extends Actor {
      var pi: Double = _
      var nrOfResults: Int = _
      val start: Long = System.currentTimeMillis

      val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

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
        println("\n\tPi approximation: \t%s\n\tCalculation time: \t%s".format(pi, duration))
        context.system.terminate()
      }
    }

    def calculate(nrOfWorkers: Int, nrOfElements: Int, nrOfMessages: Int) {
    // Create an Akka system
    val system = ActorSystem("AkkaSystem")
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val master = system.actorOf(Props(new piCalculator(nrOfWorkers, nrOfMessages, nrOfElements, listener)), name = "master")

    // start the calculation
    master ! Calculate
  }
}
