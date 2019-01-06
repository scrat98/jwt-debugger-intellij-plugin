package com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded

import com.github.scrat98.jwt.debugger.intellij.plugin.utils.JWTHighlighters
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.SwingUtilities
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.StyledDocument

data class EncodedJwt(
    val jwtToken: String,
    val header: String,
    val payload: String,
    val signature: String
)

fun createEmptyJwt() = EncodedJwt(
    "",
    "",
    "",
    "")

class JWTEncodedPanel : JPanel(BorderLayout()) {

  companion object {
    const val ENCODED_JWT_PROPERTY = "encodedJwt"
  }

  private val jwtEncodedTextArea = JTextPane().apply {
    document.addDocumentListener(object : DocumentListener {
      override fun changedUpdate(e: DocumentEvent) {
        print("The text attributes was changed")
      }

      override fun insertUpdate(e: DocumentEvent) {
        print("There was character inserted")
        updateState(e.document as StyledDocument)
        highlightDocument(e.document as StyledDocument)
      }

      override fun removeUpdate(e: DocumentEvent) {
        print("The text was removed")
      }
    })
  }

  private var encodedJwt = createEmptyJwt()

  init {
    border = BorderFactory.createTitledBorder("Encoded")
    add(JBScrollPane(jwtEncodedTextArea).apply {
      border = BorderFactory.createEmptyBorder()
    }, BorderLayout.CENTER)
  }

  fun setJwt(jwt: String) {
    jwtEncodedTextArea.text = jwt
  }

  private fun updateState(document: StyledDocument) {
    val jwtToken = document.getText(0, document.length)
    val split = jwtToken.split(".", limit = 3)

    val header = split.elementAtOrElse(0) { "" }
    val payload = split.elementAtOrElse(1) { "" }
    val signature = split.elementAtOrElse(2) { "" }

    val encodedJwt = EncodedJwt(
        jwtToken,
        header,
        payload,
        signature)
    firePropertyChange(ENCODED_JWT_PROPERTY, encodedJwt, encodedJwt)
    this.encodedJwt = encodedJwt
  }

  private fun highlightDocument(document: StyledDocument) {
    val headerHighlightParams = {
      val startPosition = 0
      val endPosition = encodedJwt.header.length
      startPosition to endPosition
    }()

    val payloadHighlightParams = {
      val startPosition = headerHighlightParams.first + headerHighlightParams.second + 1
      val endPosition = encodedJwt.payload.length
      startPosition to endPosition
    }()

    val signatureHighlightParams = {
      val startPosition = payloadHighlightParams.first + payloadHighlightParams.second + 1
      val endPosition = encodedJwt.signature.length
      startPosition to endPosition
    }()

    SwingUtilities.invokeLater {
      document.setCharacterAttributes(
          headerHighlightParams.first,
          headerHighlightParams.second,
          JWTHighlighters.headerHighlighter,
          true)

      document.setCharacterAttributes(
          payloadHighlightParams.first,
          payloadHighlightParams.second,
          JWTHighlighters.payloadHighlighter,
          true)

      document.setCharacterAttributes(
          signatureHighlightParams.first,
          signatureHighlightParams.second,
          JWTHighlighters.signatureHighlighter,
          true)

      document.setCharacterAttributes(
          headerHighlightParams.first + headerHighlightParams.second,
          1,
          JWTHighlighters.dotHighlighter,
          true)

      document.setCharacterAttributes(
          payloadHighlightParams.first + payloadHighlightParams.second,
          1,
          JWTHighlighters.dotHighlighter,
          true)
    }
  }
}