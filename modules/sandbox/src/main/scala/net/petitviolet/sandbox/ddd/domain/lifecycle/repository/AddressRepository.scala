package net.petitviolet.sandbox.ddd.domain.lifecycle.repository

import net.petitviolet.sandbox.ddd.domain.lifecycle.Repository
import net.petitviolet.sandbox.ddd.domain.model.entity.{Address, AddressId}

import scala.collection.mutable.{Map => mMap}

class AddressRepository extends Repository[AddressId, Address] {
  override val DB = mMap.empty[AddressId, Address]
}
