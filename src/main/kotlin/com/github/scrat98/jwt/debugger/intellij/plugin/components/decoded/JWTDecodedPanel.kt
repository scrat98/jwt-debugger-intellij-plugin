package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.EncodedJwt
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import java.util.*
import javax.swing.BorderFactory
import javax.swing.JPanel

class JWTDecodedPanel : JPanel(BorderLayout()) {

  companion object {
    const val DECODED_JWT_HEADER_PROPERTY = "decodedJwtHeader"

    const val DECODED_JWT_PAYLOAD_PROPERTY = "decodedJwtPayload"
  }

  private val headerDecodedPanel = HeaderDecodedPanel()

  private val payloadDecodedPanel = PayloadDecodedPanel()

  private val signatureVerifierPanel = SignatureVerifierPanel()

  init {
    border = BorderFactory.createTitledBorder("Decoded")

    val payloadAndSignaturePanels = JBSplitter(true, 0.5f).apply {
      firstComponent = payloadDecodedPanel
      secondComponent = signatureVerifierPanel
    }

    add(JBSplitter(true, 0.33f).apply {
      firstComponent = headerDecodedPanel
      secondComponent = payloadAndSignaturePanels
    })
  }

  fun setAndValidateJwt(jwt: EncodedJwt): Boolean {
    var headerJson = "{ }"
    var payloadJson = "{ }"
    var isValid = true

    try {
      headerJson = tryConvertToJson(jwt.header)
    } catch (e: Exception) {
      isValid = false
    }

    try {
      payloadJson = tryConvertToJson(jwt.payload)
    } catch (e: Exception) {
      isValid = false
    }

    headerDecodedPanel.setHeaderJson(headerJson)
    payloadDecodedPanel.setPayloadJson(payloadJson)

    return isValid
  }

  @Throws(JsonProcessingException::class)
  fun tryConvertToJson(byte64String: String): String {
    val mapper = ObjectMapper()
    val jsonNode = mapper.readTree(String(Base64.getDecoder().decode(byte64String))) ?: return "{ }"
    return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode)
  }
}