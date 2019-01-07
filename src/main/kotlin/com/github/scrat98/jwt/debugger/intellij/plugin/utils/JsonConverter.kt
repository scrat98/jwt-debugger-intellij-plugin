package com.github.scrat98.jwt.debugger.intellij.plugin.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import java.util.*

class JsonConverter {
  companion object {
    @Throws(Exception::class)
    fun tryConvertToJsonFromBase64String(base64String: String): String =
        tryConvertToJsonFromString(String(Base64.getDecoder().decode(base64String)))

    @Throws(Exception::class)
    fun tryConvertToJsonFromString(jsonString: String): String {
      val parser = Parser.default()
      val stringBuilder = StringBuilder(jsonString)
      val json = parser.parse(stringBuilder) as JsonObject
      return json.toJsonString(true)
    }

    @Throws(Exception::class)
    fun tryConvertToJsonFromBytes(bytes: ByteArray): String =
        tryConvertToJsonFromString(String(bytes))

    fun jsonIsValidFromBase64String(base64String: String): Boolean =
        jsonIsValidFromString(String(Base64.getDecoder().decode(base64String)))

    fun jsonIsValidFromString(jsonString: String): Boolean {
      return try {
        tryConvertToJsonFromString(jsonString)
        true
      } catch (e: Exception) {
        false
      }
    }

    fun jsonIsValidFromBytes(bytes: ByteArray): Boolean =
        jsonIsValidFromString(String(bytes))
  }
}