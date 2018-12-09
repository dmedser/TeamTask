package models

import anorm.SqlParser.{get, str}
import anorm._
import javax.inject.Inject
import play.api.db.DBApi

import scala.concurrent.Future
import scala.util.{ Failure, Success }

case class Customer(id: Long, name: String)

@javax.inject.Singleton
class CustomerRepository @Inject()(dbapi: DBApi)(implicit ec: DatabaseExecutionContext) {

  private val db = dbapi.database("default")

  private[models] val simple = {
    get[Long]("customer.id") ~
    get[String]("customer.name") map {
      case id ~ name => Customer(id, name)
    }
  }

  def options: Future[Seq[(String, String)]] = Future ( db.withConnection { implicit connection =>
    SQL"select * from customer order by name".
      fold(Seq.empty[(String, String)]) { (acc, row) =>
        row.as(simple) match {
          case Failure(parseError) => {
            println(s"Fails to parse $row: $parseError")
            acc
          }
          case Success(Customer(id, name)) =>
            (id.toString -> name) +: acc
        }
      }
  }).flatMap {
    case Left(error :: _) => Future.failed(error)
    case Left(_) => Future(Seq.empty)
    case Right(acc) => Future.successful(acc.reverse)
  }

}
