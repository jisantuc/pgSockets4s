package com.azavea.pgsockets4s.tile

import java.net.URI

import cats.data.NonEmptyList
import cats.effect._
import geotrellis.contrib.vlm.TargetRegion
import geotrellis.contrib.vlm.geotiff.GeoTiffRasterSource
import geotrellis.proj4.{CRS, WebMercator}
import geotrellis.raster._
import geotrellis.raster.io.geotiff.AutoHigherResolution
import geotrellis.raster.resample.{NearestNeighbor, ResampleMethod}
import geotrellis.server._
import geotrellis.server.vlm._
import geotrellis.spark.SpatialKey
import geotrellis.vector.Extent

case class ImageNode(uri: URI)

object ImageNode extends RasterSourceUtils {

  def getRasterSource(uri: String): GeoTiffRasterSource = GeoTiffRasterSource(uri)

  implicit val cogNodeRasterExtents: HasRasterExtents[ImageNode] =
    new HasRasterExtents[ImageNode] {

      def rasterExtents(self: ImageNode)(implicit contextShift: ContextShift[IO]): IO[NonEmptyList[RasterExtent]] =
        getRasterExtents(self.uri.toString)
    }

  private val invisiTile: ByteArrayTile = ByteArrayTile.empty(256, 256)

  private val invisiRaster: Raster[MultibandTile] = Raster(
    MultibandTile(invisiTile, invisiTile, invisiTile),
    Extent(0, 0, 256, 256)
  )

  override def fetchTile(
      uri: String,
      zoom: Int,
      x: Int,
      y: Int,
      crs: CRS = WebMercator,
      method: ResampleMethod = NearestNeighbor
  ): IO[Raster[MultibandTile]] =
    IO {
      val key              = SpatialKey(x, y)
      val layoutDefinition = tmsLevels(zoom)
      val rasterSource =
        getRasterSource(uri).reproject(crs, method).tileToLayout(layoutDefinition, method)

      rasterSource.read(key).map(Raster(_, layoutDefinition.mapTransform(key)))
    } flatMap {
      case Some(t) =>
        IO.pure(t)
      case _ =>
        IO.pure(invisiRaster)
    }

  implicit val imageNodeTmsReification: TmsReification[ImageNode] =
    new TmsReification[ImageNode] {

      def tmsReification(self: ImageNode, buffer: Int)(
          implicit contextShift: ContextShift[IO]
      ): (Int, Int, Int) => IO[ProjectedRaster[MultibandTile]] = (z: Int, x: Int, y: Int) => {
        def fetch(xCoord: Int, yCoord: Int): IO[Raster[MultibandTile]] =
          fetchTile(self.uri.toString, z, xCoord, yCoord, WebMercator)

        fetch(x, y).map { tile =>
          val extent = tmsLevels(z).mapTransform.keyToExtent(x, y)
          ProjectedRaster(tile.tile, extent, WebMercator)
        }
      }
    }

  implicit val imageNodeExtentReification: ExtentReification[ImageNode] =
    new ExtentReification[ImageNode] {

      def extentReification(
          self: ImageNode
      )(implicit contextShift: ContextShift[IO]): (Extent, CellSize) => IO[ProjectedRaster[MultibandTile]] =
        (extent: Extent, cs: CellSize) => {
          getRasterSource(self.uri.toString)
            .resample(TargetRegion(new GridExtent[Long](extent, cs)), NearestNeighbor, AutoHigherResolution)
            .read(extent)
            .map { ProjectedRaster(_, WebMercator) }
            .toIO {
              new Exception(s"No tile available for RasterExtent: ${RasterExtent(extent, cs)}")
            }
        }
    }
}
