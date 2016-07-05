package net.petitviolet.sandbox.ddd.domain.model.entity

import net.petitviolet.sandbox.ddd.domain.model.{Entity, Identity}

case class UserId(value: Long) extends Identity[Long]
case class User(id: UserId, name: String) extends Entity[UserId]
