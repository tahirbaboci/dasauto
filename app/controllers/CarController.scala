package controllers

import javax.inject._
import play.api.Logging
import play.api.libs.json.Json._
import play.api.mvc.{AbstractController, ControllerComponents}
import services.CarService
import models.Car
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CarController @Inject() (cc: ControllerComponents, carService: CarService) extends AbstractController(cc) with Logging{

  def listCars(sortBy: String) = Action.async {implicit  request =>
    logger.trace("ListCars: ")
    val fields: List[String] = List("id", "title", "fuel_id", "price", "new_car", "mileage", "first_registration")
    carService.listAllCars(sortBy)
      .map { cars =>
        if(cars != None){
          if (!fields.contains(sortBy)){
            BadRequest(Json.obj("status" -> "400" ,"Message" -> (s"The field you have choosen to sort is wrong, please check again")))
          }
          else {
            Ok(Json.obj("status" -> "200", "Car Adverts" -> (Json.toJson(cars))))
          }
        }
        else {
          BadRequest(Json.obj("status" -> "400", "Message" -> (s"No record of cars was found")))
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not list cars: $e")
          NotFound(s"Failed to access to cars")
      }
  }

  def getCarById(id: Int) = Action.async {implicit request =>
    logger.trace("getCarById: ")
    carService.getCar(id)
      .map { car =>
        if(car != None){
          Ok(Json.obj("status" -> "200", "Car Advert" -> (Json.toJson(car))))
        }
        else {
          BadRequest(Json.obj("status" -> "400", "Message" -> (s"Car with id: $id Not Found")))
        }
      }
      .recover {
        case e: Exception =>
          logger.error(s"Could not get the car: $e")
          NotFound(s"Failed to access to car with id $id")
      }
  }

  def addCar = Action { implicit request =>
    logger.trace("addCar: ")
    val car  = Json.fromJson[Car](request.body.asJson.get).get
    if(checkForRule(car)){
      carService.addCar(car)
        .recover {
          case e: Exception =>
            logger.error(s"Car creation failed: $e")
            Forbidden(s"Car $car can't be created")
        }
      Ok(Json.obj("status" -> "200", "Message" -> (s"Car with title : '${car.title}' added successfully")))
    }
    else {
      BadRequest(Json.obj("status" -> "400", "Message" -> ("Cannot add the car, please check the rules below"),
                                                    "Rule 1" -> "NEW CAR cannot have mileage and first_registration data",
                                                    "Rule 2" -> "USED CAR should have mileage and first_registration date"))
      //Ok(s"Cannot add the car, please check the rules below<br><br>Rules : <br>1- NEW CAR cannot have mileage and first_registration data<br>2- USED CAR should have mileage and first_registration date")
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
      Ok(Json.obj("status" -> "200", "Message" -> (s"Car with id : ${show(car.id)} updated successfully")))
    }
    else{
      Future.successful {
        Forbidden(s"forbidden to update Car : $car")
      }
      BadRequest(Json.obj("status" -> "400", "Message" -> ("Cannot add the car, please check the rules below"),
        "Rule 1" -> "NEW CAR cannot have mileage and first_registration data",
        "Rule 2" -> "USED CAR should have mileage and first_registration date",
        "Rule 3" -> "You should provide ID to be able to update the car advert"))
    }
  }

  def deleteCar(id: Int) = Action.async {implicit  request =>
    logger.trace("deleteCar: ")
    carService.deleteCar(id)
      .map { res =>
        if(res == 0){
          Ok(Json.obj("status" -> "200", "Message" -> (s"Car with id : $id does not exist")))
        }
        else {
          Ok(Json.obj("status" -> "200", "Message" -> (s"Car with id : $id deleted successfully")))
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

  //Rule : New cars cannot have mileage and first_registration data
  // Used cars should have mileage and first_registration date
  def checkForRule(car: Car): Boolean = {
    if(car.new_car == true && car.mileage != None){
      false
    }
    else if (car.new_car == true && car.first_registration != None){
      false
    }
    else if (car.new_car == false && car.mileage == None) {
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
