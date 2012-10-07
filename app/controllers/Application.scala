package controllers

import play.api._
import cache.Cache
import play.api.mvc._
import play.api.Play.current

object Application extends Controller {

  def index = Action {

    // set
    try {
      Cache.set("item.key", "item.value")
    } catch {
      case _ => println("Cacheをsetするとこでエラーのはずだが、memcachedを起動していなくてもここに来ない")
    }

    // setにキャッシュの有効期限を秒で指定できる。
    try {
      Cache.set("item.key2", "item.value2", 30)
    } catch {
      case _ => println("Cacheをsetするとこでエラーのはずだが、memcachedを起動していなくてもここに来ない")
    }

    // get 方法1
    //   方法1と2のどちらのように値を決めても良い。
    val valueFromCache1: Option[String] = Cache.getAs[String]("item.key")
    println(valueFromCache1.getOrElse("item.keyがなかった"))

    // get 方法2
    val valueFromCache2: String = Cache.getOrElse[String]("item.key") {
      "item.keyがなかった"
    }
    println(valueFromCache2)

    // get キーが存在しない場合の挙動
    val valueFromCache3: String = Cache.getOrElse[String]("hogehogeohoge") {
      "item.keyがなかった(正常)"
    }
    println(valueFromCache3)

    Ok(views.html.index("Your new application is ready."))
  }

}