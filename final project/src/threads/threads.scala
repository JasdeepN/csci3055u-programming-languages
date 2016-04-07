package ActorUI

import akka.actor._
import scala.collection.mutable.ArrayBuffer
import akka.routing.RoundRobinPool
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object threads {
  // println("WELCOME EXTENDING \"APP\" OF OBJECT ACTS AS MAIN")
  // calculate(nrOfWorkers = 4, nrOfElements = 10000, nrOfMessages = 10000)
  // print_something(nrOfWorkers = 2, nrOfMessages = 5, "This is a Thread")
  sealed trait Message
  case object Calculate extends Message
  case object start_task extends Message
  case class doSomething(msg: Any) extends Message
  case class Display_this(msg: Any) extends Message
  case class Worker_Func(msg: Any) extends Message
  case class Work(start: Int, nrOfElements: Int) extends Message
  case class Result(value: Double) extends Message
  case class Task(value: Any) extends Message
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

    def Worker_Func(msg: Any) = {
      println("could be doing calculations here")
      //can do calculations here
      msg
    }

    //work has been assigned to a worker
    def receive = {
      //calls the "work" methods
      case doSomething(msg) =>
        sender ! Task(Worker_Func(msg))
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

  class ThreadedTask(nrOfWorkers: Int, nrOfMessages: Int, listener: ActorRef, msg: String) extends Actor {
    var nrOfResults: Int = _
    val start: Long = System.currentTimeMillis

    val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

    def receive = {
      case `start_task` ⇒
        for (i ← 0 until nrOfMessages) workerRouter ! doSomething(msg)

      case Task(value) ⇒
        nrOfResults += 1
        // Send the result to the listener
        listener ! Display_this(value)
        if (nrOfResults == nrOfMessages) { //stops when # msgs == # results

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
        println("\n\tPi approximation: \t%s\n\tCalculation time: \t%s".format(pi, duration))
        context.system.terminate()

      case Display_this(input) ⇒
        println(input)
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

  def print_something(nrOfWorkers: Int, nrOfMessages: Int, msg: String) {
    // Create an Akka system
    val system = ActorSystem("AkkaSystem")
    // create the result listener, which will print the result and shutdown the system
    val listener = system.actorOf(Props[Listener], name = "listener")

    // create the master
    val Threaded_Task = system.actorOf(Props(new ThreadedTask(
      nrOfWorkers, nrOfMessages, listener, msg)),
      name = "master")

    // start the calculation
    Threaded_Task ! start_task
  }
}
