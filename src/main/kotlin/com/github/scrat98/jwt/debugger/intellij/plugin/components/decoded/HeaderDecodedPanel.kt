package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane

class HeaderDecodedPanel : JPanel(BorderLayout()) {
  private val headerTextArea = JTextPane().apply {
    foreground = JWTColors.headerColor
  }

  init {
    border = BorderFactory.createTitledBorder("Header")
    add(JBScrollPane(headerTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }
}