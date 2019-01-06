package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane

class SignatureVerifierPanel : JPanel(BorderLayout()) {
  private val signatureTextArea = JTextPane().apply {
    foreground = JWTColors.signatureColor
  }

  init {
    border = BorderFactory.createTitledBorder("Verify signature")
    add(JBScrollPane(signatureTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }
}