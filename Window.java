/**
 * Window.java
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class Window extends JFrame {
    private static Window wnd_instance = null;
    private Stack<JPanel> screenHistory;
    private String title;
    private int window_width = 800;
    private int window_height = 600;

    public Window() {
        super("Default Title");
        screenHistory = new Stack<JPanel>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(window_width, window_height);

        setVisible(true);
    }

    public static synchronized Window getWindow() {
        if (wnd_instance == null)
            wnd_instance = new Window();
        return wnd_instance;
    }

    public int width() { return window_width; }
    public int height() { return window_height; }

    public void setTitle(String title) {
        this.title = title;
        super.setTitle(title);
    }

    public void setScreen(JPanel panel) {
        pushScreen(panel);
        getContentPane().removeAll();
        getContentPane().add(panel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void back() {
        if (screenHistory.size() > 1) {
            screenHistory.pop();
            setScreen(screenHistory.pop());
        }
    }

    public void pushScreen(JPanel screen) {
        screenHistory.push(screen);
    }
    
    public JPanel popScreen() {
        return screenHistory.pop();
    }

    public void clearScreenHistory() {
        screenHistory.clear();
    }

    /**
     * confirmDialog
     * Prompts the user of a message and an option
     * to select yes or no.
     */
    public int confirmDialog(JPanel parent, String msg, String yes, String no) {
        return JOptionPane.showOptionDialog(
                parent,
                "Are you sure you want to delete?",
                "Confirm Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] { yes, no },
                no );
    }
    
    /**
     * alertDialog
     * Popup alert dialog to inform user that an invalid
     * data was entered and it could not be parsed from
     * String to given datatype.
     */
    public void alertDialog(JPanel parent, String msg) {
        JOptionPane.showMessageDialog(
                parent,
                msg,
                "Alert",
                JOptionPane.WARNING_MESSAGE );
    }

    // Convenience function
    public void alertDialog_Data(JPanel parent) {
        alertDialog(parent, "Invalid data entered.");
    }

    // Convenience function
    public void alertDialog_Data(JPanel parent, String data) {
        alertDialog(parent, "Invalid data: " + data);
    }

    /**
     * The DATABASE USER INTERFACE METHODS section contains various
     * methods and objects essential to the operation of GUI Screens.
     */
    // public enum Screens {
    //     HOME_SCREEN,
    //     EMPLOYEE_SCREEN_CREATE,
    //     EMPLOYEE_SCREEN_UPDATE,
    //     COMPANY_SCREEN_CREATE,
    //     COMPANY_SCREEN_UPDATE,
    //     PROFILE_SCREEN_CREATE,
    //     PROFILE_SCREEN_VIEW,
    //     ORDER_SCREEN_CREATE,
    //     ORDER_SCREEN_VIEW,
    //     SEARCH_SCREEN,
    //     INSTRUCTION_SCREEN
    // }

    // ------------------------------------------
    // ---------- NOT CURRENTLY IN USE ----------
    // ------------------------------------------

    /**
     * setScreen
     * Accepts a screen enumeration and sets the screen GUI based on
     * the specified value. Also accepts a ScreenData object which
     * contains specified primary key data for the given screen.
     */
    // public void setScreen(Screens screen) {
    //     setScreen(screen, null);
    // }
    
    // public void setScreen(Screens screen, ScreenData param) {
    //     switch (screen) {
    //         case Screens.HOME_SCREEN:
    //             window.setScreen(new HomeScreen());
    //             break;
    //         case Screens.EMPLOYEE_SCREEN_CREATE:
    //             window.setScreen(new EmployeeScreenCreate());
    //             break;
    //         case Screens.COMPANY_SCREEN_CREATE:
    //             window.setScreen(new CompanyScreenCreate());
    //             break;
    //         case Screens.COMPANY_SCREEN_UPDATE:
    //             window.setScreen(new CompanyScreenUpdate());
    //             break;
    //         case Screens.PROFILE_SCREEN_CREATE:
    //             window.setScreen(new ProfileScreenCreate());
    //             break;
    //         case Screens.PROFILE_SCREEN_VIEW:
    //             window.setScreen(new ProfileScreenView((ScreenData_ProfileScreen)param));
    //             break;
    //         case Screens.ORDER_SCREEN_CREATE:
    //             window.setScreen(new OrderScreenCreate());
    //             break;
    //         case Screens.ORDER_SCREEN_VIEW:
    //             window.setScreen(new OrderScreenView((ScreenData_OrderScreen)param));
    //             break;
    //         case Screens.SEARCH_SCREEN:
    //             window.setScreen(new SearchScreen((ScreenData_SearchScreen)param));
    //             break;
    //         case Screens.INSTRUCTION_SCREEN:
    //             window.setScreen(new InstructionScreen());
    //             break;
    //         default:
    //             window.setScreen(new Screen("Invalid"));
    //             break;
    //     }
    // }
}
