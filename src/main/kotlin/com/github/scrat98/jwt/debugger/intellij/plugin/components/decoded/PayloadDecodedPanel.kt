package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane

class PayloadDecodedPanel : JPanel(BorderLayout()) {
  private val payloadTextArea = JTextPane().apply {
    foreground = JWTColors.payloadColor
  }

  init {
    border = BorderFactory.createTitledBorder("Payload")
    add(JBScrollPane(payloadTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }

  fun setPayloadJson(payloadJson: String) {
    payloadTextArea.text = payloadJson
  }
}