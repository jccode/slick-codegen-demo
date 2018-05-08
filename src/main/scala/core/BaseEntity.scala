package core

trait BaseEntity {
  def id: Long
  def createTime: java.sql.Timestamp
  def updateTime: java.sql.Timestamp
}