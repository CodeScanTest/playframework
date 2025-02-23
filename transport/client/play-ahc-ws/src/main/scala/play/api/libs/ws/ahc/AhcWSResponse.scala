/*
 * Copyright (C) from 2022 The Play Framework Contributors <https://github.com/playframework>, 2011-2021 Lightbend Inc. <https://www.lightbend.com>
 */

package play.api.libs.ws.ahc

import java.net.URI

import scala.xml.Elem

import org.apache.pekko.stream.scaladsl.Source
import org.apache.pekko.util.ByteString
import play.api.libs.json.JsValue
import play.api.libs.ws._
import play.shaded.ahc.org.asynchttpclient.{ Response => AHCResponse }

/**
 * A WS HTTP Response backed by an AsyncHttpClient response.
 *
 * @param underlying the underlying WS response
 */
case class AhcWSResponse(underlying: StandaloneWSResponse) extends WSResponse with WSBodyReadables {
  def this(ahcResponse: AHCResponse) = {
    this(StandaloneAhcWSResponse(ahcResponse))
  }

  /**
   * Return the current headers of the request being constructed
   */
  override def headers: Map[String, scala.collection.Seq[String]] = underlying.headers

  /**
   * Get the underlying response object, i.e. play.shaded.ahc.org.asynchttpclient.Response
   *
   * {{{
   * val ahcResponse = response.underlying[play.shaded.ahc.org.asynchttpclient.Response]
   * }}}
   */
  override def underlying[T]: T = underlying.underlying[T]

  /**
   * The response status code.
   */
  override def status: Int = underlying.status

  /**
   * The response status message.
   */
  override def statusText: String = underlying.statusText

  /**
   * Get a response header.
   */
  override def header(key: String): Option[String] = underlying.header(key)

  /**
   * Get all the cookies.
   */
  override def cookies: scala.collection.Seq[WSCookie] = underlying.cookies

  /**
   * Get only one cookie, using the cookie name.
   */
  override def cookie(name: String): Option[WSCookie] = underlying.cookie(name)

  override def body: String = underlying.body

  override def uri: URI = underlying.uri

  /**
   * The response body as a byte string.
   */
  override def bodyAsBytes: ByteString = underlying.bodyAsBytes

  override def bodyAsSource: Source[ByteString, _] = underlying.bodyAsSource

  /**
   * Return the current headers of the request being constructed
   */
  @deprecated("Please use request.headers", since = "2.6.0")
  override def allHeaders: Map[String, scala.collection.Seq[String]] = underlying.headers

  /**
   * The response body as Xml.
   */
  @deprecated("Use response.body[Elem]", since = "2.6.0")
  override def xml: Elem = underlying.body[Elem]

  /**
   * The response body as Json.
   */
  @deprecated("Use response.body[JsValue]", since = "2.6.0")
  override def json: JsValue = underlying.body[JsValue]
}
