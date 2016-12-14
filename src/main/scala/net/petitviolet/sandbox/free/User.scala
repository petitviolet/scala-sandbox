package net.petitviolet.sandbox.free

case class User(id: Long, name: String, email: String)

object User {
  private val db: Map[Long, User] = Map(
    1L -> User(1L, "adam", "adam@gmail.com"),
    2L -> User(1L, "ben", "ben@gmail.com"),
    3L -> User(1L, "charley", "charley@gmail.com"),
    4L -> User(1L, "david", "david@gmail.com"),
    5L -> User(1L, "elen", "elen@gmail.com")
  )

  def findAll: Seq[User] = db.values.toSeq

  def findById(id: Long): Option[User] = db.get(id)

//  def put(user: User): Unit =
}
