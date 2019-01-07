package com.github.scrat98.jwt.debugger.intellij.plugin

import com.github.scrat98.jwt.debugger.intellij.plugin.components.JWTContent
import com.intellij.openapi.ui.SimpleToolWindowPanel

class JwtToolPanel : SimpleToolWindowPanel(true) {

  private val content = JWTContent()

  init {
    setContent(content)
  }
}
