package au.id.tmm.javatime4s.argonaut

import argonaut.Argonaut._
import argonaut._

private[argonaut] object DateTimeCodecUtils {
  def makeCodec[Intermediate : EncodeJson : DecodeJson, A](
    encode: A => Intermediate,
    decode: Intermediate => A,
  ): CodecJson[A] =
    CodecJson(
      a => encode(a).asJson,
      c =>
        c.as[Intermediate].flatMap { s =>
          try {
            DecodeResult.ok(decode(s))
          } catch {
            case e: Exception => DecodeResult.fail(e.getMessage, c.history)
          }
        },
    )
}
