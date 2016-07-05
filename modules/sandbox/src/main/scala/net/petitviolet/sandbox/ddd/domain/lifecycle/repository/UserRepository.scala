package net.petitviolet.sandbox.ddd.domain.lifecycle.repository

import net.petitviolet.sandbox.ddd.domain.lifecycle.Repository
import net.petitviolet.sandbox.ddd.domain.model.entity.{User, UserId}

import scala.collection.mutable.{Map => mMap}

class UserRepository extends Repository[UserId, User] {
  override val DB = mMap.empty[UserId, User]
}
