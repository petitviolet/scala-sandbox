package net.petitviolet.sandbox

import java.util.{ Calendar, GregorianCalendar }

import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks

import scalaz.Scalaz._
import scalaz._

class PropertyBasedSpec extends FeatureSpec with GeneratorDrivenPropertyChecks with Matchers {
  feature("feature") {
    scenario("scenario") {
      forAll(Gen.alphaStr) { alphaStr: String =>
        alphaStr shouldBe alphaStr.reverse.reverse
      }
    }
    scenario("Gen.calendar") {
      forAll(Gen.calendar) { calendar: Calendar =>
        whenever(calendar.get(Calendar.ERA) == GregorianCalendar.AD) {
          val before = calendar.get(Calendar.YEAR)
          calendar.add(Calendar.YEAR, 100)
          val after = calendar.get(Calendar.YEAR)
          (before + 100) shouldBe after
        }
      }
    }
    scenario("Gen.alphaStr") {
      forAll(Gen.alphaLowerStr, Gen.numStr) { (alpha, num) =>
        whenever(alpha.nonEmpty && num.nonEmpty) {
          (alpha + num).matches("^[a-z]+[0-9]+$") shouldBe true
        }
      }
    }
  }
  class Age(value: Int)
  object Age {
    private val (min, max) = (0, 120)

    def create(value: Int): Option[Age] =
      if (value >= min && value <= max) {
        Some(new Age(value))
      } else None
  }
  feature("Age with Example") {
    scenario("create valid age 1") {
      Age.create(0).isDefined shouldBe true
    }
    scenario("create valid age 2") {
      Age.create(50).isDefined shouldBe true
    }
    scenario("create valid age 3") {
      Age.create(120).isDefined shouldBe true
    }

    scenario("create invalid age 1") {
      Age.create(-1).isDefined shouldBe false
    }
    scenario("create invalid age 2") {
      Age.create(121).isDefined shouldBe false
    }
  }
  feature("Age with Example loop") {
    scenario("create valid age") {
      val values = Seq(0, 50, 120)
      values.foreach { value =>
        Age.create(value).isDefined shouldBe true
      }
    }

    scenario("create invalid age") {
      val values = Seq(-1, 121)
      values.foreach { value =>
        Age.create(value).isDefined shouldBe false
      }
    }
  }

  feature("Age with Property") {
    scenario("create valid age") {
      forAll(Gen.chooseNum(0, 120)) { value: Int =>
        Age.create(value).isDefined shouldBe true
      }
    }
    scenario("create invalid age") {
      val gen = Gen.oneOf(Gen.negNum[Int], Gen.posNum[Int].suchThat { _ > 120 })
      forAll(gen) { value: Int =>
        Age.create(value).isDefined shouldBe false
      }
    }
  }

  //  implicit class ZipGen[A, B](val g1: Gen[A]) {
  //    def zipWith[C](g2: Gen[B])(func: (Gen[A], Gen[B]) => C): C = func.apply(g1, g2)
  //  }

  case class Password(value: String)
  object Password {
    private val specs: Seq[String => ValidationNel[String, Unit]] = {
      val patterns = Seq(
        """^.*[a-zA-Z]+.*$""".r.pattern,
        """^.*[\d]+.*$""".r.pattern
      )

      Seq(
        { s: String => if (s.nonEmpty) ().success else "password must not be empty".failureNel },
        { s: String =>
          if (s.length >= 8 && s.length <= 30) ().success
          else "password length must be between 8 and 30".failureNel
        }, { s: String =>
          if (patterns.forall { _.matcher(s).matches() }) ().success
          else s"password must contains alpha and num".failureNel
        }
      )
    }

    def create(raw: String): ValidationNel[String, Password] = {
      val result: ValidationNel[String, Unit] =
        specs.map { _.apply(raw) }.foldLeft(().successNel[String]) { _ +++ _ }

      result match {
        case Success(b) => Password(raw).success
        case Failure(msgs) => msgs.failure
      }
    }
  }

  feature("string generator") {
    scenario("between 8 and 30") {
      val strGen = for {
        n <- Gen.chooseNum(8, 30)
        cs <- Gen.listOfN(n, Gen.alphaNumChar)
      } yield {
        cs.mkString
      }
      // below Gen discards too many test case
      // val strGen = Gen.alphaNumStr.suchThat { s => 8 <= s.length && s.length <= 30 }
      forAll(strGen) { (s: String) =>
        s.length >= 8 shouldBe true
        s.length <= 30 shouldBe true
      }
    }

    scenario("between 8 and 30 with arbitrary") {
      val strGen = Gen.chooseNum(8, 30).flatMap { n =>
        Gen.listOfN(n, Gen.alphaNumChar).map { cs =>
          cs.mkString
        }
      }
      implicit val arb = Arbitrary(strGen)
      forAll { (s: String) =>
        s.length >= 8 shouldBe true
        s.length <= 30 shouldBe true
      }
    }
  }

  feature("Password creation") {
    def strGenWithMinMax(min: Int, max: Int, base: Gen[Char] = Gen.alphaNumChar) =
      Gen.chooseNum(if (min < 0) 0 else min, max).flatMap { n =>
        Gen.listOfN(n, base).map { cs =>
          cs.mkString
        }
      }

    def alphaNumStrGenWithMinMax(min: Int, max: Int, base: Gen[Char] = Gen.alphaNumChar) = {
      //      val l = Stream.continually(1).map { _ =>
      //        s"${Gen.alphaChar.sample.get}${Gen.numChar.sample.get}${strGenWithMinMax(min - 2, max - 2, base).sample.get}"
      //      }.filter { s =>
      //        min <= s.length && s.length <= max
      //      }.take(100)
      //      Gen.oneOf(l)

      strGenWithMinMax(min - 2, max - 2, base).flatMap { s =>
        Gen.alphaChar.flatMap { a =>
          Gen.numChar.map { n =>
            s"$a$n$s"
          }
        }
      }
    }

    scenario("alphaNumStrGen generates nice string") {
      val gen = alphaNumStrGenWithMinMax(10, 20)
      forAll(gen) { (s: String) =>
        s.length >= 10 shouldBe true
        s.length <= 20 shouldBe true
      }
    }
    scenario("alphaNumStrGen generates nice string2") {
      val gen = alphaNumStrGenWithMinMax(1, 2)
      forAll(gen) { (s: String) =>
        s.length >= 1 shouldBe true
        s.length <= 2 shouldBe true
      }
    }
    scenario("success with proper string") {
      val shortStrGen = alphaNumStrGenWithMinMax(8, 30)
      forAll(shortStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => fail(s"raw: $s, ${msgs.stream.mkString(", ")}") },
          { succ => succeed }
        )
      }
    }
    scenario("fail with empty string") {
      val emptyStrGen = Gen.const("")
      forAll(emptyStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with short string") {
      val shortStrGen = alphaNumStrGenWithMinMax(1, 7)
      forAll(shortStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with long string") {
      val longStrGen = alphaNumStrGenWithMinMax(31, 100)
      forAll(longStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with only alpha string") {
      val alphaStrGen = strGenWithMinMax(8, 30, Gen.alphaChar)
      forAll(alphaStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with only num string") {
      val numStrGen = strGenWithMinMax(8, 30, Gen.numChar)
      forAll(numStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
  }
}
