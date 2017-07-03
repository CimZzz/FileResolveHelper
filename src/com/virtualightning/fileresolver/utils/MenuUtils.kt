package com.virtualightning.fileresolver.utils

import java.awt.Menu
import java.awt.MenuItem
import java.awt.MenuShortcut

fun newMenuItem(name : String,parentMenu : Menu,shortCutKey : Int? = null): MenuItem {
    val menuItem = MenuItem(name)
    if(shortCutKey != null)
        menuItem.shortcut = MenuShortcut(shortCutKey,false)
    parentMenu.add(menuItem)
    return menuItem
}

fun newMenu(name : String,parentMenu: Menu? = null) : Menu {
    val menuItem = Menu(name)
    parentMenu?.add(menuItem)
    return menuItem
}