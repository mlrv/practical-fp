package patterns

import cats.syntax._
import cats.implicits._
import io.estatico.newtype.macros._
import eu.timepit.refined._
import eu.timepit.refined.auto._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.collection.Contains
import eu.timepit.refined.types.string.NonEmptyString

object StronglyTypedFunctions {
  type User = Unit

  def lookup1(username: String, email: String): Option[User] = ???

  // Easy to pass invalid data...
  lookup1("", "")
  lookup1("foo@bar.com", "username")
  lookup1("meh", "123")

  // Value classes...
  case class Username1(val value: String) extends AnyVal
  case class Email1(val value: String) extends AnyVal

  // This is a bit better... we can no longer confuse the order of parameters at least
  def lookup2(username: Username1, email: Email1): Option[User] = ???

  lookup2(Username1("marco"), Email1("foo@bar.com"))

  // But we can still provide invalid data...
  lookup2(Username1(""), Email1("ciao"))

  // One way to fix it is to make the actual constructors private and provide a smart constructor
  case class Username2 private(val value: String) extends AnyVal
  case class Email2 private(val value: String) extends AnyVal

  def lookup3(username: Username2, email: Email2): Option[User] = ???

  def makeUsername2(value: String): Option[Username2] =
    if (value.nonEmpty) Username2(value).some
    else none

  def makeEmail2(value: String): Option[Email2] =
    if (value.contains("@")) Email2(value).some
    else none

  (
    makeUsername2("foo"),
    makeEmail2("foo@bar.com")
  ).mapN {
    case (username, email) => lookup3(username, email)
  }

  // One problem that we still have is that we are still working with case classes,
  // which means that the .copy() method is still there to do some damage...

 (
    makeUsername2("foo"),
    makeEmail2("foo@bar.com")
  ).mapN {
    case (username, email) => lookup3(username, email.copy("not an email"))
  } 

  // To get around this, we can use sealed abstract classes combined with smart constructors
  sealed abstract class Username3 (value: String)
  sealed abstract class Email3 (value: String)

  // Value classes have some limitations, including potential performance issues. The
  // compiler actually has to instantiate the classes at runtime in a few cases.
  
  // Let's look at a different approach for this problem, newtypes
  @newtype case class Username4(value: String)
  @newtype case class Email4(value: String)

  // This eliminates the extra allocation issues and removes the .copy() method,
  // but we can still use those classes incorrectly, which means that smart
  // constructors are still needed

  Email4("not an email")

  // We can eliminate the need for smart constructors by using refinement types
  def lookup4(username: NonEmptyString): Option[User] = ???

  type Email5 = String Refined Contains["@"]

  // Often, a simple refinement rule applies to many types, so we can use newtypes
  // combined with refinement types
  @newtype case class Brand(value: NonEmptyString)
  @newtype case class Category(value: NonEmptyString)
}
