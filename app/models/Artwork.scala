package models

case class Artwork(source: String, id: String, imageSrc: String)

case class DeleteArtworkDTO(source: String, id: String)