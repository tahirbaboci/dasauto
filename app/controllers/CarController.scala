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



  def addCar: Action[Car] = Action.async(parse.json[Car]) { request =>
    logger.trace("addCar: ")
      carService.addCar(request.body)
        .map{
          id => Ok(s"Car with id : $id added successfully")
        }
        .recover {
          case ex: Exception => Ok(ex.getCause.getMessage)
        }
  }

  def listCars(sortBy: String) = Action.async {implicit  request =>
    logger.trace("ListCars: ")
    carService.listAllCars(sortBy)
      .map {
        cars => Ok(Json.obj("Message" -> ("car '" + cars)))
      }
      .recover {
        case ex: Exception => Ok(ex.getCause.getMessage)
      }
  }
}
