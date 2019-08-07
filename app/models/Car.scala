package models

import java.sql.Date

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import play.api.libs.json._
import play.api.libs.functional.syntax._
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}

case class Car(id: Int, title: String, fuel_id: Int, price: Int, new_car: Boolean, milage: Option[Int], first_registration: Option[Date])
case class CarFormData(title: String, fuel_id: Int, price: Int, new_car: Boolean, milage: Option[Int], first_registration: Option[Date])

/*
object CarForm {
  val form = Form(
    mapping(
      "title" -> nonEmptyText,
      "fuel_id" -> number,
      "price" -> longNumber,
      "new_car" -> boolean,
      "milage" -> longNumber,
      "first_registration" -> text,
    )(CarFormData.apply)(CarFormData.unapply)
  )
}
*/

import slick.jdbc.MySQLProfile.api._

class CarTable(tag: Tag) extends Table[Car](tag, "Car"){

  def id = column[Int]("id", O.PrimaryKey,O.AutoInc)
  def title = column[String]("title")
  def fuel_id = column[Int]("fuel_id")
  def price = column[Int]("price")
  def new_car = column[Boolean]("new_car")
  def milage = column[Option[Int]]("milage")
  def first_registration = column[Option[Date]]("first_registration")

  override def * =
    (id, title, fuel_id, price, new_car, milage, first_registration) <> ((Car.apply _).tupled, Car.unapply)
}

object Car {
  implicit val carRead: Reads[Car] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "title").read[String] and
      (JsPath \ "fuel_id").read[Int] and
      (JsPath \ "price").read[Int] and
      (JsPath \ "new_car").read[Boolean] and
      (JsPath \ "milage").readNullable[Int] and
      (JsPath \ "first_registration").readNullable[Date]
    )(Car.apply _)

  /*
  implicit val carWrite: Writes[Car] = (
    (JsPath \ "id").write[Option[Int]] and
      (JsPath \ "title").write[String] and
      (JsPath \ "fuel_id").write[Int] and
      (JsPath \ "price").write[Int] and
      (JsPath \ "new_car").write[Boolean] and
      (JsPath \ "milage").write[Option[Int]] and
      (JsPath \ "first_registration").write[Option[Date]]
    )(unlift(Car.unapply))
  */
}


class Cars @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  val cars = TableQuery[CarTable]

  def add(car: Car): Future[String] = {
    dbConfig.db.run(cars += car).map(res => "Car successfully added").recover {
      case ex: Exception => ex.getCause.getMessage
    }
  }

  def delete(id: Int): Future[Int] = {
    dbConfig.db.run(cars.filter(_.id === id).delete)
  }

  def getById(id: Int): Future[Option[Car]] = {
    dbConfig.db.run(cars.filter(_.id === id).result.headOption)
  }

  def listAll: Future[Seq[Car]] = {
    dbConfig.db.run(cars.sortBy(_.id).result)
  }

}
