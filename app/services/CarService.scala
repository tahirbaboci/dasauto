package services


import javax.inject.Inject
import models.Car
import dao.CarDAO

import scala.concurrent.Future

class CarService @Inject() (cars: CarDAO) {

  def addCar(car: Car): Future[Int] = {
    cars.add(car)
  }

  def deleteCar(id: Int): Future[Boolean] = {
    cars.delete(id)
  }

  def updateCar(car: Car) : Future[Unit] = {
    cars.updateCar(car)
  }

  def getCar(id: Int): Future[Option[Car]] = {
    cars.getById(id)
  }

  def listAllCars(sortBy: String): Future[Seq[Car]] = {
    cars.listAllBy(sortBy)
  }
}