package net.petitviolet.sandbox.to_scalaz

object Model {

}

trait Identity[A] {
  val value: A

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Identity[_] => this.value == other.value
    case _ => false
  }
}
trait Entity[ID <: Identity[_]] {
  val id: ID

  override def equals(obj: scala.Any): Boolean = obj match {
    case other: Entity[_] => this.id == other.id
    case _ => false
  }
}

case class UserId(value: Long) extends Identity[Long]
case class User(id: UserId, name: String) extends Entity[UserId]

case class AddressId(value: Long) extends Identity[Long]
case class Address(id: AddressId, code: String, detail: String) extends Entity[AddressId]
