/**
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.sql.DriverManager;
import java.awt.BorderLayout;
import java.awt.Insets.*;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * InstructionScreen
 * Displays the instructions in a screen regarding how to
 * use this application.
 */
public class InstructionScreen extends Screen {
    JTextArea txt;
    JButton backBtn;

    public InstructionScreen() {
        super("Instructions");
        createHeader();

        // Load text from file?

        // Add text area component
        txt = new JTextArea("bunch of text");

        backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            window.back();
        });

        this.add(txt, BorderLayout.CENTER);
        
        createFooter();
    }
}
