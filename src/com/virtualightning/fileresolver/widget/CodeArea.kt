package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.utils.Info
import java.awt.MenuItem
import java.awt.PopupMenu
import javax.swing.JMenuItem
import javax.swing.JPopupMenu
import javax.swing.JTextArea
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.AttributeSet
import javax.swing.text.PlainDocument


var refres = false
class CodeArea : JTextField() {
    init {
        document = Document()
        document.addDocumentListener(object :DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) {
                Info("Change")
            }

            override fun insertUpdate(e: DocumentEvent?) {
                Info("Insert")
                if(refres) {
                    val popMenu = JPopupMenu()
                    popMenu.add(JMenuItem("hhaa"))
                    popMenu.add(JMenuItem("hhsadaa"))
                    popMenu.show(this@CodeArea,1,2)
                    refres = false
                }
            }

            override fun removeUpdate(e: DocumentEvent?) {
                Info("Remove")
            }

        })
    }

    class Document : PlainDocument() {
        override fun insertString(offs: Int, str: String?, a: AttributeSet?) {
            super.insertString(offs, str, a)
            if(str != null) {
                if(str.equals("."))
                    refres = true
            }
        }

        override fun remove(offs: Int, len: Int) {
            Info(getText(offs,len))
            super.remove(offs, len)
        }
    }
}