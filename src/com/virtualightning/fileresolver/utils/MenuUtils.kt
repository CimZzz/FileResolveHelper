package com.virtualightning.fileresolver.utils

import java.awt.Menu
import java.awt.MenuItem

fun newMenuItem(name : String,parentMenu : Menu) : MenuItem {
    val menuItem = MenuItem(name)
    parentMenu.add(menuItem)
    return menuItem
}

fun newMenu(name : String,parentMenu: Menu? = null) : Menu {
    val menuItem = Menu(name)
    parentMenu?.add(menuItem)
    return menuItem
}