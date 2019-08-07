package controllers

import javax.inject._
import models.CarFormData
import play.api.Logging
import play.api.libs.json.Json._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import play.filters.csp.CSPActionBuilder
import services.CarService
import models.{Car, Cars}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CarController @Inject() (cspAction: CSPActionBuilder, cc: ControllerComponents, carService: CarService) extends AbstractController(cc) with Logging{

  def index() = Action.async {implicit  request =>
    logger.trace("index: ")
    carService.listAllCars.map { cars =>
      Ok(views.html.index())
    }
  }

  def listCars() = Action.async {implicit  request =>
    logger.trace("index: ")
    carService.listAllCars.map { cars =>
      Ok(Json.obj("Message" -> ("car '" + cars)))
    }
  }
/*
  def addCar() = cspAction.async {implicit request: Request[AnyContent] =>

  }
*/
}
