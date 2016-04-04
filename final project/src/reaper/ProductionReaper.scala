class ProductionReaper extends Reaper {
  // Shutdown
  def allSoulsReaped(): Unit = {
  	println("all souls reaped")
  	context.system.terminate()
  }
}
