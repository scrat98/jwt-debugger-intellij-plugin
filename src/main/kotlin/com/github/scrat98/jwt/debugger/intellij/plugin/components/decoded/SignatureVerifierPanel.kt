package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.MessageType
import com.intellij.openapi.ui.popup.Balloon
import com.intellij.openapi.ui.popup.JBPopupFactory
import com.intellij.ui.awt.RelativePoint
import java.awt.BorderLayout
import javax.swing.*

enum class AlgorithmType {
  HS256,
  HS384,
  HS512
}

data class SignatureProps(
    val secretKey: String,
    val algorithmType: AlgorithmType
)

class SignatureVerifierPanel : JPanel(BorderLayout()) {

  companion object {
    const val VERIFIER_CHECK_TOKEN_PROPERTY = "signatureVerifierCheckToken"

    const val VERIFIER_SIGN_WITH_TOKEN_PROPERTY = "signatureVerifierSignWithToken"

    const val VERIFIER_SET_ALGORITHM_PROPERTY = "signatureVerifierSetAlgorithm"
  }

  private var algorithmType = AlgorithmType.HS256

  private val algorithmText
    get() = """<html>
        $algorithmType(<br>
        &nbsp;&nbsp;&nbsp;&nbsp;base64UrlEncode(header) + "." +<br>
        &nbsp;&nbsp;&nbsp;&nbsp;base64UrlEncode(payload),<br>
        </html>"""

  private val algorithmLabel = JLabel(algorithmText).apply {
    foreground = JWTColors.signatureColor
  }

  private val secretField = JTextField("secret").apply {
    isOpaque = false
    foreground = JWTColors.signatureColor
  }

  private val endBracket = JLabel(")").apply {
    foreground = JWTColors.signatureColor
  }

  private var signatureBase64String: String = ""

  private val verifierButton = JButton("Verify with secret").apply {
    addActionListener { event ->
      firePropertyChange(
          VERIFIER_CHECK_TOKEN_PROPERTY,
          "",
          SignatureProps(
              secretField.text,
              algorithmType
          ))
    }
  }

  private val signWithSecretButton = JButton("Sign with this secret").apply {
    addActionListener { event ->
      firePropertyChange(
          VERIFIER_SIGN_WITH_TOKEN_PROPERTY,
          "",
          SignatureProps(
              secretField.text,
              algorithmType
          ))
    }
  }

  private val algorithmDropList = ComboBox(
      listOf(
          AlgorithmType.HS256,
          AlgorithmType.HS384,
          AlgorithmType.HS512).toTypedArray()).apply {
    addActionListener {
      algorithmType = this.selectedItem as AlgorithmType
      algorithmLabel.text = algorithmText
      firePropertyChange(
          VERIFIER_SET_ALGORITHM_PROPERTY, "", SignatureProps(
          secretField.text,
          algorithmType
      ))
    }
  }

  init {
    border = BorderFactory.createTitledBorder("Verify signature")

    val verifierInput = JPanel(BorderLayout())
    verifierInput.apply {
      add(algorithmLabel, BorderLayout.PAGE_START)
      add(secretField, BorderLayout.CENTER)
      add(endBracket, BorderLayout.PAGE_END)
    }
    val verifierActions = JPanel(BorderLayout())
    verifierActions.apply {
      add(algorithmDropList, BorderLayout.PAGE_START)
      add(verifierButton, BorderLayout.CENTER)
      add(signWithSecretButton, BorderLayout.PAGE_END)
    }

    add(verifierInput, BorderLayout.LINE_START)
    add(verifierActions, BorderLayout.LINE_END)
  }

  fun setSignature(signature: String) {
    signatureBase64String = signature
  }

  fun getEncodedSignature(): String = signatureBase64String

  fun disableVerifierButton() {
    verifierButton.isEnabled = false
  }

  fun enableVerifierButton() {
    verifierButton.isEnabled = true
  }

  fun disableSignWithSecretButton() {
    signWithSecretButton.isEnabled = false
  }

  fun enableSignWithSecretButton() {
    signWithSecretButton.isEnabled = true
  }

  fun showSignatureVerifiedPopup() {
    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder("Signature Verified", MessageType.INFO, null)
        .createBalloon()
        .show(
            RelativePoint.getNorthWestOf(secretField),
            Balloon.Position.atLeft)
  }

  fun showInvalidSignaturePopup() {
    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder("Invalid Signature", MessageType.ERROR, null)
        .createBalloon()
        .show(
            RelativePoint.getNorthWestOf(secretField),
            Balloon.Position.atLeft)
  }

  fun showSuccessfullySignedPopup() {
    JBPopupFactory.getInstance()
        .createHtmlTextBalloonBuilder("Successfully Signed", MessageType.INFO, null)
        .createBalloon()
        .show(
            RelativePoint.getNorthWestOf(secretField),
            Balloon.Position.atLeft)
  }

  fun setAlgorithmType(algorithmType: AlgorithmType) {
    algorithmDropList.selectedItem = algorithmType
  }
}