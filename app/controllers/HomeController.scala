package controllers

import javax.inject._
import play.api._
import play.api.mvc._

import services._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject() (c: UpdateCheck) extends Controller {

  def index = Action {
      Ok(views.html.index())
  }

  def list(token: String) = Action {
    Ok(views.html.list(c.checkAll(token).seq))
  }

}
