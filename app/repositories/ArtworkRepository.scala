package repositories

import javax.inject.Inject
import play.api.db._
import anorm._
import scala.concurrent.{Future, ExecutionContext}

import models.Artwork
import scala.util.{Try, Success, Failure}


class ArtworkRepository @Inject() (db: Database)(implicit ec: ExecutionContext) {

  def createOrUpdateArtwork(artwork: Artwork): Future[Boolean] = Future(db.withConnection { implicit connection =>
      val insertSql = SQL("""
        INSERT INTO artwork 
        VALUES ({source}::ARTWORK_SOURCE, {id}, {image_src})
        ON CONFLICT (source, id)
        DO
          UPDATE SET image_src = {image_src}
      """)
      .on("id" -> artwork.id, "source" -> artwork.source, "image_src" -> artwork.imageSrc)

      // Use Try to catch exceptions and return a Future with meaningful result
      Try {
        insertSql.executeUpdate()
      } match {
        case Success(affectedRows) => affectedRows > 0 // Insertion successful
        case Failure(_) => false // Insertion failed due to an exception
      }
  })

  def getAllArtwork(): Future[List[Artwork]] = Future(db.withConnection { implicit connection => 
    val parser: RowParser[Artwork] = Macro.parser[Artwork]("source", "id", "image_src")
    SQL("SELECT * FROM artwork").as(parser.*)
  })

}