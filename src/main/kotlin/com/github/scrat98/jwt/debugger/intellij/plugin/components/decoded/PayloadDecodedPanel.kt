package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.CodecUtils
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTColors
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JsonConverter
import com.intellij.ui.DocumentAdapter
import com.intellij.ui.JBColor
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.border.LineBorder
import javax.swing.event.DocumentEvent
import javax.swing.text.Document

class PayloadDecodedPanel : JPanel(BorderLayout()) {
  companion object {
    const val DECODED_JWT_PAYLOAD_PROPERTY = "decodedJwtPayload"
  }

  private val documentListener = object : DocumentAdapter() {
    override fun textChanged(e: DocumentEvent) {
      updateState(e.document)
    }
  }

  private val payloadTextArea = JTextPane().apply {
    foreground = JWTColors.payloadColor
    document.addDocumentListener(documentListener)
  }

  private var payloadTextValue = payloadTextArea.text

  init {
    border = BorderFactory.createTitledBorder("Payload")
    add(JBScrollPane(payloadTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }

  fun setPayloadJson(payloadJson: String) {
    payloadTextArea.apply {
      document.removeDocumentListener(documentListener)
      text = payloadJson
      payloadTextValue = payloadJson
      document.addDocumentListener(documentListener)
    }
  }

  fun enableInvalidHeaderForm() {
    border = BorderFactory.createTitledBorder(LineBorder(JBColor.RED), "Payload")
  }

  fun disableInvalidHeaderForm() {
    border = BorderFactory.createTitledBorder(LineBorder(JBColor.BLACK), "Payload")
  }

  private fun updateState(document: Document) {
    val newPayloadText = document.getText(0, document.length)
    val oldPayloadText = payloadTextValue
    payloadTextValue = newPayloadText
    firePropertyChange(DECODED_JWT_PAYLOAD_PROPERTY, oldPayloadText, newPayloadText)
  }

  fun getEncodedPayload(): String {
    val jsonString = JsonConverter.tryConvertToJsonFromString(payloadTextValue).toJsonString()
    return CodecUtils.encodeToBase64(jsonString)
  }
}