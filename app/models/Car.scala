package models

import java.time.LocalDate
import play.api.libs.json._

case class Car(
    id: Option[Car.Id],
    title: String,
    fuelId: Int,
    price: Int,
    newCar: Boolean,
    mileage: Option[Int],
    firstRegistration: Option[LocalDate])

case class Fuel(
    id: Int,
    fuelType: String)

object Car {

    implicit val carFormat: Format[Car] = Json.format[Car]

    type Id = Int

    def validate(car: Car): Option[String] = {
      if (car.newCar) {
        if (car.mileage.isEmpty && car.firstRegistration.isEmpty) {
          None
        } else {
          Some("NEW CAR cannot have mileage and first_registration data")
        }
      } else {
        if (car.mileage.isDefined && car.firstRegistration.isDefined) {
          None
        } else {
          Some("USED CAR should have mileage and first_registration date")
        }
      }
    }

}

