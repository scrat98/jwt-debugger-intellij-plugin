package com.github.scrat98.jwt.debugger.intellij.plugin.utils

import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser

class JsonConverter {
  companion object {
    @Throws(Exception::class)
    fun tryConvertToJsonFromBase64String(base64String: String): JsonObject =
        tryConvertToJsonFromString(CodecUtils.decodeFromBase64(base64String))

    @Throws(Exception::class)
    fun tryConvertToJsonFromString(jsonString: String): JsonObject {
      val parser = Parser.default()
      val stringBuilder = StringBuilder(jsonString)
      return parser.parse(stringBuilder) as JsonObject
    }

    fun jsonIsValidFromBase64String(base64String: String): Boolean =
        jsonIsValidFromString(CodecUtils.decodeFromBase64(base64String))

    fun jsonIsValidFromString(jsonString: String): Boolean {
      return try {
        tryConvertToJsonFromString(jsonString)
        true
      } catch (e: Exception) {
        false
      }
    }
  }
}