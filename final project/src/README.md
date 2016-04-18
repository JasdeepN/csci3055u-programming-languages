#CSCI-3055U Final Project
###Scala

####Problem Statment
Distributed computing using the Akka system in Scala, with UI built on ScalaFX.

####Alternatives
#####[Erlang](https://www.erlang.org/)
The language features of Erlang make it very similar to the actor system of Akka.
#####[Quasar](https://github.com/puniverse/quasar)
Open source library for Java that add Erlang like actor system and Go channel like systems
#####[Pulsar](https://github.com/puniverse/pulsar)
Open source library for Clojure that add Erlang like actor system and Go channel like systems

####Build Tools
#####Scala Build Tool [SBT](http://www.scala-sbt.org/index.html)
+ Open source build tool for Scala and Java projects.
+ Similar to Maven/Ant
+ Native support for many test frameworks
+ Integration with Scala interpreter
+ Continuous compilation, testing, and deployment

####Code Walk

Imports:

	import akka.actor._
	import scala.collection.mutable.ArrayBuffer
	import akka.routing.RoundRobinPool
Diagnostic Code:

	class Worker() extends Actor {

	    override def preStart() {
	      println("\to Thread %s is running".format(self.path.name))
	    }

	    override def postStop() {
	      println("\tx Thread %s has stopped".format(self.path.name))
	    }
	    ...
	}
Overide the preStart and postStop methods so we know when each thread starts and stops executing

	class ThreadedTask(nrOfWorkers: Int, listener: ActorRef, msg: String) extends Actor
Generic class lets you specify the number of workers (threads), the actor that is listening, and then extra arguments for other functions etc.

	val workerRouter = context.actorOf(Props[Worker].withRouter(RoundRobinPool(nrOfWorkers)),
	name = "workerRouter")
Sets up the work round robin router to distribute work, Props sets the "worker" class and router type

	def start(nrOfWorkers: Int, msg: String) {
	    val system = ActorSystem("AkkaSystem")
	    val listener = system.actorOf(Props[Listener], name = "listener")

	    val Threaded_Task = system.actorOf(Props(new ThreadedTask(
		    nrOfWorkers, listener, msg)),
	        name = "master")

	    Threaded_Task ! start_task
	}
The method that is called by the UI to start the actor system

#####Types
Scala has the *"Any"* type which is used in the definition for ThreadedTask

	class ThreadedTask(nrOfWorkers: Int, listener: ActorRef, msg: Any) extends Actor
this allows any data type to be used, other languages would force multiple definitions instead

![Alt text](http://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")


