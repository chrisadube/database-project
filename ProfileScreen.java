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
 * ProfileScreen
 * 
 */
public class ProfileScreen extends Screen {

    public ProfileScreen() {
        super("Profiles");
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
            openNewProfileFormDialog(this);
        };
        createFooter(callCreate, "Create Profile");
    }

    public ProfileScreen(ScreenData_ProfileScreen param) {
        super("Profiles");
        createHeader();

        add(new ProfileTable(), BorderLayout.WEST);

        ActionListener callCreate = (ActionEvent e) -> {
            openNewProfileFormDialog(this);
        };
        createFooter(callCreate, "Create Profile");
    }

    /**
     * openNewProfileFormDialog
     */
    private void openNewProfileFormDialog(JPanel parent) {
        // Create the dialog
        JDialog newProfileDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Profile Form", true);
        
        // Create Panel for entry components
        GridLayout layout = new GridLayout(7,2);
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(layout);
        layout.setHgap(10);
        layout.setVgap(10);

        JLabel acvCodeLbl = new JLabel("ACV Code:");
        profilePanel.add(acvCodeLbl);
        JTextField acvCodeTxt = new JTextField("", 20);
        profilePanel.add(acvCodeTxt);

        JLabel wsnIdLbl = new JLabel("WSN ID:");
        profilePanel.add(wsnIdLbl);
        JTextField wsnIdTxt = new JTextField("", 20);
        profilePanel.add(wsnIdTxt);

        JLabel wasteStreamNameLbl = new JLabel("Waste Stream Name:");
        profilePanel.add(wasteStreamNameLbl);
        JTextField wasteStreamNameTxt = new JTextField("", 20);
        profilePanel.add(wasteStreamNameTxt);

        JLabel companyLbl = new JLabel("Company Name:");
        profilePanel.add(companyLbl);
        JTextField companyTxt = new JTextField("", 20);
        profilePanel.add(companyTxt);

        JLabel dateLbl = new JLabel("Date (yyyy-mm-dd):");
        profilePanel.add(dateLbl);
        JTextField dateTxt = new JTextField("", 20);
        profilePanel.add(dateTxt);

        JLabel shippingInfoLbl = new JLabel("Shipping Info:");
        profilePanel.add(shippingInfoLbl);
        JTextField shippingInfoTxt = new JTextField("", 20);
        profilePanel.add(shippingInfoTxt);

        JLabel wasteCodesLbl = new JLabel("Waste Codes:");
        profilePanel.add(wasteCodesLbl);
        JTextField wasteCodesTxt = new JTextField("", 20);
        profilePanel.add(wasteCodesTxt);

        profilePanel.setBorder(new EmptyBorder(100,100,100,100));

        // Create Panel for button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton newProfileBtn = new JButton("Create");
        newProfileBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Store textfield values into dataset
                try {
                    Tables.Disposal_Profile.Data data = (new Tables.Disposal_Profile()).new Data();
                    data.acv_code = acvCodeTxt.getText();
                    data.wsn_id = wsnIdTxt.getText();
                    data.wastestreamname = wasteStreamNameTxt.getText();
                    data.company_id = companyTxt.getText();
                    data.disposal_date = dateTxt.getText();
                    data.shipping_info = shippingInfoTxt.getText();
    
                    if(database.createDisposalProfile(data).success) {
                        // Handle waste codes
                        Tables.Waste_Code.Data codeData = (new Tables.Waste_Code()).new Data();
                        codeData.dispprof_code = data.acv_code;
                        codeData.dispprof_wsn = data.wsn_id;
                        
                        for(String code : wasteCodesTxt.getText().split(",")) {
                            if(!code.equals("")) {
                                codeData.waste_code = code;
                                if(!database.createWasteCode(codeData).success) {
                                    System.out.println("Error creating waste codes");
                                    // alertDialog_Data(parent, code);
                                }
                            }
                        }
                        // setScreen(Screens.HOME_SCREEN);
                        window.back();
    
                    }
                    else {
                        window.alertDialog_Data(parent, "in Profile.");
                    }
                } catch(Exception x) {
                    System.out.println("Invalid data entry.");
                    window.alertDialog_Data(parent);
                }
                
                newProfileDialog.dispose();
            }
        });
        
        btnPanel.add(newProfileBtn);

        newProfileDialog.add(profilePanel);
        newProfileDialog.add(btnPanel);

        newProfileDialog.setLayout(new BoxLayout(newProfileDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Set dialog properties
        newProfileDialog.setSize(300, 150);
        newProfileDialog.setLocationRelativeTo(this);
        newProfileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newProfileDialog.pack();

        // Make the dialog visible
        newProfileDialog.setVisible(true);
    }

    class ProfileTable extends CustTable {
        ArrayList<PK_DisposalProfile> pk_list;

        public ProfileTable() {
            super("Profile", new String[] { "Date", "Profile Number", "Waste Stream Name", "Waste Codes", "Shipping Information" });

            pk_list = new ArrayList<PK_DisposalProfile>();
            
            try {
                // if(!primary_key_value.equals("")) { // Used for searching
                    Database.QueryResult res = database.queryProfiles();
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            String date =  res.results.getString(Tables.Disposal_Profile.Column.Disposal_Date);
                            String prof =  res.results.getString(Tables.Disposal_Profile.Alias.Profile_Number);
                            String wsn =   res.results.getString(Tables.Disposal_Profile.Column.WasteStreamName);
                            String codes = res.results.getString(Tables.Waste_Code.Alias.Waste_Codes);
                            String ship =  res.results.getString(Tables.Disposal_Profile.Column.Shipping_info);

                            model.addRow(new Object[] { date, prof, wsn, codes, ship });
                            PK_DisposalProfile pk = new PK_DisposalProfile();
                            pk.acv_code =   res.results.getString(Tables.Disposal_Profile.Column.ACV_Code);
                            pk.wsn_id =     res.results.getString(Tables.Disposal_Profile.Column.WSN_ID);
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Failed to query Profile data.");
                        // window.alertDialog_Data("Failed to query Profile data.");
                    }
                // }

                table.setModel(model);

                this.add(scrollPane);
            } catch(Exception e) {
                System.out.println("Exception populating Profile Table:\n" + e);
                // window.alertDialog("Exception populating data.");
            }
        }
    }

    class PK_DisposalProfile {
        public String acv_code;
        public String wsn_id;
    }
}

/**
 * ProfileScreenView
 * 
 */
// class ProfileScreenView extends Screen {
//     JPanel companyPanel, buttonPanel, joinPanel;
//     JButton backBtn, saveBtn;

//     public ProfileScreenView(ScreenData_ProfileScreen param) {
//         super("Profile");

//         companyPanel = new ProfileCompanyViewPanel(param.company_name);
//         companyPanel.add(Box.createHorizontalStrut(450));

//         joinPanel = new JPanel();
//         joinPanel.setBorder(new EmptyBorder(0,0,0,0));
//         joinPanel.setLayout(new BoxLayout(joinPanel, BoxLayout.Y_AXIS));

//         joinPanel.add(companyPanel);
//         joinPanel.add(Box.createVerticalStrut(15));
//         joinPanel.add(new ProfileTable(param.company_name));

//         this.add(joinPanel);

//         backBtn = new JButton("Back");
//         backBtn.addActionListener(e -> {
//             ScreenData_SearchScreen blank_param = new ScreenData_SearchScreen();
//             blank_param.searchTerm = "";
//             setScreen(Screens.SEARCH_SCREEN, blank_param);
//         });

//         saveBtn = new JButton("Save");
//         saveBtn.addActionListener(e -> {
//         });

//         this.footer.glue();
//         this.footer.add(backBtn);
//         this.footer.strut();
//         this.footer.add(saveBtn);
//         this.footer.glue();
//         this.addFooter();
//     }
// }

/**
 * ProfileCompanyViewPanel
 * Company data view panel for the profile screen
 */
// class ProfileCompanyViewPanel extends JPanel {
//     JPanel p1, p2;
//     String name="", address="", contact="", phones="", epa_id="", gen_id="";

//     // Company Panel
//     public ProfileCompanyViewPanel(String primary_key_value) {
//         try {
//             QueryResult res = queryCompany(primary_key_value);
//             if(res.success) {
//                 while(res.results.next()) {
//                     name =      res.results.getString(TCompany.Column.Name);
//                     address =   res.results.getString(TCompany.Column.Address);
//                     contact =   res.results.getString(TCompany.Column.Contact);
//                     epa_id =    res.results.getString(TCompany.Column.EPA_IP_no);
//                     gen_id =    res.results.getString(TCompany.Column.Generator_no);
//                 }
//             } else {
//                 alertDialog_Data("Failed to query Profile Company data.");
//             }
//             QueryResult res_phone = queryCompanyPhone(primary_key_value);
//             if(res_phone.success) {
//                 while(res_phone.results.next()) {
//                     phones = res_phone.results.getString(TCompany_Phone.Alias.Phone_numbers);
//                 }
//             } else {
//                 alertDialog_Data("Failed to query phone data from Profile Company.");
//             }
//         } catch(Exception e) {
//             System.out.println("Exception populating Profile Company View Panel:\n" + e);
//             alertDialog("Exception populating data.");
//         }
        
//         p1 = new JPanel();
//         p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
//         p2 = new JPanel();
//         p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

//         this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

//         p1.add(new JLabel("Company Name:"));
//         p2.add(new JLabel(name));
//         p1.add(new JLabel("Address:"));
//         p2.add(new JLabel(address));
//         p1.add(new JLabel("Contact:"));
//         p2.add(new JLabel(contact));
//         p1.add(new JLabel("Phone"));
//         p2.add(new JLabel(phones));
//         p1.add(new JLabel("EPA ID #:"));
//         p2.add(new JLabel(epa_id));
//         p1.add(new JLabel("Generator #:"));
//         p2.add(new JLabel(gen_id));

//         this.add(p1);
//         this.add(Box.createRigidArea(new Dimension(10, 0)));
//         this.add(p2);
//     }
// }

/**
 * ProfileTable
 * JTable to be populated with Profile data.
 */
// class ProfileTable extends CustTable {
//     ArrayList<PK_DisposalProfile> pk_list;

//     public ProfileTable(String primary_key_value) {
//         super("Profiles", new String[] { "Date", "Profile Number", "Waste Stream Name",
//         "Waste Codes", "Shipping Information" });

//         pk_list = new ArrayList<PK_DisposalProfile>();

//         try {
//             if(!primary_key_value.equals("")) {
//                 QueryResult res = queryCompany_DPs(primary_key_value);
//                 if(res.success) {
//                     while(res.results.next()) {
//                         // Get what to display
//                         String date =       res.results.getString(Tables.Disposal_Profile.Column.Disposal_Date);
//                         String prof =       res.results.getString(Tables.Disposal_Profile.Alias.Profile_Number);
//                         String wsn =        res.results.getString(Tables.Disposal_Profile.Column.WasteStreamName);
//                         String codes =      res.results.getString(Tables.Waste_Code.Alias.Waste_Codes);
//                         String ship =       res.results.getString(Tables.Disposal_Profile.Column.Shipping_info);
                        
//                         model.addRow(new Object[] { date, prof, wsn, codes, ship });
//                         PK_DisposalProfile pk = new PK_DisposalProfile();
//                         pk.acv_code =   res.results.getString(TDisposal_Profile.Column.ACV_Code);
//                         pk.wsn_id =     res.results.getString(TDisposal_Profile.Column.WSN_ID);
//                         pk_list.add(pk);
//                     }
//                 } else {
//                     alertDialog_Data("Failed to query Profile data.");
//                 }
//             }

//             table.setModel(model);

//             this.add(scrollPane);
//         } catch(Exception e) {
//             System.out.println("Exception populating Search Order Table:\n" + e);
//             alertDialog("Exception populating data.");
//         }
//     }
// }
