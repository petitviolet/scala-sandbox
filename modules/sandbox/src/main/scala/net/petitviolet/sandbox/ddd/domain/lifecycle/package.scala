package net.petitviolet.sandbox.ddd.domain

import net.petitviolet.sandbox.ddd.domain.model.{Identity, Entity}

package object lifecycle {

  /**
    * 集約に関する操作
    *
    * @tparam ID
    * @tparam E
    */
  trait Repository[ID <: Identity[_], E <: Entity[ID]] {

    import scala.collection.mutable.{Map => mMap}

    val DB: mMap[ID, E]

    def resolveById(id: ID): Option[E] = DB.get(id)

    def store(entity: E): Boolean =
      DB.get(entity.id) match {
        case Some(_) =>
          false
        case None =>
          DB.put(entity.id, entity)
          true
      }

    def update(id: ID, entity: E) = DB.update(id, entity)

    def delete(id: ID): Boolean = DB.remove(id).isDefined

    def exists(id: ID) = DB.get(id).isDefined
  }


}
