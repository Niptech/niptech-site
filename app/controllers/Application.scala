package controllers

import play.api._
import play.api.mvc._
import scala.concurrent.Future
import play.api.Play.current
import play.api.Play.{configuration => conf}

import scala.concurrent.ExecutionContext.Implicits.global
import wcm.WordpressAPI
import play.api.libs.ws.WS
import play.api.cache.Cached

object Application extends Controller {

  def index = Action {
    implicit request =>
      preloadEpisodesList
      Ok(views.html.index())
  }

  val port = conf.getString("http.port").getOrElse("80")

  def preloadLink(link: String) = {
    WS.url("http://localhost:" + port + link).get
    ""
  }

  def preloadEpisodesList = {
    preloadLink("/niptech")
    preloadLink("/nipdev")
    preloadLink("/niplife")
  }

  def displayEpisode(id: Int) = Cached("detail/" + id, 60) {
    Action.async(
      WordpressAPI.getEpisodeById(id).map {
        res =>
          Ok(views.html.episodeDetail(res \ "post"))
      }
    )
  }

  def episodeList(podcast: String, page: Int, count: Int) = Cached(podcast + ":" + page + ":" + count, 60) {
    Action.async(
      WordpressAPI.getLatestEpisodes(podcast, page, count).map {
        res =>
          Ok(views.html.episodeList(res))
      }
    )
  }

}