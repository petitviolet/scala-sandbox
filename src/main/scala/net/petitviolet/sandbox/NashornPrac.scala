package net.petitviolet.sandbox

import java.io.FileReader
import javax.script.{Compilable, Invocable, ScriptEngine, ScriptEngineManager}

import scala.language.implicitConversions

object NashornPrac extends App {
  implicit def toAnyRef[A](seq: Seq[A]): Seq[AnyRef] = seq.map(_.asInstanceOf[AnyRef])

  val loadJStat = new FileReader("src/main/resources/jstat.min.js")
  //  val loadJStat = """load("https://raw.githubusercontent.com/jstat/jstat/master/dist/jstat.min.js");"""

  NashornService.compile(loadJStat)

  val function = F("f",
    """
    var f = function(a, b) {
      return jStat.beta.sample(a, b);
    };

    var console = {
        log: function(s) {
            java.lang.System.out.println(s);
        }
    };

    var ucb = function(creative_imp,creative_vimp,creative_click,creative_ctr,
                 ad_group_imp, ad_group_vimp, ad_group_click, ad_group_ctr) {
        return 100.0 * (creative_ctr + Math.sqrt(2 * Math.log(ad_group_imp)) / creative_imp);
    };

    var sum_imp1 = 1000 + 100 + 10000,
        sum_imp2 = 10000 + 1000 + 10000;

    console.log(ucb(1000, 0, 1, 0.10 * 0.01, sum_imp1, 0, 0, 0));
    console.log(ucb(100, 0, 0, 0.00 * 0.01, sum_imp1, 0, 0, 0));
    console.log(ucb(10000, 0, 20, 0.20 * 0.01, sum_imp1, 0, 0, 0));

    console.log('');

    console.log(ucb(10000, 0, 10, 0.10 * 0.01, sum_imp2, 0, 0, 0));
    console.log(ucb(1000, 0, 1, 0.10 * 0.01, sum_imp2, 0, 0, 0));
    console.log(ucb(10000, 0, 20, 0.20 * 0.01, sum_imp2, 0, 0, 0));
    """)
  NashornService.compile(function)

  NashornService.invokeAs[Double](function, Seq(1, 2))

  case class Creative(imp: Int, ctr: Double)
  case class AdGroup(imp: Int)

  val UCB = F("xxx",
    """
    var xxx = function(creative, ad_group, f) {
        console.log("creative: " + creative);
        console.log("ad_group: " + ad_group);
        console.log("ad_group: " + f(ad_group));
        return 100.0 * (creative.ctr() + Math.sqrt(2 * Math.log(ad_group.imp())) / creative.imp());
    };
    """
  )
  val x: AdGroup => Int = adGroup => adGroup.imp * 100000
  NashornService.compile(UCB)
  NashornService.invokeAs[Double](UCB, Seq(Creative(10000, 0.20 * 0.01), AdGroup(21000), x))
//  val α = 1
//  val results = (1 to 100) map { β =>
//    val arguments = Seq(α, β)
//    ((α, β), NashornService.invokeAs[Double](function, arguments))
//  }
//  println(results.maxBy { case ((a, b), score) => score })

}

case class F(name: String, function: String)


private object NashornService {
  private val ENGINE_NAME = "nashorn"
  private type ENGINE_TYPE = ScriptEngine with Invocable
  private val engine = createEngine()

  private def createEngine() = new ScriptEngineManager().getEngineByName(ENGINE_NAME).asInstanceOf[ENGINE_TYPE]

  /**
    * jsの関数をコンパイルして保存する
    * 同名の関数は後勝ちになる
    *
    * @param f
    */
  def compile(f: F): Unit = compile(f.function)

  def compile(function: String): Unit = engine.asInstanceOf[Compilable].compile(function).eval

  def compile(file: FileReader): Unit = {
    engine.asInstanceOf[Compilable].compile(file).eval
  }

  /**
    * 関数を実行する
    * 指定した関数名の関数が見付からなかった場合や関数内で例外が発生した場合は、例外が発生する
    *
    * @param f
    * @param args
    * @tparam A
    * @return
    */
  def invokeAs[A](f: F, args: Seq[AnyRef]): A = {
    invokeAs[A](f.name, args)
  }

  def invokeAs[A](functionName: String, args: Seq[AnyRef]): A = {
    val result = engine.invokeFunction(functionName, args: _*)
    println(s"NashornService: funcName => ${functionName}, args => $args, results => $result")
    result.asInstanceOf[A]
  }
}
