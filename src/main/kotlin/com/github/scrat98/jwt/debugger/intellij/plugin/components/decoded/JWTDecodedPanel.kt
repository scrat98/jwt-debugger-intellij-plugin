package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.EncodedJwt
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.createEmptyEncodedJwt
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.CodecUtils
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
    addPropertyListenerOnSignatureVerifierPanel()
  }

  fun setAndValidateEncodedJwt(jwt: EncodedJwt): Boolean {
    var headerJson = "{}"
    var payloadJson = "{}"
    var isValid = true

    try {
      headerJson = JsonConverter.tryConvertToJsonFromBase64String(jwt.header).toJsonString(true)
    } catch (e: Exception) {
      isValid = false
    }

    try {
      payloadJson = JsonConverter.tryConvertToJsonFromBase64String(jwt.payload).toJsonString(true)
    } catch (e: Exception) {
      isValid = false
    }

    headerDecodedPanel.apply {
      setHeaderJson(headerJson)
      disableInvalidHeaderForm()
    }
    payloadDecodedPanel.apply {
      setPayloadJson(payloadJson)
      disableInvalidHeaderForm()
    }
    signatureVerifierPanel.setSignature(jwt.signature)
    encodedJwt = jwt

    if (isValid) {
      signatureVerifierPanel.enableSignWithSecretButton()
      signatureVerifierPanel.enableVerifierButton()
    } else {
      signatureVerifierPanel.disableSignWithSecretButton()
      signatureVerifierPanel.disableVerifierButton()
    }

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

  private fun addPropertyListenerOnSignatureVerifierPanel() {
    signatureVerifierPanel.addPropertyChangeListener(
        SignatureVerifierPanel.VERIFIER_CHECK_TOKEN_PROPERTY
    ) { event ->
      val signatureProps = event.newValue as SignatureProps
      val signatureBase64 =
          CodecUtils.getSignatureBase64String(encodedJwt.header, encodedJwt.payload, signatureProps)
      if (signatureBase64 == encodedJwt.signature) {
        signatureVerifierPanel.showSignatureVerifiedPopup()
      } else {
        signatureVerifierPanel.showInvalidSignaturePopup()
      }
    }

    signatureVerifierPanel.addPropertyChangeListener(
        SignatureVerifierPanel.VERIFIER_SIGN_WITH_TOKEN_PROPERTY
    ) { event ->
      updateJwt()
      val signatureProps = event.newValue as SignatureProps
      val signatureBase64 =
          CodecUtils.getSignatureBase64String(encodedJwt.header, encodedJwt.payload, signatureProps)
      signatureVerifierPanel.setSignature(signatureBase64)
      updateJwt()
      signatureVerifierPanel.showSuccessfullySignedPopup()
    }

    signatureVerifierPanel.addPropertyChangeListener(
        SignatureVerifierPanel.VERIFIER_SET_ALGORITHM_PROPERTY
    ) { event ->
      val signatureProps = event.newValue as SignatureProps
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
      signatureVerifierPanel.enableSignWithSecretButton()
      signatureVerifierPanel.enableVerifierButton()
      firePropertyChange(DECODED_JWT_PROPERTY, oldEncodedJwt, newEncodedJwt)
    } catch (e: Exception) {
      val newEncodedJwt = createEmptyEncodedJwt()
      val oldEncodedJwt = encodedJwt
      encodedJwt = newEncodedJwt
      signatureVerifierPanel.disableSignWithSecretButton()
      signatureVerifierPanel.disableVerifierButton()
      firePropertyChange(DECODED_JWT_PROPERTY, oldEncodedJwt, newEncodedJwt)
    }
  }
}