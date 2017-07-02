package com.virtualightning.fileresolver;

import com.virtualightning.fileresolver.environment.ContextKt;
import com.virtualightning.fileresolver.ui.MainUI;

public class Main {
    public static void main(String[] args) {
        ContextKt.initContext();
        new MainUI();
    }
}
