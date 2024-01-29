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
 * CompanyScreen
 * The base class for Company screen data. Contains all necessary
 * JComponent objects to create or modify a Company object.
 */
public class CompanyScreen extends Screen {
    
    public CompanyScreen() {
        super("Company Screen");
        createHeader();

        add(new CompanyTable(), BorderLayout.CENTER);

        ActionListener callCreate = (ActionEvent e) -> {
            openNewCompanyFormDialog(this);
        };
        createFooter(callCreate, "Create Company");
    }

    /**
     * openNewCompanyFormDialog
     */
    private void openNewCompanyFormDialog(JPanel parent) {
        // Create the dialog
        JDialog newCompanyDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Company Form", true);
        
        // Create Panel for entry components
        GridLayout entryLayout = new GridLayout(6,2);
        entryLayout.setHgap(10);
        entryLayout.setVgap(10);
        JPanel entryPanel = new JPanel(entryLayout);
        entryPanel.setBorder(new EmptyBorder(20,20,20,20));
        
        JLabel nameLbl = new JLabel("Company Name:");
        entryPanel.add(nameLbl);
        JTextField nameTxt = new JTextField("", 20);
        entryPanel.add(nameTxt);

        JLabel addressLbl = new JLabel("Address:");
        entryPanel.add(addressLbl);
        JTextField addressTxt = new JTextField("", 20);
        entryPanel.add(addressTxt);

        JLabel contactLbl = new JLabel("Contact:");
        entryPanel.add(contactLbl);
        JTextField contactTxt = new JTextField("", 20);
        entryPanel.add(contactTxt);

        JLabel phoneLbl = new JLabel("Phone:");
        entryPanel.add(phoneLbl);
        JTextField phoneTxt = new JTextField("", 20);
        entryPanel.add(phoneTxt);

        JLabel epaidLbl = new JLabel("EPA ID:");
        entryPanel.add(epaidLbl);
        JTextField epaidTxt = new JTextField("", 20);
        entryPanel.add(epaidTxt);

        JLabel genidLbl = new JLabel("Generator ID:");
        entryPanel.add(genidLbl);
        JTextField genidTxt = new JTextField("", 20);
        entryPanel.add(genidTxt);

        // Create Panel for button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton newCompanyBtn = new JButton("Create");
        newCompanyBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Store textfield values into dataset
                try {
                    Tables.Company.Data data = (new Tables.Company()).new Data();
                    data.name = nameTxt.getText();
                    data.address = addressTxt.getText();
                    data.contact = contactTxt.getText();
                    data.epa_id = epaidTxt.getText();
                    data.gen_id = genidTxt.getText();

                    if(database.createCompany(data).success) {
                        // Handle phone numbers
                        Tables.Company_Phone.Data phoneData = (new Tables.Company_Phone()).new Data();
                        phoneData.company_name = data.name;
    
                        for(String phone : phoneTxt.getText().split(",")) {
                            if(!phone.equals("")) {
                                phoneData.phone = phone;
                                if(!database.createCompanyPhone(phoneData).success) {
                                    System.out.println("Error creating company phones");
                                    window.alertDialog_Data(parent, "Failed to create employee phone." + phone);
                                }
                            }
                        }
                        window.back();
                    }
                    else {
                        window.alertDialog(parent, "Failed to create company.");
                    }
                } catch (Exception x) {
                    System.out.println("Cannot convert value." + x);
                    // window.alertDialog_Data(parent, "error");
                }
                
                newCompanyDialog.dispose();
            }
        });
        
        btnPanel.add(newCompanyBtn);

        newCompanyDialog.add(entryPanel);
        newCompanyDialog.add(btnPanel);

        newCompanyDialog.setLayout(new BoxLayout(newCompanyDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Set dialog properties
        newCompanyDialog.setSize(300, 150);
        newCompanyDialog.setLocationRelativeTo(this);
        newCompanyDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newCompanyDialog.pack();

        // Make the dialog visible
        newCompanyDialog.setVisible(true);
    }

    private void openUpdateCompanyFormDialog(JPanel parent) {

    }

    class CompanyTable extends CustTable {
        ArrayList<PK_Company> pk_list;

        public CompanyTable() {
            super("Company", new String[] { "Name", "Address", "Contact", "Phone", "EPA ID", "Gen ID" });

            pk_list = new ArrayList<PK_Company>();
            
            try {
                // if(!primary_key_value.equals("")) { // Used for searching
                    Database.QueryResult res = database.queryCompanies();
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            String name =     res.results.getString(Tables.Company.Column.Name);
                            String address =    res.results.getString(Tables.Company.Column.Address);
                            String contact =  res.results.getString(Tables.Company.Column.Contact);
                            // String  =  res.results.getString(Tables.Company.Column.Minit);
                            String epa_id =  res.results.getString(Tables.Company.Column.EPA_IP_no);
                            String gen_id = res.results.getString(Tables.Company.Column.Generator_no);

                            model.addRow(new Object[] { name, address, contact, epa_id, gen_id });
                            PK_Company pk = new PK_Company();
                            pk.name =   res.results.getString(Tables.Company.Column.Name);
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Failed to query Company data.");
                        // window.alertDialog_Data("Failed to query Profile data.");
                    }
                // }

                table.setModel(model);

                this.add(scrollPane);
            } catch(Exception e) {
                System.out.println("Exception populating Company Table:\n" + e);
                // window.alertDialog("Exception populating data.");
            }
        }
    }

    class PK_Company {
        public String name;
    }
}

/**
 * CompanyScreenCreate
 * Subclass of CompanyScreen that allows for Company object
 * creation.
 */
// public class CompanyScreenCreate extends CompanyScreen {
//     JButton createBtn;
    
//     public CompanyScreenCreate() {
//         createBtn = new JButton("Create");
//         createBtn.addActionListener(e -> {
//             // Store textfield values into dataset
//             try {
//                 TCompany.Data data = new TCompany().new Data();
//                 data.name = nameTxt.getText();
//                 data.address = addressTxt.getText();
//                 data.contact = contactTxt.getText();
//                 data.epa_id = epaIDTxt.getText();
//                 data.gen_id = genIDTxt.getText();

//                 if(createCompany(data).success) {
//                     // Handle phone numbers
//                     TCompany_Phone.Data phoneData= new TCompany_Phone().new Data();
//                     phoneData.company_name = data.name;

//                     for(String phone : phoneTxt.getText().split(",")) {
//                         if(!phone.equals("")) {
//                             phoneData.phone = phone;
//                             if(!createCompanyPhone(phoneData).success)
//                                 alertDialog_Data(phone);
//                         }
//                     }
//                     setScreen(Screens.HOME_SCREEN);
//                 }
//                 else {
//                     alertDialog("Failed to create company.");
//                 }
//             } catch(Exception x) {
//                 System.out.println("Invalid data entry.");
//                 // alertDialog_Data();
//             }
//         });
        
//         this.footer.add(createBtn);
//         this.footer.glue();
//     }
// }

/**
 * Subclass of CompanyScreen object that allows for Company
 * object modification.
 */
// public class CompanyScreenUpdate extends CompanyScreen {
//     JButton updateBtn;
    
//     public CompanyScreenUpdate() {
//         updateBtn = new JButton("Update");
//         updateBtn.addActionListener(e -> {
//             TCompany.Data data = new TCompany().new Data();
//             if(updateCompany(data).success)
//                 setScreen(Screens.HOME_SCREEN);
//             else
//                 alertDialog("Failed to update company.");
//         });
        
//         this.footer.add(updateBtn);
//         this.footer.glue();
//     }
// }
