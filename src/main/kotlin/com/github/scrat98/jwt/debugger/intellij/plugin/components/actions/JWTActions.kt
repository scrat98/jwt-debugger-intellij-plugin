package com.github.scrat98.jwt.debugger.intellij.plugin.components.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class JWTActions {
  companion object {
    val CREATE_NEW_JWT =
        object : AnAction("New JWT", "Create a new JWT", AllIcons.FileTypes.Any_type) {
          override fun actionPerformed(e: AnActionEvent) {}

          override fun isDumbAware(): Boolean {
            return true
          }
        }

    val COPY_JWT_PAYLOAD =
        object : AnAction(
            "Copy payload as JSON", "Copy JWT payload to clipboard as JSON",
            AllIcons.FileTypes.Json) {
          override fun actionPerformed(e: AnActionEvent) {}

          override fun isDumbAware(): Boolean {
            return true
          }
        }
  }
}