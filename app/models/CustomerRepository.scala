package models

import anorm.SqlParser.{get, str}
import anorm._
import javax.inject.Inject
import play.api.db.DBApi

import scala.concurrent.Future
import scala.util.{Failure, Success}

case class Customer(id: Long, name: String)
case class CustomerOptions(stringId: String, name: String)

@javax.inject.Singleton
class CustomerRepository @Inject()(dbapi: DBApi)(
    implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  private[models] val customerParser = {
    get[Long]("customer.id") ~
      get[String]("customer.name") map {
      case id ~ name ⇒ Customer(id, name)
    }
  }

  private val customerOptionsParser = {
    get[Long]("customer.id") ~
      get[String]("customer.name") map {
      case id ~ name ⇒ CustomerOptions(id.toString, name)
    }
  }

  def options: Future[Seq[CustomerOptions]] =
    Future(db.withConnection { implicit connection ⇒
      SQL"select * from customer order by name".as(customerOptionsParser.*)
    })

}
