package application.configuration

import org.apache.log4j.Logger;

/**
  * Created by nuno on 17-08-2016.
  */
object Holder extends Serializable {
  @transient lazy val log = Logger.getLogger(getClass.getName)
}
