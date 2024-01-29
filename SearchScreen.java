/**
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.awt.Insets.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * SearchScreen
 * Screen that allows the user to search via a term. The term
 * is queried to the database and the ResultSet is iterated to
 * display desired information into respective tables. The user
 * can select rows from the table for additional information or
 * to modify existing data.
 */
class SearchScreen extends Screen {
    JPanel entryPanel, profilesPanel, ordersPanel, tablePanel, joinPanel;
    JButton searchBtn, backBtn;
    JLabel searchLbl;
    JTextField searchTxt;

    public SearchScreen() {
        this(null, false);
    }
    
    public SearchScreen(ScreenData_SearchScreen param) {
        this(param, ( param.searchTerm.equals("") ? false : true ));
    }
    
    public SearchScreen(ScreenData_SearchScreen param, boolean runSearch) {
        super("Search");
        createHeader();

        String term = "";
        if(param != null && runSearch)
            term = param.searchTerm;
        
        searchLbl = new JLabel("Search Term:");
        searchLbl.setHorizontalAlignment(JLabel.RIGHT);
        searchTxt = new JTextField(term, 20);
        searchTxt.addActionListener(e -> { searchBtn.doClick(); });
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(e -> {
            updateTables();
        });

        entryPanel = new JPanel();
        entryPanel.setBorder(new EmptyBorder(20,200,20,200));
        entryPanel.setLayout(new GridLayout(1,3));
        entryPanel.add(searchLbl);
        entryPanel.add(searchTxt);
        entryPanel.add(searchBtn);

        profilesPanel = new SearchProfileTable(term);
        ordersPanel = new SearchOrderTable(term);

        tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));
        tablePanel.add(profilesPanel);
        tablePanel.add(ordersPanel);

        joinPanel = new JPanel();
        joinPanel.setBorder(new EmptyBorder(0,10,20,10));
        joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));
        joinPanel.add(entryPanel);
        joinPanel.add(tablePanel);

        add(joinPanel, BorderLayout.CENTER);

        createFooter();
    }

    public void updateTables() {
        tablePanel.remove(profilesPanel);
        tablePanel.remove(ordersPanel);
        profilesPanel = new SearchProfileTable(searchTxt.getText());
        ordersPanel = new SearchOrderTable(searchTxt.getText());
        tablePanel.add(profilesPanel);
        tablePanel.add(ordersPanel);
        revalidate();
        repaint();
    }

    /**
     * SearchOrderTable
     * JTable to be populated with Order table data.
     */
    class SearchOrderTable extends CustTable {
        ArrayList<String> pk_list;

        public SearchOrderTable(String term) {
            super("Orders", new String[] { "Date", "Company Name" });

            pk_list = new ArrayList<String>();

            try {
                if(!term.equals("")) {
                    Database.QueryResult res = database.querySearchOrders(term);
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            String date =       res.results.getString(Tables.Order.Column.Order_date);
                            String co_name =    res.results.getString(Tables.Order.Column.Company_name);
                            model.addRow(new Object[] { date, co_name });
                            pk_list.add(res.results.getString(Tables.Order.Column.PO_no));
                        }
                    } else {
                        System.out.println("Failed to query Order data.");
                        // alertDialog_Data("search order.");
                    }
                }

                table.setModel(model);

                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) { // Double click
                            JTable target = (JTable)me.getSource();
                            int row = target.getSelectedRow();

                            if(row != -1) {
                                ScreenData_OrderScreen param = new ScreenData_OrderScreen();
                                param.po_no = pk_list.get(row);
                                // Send data as parameter
                                // setScreen(Screens.ORDER_SCREEN_VIEW, param);
                                window.setScreen(new OrderScreen(param));
                            }
                        }
                    }
                });

                this.add(scrollPane);
            } catch(Exception e) {
                System.out.println("Exception populating Search Order Table:\n" + e);
                // alertDialog("Exception populating data.");
            }
        }
    }

    /**
     * SearchProfileTable
     * JTable to be populated with Order table data.
     */
    class SearchProfileTable extends CustTable {
        ArrayList<String> pk_list;

        public SearchProfileTable(String term) {
            super("Profiles", new String[] { "Company Name" });

            pk_list = new ArrayList<String>();

            try {
                if(!term.equals("")) {
                    Database.QueryResult res = database.querySearchProfile(term);
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            String co_name = res.results.getString(Tables.Disposal_Profile.Column.Company_ID);
                            model.addRow(new Object[] { co_name });
                            pk_list.add(co_name);
                        }
                    } else {
                        System.out.println("Failed to query Profile data.");
                        // alertDialog_Data("search profile.");
                    }
                }
                table.setModel(model);

                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) { // Double click
                            JTable target = (JTable)me.getSource();
                            int row = target.getSelectedRow();

                            if(row != -1) {
                                ScreenData_ProfileScreen param = new ScreenData_ProfileScreen();
                                // get primary key from row id
                                param.company_name = pk_list.get(row);
                                // send as parameter
                                // setScreen(Screens.PROFILE_SCREEN_VIEW, param);
                                window.setScreen(new ProfileScreen(param));
                            }
                        }
                    }
                });

                this.add(scrollPane);
            } catch(Exception e) {
                System.out.println("Exception populating Search Order Table:\n" + e);
                // alertDialog("Exception populating data.");
            }
        }
    }
}
