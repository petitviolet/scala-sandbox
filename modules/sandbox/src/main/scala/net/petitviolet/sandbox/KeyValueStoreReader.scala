package net.petitviolet.sandbox

import scala.language.implicitConversions

trait KvsType {
  type Key = String
  type Value = String
}

object KeyValueStoreReader extends KvsType {
  trait KVS {
    def get(key: Key): Value
    def put(key: Key, value: Value): Unit
    def delete(key: Key): Unit
  }

  case class Reader[A, B](f: A => B) {
    def apply(a: A) = f(a)
    def map[C](g: B => C): Reader[A, C] = g compose f
    def flatMap[C](g: B => Reader[A, C]): Reader[A, C] = (a: A) => g(f(a))(a)
  }

  implicit def toReader[A, B](f: A => B): Reader[A, B] = Reader(f)

  //  def modify[A](key: Key, f: Value => Value)(implicit reader: Reader[KVS, A]): Reader[KVS, Unit] = {
  //    for {
  //      v <- reader.get(key)
  //      _ <- kvs.put(key, f(v))
  //    } yield ()
  //  }

}

object KeyValueStoreFree extends KvsType {
  sealed trait KVS[A]
  case class Get[A](key: Key, f: Value => A) extends KVS[A]
  case class Put[A](key: Key, value: Value, a: A) extends KVS[A]
  case class Delete[A](key: Key, a: A) extends KVS[A]

  //  def modify(key: Key, f: Value => Value): KVS[KVS[Unit]] =
  //    Get(key, (v: Value) => Put(f(v), ()))

}
