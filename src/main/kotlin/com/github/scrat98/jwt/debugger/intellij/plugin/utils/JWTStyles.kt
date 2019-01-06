package com.github.scrat98.jwt.debugger.intellij.plugin.utils

import com.intellij.ui.JBColor
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class JWTHighlighters {
  companion object {
    private fun createDefaultHighlighter() =
        SimpleAttributeSet().also {
          StyleConstants.setForeground(it, JWTColors.defaultColor)
        }

    val defaultHighlighter = createDefaultHighlighter()

    val headerHighlighter =
        createDefaultHighlighter().also { StyleConstants.setForeground(it, JWTColors.headerColor) }

    val payloadHighlighter =
        createDefaultHighlighter().also { StyleConstants.setForeground(it, JWTColors.payloadColor) }

    val signatureHighlighter =
        createDefaultHighlighter().also { StyleConstants.setForeground(it, JWTColors.signatureColor) }

    val dotHighlighter =
        createDefaultHighlighter().also { StyleConstants.setForeground(it, JWTColors.defaultColor) }
  }
}

class JWTColors {
  companion object {
    val headerColor = JBColor.RED

    val payloadColor = JBColor.MAGENTA

    val signatureColor = JBColor.CYAN

    val defaultColor = JBColor.BLACK
  }
}