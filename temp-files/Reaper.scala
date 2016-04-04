import akka.actor._
import akka.testkit._
// import org.scalatest._
// import org.scalatest.matchers.MustMatchers
import akka.routing._
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.duration._

object Reaper {
  // Used by others to register an Actor for watching
  case class WatchMe(ref: ActorRef)
}

abstract class Reaper extends Actor {
  import Reaper._

  // Keep track of what we're watching
  val watched = ArrayBuffer.empty[ActorRef]

  // Derivations need to implement this method.  It's the
  // hook that's called when everything's dead
  def allSoulsReaped(): Unit

  // Watch and check for termination
  final def receive = {
    case WatchMe(ref) =>
    context.watch(ref)
    watched += ref
    case Terminated(ref) =>
    watched -= ref
    if (watched.isEmpty) allSoulsReaped()
  }
}

