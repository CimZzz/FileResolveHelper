package com.virtualightning.fileresolver.widget

import com.virtualightning.fileresolver.entity.Format
import com.virtualightning.fileresolver.environment.byteTableName
import com.virtualightning.fileresolver.environment.formatTableName
import javax.swing.table.DefaultTableModel


class FormatTableModel : DefaultTableModel() {
    var formatDataList: ArrayList<Format<*>>? = null
    set(value) {
        field = value
    }

    override fun getRowCount(): Int {
        return formatDataList?.size?:0
    }

    override fun getColumnCount(): Int {
        return formatTableName.size
    }

    override fun getColumnName(column: Int): String {
        return formatTableName[column]
    }

    override fun getColumnClass(columnIndex: Int): Class<*> {
        return String.javaClass
    }

    override fun isCellEditable(row: Int, column: Int): Boolean {
        return false
    }

    override fun getValueAt(row: Int, column: Int): Any {
        val data = formatDataList!![row]

        when(column) {
            0->return data.formatName
            1->return data.typeName()
            2->return "${data.sizeOfData()}"
            3-> if(data.e == null) return "${data.e}"
        }

        return ""
    }
}