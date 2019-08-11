package dao

import java.time.LocalDate

import javax.inject.Inject
import models.{Car, Fuel}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}

class CarDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                      (implicit executionContext: ExecutionContext)
                       extends HasDatabaseConfigProvider[JdbcProfile] {

  val cars = TableQuery[CarTable]

  def add(car: Car): Future[Car.Id] = {
    dbConfig.db.run(cars += car)
  }

  def delete(id: Int): Future[Boolean] = {
    dbConfig.db.run(cars.filter(_.id === id).delete).map(res => res > 0)
  }

  def updateCar(car: Car): Future[Unit] = {
    dbConfig.db.run(cars.filter(_.id === car.id).update(car)).map(_ => ())
  }

  def getById(id: Int): Future[Option[Car]] = {
    dbConfig.db.run(cars.filter(_.id === id).result.headOption)
  }

  def listAllBy(fieldToSortBy: String): Future[Seq[Car]] = {
    fieldToSortBy match {
      case "title" => dbConfig.db.run(cars.sortBy(_.title).result)
      case "fuelId" => dbConfig.db.run(cars.sortBy(_.fuelId).result)
      case "price" => dbConfig.db.run(cars.sortBy(_.price).result)
      case "newCar" => dbConfig.db.run(cars.sortBy(_.newCar).result)
      case "mileage" => dbConfig.db.run(cars.sortBy(_.mileage).result)
      case "firstRegistration" => dbConfig.db.run(cars.sortBy(_.firstRegistration).result)
      case "id" => dbConfig.db.run(cars.sortBy(_.id).result)
      case _ => Future.failed(new IllegalArgumentException(s"Unknown column $fieldToSortBy"))
    }
  }

  class CarTable(tag: Tag) extends Table[Car](tag, "Car") {

    override def * =
      (id, title, fuelId, price, newCar, mileage, firstRegistration) <> ((Car.apply _).tupled, Car.unapply)

    def id = column[Option[Car.Id]]("id", O.PrimaryKey, O.AutoInc)

    def title = column[String]("title")

    def fuelId = column[Int]("fuelId")

    def price = column[Int]("price")

    def newCar = column[Boolean]("newCar")

    def mileage = column[Option[Int]]("mileage")

    def firstRegistration = column[Option[LocalDate]]("firstRegistration")
  }

  class FuelTable(tag: Tag) extends Table[Fuel](tag, "Fuel") {

    override def * =
      (id, fuelType) <> ((Fuel.apply _).tupled, Fuel.unapply)

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def fuelType = column[String]("fuelType")
  }
}
