package controllers

import play.api._
import play.api.mvc._
import util.TKL
import scala.slick.driver.H2Driver.simple._
import model.LocationUpdates
import Database.threadLocalSession
import play.api.db.DB
import play.api.Play.current
import play.api.libs.json._

object Application extends Controller {
  lazy val database = Database.forDataSource(DB.getDataSource())

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
  
  def startService = Action {
  	TKL.startRunner
  	Ok("Service started")
  }

  def stopService = Action {
  	TKL.stopRunner
  	Ok("Service stopped")
  }

  def getStatus = Action {
	val json = database withSession {
      val list = for (u <- LocationUpdates) yield u.journey
      Json.toJson(list.list)
    }			
	Ok(TKL.getStatus +"\n" + json)
  }

}