package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.EncodedJwt
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.createEmptyEncodedJwt
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JsonConverter
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel

class JWTDecodedPanel : JPanel(BorderLayout()) {

  companion object {
    const val DECODED_JWT_PROPERTY = "decodedJwt"
  }

  private val headerDecodedPanel = HeaderDecodedPanel()

  private val payloadDecodedPanel = PayloadDecodedPanel()

  private val signatureVerifierPanel = SignatureVerifierPanel()

  private var encodedJwt = createEmptyEncodedJwt()

  init {
    border = BorderFactory.createTitledBorder("Decoded")

    add(JBSplitter(true, 0.5f).apply {
      firstComponent = headerDecodedPanel
      secondComponent = payloadDecodedPanel
    }, BorderLayout.CENTER)
    add(signatureVerifierPanel, BorderLayout.SOUTH)

    addPropertyListenerOnHeaderDecodedPanel()
    addPropertyListenerOnPayloadDecodedPanel()
  }

  fun setAndValidateEncodedJwt(jwt: EncodedJwt): Boolean {
    var headerJson = "{}"
    var payloadJson = "{}"
    var isValid = true

    try {
      headerJson = JsonConverter.tryConvertToJsonFromBase64String(jwt.header)
    } catch (e: Exception) {
      isValid = false
    }

    try {
      payloadJson = JsonConverter.tryConvertToJsonFromBase64String(jwt.payload)
    } catch (e: Exception) {
      isValid = false
    }

    headerDecodedPanel.setHeaderJson(headerJson)
    payloadDecodedPanel.setPayloadJson(payloadJson)
    signatureVerifierPanel.setSignature(jwt.signature)

    return isValid
  }

  private fun addPropertyListenerOnHeaderDecodedPanel() {
    headerDecodedPanel.addPropertyChangeListener(
        HeaderDecodedPanel.DECODED_JWT_HEADER_PROPERTY
    ) { event ->
      val headerText = event.newValue as String
      if (JsonConverter.jsonIsValidFromString(headerText)) {
        headerDecodedPanel.disableInvalidHeaderForm()
      } else {
        headerDecodedPanel.enableInvalidHeaderForm()
      }
      updateJwt()
    }
  }

  private fun addPropertyListenerOnPayloadDecodedPanel() {
    payloadDecodedPanel.addPropertyChangeListener(
        PayloadDecodedPanel.DECODED_JWT_PAYLOAD_PROPERTY
    ) { event ->
      val payloadText = event.newValue as String
      if (JsonConverter.jsonIsValidFromString(payloadText)) {
        payloadDecodedPanel.disableInvalidHeaderForm()
      } else {
        payloadDecodedPanel.enableInvalidHeaderForm()
      }
      updateJwt()
    }
  }

  private fun updateJwt() {
    try {
      val encodedHeader = headerDecodedPanel.getEncodedHeader()
      val encodedPayload = payloadDecodedPanel.getEncodedPayload()
      val encodedSignature = signatureVerifierPanel.getEncodedSignature()
      val encodedJwtToken = "$encodedHeader.$encodedPayload.$encodedSignature"
      val newEncodedJwt = EncodedJwt(
          encodedJwtToken,
          encodedHeader,
          encodedPayload,
          encodedSignature)
      val oldEncodedJwt = encodedJwt
      encodedJwt = newEncodedJwt
      firePropertyChange(DECODED_JWT_PROPERTY, oldEncodedJwt, newEncodedJwt)
    } catch (e: Exception) {
      val newEncodedJwt = createEmptyEncodedJwt()
      val oldEncodedJwt = encodedJwt
      encodedJwt = newEncodedJwt
      firePropertyChange(DECODED_JWT_PROPERTY, oldEncodedJwt, newEncodedJwt)
    }
  }
}