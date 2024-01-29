/**
 * LoginScreen
 * The first screen that is displayed to the user. It contains
 * various buttons to create new objects into the database and
 * allows the user to initiate a search or view instructions.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.awt.Insets.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LoginScreen extends Screen {
    JPanel newBtnPanel, companySearchPanel, joinPanel;
    GridLayout newBtnLayout, companySearchLayout;
    
    JPanel loginPanel;
    JLabel usernameLbl, passwordLbl;
    JTextField usernameInput, passwordInput;
    JButton loginBtn, createAccountBtn;

    public LoginScreen() {
        super("Login");
        createHeader(false);

        loginPanel = new LoginPanel();
        add(loginPanel);
    }

    class LoginPanel extends JPanel {
        
        public LoginPanel() {
            // Set layout to GridBagLayout for a simple grid structure
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Row 1: Username
            usernameLbl = new JLabel("Username:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(5, 5, 5, 5);
            add(usernameLbl, gbc);

            usernameInput = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = 0;
            add(usernameInput, gbc);

            // Row 2: Password
            passwordLbl = new JLabel("Password:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(passwordLbl, gbc);

            passwordInput = new JTextField(20);
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(passwordInput, gbc);

            // Row 3: Login Button
            loginBtn = new JButton("Login");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2; // Make the button span across two columns
            loginBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Get username and password from input fields
                    String username = usernameInput.getText();
                    String password = passwordInput.getText();
                    // TODO: Check if username and password are valid
                    // ...
                    database.setCredentials(new DatabaseCredentials(username, password));

                    System.out.println("Connecting...");
                    if (database.connect()) {
                        System.out.println("Connected.");
                        window.setScreen(new HomeScreen());
                    } else {
                        System.out.println("Failure to connect.");
                        JOptionPane.showMessageDialog(null, "Failed to connect to database.");
                    }
                }
            });
            add(loginBtn, gbc);

            createFooter(FooterType.EXIT);
        }
    }
}
