package net.petitviolet.sandbox.ddd.domain

package object model {
  /**
   * DBのPrimaryKeyに対応する
   * @tparam A
   */
  trait Identity[A] {
    val value: A

    override def equals(obj: scala.Any): Boolean = obj match {
      case other: Identity[_] => this.value == other.value
      case _ => false
    }
  }

  /**
   * DBの1カラムに対応するデータ
   * @tparam ID
   */
  trait Entity[ID <: Identity[_]] {
    val id: ID

    override def equals(obj: scala.Any): Boolean = obj match {
      case other: Entity[_] => this.id == other.id
      case _ => false
    }
  }

}
