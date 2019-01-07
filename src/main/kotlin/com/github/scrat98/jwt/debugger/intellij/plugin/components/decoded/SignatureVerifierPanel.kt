package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import java.awt.BorderLayout
import javax.swing.*

enum class AlgorithmType {
  HMACSHA256,
  HMACSHA384,
  HMACSHA512
}

class SignatureVerifierPanel : JPanel(BorderLayout()) {
  private var algorithmType = AlgorithmType.HMACSHA256

  private val algorithmText = JLabel(
      """<html>
        $algorithmType(<br>
        &nbsp;&nbsp;&nbsp;&nbsp;base64UrlEncode(header) + .<br>
        &nbsp;&nbsp;&nbsp;&nbsp;base64UrlEncode(payload),<br>
        </html>""").apply {
    foreground = JWTColors.signatureColor
  }

  private val secretField = JTextField("secret-key").also {
    border = BorderFactory.createEmptyBorder(40, 40, 40, 40)
    foreground = JWTColors.signatureColor
  }

  private val endBracket = JLabel(")").apply {
    foreground = JWTColors.signatureColor
  }

  private var signatureBase64String: String = ""

  init {
    border = BorderFactory.createTitledBorder("Verify signature")

    val verifierInput = JPanel(BorderLayout())
    verifierInput.apply {
      add(algorithmText, BorderLayout.PAGE_START)
      add(secretField, BorderLayout.CENTER)
      add(endBracket, BorderLayout.PAGE_END)
    }
    val verifierActions = JPanel(BorderLayout())
    verifierActions.apply {
      add(JButton("Choose algo"), BorderLayout.PAGE_START)
      add(JButton("Verify with secret"), BorderLayout.CENTER)
      add(JButton("Sign with this secret"), BorderLayout.PAGE_END)
    }

    add(verifierInput, BorderLayout.LINE_START)
    add(verifierActions, BorderLayout.LINE_END)
  }

  fun setSignature(signature: String) {
    signatureBase64String = signature
  }

  fun getEncodedSignature(): String = signatureBase64String

  fun setAlgorithmType(algorithmType: AlgorithmType) {
    this.algorithmType = algorithmType
  }
}