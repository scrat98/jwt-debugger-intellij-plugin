package com.github.scrat98.jwt.debugger.intellij.plugin.components

import com.github.scrat98.jwt.debugger.intellij.plugin.components.decoded.JWTDecodedPanel
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.EncodedJwt
import com.github.scrat98.jwt.debugger.intellij.plugin.components.encoded.JWTEncodedPanel
import com.intellij.ui.JBSplitter
import java.awt.BorderLayout
import javax.swing.JPanel

class JWTContent : JPanel(BorderLayout()) {
  private val jwtEncodedPanel = JWTEncodedPanel()

  private val jwtDecodedPanel = JWTDecodedPanel()

  init {
    addPropertyListenerOnJwtEncodedPanel()
    addPropertyListenerOnJwtDecodedPanel()
    add(JBSplitter(true, 0.2f).apply {
      firstComponent = jwtEncodedPanel
      secondComponent = jwtDecodedPanel
    }, BorderLayout.CENTER)
    jwtEncodedPanel.resetJwt()
  }

  private fun addPropertyListenerOnJwtEncodedPanel() {
    jwtEncodedPanel.addPropertyChangeListener(
        JWTEncodedPanel.ENCODED_JWT_PROPERTY
    ) { event ->
      val encodedJwt = event.newValue as EncodedJwt
      val jwtIsValid = jwtDecodedPanel.setAndValidateEncodedJwt(encodedJwt)

      if (jwtIsValid) {
        jwtEncodedPanel.disableInvalidJwtForm()
      } else {
        jwtEncodedPanel.enableInvalidJwtForm()
      }
    }
  }

  private fun addPropertyListenerOnJwtDecodedPanel() {
    jwtDecodedPanel.addPropertyChangeListener(
        JWTDecodedPanel.DECODED_JWT_PROPERTY
    ) { event ->
      val encodedJwt = event.newValue as EncodedJwt
      jwtEncodedPanel.apply {
        disableInvalidJwtForm()
        setEncodedJwt(encodedJwt)
      }
    }
  }
}