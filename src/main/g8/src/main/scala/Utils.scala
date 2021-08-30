package utils;

import cats.effect.std.Console
import cats.Show
import cats.Applicative
import fs2.Stream

trait SimpleConsole[F[_]]:
  def println[A: Show](a: A): F[Unit]

given simpleConsole[F[_]: Console]: SimpleConsole[F] with
  def println[A: Show](a: A): F[Unit] = Console[F].println(a)


def printLeft[F[_]: Applicative: SimpleConsole, E: Show, A]
    : Stream[F, Either[E, A]] => Stream[F, Either[E, A]] = _.evalTap {
  case Left(e)  => summon[SimpleConsole[F]].println(e)
  case Right(_) => Applicative[F].unit
}
