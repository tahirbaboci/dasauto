package services

import javax.inject.Inject
import models.{Car, Cars}

import scala.concurrent.Future

class CarService @Inject() (cars: Cars) {

  def addCar(car: Car): Future[String] = {
    cars.add(car)
  }

  def deleteCar(id: Int): Future[Int] = {
    cars.delete(id)
  }

  def getCar(id: Int): Future[Option[Car]] = {
    cars.getById(id)
  }

  def listAllCars(sortBy: String): Future[Seq[Car]] = {
    cars.listAllBy(sortBy)
  }
}
