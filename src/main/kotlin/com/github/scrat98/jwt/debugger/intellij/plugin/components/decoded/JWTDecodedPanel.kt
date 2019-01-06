package com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded

import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import javax.swing.BorderFactory
import javax.swing.JPanel

class JWTDecodedPanel : JPanel(BorderLayout()) {

  companion object {
    const val DENCODED_JWT_HEADER_PROPERTY = "decodedJwtHeader"

    const val DENCODED_JWT_PAYLOAD_PROPERTY = "decodedJwtPayload"
  }

  private val headerDecodedPanel = HeaderDecodedPanel()

  private val payloadDecodedPanel = PayloadDecodedPanel()

  private val signatureVerifierPanel = SignatureVerifierPanel()

  init {
    border = BorderFactory.createTitledBorder("Decoded")

    val payloadAndSignaturePanels = JBSplitter(true, 0.5f).apply {
      firstComponent = payloadDecodedPanel
      secondComponent = signatureVerifierPanel
    }

    add(JBSplitter(true, 0.33f).apply {
      firstComponent = headerDecodedPanel
      secondComponent = payloadAndSignaturePanels
    })
  }
}