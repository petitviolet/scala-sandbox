package net.petitviolet.sandbox


object InitRandom extends App {

  import scala.util.Random
  import scala.util.Random.shuffle

  def now = System.currentTimeMillis

  val NUM = 1000000
  val RANGE = (1 until NUM).toStream

  var start = now
  var end = now
  var result: Seq[Int] = Seq()

  start = now
  result = RANGE.map(i => (new Random).nextInt(NUM)).toSeq
  end = now
  println(s"Finish: ${end - start}, ${result.length}")

  start = now
  val r = new Random
  result = RANGE.map(i => r.nextInt(NUM)).toSeq
  end = now
  println(s"Finish: ${end - start}, ${result.length}")

  start = now
  val l = shuffle(RANGE).view.zipWithIndex.toMap
  result = RANGE.flatMap(i => l.get(i)).toSeq
  end = now
  println(s"Finish: ${end - start}, ${result.length}")
}

