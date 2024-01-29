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
 * Base JTable and Panel
 */
class CustTable extends JPanel { // abstract?
    DefaultTableModel model;
    JTable table;
    JScrollPane scrollPane;

    public CustTable(String title, String[] header) {
        model = new DefaultTableModel(null, header) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        this.setLayout(new BorderLayout());

        this.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEmptyBorder(),
            title,
            TitledBorder.CENTER,
            TitledBorder.TOP));
        
        table = new JTable();
        
        table.setCellSelectionEnabled(false);
        table.setRowSelectionAllowed(true);
        table.setFillsViewportHeight(true);
        scrollPane = new JScrollPane(table);
    }
}
