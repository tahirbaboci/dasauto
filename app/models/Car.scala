package models

import java.sql.Date
import play.api.libs.json._

case class Car(id: Int, title: String, fuel_id: Int, price: Int, new_car: Boolean, milage: Option[Int], first_registration: Option[Date])

object Car {

  implicit val carFormat = Json.format[Car]

  type Id = Int
}

