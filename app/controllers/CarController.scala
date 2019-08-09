package controllers

import javax.inject._
import play.api.Logging
import play.api.libs.json.Json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request, Result}
import play.filters.csp.CSPActionBuilder
import services.CarService
import models.{Car}
import play.api.libs.json.Json
import play.libs.exception.ExceptionUtils

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CarController @Inject() (cspAction: CSPActionBuilder, cc: ControllerComponents, carService: CarService) extends AbstractController(cc) with Logging{


/*
  def addCar: Action[Car] = Action.async(parse.json[Car]) { request =>
    logger.trace("addCar: ")
    request.body
      carService.addCar(request.body)
        .map{ id =>
          Ok(s"Car with id : $id added successfully")
        }
        .recover {
          case ex: Exception => Ok(ex.getCause.getMessage)
        }
  }
*/

  def addCar = Action { implicit request =>
    val car  = Json.fromJson[Car](request.body.asJson.get).get
    if(car.new_car == true && (car.milage != 0 || car.first_registration != null)){
      Future.successful {
        Forbidden(s"forbidden to add Car : $car")
      }
      Ok(s"Car with title : ${car.title} failed to be added because you have added milage or/and first_registration data for new car")
    }
    else {
      carService.addCar(car)
        .map{ id =>
          Ok(s"Car with id : $id added successfully")
        }
        .recover {
          case e: Exception =>
            logger.error(s"Car creation failed: $e")
            Forbidden(s"Car $car can't be created")
        }
      Ok(Json.toJson(car))
    }
  }

  def listCars(sortBy: String) = Action.async {implicit  request =>
    logger.trace("ListCars: ")
    carService.listAllCars(sortBy)
      .map { cars =>
        Ok(Json.obj("Message" -> ("car '" + cars)))
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not list cars: $e")
          NotFound(s"Failed to access to cars")
      }
  }

  def deleteCar(id: Int) = Action.async {implicit  request: Request[AnyContent] =>
    carService.deleteCar(id)
      .map { res =>
        Ok(s"Car with id : $id deleted successfully")
      }
      .recover {
        case e: Exception =>
          logger.error(s"Failed to delete car: $e")
          Forbidden(s"Failed to delete car with id : $id")
      }
  }



}
