package models
import anorm.SqlParser.{get, scalar}
import anorm._
import javax.inject.Inject
import play.api.db.DBApi
import scala.concurrent.Future

case class Project(id: Long,
                   name: String,
                   customerId: Long)

object Project {
  implicit def toParameters: ToParameterList[Project] =
    Macro.toParameters[Project]
}

@javax.inject.Singleton
class ProjectRepository @Inject()(dbapi: DBApi, customerRepository: CustomerRepository)(implicit ec: DatabaseExecutionContext) {


  private val db = dbapi.database("default")


  private val simple = {
    get[Long]("project.id") ~
    get[String]("project.name") ~
    get[Long]("project.customer_id") map {
      case id ~ name ~ customerId =>
        Project(id, name, customerId)
    }
  }


  private val withCustomer = simple ~ customerRepository.simple map {
    case project ~ customer => project -> customer
  }


  def findById(id: Long): Future[Option[Project]] = Future {
    db.withConnection { implicit connection =>
      SQL"select * from project where id = $id".as(simple.singleOpt)
    }
  }(ec)


  def list: Future[Seq[(Project, Customer)]] = Future {
    db.withConnection { implicit connection =>
      SQL"""
        select * from project
        left join customer on project.customer_id = customer.id
      """.as(withCustomer.*)
    }
  }(ec)


  def insert(project: Project): Future[Option[Long]] = Future {
    db.withConnection { implicit connection =>
      SQL("""
        insert into project values (
          (select next value for project_seq),
          {name}, {customerId}
        )
      """).bind(project).executeInsert()
    }
  }(ec)


  def update(idOfProjectForUpdate: Long, project: Project) = Future {
    db.withConnection { implicit connection =>
      SQL(
        """
          update project set name = {name}, customer_id = {customerId} where id = {id}
        """).bind(project.copy(id = idOfProjectForUpdate)).executeUpdate()
    }
  }(ec)

  def delete(id: Long) = Future {
    db.withConnection { implicit connection =>
      SQL"delete from project where id = $id".executeUpdate()
    }
  }(ec)

}
