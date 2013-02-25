package model

import scala.slick.driver.H2Driver.simple._

case class LocationUpdate(id: Option[Int] = None, 
							journey: String, line: String, 
							direction: String, lat: Double, 
							lon: Double, timestamp: Long, 
							currentstop: String, prevstop: String)

object LocationUpdates extends Table[LocationUpdate]("LOCATIONUPDATE") {
	def id = column[Int]("ID", O.PrimaryKey, O.AutoInc)
	def journey = column[String]("JOURNEY")
	def line = column[String]("LINE")
	def direction = column[String]("DIRECTION")
	def lat = column[Double]("LAT")
	def lon = column[Double]("LON")
	def timestamp = column[Long]("TIMESTAMP")
	def currentstop = column[String]("CURRENTSTOP")
	def prevstop = column[String]("PREVSTOP")
	def * = id.? ~ journey ~ line ~ direction ~ lat ~ lon ~ timestamp ~ currentstop ~ prevstop <>(LocationUpdate, LocationUpdate.unapply _)
}