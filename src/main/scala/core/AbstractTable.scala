package core

import slick.lifted.Rep

trait AbstractTable {
  def id: Rep[Int]
}
