package com.zetcode;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
Java Tetris game clone

Author: Jan Bodnar
Website: https://zetcode.com
 */

public class Tetris extends JFrame {

    private JLabel statusbar;

    public Tetris() {

        initUI();
    }
