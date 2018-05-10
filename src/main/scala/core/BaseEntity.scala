package core

import java.sql.Timestamp

import shapeless._

trait BaseEntity {
  def id: Int
  def createTime: Timestamp
  def updateTime: Timestamp

  val witCreateTime = Witness('createTime)
  val witUpdateTime = Witness('updateTime)
  type TypeCreateTime = witCreateTime.T
  type TypeUpdateTime = witUpdateTime.T
}

object BaseEntity {
  def copyWithUpdateTime[T <: BaseEntity](t: T, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeCreateTime, Timestamp]): T = {
    val len = mkLens()
    len.set(t)(time)
  }

  def copyWithCreateTime[T <: BaseEntity](t: T, time: Timestamp)(implicit mkLens: MkFieldLens.Aux[T, T#TypeUpdateTime, Timestamp]): T = {
    val len = mkLens()
    len.set(t)(time)
  }
}

