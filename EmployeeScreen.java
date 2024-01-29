/**
 * EmployeeScreen.java
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
import java.util.ArrayList;

/**
 * EmployeeScreen
 * The base class to creating or modifying an Employee to or
 * from the database. It contains all necessary JComponent
 * objects.
 */
public class EmployeeScreen extends Screen {

    public EmployeeScreen() {
        super("Employees");
        createHeader();

        add(new EmployeeTable(), BorderLayout.CENTER);
        
        ActionListener callCreate = (ActionEvent e) -> {
                openNewEmployeeFormDialog(this);
        };
        createFooter(callCreate, "Create Employee");
    }

    /**
     * openNewEmployeeFormDialog
     */
    private void openNewEmployeeFormDialog(JPanel parent) {
        // Create the dialog
        JDialog newEmployeeDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "New Employee Form", true);
        
        // Create Panel for entry components
        GridLayout entryLayout = new GridLayout(6,2);
        entryLayout.setHgap(10);
        entryLayout.setVgap(10);
        JPanel entryPanel = new JPanel(entryLayout);
        entryPanel.setBorder(new EmptyBorder(20,20,20,20));
        
        JLabel empIdLbl = new JLabel("Employee ID:");
        entryPanel.add(empIdLbl);
        JTextField empIdTxt = new JTextField("", 20);
        entryPanel.add(empIdTxt);

        JLabel ssnLbl = new JLabel("SSN:");
        entryPanel.add(ssnLbl);
        JTextField ssnTxt = new JTextField("", 20);
        entryPanel.add(ssnTxt);

        JLabel fnameLbl = new JLabel("First name:");
        entryPanel.add(fnameLbl);
        JTextField fnameTxt = new JTextField("", 20);
        entryPanel.add(fnameTxt);

        JLabel minitLbl = new JLabel("Middle Init:");
        entryPanel.add(minitLbl);
        JTextField minitTxt = new JTextField("", 20);
        entryPanel.add(minitTxt);

        JLabel lnameLbl = new JLabel("Last name:");
        entryPanel.add(lnameLbl);
        JTextField lnameTxt = new JTextField("", 20);
        entryPanel.add(lnameTxt);

        JLabel salaryLbl = new JLabel("Salary:");
        entryPanel.add(salaryLbl);
        JTextField salaryTxt = new JTextField("", 20);
        entryPanel.add(salaryTxt);

        // Create Panel for button
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton newEmpBtn = new JButton("Create");
        newEmpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Store textfield values into dataset
                try {
                    Tables.Employee.Data data = (new Tables.Employee()).new Data();
                    data.emp_id = empIdTxt.getText();
                    data.ssn = ssnTxt.getText();
                    data.fname = fnameTxt.getText();
                    data.minit = minitTxt.getText();
                    data.lname = lnameTxt.getText();
                    data.salary = Integer.parseInt(salaryTxt.getText());
                    if(database.createEmployee(data).success)
                        window.back();
                    else
                        window.alertDialog(parent, "Failed to create employee.");
                } catch (Exception x) {
                    System.out.println("Cannot convert value." + x);
                    window.alertDialog_Data(parent);
                }
                
                newEmployeeDialog.dispose();
            }
        });
        
        btnPanel.add(newEmpBtn);

        newEmployeeDialog.add(entryPanel);
        newEmployeeDialog.add(btnPanel);

        newEmployeeDialog.setLayout(new BoxLayout(newEmployeeDialog.getContentPane(), BoxLayout.Y_AXIS));

        // Set dialog properties
        newEmployeeDialog.setSize(300, 150);
        newEmployeeDialog.setLocationRelativeTo(this);
        newEmployeeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        newEmployeeDialog.pack();

        // Make the dialog visible
        newEmployeeDialog.setVisible(true);
    }

    class EmployeeTable extends CustTable {
        ArrayList<PK_Employees> pk_list;

        public EmployeeTable() {
            super("Employees", new String[] { "ID", "SSN", "First Name", "Middle Initial", "Last Name", "Salary" });

            pk_list = new ArrayList<PK_Employees>();
            
            try {
                // if(!primary_key_value.equals("")) { // Used for searching
                    Database.QueryResult res = database.queryEmployees();
                    if(res.success) {
                        while(res.results.next()) {
                            // Get what to display
                            String id =     res.results.getString(Tables.Employee.Column.EmpId);
                            String ssn =    res.results.getString(Tables.Employee.Column.SSN);
                            String fname =  res.results.getString(Tables.Employee.Column.Fname);
                            String minit =  res.results.getString(Tables.Employee.Column.Minit);
                            String lname =  res.results.getString(Tables.Employee.Column.Lname);
                            String salary = res.results.getString(Tables.Employee.Column.Salary);

                            model.addRow(new Object[] { id, ssn, fname, minit, lname, salary });
                            PK_Employees pk = new PK_Employees();
                            pk.emp_id =   res.results.getString(Tables.Employee.Column.EmpId);
                            pk_list.add(pk);
                        }
                    } else {
                        System.out.println("Failed to query Employee data.");
                        // window.alertDialog_Data("Failed to query Profile data.");
                    }
                // }

                table.setModel(model);

                this.add(scrollPane);
            } catch(Exception e) {
                System.out.println("Exception populating Employee Table:\n" + e);
                // window.alertDialog("Exception populating data.");
            }
        }
    }

    class PK_Employees {
        public String emp_id;
    }

}
