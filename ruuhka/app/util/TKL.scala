package util


import org.joda.time.DateTime
import org.joda.time.DateTime._
import play.api.libs.ws._
import play.api.libs.json._
import scala.concurrent._
import play.api.libs.concurrent.Execution.Implicits._
import scala.slick.driver.H2Driver.simple._
import Database.threadLocalSession
import model.LocationUpdates
import model.LocationUpdate
import play.api.db.DB
import play.api.Play.current


object TKL {
    lazy val database = Database.forDataSource(DB.getDataSource())

	var runnerOn = false
	var lastResponse = 0L
	
	def setLastResponse( update: Long ) {
		lastResponse = update
	}

	def stopRunner() = {
		runnerOn = false
	}

	def startRunner() = {
		if( !runnerOn ) {
			runnerOn = true
			initRunner		
		}
	}

	def getStatus() = {
		val update = new DateTime(lastResponse).toLocalDateTime
		runnerOn  match {
  			case x if x => "Taustapalvelu käynnissä	Edellinen update " + update
  			case _ => "Taustapalvelu pysäytetty"
  		}
	}

	private def initRunner() = {
		new Thread(new Runnable {
	    	def run() {
				while( runnerOn ) {
					val next = now.plusSeconds(10)
					while( now.getMillis < next.getMillis) {}
					val url = "http://lissu.tampere.fi/ajax_servers/busLocations.php?ts=" + next.getMillis
					println("Fetching from url " + url)
					val tmp = WS.url(url).get.map( 
						response => {
							//database withSession {
							val json = response.json
							val updateList = json.as[List[JsObject]]
							println("Updates: " + updateList.length)
							val resultList = for( update <- updateList ) yield {									
								val journey: String = (update \ "journeyId").as[String]								
								val line = ((update \ "lCode").as[String])								
								val direction= (update \ "direction").as[String]
								val lat= ((update \ "x").as[String]).toDouble
								val lon = ((update \ "y").as[String]).toDouble
								val curr = (update \ "currStop").as[String]
								val prev = (update \ "prevStop").as[String]
								val ts = next.getMillis
								
								LocationUpdate(
									None,									
									journey,
									line,
									direction,
									lat,
									lon,
									ts,
									curr,
									prev)									
								
							}
							println( resultList )
							database withSession {
								for( result <- resultList ) {
								  LocationUpdates insert result
								}							  
							}
						}
					)					
					//println ("GetReady")
					//TKL.setLastResponse(next.getMillis)
				}
			}

		}).start
	}
	//http://lissu.tampere.fi/ajax_servers/busLocations.php?ts=1361632542334
}