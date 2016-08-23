package presentation


import model.CoreActor
import org.apache.log4j.Logger

/**
  * Created by nuno on 17-08-2016.
  */
object ApplicationMain {

  private val log: Logger = Logger.getLogger(getClass.getName)

  def main(args: Array[String]): Unit = {

    log.info("Starting Application")
    CoreActor.startCommunication
    log.info("Exiting Application")

  }

}
