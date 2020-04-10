package patterns

import cats.data.State

object SequentialVsConcurrentState {
  val nextInt: State[Int, Int] =
    State(s => (s + 1, s * 2))

  def seq = for {
    n1 <- nextInt
    n2 <- nextInt
    n3 <- nextInt
  } yield n1 + n2 + n3

}