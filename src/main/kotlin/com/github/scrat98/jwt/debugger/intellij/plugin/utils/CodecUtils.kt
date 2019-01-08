package com.github.scrat98.jwt.debugger.intellij.plugin.utils

import com.auth0.jwt.algorithms.Algorithm
import com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded.AlgorithmType
import com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded.SignatureProps
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.StringUtils
import java.nio.charset.StandardCharsets

class CodecUtils {
  companion object {
    fun decodeFromBase64(base64String: String): String =
        StringUtils.newStringUtf8(Base64.decodeBase64(base64String))

    fun decodeFromBase64(bytes: ByteArray): String =
        StringUtils.newStringUtf8(Base64.decodeBase64(bytes))

    fun encodeToBase64(string: String) =
        Base64.encodeBase64URLSafeString(string.toByteArray(StandardCharsets.UTF_8))

    fun encodeToBase64(bytes: ByteArray) =
        Base64.encodeBase64URLSafeString(bytes)

    fun getSignatureBase64String(
        header: String,
        payload: String,
        signatureProps: SignatureProps
    ): String {
      val signatureBytes = getSignatureBytes(header, payload, signatureProps)
      return CodecUtils.encodeToBase64(signatureBytes)
    }

    fun getSignatureBytes(
        header: String,
        payload: String,
        signatureProps: SignatureProps
    ): ByteArray {
      val contentBytes = "$header.$payload".toByteArray(StandardCharsets.UTF_8)
      return when (signatureProps.algorithmType) {
        AlgorithmType.HS256 -> Algorithm.HMAC256(signatureProps.secretKey).sign(contentBytes)
        AlgorithmType.HS384 -> Algorithm.HMAC384(signatureProps.secretKey).sign(contentBytes)
        AlgorithmType.HS512 -> Algorithm.HMAC512(signatureProps.secretKey).sign(contentBytes)
      }
    }
  }
}