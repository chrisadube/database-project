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
 * OrderScreen
 * Base class for Order screen that contains all necessary
 * JComponents to display or retrieve data.
 */
public class OrderScreen extends Screen {

    public OrderScreen() {
        super("Orders");
        createHeader();

        JPanel contentPanel = new JPanel(new FlowLayout()); // BorderBox?

        JLabel emptyLbl = new JLabel("No company selected.");
        contentPanel.add(emptyLbl);

        JButton createBtn = new JButton("Search");
        createBtn.addActionListener(e -> {
            window.setScreen(new SearchScreen());
        });
        contentPanel.add(createBtn);

        add(contentPanel, BorderLayout.CENTER);

        ActionListener callCreate = (ActionEvent e) -> {
            openNewOrderFormDialog(this);
        };
        createFooter(callCreate, "Create Order");
    }

    public OrderScreen(ScreenData_OrderScreen param) {
        super("Orders");
        createHeader();

        JButton cancelOrderBtn = new JButton("Cancel Order");
        cancelOrderBtn.setMinimumSize(new Dimension(160, 25));
        cancelOrderBtn.setMaximumSize(new Dimension(160, 25));
        cancelOrderBtn.setAlignmentX(CENTER_ALIGNMENT);
        cancelOrderBtn.addActionListener(e -> {
            // dialog to verify and enter fee
            cancelOrderDialog(param.po_no);
        });

        JButton addStopBtn = new JButton("Add Stop");
        addStopBtn.setMinimumSize(new Dimension(160, 25));
        addStopBtn.setMaximumSize(new Dimension(160, 25));
        addStopBtn.setAlignmentX(CENTER_ALIGNMENT);
        addStopBtn.addActionListener(e -> {
            // dialog to enter stop no and fee
            addStopToOrder(param.po_no);
        });

        JPanel modBtnPanel = new JPanel();
        modBtnPanel.setLayout(new BoxLayout(modBtnPanel, BoxLayout.Y_AXIS));
        modBtnPanel.setBorder(new EmptyBorder(30,50,30,50));
        modBtnPanel.add(cancelOrderBtn);
        modBtnPanel.add(addStopBtn);
        

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(new OrderCompanyViewPanel(param));
        topPanel.add(modBtnPanel);

        // JPanel contentPanel = new JPanel(new GridLayout(2,1));
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.add(topPanel);
        contentPanel.add(new ObjectTable(param));

        add(contentPanel, BorderLayout.CENTER);

        ActionListener callCreate = (ActionEvent e) -> {
            openNewOrderFormDialog(this);
        };
        createFooter(callCreate, "Create Order");
    }

    /**
     * openNewOrderFormDialog
     */
    private void openNewOrderFormDialog(JPanel parent) {
        // Create the dialog
        JDialog newOrderDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Order Form", true);
        
        // Create Panel for entry components
        GridLayout layout = new GridLayout(8,2);
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(layout);
        layout.setHgap(10);
        layout.setVgap(10);
        
        JLabel po_noLbl = new JLabel("PO number:");
        orderPanel.add(po_noLbl);
        JTextField po_noTxt = new JTextField("", 20);
        orderPanel.add(po_noTxt);

        JLabel companyLbl = new JLabel("Company Name:");
        orderPanel.add(companyLbl);
        JTextField companyTxt = new JTextField("", 20);
        orderPanel.add(companyTxt);

        JLabel descLbl = new JLabel("Description:");
        orderPanel.add(descLbl);
        JTextField descTxt = new JTextField("", 20);
        orderPanel.add(descTxt);

        JLabel salesLbl = new JLabel("Salesperson:");
        orderPanel.add(salesLbl);
        JTextField salesTxt = new JTextField("", 20);
        orderPanel.add(salesTxt);

        JLabel dateLbl = new JLabel("Date (yyyy-mm-dd):");
        orderPanel.add(dateLbl);
        JTextField dateTxt = new JTextField("", 20);
        orderPanel.add(dateTxt);

        JLabel billAddrLbl = new JLabel("Client Billing Address:");
        orderPanel.add(billAddrLbl);
        JTextField billAddrTxt = new JTextField("", 20);
        orderPanel.add(billAddrTxt);

        JLabel transTypeLbl = new JLabel("Transport Type:");
        orderPanel.add(transTypeLbl);
        JTextField transTypeTxt = new JTextField("", 20);
        orderPanel.add(transTypeTxt);

        JLabel manFeeLbl = new JLabel("Manifest Fee:");
        orderPanel.add(manFeeLbl);
        JTextField manFeeTxt = new JTextField("", 20);
        orderPanel.add(manFeeTxt);

        orderPanel.setBorder(new EmptyBorder(120,200,120,200));

        // Create Panel for button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton newOrderBtn = new JButton("Create");
        newOrderBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Store textfield values into dataset
                try {
                    Tables.Order.Data data = (new Tables.Order()).new Data();
                    data.po_no = po_noTxt.getText();
                    data.company_name = companyTxt.getText();
                    data.description = descTxt.getText();
                    data.salesperson = salesTxt.getText();
                    data.order_date = dateTxt.getText();
                    data.client_billing_address = billAddrTxt.getText();
                    data.transport_type = transTypeTxt.getText();
                    data.manifest_fee = Double.parseDouble(manFeeTxt.getText());
                    
                    if(database.createOrder(data).success) {
                        window.back();
                    } else {
                        System.out.println("Failed to create order.");
                        // alertDialog("Failed to create order. ID,~Name?");
                    }
                } catch(Exception x) {
                    System.out.println("Cannot parse value:\n" + x);
                    // alertDialog_Data();
                }
                
                newOrderDialog.dispose();
            }
        });
        
        btnPanel.add(newOrderBtn);

        newOrderDialog.add(orderPanel);
        newOrderDialog.add(btnPanel);

        newOrderDialog.setLayout(new BoxLayout(newOrderDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Set dialog properties
        newOrderDialog.setSize(300, 150);
        newOrderDialog.setLocationRelativeTo(this);
        newOrderDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newOrderDialog.pack();

        // Make the dialog visible
        newOrderDialog.setVisible(true);
    }



    public void cancelOrderDialog(String po_no) {
        JTextField feeTxt = new JTextField("", 20);
        Object[] objs = {
            "Cancelation Fee:", feeTxt
        };
    
        Tables.Order.Data data = (new Tables.Order()).new Data();
    
        int option = JOptionPane.showConfirmDialog(null, objs, "Cancel Order", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            // Set data
            data.po_no = po_no;
            try {
                data.cancelation_fee = Double.parseDouble(feeTxt.getText());
                data.canceled = true;
            } catch(Exception x) {
                System.out.println("Invalid fee.");
                // alertDialog("Invalid fee.");
            }
    
            if(!database.updateCancelOrder(data).success)
                System.out.println("Unable to assess manifest fee to order " + po_no);
                // alertDialog("Unable to assess manifest fee to order " + po_no);
        }
    }
    
    public void addStopToOrder(String po_no) {
        JTextField stopTxt = new JTextField("", 20);
        JTextField feeTxt = new JTextField("", 20);
        Object[] objs = {
            "Stop Number:", stopTxt,
            "Stop Fee:", feeTxt
        };
    
        Tables.Order_Stop_Fees.Data data = (new Tables.Order_Stop_Fees()).new Data();
    
        int option = JOptionPane.showConfirmDialog(null, objs, "Add Stop To Order", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            // Set data
            data.po_no = po_no;
            try {
                data.stop_no = Integer.parseInt(stopTxt.getText());
                data.stop_fee = Double.parseDouble(feeTxt.getText());
            } catch(Exception x) {
                System.out.println("Invalid fee.");
                // alertDialog("Invalid fee.");
            }
    
            if(!database.updateAddStopToOrder(data).success)
                System.out.println("Unable to add stop to order " + po_no);
                // alertDialog("Unable to add stop to order " + po_no);
        }
    }
    
    // public void addObjectToOrder(String po_no) {
    //     JTextField stopTxt = new JTextField("", 20);
    //     JTextField feeTxt = new JTextField("", 20);
    //     Object[] objs = {
    //         "Stop Number:", stopTxt,
    //         "Cancelation Fee:", feeTxt
    //     };
    
    //     Tables.Order_Stop_Fees.Data data = (new Tables.Order_Stop_Fees()).new Data();
    
    //     int option = JOptionPane.showConfirmDialog(null, objs, "Add Object To Order", JOptionPane.OK_CANCEL_OPTION);
        
    //     if (option == JOptionPane.OK_OPTION) {
    //         // Set data
    //         data.po_no = po_no;
    //         try {
    //             data.stop_no = Integer.parseInt(stopTxt.getText());
    //             data.stop_fee = Double.parseDouble(feeTxt.getText());
    //         } catch(Exception x) {
    //             System.out.println("Invalid fee.");
    //             // alertDialog("Invalid fee.");
    //         }
    
    //         if(!database.updateAddStopToOrder(data).success)
    //             System.out.println("Unable to add object to order " + po_no);
    //             // alertDialog("Unable to add object to order " + po_no);
    //     }
    // }

    /**
     * OrderCompanyViewPanel
     * Company data viewer for the Order object.
     */
    class OrderCompanyViewPanel extends JPanel {
        JPanel p1, p2;
        String po_no="", company_name="", description="", salesperson="",
                order_date="", client_billing_address="", transport_type="",
                transport_duration="";
        double manifest_fee, cancelation_fee;
        boolean canceled;

        public OrderCompanyViewPanel(ScreenData_OrderScreen param) {
            String pk_po_no = param.po_no;
            if(!pk_po_no.equals("")) {
                try {
                    Database.QueryResult res = database.queryOrdersPo_no(param.po_no);
                    if(res.success) {
                        while(res.results.next()) {
                            po_no =                    res.results.getString(Tables.Order.Column.PO_no);
                            company_name =             res.results.getString(Tables.Order.Column.Company_name);
                            description =              res.results.getString(Tables.Order.Column.Description);
                            salesperson =              res.results.getString(Tables.Order.Column.Salesperson);
                            order_date =               res.results.getString(Tables.Order.Column.Order_date);
                            client_billing_address =   res.results.getString(Tables.Order.Column.Client_billing_address);
                            transport_type =           res.results.getString(Tables.Order.Column.Transport_type);
                            transport_duration =       res.results.getString(Tables.Order.Column.Transport_duration);
                            manifest_fee =             res.results.getDouble(Tables.Order.Column.Manifest_fee);
                            canceled =                 res.results.getString(Tables.Order.Column.Canceled).equals("1") ? true : false;
                            cancelation_fee =          res.results.getDouble(Tables.Order.Column.Cancelation_fee);
                        }
                    } else {
                        System.out.println("Failed to query Company data");
                        // alertDialog("Failed to query Company data.");
                    }
                } catch(Exception e) {
                    System.out.println("Failed to query\n" + e);
                    // alertDialog("Failed to query Order Screen View. PK=" + param.po_no);
                }
            }

            p1 = new JPanel();
            p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
            p2 = new JPanel();
            p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

            // Populate labels with queried data
            p1.add(new JLabel("Client Name:"));
            p2.add(new JLabel(company_name));
            p1.add(new JLabel("Date:"));
            p2.add(new JLabel(order_date));
            p1.add(new JLabel("Project Description:"));
            p2.add(new JLabel(description));
            p1.add(new JLabel("Salesperson"));
            p2.add(new JLabel(salesperson));
            p1.add(new JLabel("Purchase Order Number:"));
            p2.add(new JLabel(po_no));
            p1.add(new JLabel("Client Billing Address:"));
            p2.add(new JLabel(client_billing_address));

            this.add(p1);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
            this.add(p2);
        }
    }

    /**
     * ObjectTable
     * JTable to be populated with Object data.
     */
    class ObjectTable extends CustTable {
        ArrayList<PK_Obj_Sub> pk_list;
        String pk_po_no;

        public ObjectTable(ScreenData_OrderScreen param) {
            super("Objects", new String[] { "Quantity", "Quote", "Price/Container",
                                            "Total Price for Customer", "Cost/Container",
                                            "Total Cost", "Price Code" } );
            
            pk_po_no = param.po_no;
            
            pk_list = new ArrayList<PK_Obj_Sub>();
            
            double total_price = 0.0;
            double total_cost = 0.0;
            
            // Query all objects where po_no
            try {
                Database.QueryResult res;
                if(!pk_po_no.equals("")) {
                    // join: OBJECT , WASTE
                    res = database.queryOrderForObjects_Waste(pk_po_no);
                    
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            int quantity    = res.results.getInt(Tables.TObject.Column.Quantity);
                            String quote    = res.results.getString(Tables.TObject.Column.Quote);
                            double price    = res.results.getDouble(Tables.TObject.Column.Price);
                            double t_price  = quantity * price; // Query instead?
                            double cost     = res.results.getDouble(Tables.TObject.Column.Cost);
                            double t_cost   = quantity * cost;
                            total_price     += t_price;
                            total_cost      += t_cost;
                            String price_code = res.results.getString(Tables.Waste.Column.ACV_Code);
                            
                            model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), price_code });
                            PK_Obj_Sub pk = new PK_Obj_Sub();
                            pk.subtype = Subtype.WASTE;
                            pk.po_no = pk_po_no;
                            pk.quote = quote;
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Objects: object data");
                        // alertDialog_Data("object data.");
                    }
                    
                    // join: OBJECT , MATERIAL
                    res = database.queryOrderForObjects_Material(pk_po_no);
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            int quantity    = res.results.getInt(Tables.TObject.Column.Quantity);
                            String quote    = res.results.getString(Tables.TObject.Column.Quote);
                            double price    = res.results.getDouble(Tables.TObject.Column.Price);
                            double t_price  = quantity * price;
                            double cost     = res.results.getDouble(Tables.TObject.Column.Cost);
                            double t_cost   = quantity * cost;
                            total_price     += t_price;
                            total_cost      += t_cost;
                            
                            model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                            PK_Obj_Sub pk = new PK_Obj_Sub();
                            pk.subtype = Subtype.MATERIAL;
                            pk.po_no = pk_po_no;
                            pk.quote = quote;
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Objects: material data");
                        // alertDialog_Data("material data.");
                    }
                    
                    // join: OBJECT , EQUIPMENT
                    res = database.queryOrderForObjects_Equipment(pk_po_no);
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            int quantity    = res.results.getInt(Tables.TObject.Column.Quantity);
                            String quote    = res.results.getString(Tables.TObject.Column.Quote);
                            double price    = res.results.getDouble(Tables.TObject.Column.Price);
                            double t_price  = quantity * price;
                            double cost     = res.results.getDouble(Tables.TObject.Column.Cost);
                            double t_cost   = quantity * cost;
                            total_price     += t_price;
                            total_cost      += t_cost;
                            
                            model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                            PK_Obj_Sub pk = new PK_Obj_Sub();
                            pk.subtype = Subtype.EQUIPMENT;
                            pk.po_no = pk_po_no;
                            pk.quote = quote;
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Objects: equipment data");
                        // alertDialog_Data("equipment data.");
                    }
                    
                    // join: OBJECT , LABOR
                    res = database.queryOrderForObjects_Labor(pk_po_no);
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            int quantity    = res.results.getInt(Tables.TObject.Column.Quantity);
                            String quote    = res.results.getString(Tables.TObject.Column.Quote);
                            double price    = res.results.getDouble(Tables.TObject.Column.Price);
                            double t_price  = quantity * price;
                            double cost     = res.results.getDouble(Tables.TObject.Column.Cost);
                            double t_cost   = quantity * cost;
                            total_price     += t_price;
                            total_cost      += t_cost;
                                                
                            model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                            PK_Obj_Sub pk = new PK_Obj_Sub();
                            pk.subtype = Subtype.LABOR;
                            pk.po_no = pk_po_no;
                            pk.quote = quote;
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Objects: labor data");
                        // alertDialog_Data("labor data.");
                    }
                    
                    // Query ORDER to get Manifest fee, transport, cancelation...
                    res = database.queryOrderForParams(pk_po_no);
                    double stop_fees = 0, cancelation_fee = 0, manifest_fee = 0;
                    String trans_dur = "", trans_type = "";
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            stop_fees       = res.results.getDouble(Tables.Order_Stop_Fees.Alias.Stop_fees);
                            cancelation_fee = res.results.getDouble(Tables.Order.Alias.Cancel_fee);
                            manifest_fee    = res.results.getDouble(Tables.Order.Column.Manifest_fee);
                            trans_dur       = res.results.getString(Tables.Order.Column.Transport_duration);
                            trans_type      = res.results.getString(Tables.Order.Column.Transport_type);
                        }
                    } else {
                        System.out.println("Objects: failed to query order pricing data");
                        // alertDialog("Failed to query order pricing data.");
                    }

                    if(cancelation_fee != 0) {
                        model.addRow(new Object[] { null, "Cancelation Fee", null, null, dollar(cancelation_fee), null, "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = Subtype.NONE;
                        pk.po_no = pk_po_no;
                        pk.quote = "Cancelation Fee";
                        pk_list.add(pk);
                    }
                    if(stop_fees != 0) {
                        model.addRow(new Object[] { null, "Stop Fees", null, null, dollar(stop_fees), null, "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = Subtype.NONE;
                        pk.po_no = pk_po_no;
                        pk.quote = "Stop Fees";
                        pk_list.add(pk);
                    }
                    if(manifest_fee != 0) {
                        model.addRow(new Object[] { null, "Manifest Fee", null, null, dollar(manifest_fee), null, "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = Subtype.NONE;
                        pk.po_no = pk_po_no;
                        pk.quote = "Manifest Fee";
                        pk_list.add(pk);
                    }
                    if(trans_type != null) {
                        model.addRow(new Object[] { null, "Transport: " + trans_type, null, null, null, null, "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = Subtype.NONE;
                        pk.po_no = pk_po_no;
                        pk.quote = "Trans Type";
                        pk_list.add(pk);
                    }
                    if(trans_dur != null) {
                        model.addRow(new Object[] { null, "Trans Duration: " + trans_dur, null, null, null, null, "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = Subtype.NONE;
                        pk.po_no = pk_po_no;
                        pk.quote = "Trans Dur";
                        pk_list.add(pk);
                    }
                    
                    // Add fees to total
                    total_price += stop_fees;
                    total_price += cancelation_fee;
                    total_price += manifest_fee;

                    // Note: Total Cost, Price, and Profit could be done via query
                    model.addRow(new Object[] { null, "", null, null, null, null, "" }); // Blank row
                    model.addRow(new Object[] { null, "Total", null, dollar(total_price), null, dollar(total_cost), "" });
                    model.addRow(new Object[] { null, "Profit", null, null, null, dollar(total_price-total_cost), "" });
                }

                table.setModel(model);

                table.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent me) {
                        if (me.getClickCount() == 2) { // Double click
                            JTable target = (JTable)me.getSource();
                            int row = target.getSelectedRow();

                            // Optional Implementation: DOUBLE CLICK ON COLUMN TO MODIFY
                            if(row != -1 && pk_list.get(row).subtype != Subtype.NONE) { // Using short-circuit
                                // Double clicking gives option to delete
                                PK_Obj_Sub obj = pk_list.get(row);
                                switch(confirmDialog("Are you sure to delete?", "Confirm", "Cancel")) {
                                    case JOptionPane.YES_OPTION:
                                        if(database.updateDeleteObject(obj.po_no, obj.quote).success) {
                                            // setScreen(Screens.ORDER_SCREEN_VIEW, param);
                                            // window.setScreen(new OrderScreen(param));
                                            window.setScreen(new OrderScreen());
                                            System.out.println("set screen ORDER_SCREEN_VIEW");
                                        } else {
                                            System.out.println("Cannot delete object");
                                            // alertDialog("Cannot delete object.");
                                        }
                                        break;
                                }
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
     * confirmDialog
     * Prompts the user of a message and an option
     * to select yes or no.
     */
    int confirmDialog(String msg, String yes, String no) {
        return JOptionPane.showOptionDialog(
                window,
                "Are you sure you want to delete?",
                "Confirm Dialog",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[] { yes, no },
                no );
    }

    /**
     * dollar
     * Helper function to display two decimals
     */
    String dollar(double value) {
        return String.format("%.2f", value);
    }

    public static enum Subtype {
        WASTE,
        MATERIAL,
        LABOR,
        EQUIPMENT,
        NONE
    }

    /**
     * Primary Key holder for sublcasses of Object
     * (Waste, Material, Labor, Equipment) used
     * for ObjectTable
     * 
     */
    class PK_Obj_Sub {
        public Subtype subtype;
        public String po_no;
        public String quote;
    }

    /**
     * OrderScreenView
     * Allows the user to modify an existing Order object.
     */
    // class OrderScreenView extends JPanel { //(Pricing)
    //     JPanel companyPanel, joinPanel, modBtnPanel;
    //     JButton backBtn, saveBtn, addStopBtn, cancelOrderBtn, addObjectBtn;

    //     public OrderScreenView(ScreenData_OrderScreen param) {
    //         super("Orders");

    //         modBtnPanel = new JPanel();
    //         modBtnPanel.setBorder(new EmptyBorder(0,0,0,0));
    //         modBtnPanel.setLayout(new BoxLayout(modBtnPanel, BoxLayout.Y_AXIS));

    //         cancelOrderBtn = new JButton("Cancel Order");
    //         cancelOrderBtn.addActionListener(e -> {
    //             // dialog to verify and enter fee
    //             cancelOrderDialog(param.po_no);
    //         });

    //         addStopBtn = new JButton("Add Stop");
    //         addStopBtn.addActionListener(e -> {
    //             // dialog to enter stop no and fee
    //             addStopToOrder(param.po_no);
    //         });

    //         // Object would go here...

    //         modBtnPanel.add(cancelOrderBtn);
    //         modBtnPanel.add(Box.createVerticalStrut(10));
    //         modBtnPanel.add(addStopBtn);

    //         companyPanel = new OrderCompanyViewPanel(param);
    //         companyPanel.add(Box.createHorizontalStrut(250));
    //         companyPanel.add(modBtnPanel);

    //         joinPanel = new JPanel();
    //         joinPanel.setBorder(new EmptyBorder(0,0,0,0));
    //         joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));

    //         joinPanel.add(companyPanel);
    //         joinPanel.add(Box.createVerticalStrut(15));
    //         joinPanel.add(new ObjectTable(param));

    //         this.add(joinPanel);

    //         saveBtn = new JButton("Save");
    //         saveBtn.addActionListener(e -> {

    //         });

    //         backBtn = new JButton("Back");
    //         backBtn.addActionListener(e -> {
    //             ScreenData_SearchScreen blank_param = new ScreenData_SearchScreen();
    //             blank_param.searchTerm = "";
    //             // setScreen(Screens.SEARCH_SCREEN, blank_param);
    //             // window.setScreen(new SearchScreen(blank_param));
    //             window.back();
    //         });
    //     }
    // }

}
