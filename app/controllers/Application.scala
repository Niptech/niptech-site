package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global
import wcm.WordpressAPI
import play.api.libs.ws.WS

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def displayEpisode(id: Int) = Action.async(
    WordpressAPI.getEpisodeById(id).map {
      res =>
        Ok(views.html.episodeDetail(res \ "post"))
    }
  )

  def episodeList(podcast: String, page: Int, count: Int) = Action.async(
    WordpressAPI.getLatestEpisodes(podcast, page, count).map {
      res =>
        Ok(views.html.episodeList(res))
    }
  )

  def test = Action.async(
    WS.url("http://www.niptech.com/?json=get_post&id=3357")
      .get
      .map {
      res =>
        Ok(res.body)
    }
  )

}