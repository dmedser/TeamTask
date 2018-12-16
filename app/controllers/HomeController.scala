package controllers

import javax.inject._
import play.api.mvc._
import models._
import play.api.data.Form
import play.api.data.Forms._
import views.html

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HomeController @Inject()(
    customerService: CustomerRepository,
    projectService: ProjectRepository,
    cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {

  val Home: Result = Redirect(routes.HomeController.listProjects())

  val projectForm = Form(
    mapping(
      "id" → ignored(0.toLong),
      "name" → nonEmptyText,
      "customerId" → longNumber,
    )(Project.apply)(Project.unapply)
  )



  def index: Action[AnyContent] = Action {
    Home
  }

  def listProjects: Action[AnyContent] = Action.async { implicit request ⇒
    projectService.list.map { projectsWithCustomers ⇒
      Ok(views.html.listProjects(projectsWithCustomers))
    }
  }

  def createProject: Action[AnyContent] = Action.async { implicit request ⇒
    customerService.options.map { options ⇒
      Ok(
        views.html.createForm(projectForm,
                              options.map(customerOption ⇒
                                customerOption.stringId → customerOption.name)))
    }
  }

  def saveProject: Action[AnyContent] = Action.async { implicit request ⇒
    projectForm.bindFromRequest.fold(
      formWithErrors ⇒
        customerService.options.map { options ⇒
          BadRequest(
            html.createForm(formWithErrors,
                            options.map(customerOption ⇒
                              customerOption.stringId → customerOption.name)))
      },
      project ⇒ {
        projectService.insert(project).map { _ ⇒
          Home.flashing("success" → s"Project ${project.name} has been created")
        }
      }
    )
  }

  def editProject(id: Long): Action[AnyContent] = Action.async {
    implicit request ⇒
      projectService.findById(id).flatMap {
        case Some(project) ⇒
          customerService.options.map { options ⇒
            Ok(
              html.editProject(
                id,
                projectForm.fill(project),
                options.map(customerOption ⇒
                  customerOption.stringId → customerOption.name)))
          }
        case None ⇒
          Future.successful(NotFound)
      }
  }

  def updateProject(id: Long): Action[AnyContent] = Action.async {
    implicit request ⇒
      projectForm.bindFromRequest.fold(
        formWithErrors ⇒ {
          customerService.options.map { options ⇒
            BadRequest(
              html.editProject(
                id,
                formWithErrors,
                options.map(customerOption ⇒
                  customerOption.stringId → customerOption.name)))
          }
        },
        project ⇒ {
          projectService.update(id, project).map { _ ⇒
            Home.flashing(
              "success" → s"Project ${project.name} has been updated")
          }
        }
      )
  }

  def deleteProject(id: Long): Action[AnyContent] = Action.async {
    projectService.delete(id).map { _ ⇒
      Home.flashing("success" → "Project has been deleted")
    }
  }

}
