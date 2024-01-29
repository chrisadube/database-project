/**
 * ObjectTable
 * JTable to be populated with Object data.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Insets.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ObjectTable extends CustTable {
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
            QueryResult res;
            if(!pk_po_no.equals("")) {
                // join: OBJECT , WASTE
                res = queryOrderForObjects_Waste(pk_po_no);
                
                if(res.success) {
                    while(res.results.next()) {
                        // Get what to display
                        int quantity    = res.results.getInt(TObject.Column.Quantity);
                        String quote    = res.results.getString(TObject.Column.Quote);
                        double price    = res.results.getDouble(TObject.Column.Price);
                        double t_price  = quantity * price; // Query instead?
                        double cost     = res.results.getDouble(TObject.Column.Cost);
                        double t_cost   = quantity * cost;
                        total_price     += t_price;
                        total_cost      += t_cost;
                        String price_code = res.results.getString(TWaste.Column.ACV_Code);
                        
                        model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), price_code });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = PK_Obj_Sub.Subtype.WASTE;
                        pk.po_no = pk_po_no;
                        pk.quote = quote;
                        pk_list.add(pk);
                    }
                } else {
                    alertDialog_Data("object data.");
                }
                
                // join: OBJECT , MATERIAL
                res = queryOrderForObjects_Material(pk_po_no);
                if(res.success) {
                    while(res.results.next()) {
                        // Get what to display
                        int quantity    = res.results.getInt(TObject.Column.Quantity);
                        String quote    = res.results.getString(TObject.Column.Quote);
                        double price    = res.results.getDouble(TObject.Column.Price);
                        double t_price  = quantity * price;
                        double cost     = res.results.getDouble(TObject.Column.Cost);
                        double t_cost   = quantity * cost;
                        total_price     += t_price;
                        total_cost      += t_cost;
                        
                        model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = PK_Obj_Sub.Subtype.MATERIAL;
                        pk.po_no = pk_po_no;
                        pk.quote = quote;
                        pk_list.add(pk);
                    }
                } else {
                    alertDialog_Data("material data.");
                }
                
                // join: OBJECT , EQUIPMENT
                res = queryOrderForObjects_Equipment(pk_po_no);
                if(res.success) {
                    while(res.results.next()) {
                        // Get what to display
                        int quantity    = res.results.getInt(TObject.Column.Quantity);
                        String quote    = res.results.getString(TObject.Column.Quote);
                        double price    = res.results.getDouble(TObject.Column.Price);
                        double t_price  = quantity * price;
                        double cost     = res.results.getDouble(TObject.Column.Cost);
                        double t_cost   = quantity * cost;
                        total_price     += t_price;
                        total_cost      += t_cost;
                        
                        model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = PK_Obj_Sub.Subtype.EQUIPMENT;
                        pk.po_no = pk_po_no;
                        pk.quote = quote;
                        pk_list.add(pk);
                    }
                } else {
                    alertDialog_Data("equipment data.");
                }
                
                // join: OBJECT , LABOR
                res = queryOrderForObjects_Labor(pk_po_no);
                if(res.success) {
                    while(res.results.next()) {
                        // Get what to display
                        int quantity    = res.results.getInt(TObject.Column.Quantity);
                        String quote    = res.results.getString(TObject.Column.Quote);
                        double price    = res.results.getDouble(TObject.Column.Price);
                        double t_price  = quantity * price;
                        double cost     = res.results.getDouble(TObject.Column.Cost);
                        double t_cost   = quantity * cost;
                        total_price     += t_price;
                        total_cost      += t_cost;
                                            
                        model.addRow(new Object[] { quantity, quote, dollar(price), dollar(t_price), dollar(cost), dollar(t_cost), "" });
                        PK_Obj_Sub pk = new PK_Obj_Sub();
                        pk.subtype = PK_Obj_Sub.Subtype.LABOR;
                        pk.po_no = pk_po_no;
                        pk.quote = quote;
                        pk_list.add(pk);
                    }
                } else {
                    alertDialog_Data("labor data.");
                }
                
                // Query ORDER to get Manifest fee, transport, cancelation...
                res = queryOrderForParams(pk_po_no);
                double stop_fees = 0, cancelation_fee = 0, manifest_fee = 0;
                String trans_dur = "", trans_type = "";
                if(res.success) {
                    while(res.results.next()) {
                        // Get what to display
                        stop_fees       = res.results.getDouble(TOrder_Stop_Fees.Alias.Stop_fees);
                        cancelation_fee = res.results.getDouble(TOrder.Alias.Cancel_fee);
                        manifest_fee    = res.results.getDouble(TOrder.Column.Manifest_fee);
                        trans_dur       = res.results.getString(TOrder.Column.Transport_duration);
                        trans_type      = res.results.getString(TOrder.Column.Transport_type);
                    }
                } else {
                    alertDialog("Failed to query order pricing data.");
                }

                if(cancelation_fee != 0) {
                    model.addRow(new Object[] { null, "Cancelation Fee", null, null, dollar(cancelation_fee), null, "" });
                    PK_Obj_Sub pk = new PK_Obj_Sub();
                    pk.subtype = PK_Obj_Sub.Subtype.NONE;
                    pk.po_no = pk_po_no;
                    pk.quote = "Cancelation Fee";
                    pk_list.add(pk);
                }
                if(stop_fees != 0) {
                    model.addRow(new Object[] { null, "Stop Fees", null, null, dollar(stop_fees), null, "" });
                    PK_Obj_Sub pk = new PK_Obj_Sub();
                    pk.subtype = PK_Obj_Sub.Subtype.NONE;
                    pk.po_no = pk_po_no;
                    pk.quote = "Stop Fees";
                    pk_list.add(pk);
                }
                if(manifest_fee != 0) {
                    model.addRow(new Object[] { null, "Manifest Fee", null, null, dollar(manifest_fee), null, "" });
                    PK_Obj_Sub pk = new PK_Obj_Sub();
                    pk.subtype = PK_Obj_Sub.Subtype.NONE;
                    pk.po_no = pk_po_no;
                    pk.quote = "Manifest Fee";
                    pk_list.add(pk);
                }
                if(trans_type != null) {
                    model.addRow(new Object[] { null, "Transport: " + trans_type, null, null, null, null, "" });
                    PK_Obj_Sub pk = new PK_Obj_Sub();
                    pk.subtype = PK_Obj_Sub.Subtype.NONE;
                    pk.po_no = pk_po_no;
                    pk.quote = "Trans Type";
                    pk_list.add(pk);
                }
                if(trans_dur != null) {
                    model.addRow(new Object[] { null, "Trans Duration: " + trans_dur, null, null, null, null, "" });
                    PK_Obj_Sub pk = new PK_Obj_Sub();
                    pk.subtype = PK_Obj_Sub.Subtype.NONE;
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
                        if(row != -1 && pk_list.get(row).subtype != PK_Obj_Sub.Subtype.NONE) { // Using short-circuit
                            // Double clicking gives option to delete
                            PK_Obj_Sub obj = pk_list.get(row);
                            switch(confirmDialog("Are you sure to delete?", "Confirm", "Cancel")) {
                                case JOptionPane.YES_OPTION:
                                    if(updateDeleteObject(obj.po_no, obj.quote).success)
                                        setScreen(Screens.ORDER_SCREEN_VIEW, param);
                                    else
                                        alertDialog("Cannot delete object.");
                                    break;
                            }
                        }
                    }
                }
            });

            this.add(scrollPane);
        } catch(Exception e) {
            System.out.println("Exception populating Search Order Table:\n" + e);
            alertDialog("Exception populating data.");
        }            
    }

    /**
     * Primary Key holder for sublcasses of Object
     * (Waste, Material, Labor, Equipment) used
     * for ObjectTable
     * 
     */
    public class PK_Obj_Sub {
        public static enum Subtype {
            WASTE,
            MATERIAL,
            LABOR,
            EQUIPMENT,
            NONE
        }

        public Subtype subtype;
        public String po_no;
        public String quote;
    }

    /**
     * dollar
     * Helper function to display two decimals
     */
    public String dollar(double value) {
        return String.format("%.2f", value);
    }
}
