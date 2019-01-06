package com.github.scrat98.jwt.debugger.intellij.plugin.components

import com.github.scrat98.jwt.debugger.intellij.plugin.components.actions.JWTActions
import com.github.scrat98.jwt.debugger.intellij.plugin.utils.ToolBarProvider
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.DefaultActionGroup
import java.awt.BorderLayout
import javax.swing.JPanel

class JWTToolBar : JPanel(BorderLayout()) {

  init {
    add(
        ToolBarProvider.createToolBar("jwtToolbar", createToolBarActions()),
        BorderLayout.CENTER)
  }

  private fun createToolBarActions(): ActionGroup = DefaultActionGroup().apply {
    add(JWTActions.CREATE_NEW_JWT)
    add(JWTActions.COPY_JWT_PAYLOAD)
  }
}