package sttp.client3

import sttp.model.{Header, HeaderNames, StatusCode}

import scala.collection.immutable.Seq

/**
  * @param history If redirects are followed, and there were redirects,
  *                contains responses for the intermediate requests.
  *                The first response (oldest) comes first.
  */
case class Response[T](
    body: T,
    code: StatusCode,
    statusText: String,
    headers: Seq[Header],
    history: List[Response[Unit]],
    request: RequestMetadata
) extends ResponseMetadata {
  def show(includeBody: Boolean = true, sensitiveHeaders: Set[String] = HeaderNames.SensitiveHeaders): String = {
    val headers = headersToStringSafe(sensitiveHeaders).mkString(", ")
    val body = if (includeBody) s", body: ${this.body}" else ""
    s"$code $statusText, headers: $headers$body"
  }

  override def toString: String = s"Response($body,$code,$statusText,${headersToStringSafe()},$history,$request)"
}

object Response {

  /**
    * Convenience method to create a Response instance, mainly useful in tests using
    * [[sttp.client3.testing.SttpBackendStub]] and partial matchers.
    */
  def apply[T](body: T, code: StatusCode): Response[T] =
    Response(body, code, "", Nil, Nil, RequestMetadata.ExampleGet)

  /**
    * Convenience method to create a Response instance, mainly useful in tests using
    * [[sttp.client3.testing.SttpBackendStub]] and partial matchers.
    */
  def apply[T](body: T, code: StatusCode, statusText: String): Response[T] =
    Response(body, code, statusText, Nil, Nil, RequestMetadata.ExampleGet)

  /**
    * Convenience method to create a Response instance, mainly useful in tests using
    * [[sttp.client3.testing.SttpBackendStub]] and partial matchers.
    */
  def apply[T](body: T, code: StatusCode, statusText: String, headers: Seq[Header]): Response[T] =
    Response(body, code, statusText, headers, Nil, RequestMetadata.ExampleGet)

  /**
    * Convenience method to create a Response instance, mainly useful in tests using
    * [[sttp.client3.testing.SttpBackendStub]] and partial matchers.
    */
  def ok[T](body: T): Response[T] = apply(body, StatusCode.Ok, "OK")
}
