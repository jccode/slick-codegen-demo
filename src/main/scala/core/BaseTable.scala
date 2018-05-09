package core

import slick.lifted.Rep

trait BaseTable {
  def id: Rep[Int]
}
