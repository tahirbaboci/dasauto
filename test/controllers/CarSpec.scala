package controllers

import java.time.LocalDate

import org.scalatest.FlatSpec
import play.api.mvc.Results
import models.Car
import models.Car._
import org.junit.Assert

class CarSpec extends FlatSpec with Results {

  "Car" should " validate " in {
      Assert.assertEquals(
        validate(Car(None, "title", 1, 232323, true, Some(33333), None)),
        Some("NEW CAR cannot have mileage and first_registration data"))

      Assert.assertEquals(
        validate(Car(None, "title", 1, 232323, true, None, None)),
        None)

      Assert.assertEquals(
        validate(Car(None, "title", 1, 232323, false, None, None)),
        Some("USED CAR should have mileage and first_registration date"))

      Assert.assertEquals(
        validate(Car(None, "title", 1, 232323, false, Some(234234), Some(LocalDate.of(2015,3,12)))),
        None)
  }


}
