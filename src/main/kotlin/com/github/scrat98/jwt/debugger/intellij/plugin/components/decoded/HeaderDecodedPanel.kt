package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JsonConverter
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import java.util.*
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.border.LineBorder
import javax.swing.event.DocumentEvent
import javax.swing.text.Document

class HeaderDecodedPanel : JPanel(BorderLayout()) {
  companion object {
    const val DECODED_JWT_HEADER_PROPERTY = "decodedJwtHeader"
  }

  private val documentListener = object : DocumentAdapter() {
    override fun textChanged(e: DocumentEvent) {
      updateState(e.document)
    }
  }

  private val headerTextArea = JTextPane().apply {
    foreground = JWTColors.headerColor
    document.addDocumentListener(documentListener)
  }

  private var headerTextValue = headerTextArea.text

  init {
    border = BorderFactory.createTitledBorder("Header")
    add(JBScrollPane(headerTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }

  fun setHeaderJson(headerJson: String) {
    headerTextArea.apply {
      document.removeDocumentListener(documentListener)
      text = headerJson
      headerTextValue = headerJson
      document.addDocumentListener(documentListener)
    }
  }

  private fun updateState(document: Document) {
    val newHeaderText = document.getText(0, document.length)
    val oldHeaderText = headerTextValue
    headerTextValue = newHeaderText
    firePropertyChange(DECODED_JWT_HEADER_PROPERTY, oldHeaderText, newHeaderText)
  }

  fun enableInvalidHeaderForm() {
    border = BorderFactory.createTitledBorder(LineBorder(JBColor.RED), "Header")
  }

  fun disableInvalidHeaderForm() {
    border = BorderFactory.createTitledBorder(LineBorder(JBColor.BLACK), "Header")
  }

  fun getEncodedHeader(): String {
    val jsonString = JsonConverter.tryConvertToJsonFromString(headerTextValue)
    return Base64.getEncoder().encodeToString(jsonString.toByteArray())
  }
}