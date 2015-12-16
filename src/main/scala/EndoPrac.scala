import scalaz._, Scalaz._

object EndoPrac extends App {
  val add: Int => Int => Int = i => n => {
    println(s"n -> $n")
    n + i
  }
  val mul: Int => Int => Int = i => n => {
    println(s"n -> $n")
    n * i
  }

  println((add |+| mul).apply(5)(10))

  case class UserInfo(id: Long, name: String, address: String, isMale: Boolean)

  object UserInfo {
    def default(): UserInfo = UserInfo(1L, "", "", true)
  }

  case class UserSetting(run: UserInfo => UserInfo) {
    def ~>(other: UserSetting) = {
      UserSetting(other.run compose run)
    }
    def apply = run(UserInfo.default())
  }

  def name(name: String): UserSetting = UserSetting(_.copy(name = name))

  def address(address: String): UserSetting = UserSetting(_.copy(address = address))

  def male(): UserSetting = UserSetting(_.copy(isMale = true))

  def female(): UserSetting = UserSetting(_.copy(isMale = false))

  //  type UserSetting = Endo[UserInfo]
  def tokyoMale(newName: String): UserSetting = {
    name(newName) ~> address("tokyo") ~> male
  }

  println(tokyoMale("hoge").apply)
}
