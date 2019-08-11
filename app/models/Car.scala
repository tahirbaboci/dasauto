package models

import java.sql.Date
import play.api.libs.json._

case class Car(id: Option[Int], title: String, fuel_id: Int, price: Int, new_car: Boolean, mileage: Option[Int], first_registration: Option[Date])
case class Fuel(id: Int, fueltype: String)

object Car {

  implicit val carFormat = Json.format[Car]

  type Id = Int
}

