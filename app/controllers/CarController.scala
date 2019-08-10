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


  def listCars(sortBy: String) = cspAction.async {implicit  request =>
    logger.trace("ListCars: ")
    val fields: List[String] = List("id", "title", "fuel_id", "price", "new_car", "milage", "first_registration")
    carService.listAllCars(sortBy)
      .map { cars =>
        if(cars != None){
          if (!fields.contains(sortBy)){
            Ok(Json.obj("Message" -> (s"The field you have choosen to sort is wrong, please check again")))
          }
          else {
            Ok(Json.obj("Message" -> ("car '" + cars)))
          }
        }
        else {
          Ok(Json.obj("Message" -> (s"No record of cars was found")))
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not list cars: $e")
          NotFound(s"Failed to access to cars")
      }
  }

  def getCarById(id: Int) = cspAction.async {implicit request =>
    logger.trace("getCarById: ")
    carService.getCar(id)
      .map { car =>
        if(car != None){
          Ok(Json.obj("Message" -> ("car '" + car)))
        }
        else {
          Ok(Json.obj("Message" -> (s"Car with id: $id Not Found")))
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not get the car: $e")
          NotFound(s"Failed to access to car with id $id")
      }
  }

  def addCar = cspAction { implicit request =>
    logger.trace("addCar: ")
    val car  = Json.fromJson[Car](request.body.asJson.get).get
    if(checkForRule(car)){
      carService.addCar(car)
        .recover {
          case e: Exception =>
            logger.error(s"Car creation failed: $e")
            Forbidden(s"Car $car can't be created")
        }
      Ok(s"Car with title : '${car.title}' added successfully")
    }
    else {
      Ok(s"Cannot add the car, please check the rules below<br><br>Rules : <br>1- NEW CAR cannot have milage and first_registration data<br>2- USED CAR should have milage and first_registration date")
    }
  }

  def modifyCar = Action { implicit request =>
    logger.trace("modifyCar: ")
    val car  = Json.fromJson[Car](request.body.asJson.get).get
    if(checkForRule(car) && car.id != None){
      carService.updateCar(car)
        .recover {
          case e: Exception =>
            logger.error(s"Car updating failed: $e")
            Forbidden(s"Car $car can't be updated")
        }
      Ok(s"Car with id : ${show(car.id)} updated successfully")
    }
    else{
      Future.successful {
        Forbidden(s"forbidden to update Car : $car")
      }
      Ok(s"Cannot update the car, please check the rules below<br><br>Rules : <br>1- NEW CAR cannot have milage and first_registration data<br>2- USED CAR should have milage and first_registration date<br>3- To update the car information you should provide the car id")
    }
  }

  def deleteCar(id: Int) = Action.async {implicit  request =>
    logger.trace("deleteCar: ")
    carService.deleteCar(id)
      .map { res =>
        if(res == 0){
          Ok(s"Car with id : $id does not exist")
        }
        else {
          Ok(s"Car with id : $id deleted successfully")
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Failed to delete car: $e")
          Forbidden(s"Failed to delete car with id : $id")
      }
  }

  def show(x: Option[Int]) = x match {
    case Some(s) => s
    case None => None
  }

  //Rule : New cars cannot have milage and first_registration data
  // Used cars should have milage and first_registration date
  def checkForRule(car: Car): Boolean = {
    if(car.new_car == true && car.milage != None){
      false
    }
    else if (car.new_car == true && car.first_registration != None){
      false
    }
    else if (car.new_car == false && car.milage == None) {
      false
    }
    else if (car.new_car == false && car.first_registration == None) {
      false
    }
    else{
      true
    }
  }
}
