package com.azavea.pgsockets4s.database.util

import doobie._

trait Filterables {

  implicit val fragmentFilter: Filterable[Any, doobie.Fragment] =
    Filterable[Any, Fragment] { fragment: Fragment =>
      List(Some(fragment))
    }

  implicit def maybeTFilter[T](
      implicit filterable: Filterable[Any, T]
  ): Filterable[Any, Option[T]] = Filterable[Any, Option[T]] {
    case None        => List.empty[Option[Fragment]]
    case Some(thing) => filterable.toFilters(thing)
  }

  implicit def listTFilter[T](
      implicit filterable: Filterable[Any, T]
  ): Filterable[Any, List[T]] = Filterable[Any, List[T]] {
    someFilterables: List[T] =>
      {
        someFilterables
          .map(filterable.toFilters)
          .foldLeft(List.empty[Option[Fragment]])(_ ++ _)
      }
  }
}

object Filterables extends Filterables
