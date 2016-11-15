package net.petitviolet.sandbox

object SetPrac extends App {
  val sets = 1L to 1000L toSet

  assert(sets.size == 1000)
  assert((sets - 1L).size == 999)
  assert((sets - (1L, 2L, 3L)).size == 997)
}
