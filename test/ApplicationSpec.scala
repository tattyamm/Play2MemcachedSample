package test

import org.specs2.mutable._
import play.api._
import cache.Cache
import play.api.mvc._
import play.api.Play.current

import play.api.test._
import play.api.test.Helpers._


class ApplicationSpec extends Specification {


  "memcached" should {
    "値を保存してそれを取得できる" in {
      running(TestServer(9000)) {
        val key = "key!"
        val value = "value!"

        //set
        try {
          Cache.set(key, value)
        } catch {
          case _ => println("Cacheをsetするとこでエラー")
        }

        //get
        val valueFromCache: String = Cache.getOrElse[String](key) {
          "keyがなかった"
        }

        value must_== (valueFromCache)
      }
    }

    "存在しない値は取り出せない" in {
      running(TestServer(9000)) {
        val novalue = "vale is not found"
        //get
        val valueFromCache2: String = Cache.getOrElse[String]("nokey") {
          novalue
        }

        novalue must_== (valueFromCache2)
      }
    }

    "expireが指定通り動く" in {
      running(TestServer(9000)) {
        val key = "key!"
        val value = "value!"
        val novalue = "value is not found"

        //set
        try {
          Cache.set(key, value, 1)
        } catch {
          case _ => println("Cacheをsetするとこでエラー")
        }

        //get
        val valueFromCache1: String = Cache.getOrElse[String](key) {
          novalue
        }

        valueFromCache1 must_== (value) //この時はまだ値がある

        //待機
        Thread.sleep(1100)

        //get
        val valueFromCache2: String = Cache.getOrElse[String](key) {
          novalue
        }

        valueFromCache2 must_== (novalue) //時間経過で値がなくなる
      }
    }

  }
}