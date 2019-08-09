package DAO

import java.sql.Date

import javax.inject.Inject
import models.Car
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile
import slick.jdbc.MySQLProfile.api._
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}

class CarDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  class CarTable(tag: Tag) extends Table[Car](tag, "Car"){

    def id = column[Option[Int]]("id", O.PrimaryKey,O.AutoInc)
    def title = column[String]("title")
    def fuel_id = column[Int]("fuel_id")
    def price = column[Int]("price")
    def new_car = column[Boolean]("new_car")
    def milage = column[Option[Int]]("milage")
    def first_registration = column[Option[Date]]("first_registration")

    override def * =
      (id, title, fuel_id, price, new_car, milage, first_registration) <> ((Car.apply _).tupled, Car.unapply)
  }


  val cars = TableQuery[CarTable]

  def add(car: Car): Future[Car.Id] = {
    dbConfig.db.run(cars += car)
  }

  def delete(id: Int): Future[Car.Id] = {
    dbConfig.db.run(cars.filter(_.id === id).delete)
  }

  def getById(id: Int): Future[Option[Car]] = {
    dbConfig.db.run(cars.filter(_.id === id).result.headOption)
  }

  def listAllBy(sortby: String): Future[Seq[Car]] = {
    if(sortby == "title"){
      dbConfig.db.run(cars.sortBy(_.title).result)
    }else if(sortby == "fuel_id"){
      dbConfig.db.run(cars.sortBy(_.fuel_id).result)
    }else if(sortby == "price"){
      dbConfig.db.run(cars.sortBy(_.price).result)
    }else if(sortby == "new_car"){
      dbConfig.db.run(cars.sortBy(_.new_car).result)
    }else if(sortby == "milage"){
      dbConfig.db.run(cars.sortBy(_.milage).result)
    }else if(sortby == "first_registration"){
      dbConfig.db.run(cars.sortBy(_.first_registration).result)
    }else if(sortby == "id") {
      dbConfig.db.run(cars.sortBy(_.id).result)
    }else {
      //exception : "No filter has been applied or wrong property to sort"
      dbConfig.db.run(cars.sortBy(_.id).result)
    }
  }
}
