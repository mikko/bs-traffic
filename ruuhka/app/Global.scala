import scala.slick.driver.H2Driver.simple._
import scala.slick.driver.H2Driver.simple.Database.threadLocalSession

import play.api.Application
import play.api.GlobalSettings
import play.api.Play.current
import play.api.db.DB
import util.TKL
import model.LocationUpdate
import model.LocationUpdates

object Global extends GlobalSettings {
  
  override def onStart(app: Application) {
    lazy val database = Database.forDataSource(DB.getDataSource())
    /*
    database.withSession {
    	LocationUpdates.ddl.create
    }
    * 
    */
    /*
    database withSession {
		LocationUpdates insert  new LocationUpdate(
			None,									
			"jotain",
			11,
			1,
			1.0d,
			2.0d,
			111l,
			1,
			2
		)		
	}
	* 
	*/
  	println ("Global ran")
    TKL.startRunner()
    println("Service running")
  }

  override def onStop(app: Application) {
  	println("onStop")
  	TKL.stopRunner()
  }
}
