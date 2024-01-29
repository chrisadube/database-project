/**
 * HomeScreen
 * The first screen that is displayed to the user. It contains
 * various buttons to create new objects into the database and
 * allows the user to initiate a search or view instructions.
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

public class HomeScreen extends Screen {
    JPanel newBtnPanel, companySearchPanel, joinPanel;
    GridLayout newBtnLayout, companySearchLayout;
    JButton newEmployeeBtn, newCompanyBtn, newProfileBtn, newOrderBtn, instructionBtn, searchBtn, exitBtn;
    JLabel companyNameLbl;
    JTextField companyNameInput;

    public HomeScreen() {
        super("Home");
        createHeader(false);
        
        // Button cluster
        newBtnPanel = new JPanel();
        newBtnLayout = new GridLayout(4,1);
        newBtnLayout.setVgap(5);
        newBtnPanel.setLayout(newBtnLayout);

        newEmployeeBtn = new JButton("Employees");
        newEmployeeBtn.addActionListener(e -> {
            window.setScreen(new EmployeeScreen());
        });
        newBtnPanel.add(newEmployeeBtn);

        newCompanyBtn = new JButton("Companies");
        newCompanyBtn.addActionListener(e -> {
            window.setScreen(new CompanyScreen());
        });
        newBtnPanel.add(newCompanyBtn);

        newProfileBtn = new JButton("Profiles");
        newProfileBtn.addActionListener(e -> {
            window.setScreen(new ProfileScreen());
        });
        newBtnPanel.add(newProfileBtn);

        newOrderBtn = new JButton("Orders");
        newOrderBtn.addActionListener(e -> {
            window.setScreen(new OrderScreen());
        });
        newBtnPanel.add(newOrderBtn);

        // Search cluster
        companySearchPanel = new JPanel();
        companySearchLayout = new GridLayout(3,1);
        companySearchLayout.setVgap(10);
        companySearchPanel.setLayout(companySearchLayout);

        companyNameLbl = new JLabel("Company Name:");
        companySearchPanel.add(companyNameLbl);

        companyNameInput = new JTextField("", 20);
        companyNameInput.addActionListener(e -> { searchBtn.doClick(); });
        companySearchPanel.add(companyNameInput);
        
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            ScreenData_SearchScreen param = new ScreenData_SearchScreen();
            param.searchTerm = companyNameInput.getText();
            // setScreen(Screens.SEARCH_SCREEN, param);
            window.setScreen(new SearchScreen(param));
        });
        companySearchPanel.add(searchBtn);

        joinPanel = new JPanel();
        joinPanel.setBorder(new EmptyBorder(180,100,180,100));
        joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.X_AXIS));
        joinPanel.add(newBtnPanel);
        joinPanel.add(Box.createHorizontalStrut(100));
        joinPanel.add(companySearchPanel);

        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });
        
        // Instruction button
        instructionBtn = new JButton("Instructions");
        instructionBtn.addActionListener(e -> {
            window.setScreen(new InstructionScreen());
        });

        add(joinPanel, BorderLayout.CENTER);
        
        createFooter(FooterType.EXIT);
    }
}
