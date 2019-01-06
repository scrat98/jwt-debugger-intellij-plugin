package com.github.scrat98.jwt.debugger.intellij.plugin

import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class JwtToolWindowFactory : ToolWindowFactory, DumbAware {
  override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
    val jwtToolWindow = JwtToolPanel()
    val contentFactory = ContentFactory.SERVICE.getInstance()
    val content = contentFactory.createContent(jwtToolWindow, "", false)
    toolWindow.contentManager.addContent(content)
  }
}