package com.motompro.tcp_config.window;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private static final String WINDOW_TITLE = "TCPConfig";
    private static final Dimension DEFAULT_WINDOW_DIMENSION = new Dimension(960, 540);

    public MainWindow() {
        init();
        this.setVisible(true);
    }

    private void init() {
        this.setTitle(WINDOW_TITLE);
        this.setSize(DEFAULT_WINDOW_DIMENSION);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
