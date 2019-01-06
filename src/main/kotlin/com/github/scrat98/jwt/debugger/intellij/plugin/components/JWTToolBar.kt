package com.github.scrat98.jwt.debugger.intellij.plugin.components

import org.apache.batik.util.gui.DropDownComponent
import java.awt.BorderLayout
import javax.swing.JButton
import javax.swing.JPanel

class JWTToolBar : JPanel(BorderLayout()) {

  init {
    add(DropDownComponent(JButton("fasf")), BorderLayout.CENTER)
  }
}