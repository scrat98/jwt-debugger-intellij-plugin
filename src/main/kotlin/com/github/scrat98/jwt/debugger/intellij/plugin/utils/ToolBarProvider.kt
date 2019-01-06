package com.github.scrat98.jwt.debugger.intellij.plugin.utils

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import javax.swing.JComponent

object ToolBarProvider {
  @JvmOverloads
  fun createToolBar(
      name: String,
      actions: ActionGroup,
      targetComponent: JComponent? = null,
      horizontal: Boolean = true
  ): JComponent {
    val actionManager = ActionManager.getInstance()
    val actionToolBar = actionManager.createActionToolbar(
        name,
        actions,
        horizontal)
    if (targetComponent != null) {
      actionToolBar.setTargetComponent(targetComponent)
    }
    return actionToolBar.component
  }
}
