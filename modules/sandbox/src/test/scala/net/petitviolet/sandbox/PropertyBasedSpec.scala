package net.petitviolet.sandbox

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
        { s: String => if (s.nonEmpty) ().success else "password must not be empty".failureNel }, { s: String =>
          if (s.length >= 8 && s.length <= 16) ().success
          else "password length must be between 8 and 16".failureNel
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
    scenario("between 8 and 16") {
      val strGen = for {
        n <- Gen.chooseNum(8, 16)
        cs <- Gen.listOfN(n, Gen.alphaNumChar)
      } yield {
        cs.mkString
      }
      // below Gen discards too many test case
      // val strGen = Gen.alphaNumStr.suchThat { s => 8 <= s.length && s.length <= 16 }
      forAll(strGen) { (s: String) =>
        s.length >= 8 shouldBe true
        s.length <= 16 shouldBe true
      }
    }

    scenario("between 8 and 16 with arbitrary") {
      val strGen = Gen.chooseNum(8, 16).flatMap { n =>
        Gen.listOfN(n, Gen.alphaNumChar).map { cs =>
          cs.mkString
        }
      }
      implicit val arb = Arbitrary(strGen)
      forAll { (s: String) =>
        s.length >= 8 shouldBe true
        s.length <= 16 shouldBe true
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
      val shortStrGen = alphaNumStrGenWithMinMax(8, 16)
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
      val longStrGen = alphaNumStrGenWithMinMax(17, 100)
      forAll(longStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with only alpha string") {
      val alphaStrGen = strGenWithMinMax(8, 16, Gen.alphaChar)
      forAll(alphaStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
    scenario("fail with only num string") {
      val numStrGen = strGenWithMinMax(8, 16, Gen.numChar)
      forAll(numStrGen) { (s: String) =>
        Password.create(s).fold(
          { msgs => succeed },
          { succ => fail(s"raw: $s") }
        )
      }
    }
  }
}
