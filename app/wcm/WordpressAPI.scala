package wcm

import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json
import controllers.Application


/**
 * Created by croiseaux on 01/01/2014.
 */
object WordpressAPI {

  val NIPTECH = "niptech"
  val NIPDEV = "nipdev"
  val NIPLIFE = "niplife"
  val NIPSALES = "nipsales"

  private val logger = Logger(Application.getClass)

  def wcmRq(query: String) =
    WS.url(s"http://www.niptech.com/?json=$query")
      .get
      .map {
      res =>
        logger.debug(s"Wordpress API request for query $query return status = ${res.status} content = ${res.body}")
        res.status match {
          case 200 => res.json
          case error =>
            logger.error(s"Wordpress API request for query $query return status = ${res.status} content = ${res.body}")
            Json.obj(
              "errorCode" -> error.toString,
              "message" -> res.body
            )
        }
    }

  def getLatestEpisodes(podcast: String, n: Int) = wcmRq(s"get_category_posts&slug=$podcast&count=$n")

  def getEpisodeById(id: Int) = wcmRq(s"get_post&id=$id")

}

