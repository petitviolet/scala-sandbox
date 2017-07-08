package net.petitviolet.sandbox.ddd.domain.model.entity

import net.petitviolet.sandbox.ddd.domain.model.{ Entity, Identity }

case class AddressId(value: Long) extends Identity[Long]
case class Address(id: AddressId, code: String, detail: String) extends Entity[AddressId]
