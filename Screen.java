/**
 * The base class for all application screens. It abstracts
 * much information such as the header and provides a
 * layout that is general to all screens, resulting in
 * consistent graphics.
 */

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.awt.Insets.*;

public class Screen extends JPanel {
    static Window window;
    static Database database;
    String headerText;
    
    public Screen(String headerText) {
        this.headerText = headerText;

        setBorder(new EmptyBorder(5,5,5,5));
        setLayout(new BorderLayout());

    }

    public static void setWindow(Window sWindow) {
        Screen.window = sWindow;
    }

    public static void setDatabase(Database sDatabase) {
        Screen.database = sDatabase;
    }

    public void createHeader() {
        createHeader(true);
    }

    public void createHeader(boolean useHomeBtn) {
        add(new Header(headerText, useHomeBtn), BorderLayout.NORTH);
    }

    class Header extends JPanel {
        public Header(String headerText, boolean useHomeBtn) {
            setLayout(new BorderLayout());
            JLabel title = new JLabel(headerText, SwingConstants.CENTER);
            title.setFont(new Font("Verdana", Font.BOLD, 24));
            add(title, BorderLayout.NORTH);
            if (useHomeBtn) {
                JButton homeBtn = new JButton("Home");
                homeBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        window.clearScreenHistory();
                        window.setScreen(new HomeScreen());
                    }
                });
                add(homeBtn, BorderLayout.WEST);
            }
        }
    }

    public void createFooter() {
        createFooter(FooterType.BACK);
    }

    public void createFooter(FooterType type) {
        createFooter(type, null, null);
    }

    public void createFooter(ActionListener callCreate, String createBtnText) {
        createFooter(FooterType.CREATE, callCreate, createBtnText);
    }

    public void createFooter(FooterType type, ActionListener callCreate, String createBtnText) {
        switch (type) {
            case CREATE: 
                add(new CreateFooter(callCreate, createBtnText), BorderLayout.SOUTH);
                break;
            case EXIT:
                add(new ExitFooter(), BorderLayout.SOUTH);
                break;
            case BACK: ; // Redundant with default
                    // add(new BackFooter(), BorderLayout.SOUTH);
                    // break;
            default:
                add(new BackFooter(), BorderLayout.SOUTH);
                break;
        }
    }

    // public void createBackCreateFooter(ActionListener callCreate, String createBtnText) {
    //     add(new BackCreateFooter(callCreate, createBtnText), BorderLayout.SOUTH);
    // }

    // class BackCreateFooter extends JPanel {
    //     public BackCreateFooter(ActionListener callCreate, String createBtnText) {
    //         setLayout(new BorderLayout());

    //         JButton backBtn = new JButton("Back");
    //         backBtn.addActionListener(new ActionListener() {
    //             @Override
    //             public void actionPerformed(ActionEvent e) {
    //                 window.back();
    //             }
    //         });
    //         add(backBtn, BorderLayout.WEST);

    //         JButton createBtn = new JButton(createBtnText);
    //         createBtn.addActionListener(callCreate);
    //         add(createBtn, BorderLayout.EAST);
    //     }
    // }


    static enum FooterType {
        CREATE,
        BACK,
        EXIT;
    }

    /**
     * Create Footer
     * Creates a footer with a back button at the left
     * and a create button at the right. Useful for
     * screens that create new entries to the database.
     */
    class CreateFooter extends JPanel {
        public CreateFooter(ActionListener callCreate, String createBtnText) {
            setLayout(new BorderLayout());

            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    window.back();
                }
            });
            add(backBtn, BorderLayout.WEST);

            JButton createBtn = new JButton(createBtnText);
            createBtn.addActionListener(callCreate);
            add(createBtn, BorderLayout.EAST);
        }
    }

    /**
     * Back Footer
     * Creates a footer with a back button at the left.
     */
    class BackFooter extends JPanel {
        public BackFooter() {
            setLayout(new BorderLayout());
            JButton backBtn = new JButton("Back");
            backBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    window.back();
                }
            });
            add(backBtn, BorderLayout.WEST);
        }
    }

    /**
     * Exit Footer
     * Creates a footer with an exit button at the left.
     */
    class ExitFooter extends JPanel {
        public ExitFooter() {
            setLayout(new BorderLayout());
            JButton exitBtn = new JButton("Exit");
            exitBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            add(exitBtn, BorderLayout.WEST);
        }
    }
}




// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.awt.Font;
// import java.sql.DriverManager;
// import java.awt.BorderLayout;
// import java.awt.Insets.*;
// import javax.swing.JScrollBar;
// import javax.swing.JScrollPane;
// import javax.swing.border.EmptyBorder;

// public class Screen extends JPanel {
//     JLabel header;
//     BorderLayout lay;
//     ButtonFooter footer;
    
//     public Screen(String headerTxt) {
//         header = new JLabel(headerTxt);
//         header.setFont(new Font("Verdana", Font.BOLD, 24));
//         header.setHorizontalAlignment(SwingConstants.CENTER);
//         lay = new BorderLayout(10,10);
//         this.setBorder(new EmptyBorder(5,5,5,5));
//         this.setLayout(lay);
//         this.add(header, BorderLayout.NORTH);
//         footer = new ButtonFooter();
//     }

//     protected void setHeader(String txt) {
//         header.setText(txt);
//     };

//     protected void addFooter() {
//         this.add(footer, BorderLayout.SOUTH);
//     }

//     /**
//      * button footer
//      */
//     public class ButtonFooter extends JPanel {

//         public ButtonFooter() {
//             this.setBorder(new EmptyBorder(0,0,10,0));
//             this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//         }

//         public void glue() { this.add(Box.createHorizontalGlue()); }
//         public void strut() { this.strut(100); }
//         public void strut(int width) { this.add(Box.createHorizontalStrut(width)); }
//     }
// }
