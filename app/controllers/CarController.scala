package controllers

import javax.inject._
import play.api.Logging
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.CarService
import models.Car._
import models.Car

import scala.concurrent.ExecutionContext.Implicits.global


class CarController @Inject()(cc: ControllerComponents, carService: CarService) extends AbstractController(cc) with Logging {

  def listCars(fieldToSortBy: String): Action[AnyContent] = Action.async { implicit request =>
    logger.trace("ListCars: ")
    val fields: List[String] = List("id", "title", "fuelId", "price", "newCar", "mileage", "firstRegistration")
    carService.listAllCars(fieldToSortBy)
      .map { cars =>
        if (cars.nonEmpty) {
          if (!fields.contains(fieldToSortBy)) {
            BadRequest(s"The field you have choosen to sort is wrong, please check again")
          } else {
            Ok(Json.toJson(cars))
          }
        } else {
          BadRequest(s"No record of cars was found")
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not list cars: $e")
          NotFound(s"Failed to access to cars")
      }
  }

  def getCarById(id: Int): Action[AnyContent] = Action.async { implicit request =>
    logger.trace("getCarById: ")
    carService.getCar(id)
      .map { car =>
        if (car.isDefined) {
          Ok(Json.toJson(car))
        } else {
          BadRequest(s"Car with id: $id Not Found")
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not get the car: $e")
          NotFound(s"Failed to access to car with id $id")
      }
  }

  def addCar: Action[AnyContent] = Action { implicit request =>
    logger.trace("addCar: ")
    val car = Json.fromJson[Car](request.body.asJson.get).get
    validate(car) match {
      case None => {
        carService.addCar(car)
          .recover {
            case e: Exception =>
              logger.error(s"Car creation failed: $e")
              Forbidden(s"Car $car can't be created")
          }
        Ok(s"Car with title : '${car.title}' added successfully")
      }
      case Some(error) => BadRequest(error)
    }
  }

  def modifyCar: Action[AnyContent] = Action { implicit request =>
    logger.trace("modifyCar: ")
    val car = Json.fromJson[Car](request.body.asJson.get).get
    car.id match {
      case None => BadRequest("You should provide ID to be able to update the car advert")
      case Some(id) =>
        validate(car) match {
          case None => {
            carService.updateCar(car)
              .recover {
                case e: Exception =>
                  logger.error(s"Car updating failed: $e")
                  Forbidden(s"Car $car can't be updated")
              }
            Ok(s"Car with id : $id updated successfully")
          }
          case Some(error) => BadRequest(error)
      }
    }
  }

  def deleteCar(id: Int): Action[AnyContent] = Action.async { implicit request =>
    logger.trace("deleteCar: ")
    carService.deleteCar(id)
      .map { res =>
        if (!res) {
          BadRequest(s"Car with id : $id does not exist")
        } else {
          Ok(s"Car with id : $id deleted successfully")
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Failed to delete car: $e")
          Forbidden(s"Failed to delete car with id : $id")
      }
  }
}
