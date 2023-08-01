package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import scala.collection.mutable
import play.api.libs.json._

import models._
import repositories.ArtworkRepository
import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.util.Success

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class CollectionController @Inject()(val controllerComponents: ControllerComponents, artworkRepository: ArtworkRepository)(implicit ec: ExecutionContext) extends BaseController {
    
  implicit val ArtworkJson: OFormat[Artwork] = Json.format[Artwork]
  implicit val DeleteArtworkDTOJson: OFormat[DeleteArtworkDTO] = Json.format[DeleteArtworkDTO]

  def getUserCollection() = Action.async { implicit request =>
    val futureResult: Future[List[Artwork]] = artworkRepository.getAllArtwork()

    futureResult.map { list =>
      if (list.isEmpty) {
        NoContent
      } else {
        Ok(Json.toJson(list))
      }
    }
  }

  def saveArtworkToCollection() = Action.async { implicit request => 
    val content = request.body 
    val jsonObject = content.asJson 
    val artwork: Option[Artwork] = 
      jsonObject.flatMap( 
        Json.fromJson[Artwork](_).asOpt 
      )

    artwork match {
      case Some(newArtwork) =>
        val futureResult: Future[Boolean] = artworkRepository.createOrUpdateArtwork(newArtwork)
        
        futureResult.map {
          case true =>
            Created(Json.toJson(newArtwork))
          case false =>
            InternalServerError("Artwork insertion failed.")
        }

      case None => Future {
        BadRequest
      }
    }
  }

  def deleteArtworkFromCollection() = Action.async { implicit request =>
    val content = request.body 
    val jsonObject = content.asJson 
    val deleteDTO: Option[DeleteArtworkDTO] = 
      jsonObject.flatMap( 
        Json.fromJson[DeleteArtworkDTO](_).asOpt 
      )

    deleteDTO match {
      case Some(deleteDTO) =>
        val futureResult: Future[Boolean] = artworkRepository.deleteArtwork(deleteDTO)

        futureResult.map {
          case true =>
            Ok
          case false =>
            InternalServerError("Artwork deletion failed.")
        }

      case None => Future {
        BadRequest
      }
    }
  }
}
