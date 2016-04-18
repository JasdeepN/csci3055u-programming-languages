package ActorUI

import akka.actor._
import scala.collection.mutable.ArrayBuffer
import akka.routing.RoundRobinPool

/**
 * Creates a thread object which contains everything needed to start
 * a multi threaded operation
 */

object threads {

   sealed trait Message

   case object start_task extends Message
   case class doSomething(msg: Any) extends Message
   case class Display_this(msg: Any) extends Message
   case class Worker_Func(msg: Any) extends Message
   case class Task(value: Any) extends Message

   /**
    * Generic class (basically a blank template) for what messages are
    * sent between workers
    */

   class Worker() extends Actor {

      override def preStart() {
         println("\to Thread %s is running".format(self.path.name))
      }

      override def postStop() {
         println("\tx Thread %s has stopped".format(self.path.name))
      }

      /**
       * Template method you could replace this with some meaningful calculations
       */

      def Worker_Func(msg: Any) = {
         println("could be doing calculations here")
         //can do calculations here
         msg
      }

      /**
       * Recieves different "work" calls dispatchs the correct worker
       */

      def receive = {
         //calls the "work" methods
         case doSomething(msg) =>
            sender ! Task(Worker_Func(msg))
      }
   }

   /**
    * Listener actor class executes based on case it recieves
    */

   class Listener extends Actor {

      def receive = {
         case Display_this(input) =>
            println(input)
            context.system.terminate()
         //...
      }
   }

   /**
    * Creates the round robin work router and contains the recieve cases
    * Usually called directly by start
    *
    * @pram number of threads
    * @pram listener actor
    * @pram other args
    */

   class ThreadedTask(nrOfWorkers: Int, listener: ActorRef, msg: Any) extends Actor {

      val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)), name = "workerRouter")

      def receive = {
         case `start_task` ⇒
            workerRouter ! doSomething(msg)

         case Task(value) ⇒
            // Send the result to the listener
            listener ! Display_this(value)

            // Stops this actor and all its supervised children
            context.stop(self)
      }
   }

   /**
    * Handles creating the actor system, starting the listener, and "Threaded_Task"
    * Currently set up to only print a message
    *
    * @pram number of threads to be used
    * @pram msg to print
    */

   def start(nrOfWorkers: Int, msg: String) {
      // Create an Akka system
      val system = ActorSystem("AkkaSystem")
      // create the result listener, which will print the result and shutdown the system
      val listener = system.actorOf(Props[Listener], name = "listener")

      // create the master
      val Threaded_Task = system.actorOf(Props(new ThreadedTask(
         nrOfWorkers, listener, msg)),
         name = "master")

      // start the calculation
      Threaded_Task ! start_task
   }
}
