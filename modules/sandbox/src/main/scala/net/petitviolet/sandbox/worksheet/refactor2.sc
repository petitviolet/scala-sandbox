import Main.Validation

object Member {

  val sampleMember =
    new Member("001", "toyoshima@fringe81.com", "dummy-password", 1)

}

object MemberRepository {
  def findById(id: String): Option[Member] = Some(Member.sampleMember)
}


object MemberSpecification {

  def satisfiedWhenActivating(password: String)(member: Member)(implicit ctx: UniposContext): Validation[Member] =
  // 本来はmemberとctx.loginMemberが同じ人かの確認、memberのstatusが招待状態（１）かどうかの確認、パスワード値が妥当かどうかのチェックを行う
    Right(member)

  def satisfiedWhenChangingPwd(currentPwd: String, newPwd: String)(member: Member)(implicit ctx: UniposContext): Validation[Member] =
  // 本来はmemberとctx.loginMemberが同じ人かの確認、memberのstatusがActive（２）かどうかの確認、memberのpasswordとcurrentPwdが一致しているか、currentPwdとnewPwdが異なっているかのチェックを行う
    Right(member)

  def satisfiedWhenResetPwd(newPwd: String)(member: Member)(implicit ctx: UniposContext): Validation[Member] =
  // 本来はmemberとctx.loginMemberが同じ人かの確認、memberのstatusがActive（２）かどうかの確認、パスワード値が妥当かどうかのチェックを行う
    Right(member)

}

case class UniposContext(loginMember: Member)

object Main extends App {

  type ErrorMessages = List[String]
  type Validation[T] = Either[ErrorMessages, T]

  val memberId = "001"
  val password = "fringe"
  val loginMember = Member.sampleMember

  implicit val ctx = UniposContext(loginMember)

  /* activate前 */

  val memberO = MemberRepository.findById(memberId)
  memberO.foreach(println)

  // activateする
  val memberOV: Option[Validation[Member]] =
    memberO.map(_.activate(password))

  /* activate後（パスワード及び状態が変わる） */

  memberOV.foreach { memberV =>
    memberV.foreach(println)
  }

}

class Member private(val id: String, val mailAddress: String, val password: String, val status: Int) {

  private def copy(_mailAddress: String = mailAddress, _password: String = password, _status: Int = status): Member =
    new Member(id, _mailAddress, _password, _status)

  override def toString(): String = s"Member(id = $id, mailAddress = $mailAddress, password = ${password.take(1) }*******, status = $status)"

  // specを通過すれば、パスワードを更新する
  private def specAndUpdatePassword(newPass: String)(spec: Member => Validation[Member]): Validation[Member] = {
    spec(this).map { _.copy(_password = newPass) }
  }

  /**
   * Invite(1)状態からActive(2)状態にする。その際、招待メールに添付されたリンクから開いた画面で入力されたパスワードを設定する。
   */
  def activate(password: String)(implicit ctx: UniposContext): Validation[Member] =
    specAndUpdatePassword(password)(MemberSpecification.satisfiedWhenActivating(password)).map {
      _.copy(_status = 2)
    }

  /**
   * プロフィール画面からパスワードを変更する。その際、現在のパスワードと新しく設定したいパスワードを入力してもらい問題なければ新しいパスワードに変更する
   */
  def changePassword(currentPwd: String, newPwd: String)(implicit ctx: UniposContext): Validation[Member] = {
    specAndUpdatePassword(newPwd)(MemberSpecification.satisfiedWhenChangingPwd(currentPwd, newPwd))
  }

  /**
   * パスワードを忘れた方はこちら、のリンクからメール送信後、新たなパスワードで変更する
   */
  def resetPassword(newPwd: String)(implicit ctx: UniposContext): Validation[Member] = {
    specAndUpdatePassword(newPwd)(MemberSpecification.satisfiedWhenResetPwd(newPwd))
  }




















}


Main.main(Array[String]())
