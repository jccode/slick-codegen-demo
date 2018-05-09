package core

trait BaseEntity {
  def id: Int
  def createTime: java.sql.Timestamp
  def updateTime: java.sql.Timestamp
}

