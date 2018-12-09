package controllers
import javax.inject._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import views.html

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(customerService: CustomerRepository,
                               projectService: ProjectRepository,
                               cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {


  val Home = Redirect(routes.HomeController.listProjects())


  val projectForm = Form(
    mapping(
      "id" -> ignored(0.toLong),
      "name" -> nonEmptyText,
      "customerId" -> longNumber,
    )(Project.apply)(Project.unapply)
  )


  def index = Action {
    Redirect(routes.HomeController.listProjects())
  }



  def listProjects = Action.async { implicit request =>
    projectService.list.map { projectsWithCustomers =>
      Ok(views.html.listProjects(projectsWithCustomers))
    }
  }


  def createProject= Action.async { implicit request =>
    customerService.options.map { options =>
      Ok(views.html.createForm(projectForm, options))
    }
  }


  def saveProject = Action.async { implicit request =>
    projectForm.bindFromRequest.fold(
      formWithErrors => customerService.options.map { options =>
        BadRequest(html.createForm(formWithErrors, options))
      },
      project => {
        projectService.insert(project).map { _ =>
          Home.flashing("success" -> "Project %s has been created".format(project.name))
        }
      }
    )
  }


  def editProject(id: Long) = Action.async { implicit request =>
    projectService.findById(id).flatMap {
      case Some(project) =>
        customerService.options.map { options =>
          Ok(html.editProject(id, projectForm.fill(project), options))
        }
      case _ =>
        Future.successful(NotFound)
    }
  }


  def updateProject(id: Long) = Action.async { implicit request =>
    projectForm.bindFromRequest.fold(
      formWithErrors=> {
        customerService.options.map { options =>
          BadRequest(html.editProject(id, formWithErrors, options))
        }
      },
      project => {
        projectService.update(id, project).map { _ =>
          Home.flashing("success" -> "Project %s has been updated".format(project.name))
        }
      }
    )
  }


  def deleteProject(id: Long) = Action.async {
    projectService.delete(id).map { _ =>
      Home.flashing("success" -> "Project has been deleted")
    }
  }

}