package patterns

import cats.effect.concurrent.Ref
import cats.effect.Sync
import cats.effect.IO
import cats.implicits._

trait Counter[F[_]] {
  def incr: F[Unit]
  def get: F[Int]
}

// Constructor is private because we don't want state to leak
class LiveCounter[F[_]] private ( 
  ref: Ref[F, Int]
) extends Counter[F] {
  def incr: F[Unit] = ref.update(_ + 1)
  def get: F[Int] = ref.get
}

// make returns the Counter wrapped in F as its creation is effectful
object LiveCounter {
  def make[F[_] : Sync]: F[Counter[F]] =
    Ref.of[F, Int](0).map(new LiveCounter(_))
}

// Other programs will interact with the counter via its interface
object program {
  def program(counter: Counter[IO]): IO[Unit] =
    counter.incr *> IO()
}
