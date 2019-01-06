package com.github.scrat98.jwt.debugger.intellij.plugin.components

import com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded.JWTDecodedPanel
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.JWTEncodedPanel
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import javax.swing.JPanel

class JWTContent : JPanel(BorderLayout()) {
  private val jwtEncodedPanel = JWTEncodedPanel()

  private val jwtDecodedPanel = JWTDecodedPanel()

  init {
    add(JBSplitter(true, 0.2f).apply {
      firstComponent = jwtEncodedPanel
      secondComponent = jwtDecodedPanel
    }, BorderLayout.CENTER)
  }
}