/**
 * 
 */

import java.util.ArrayList;

public class Tables {
    /**
     * The TABLE, DATA, AND COLUMN SET CLASSES section contains a set of
     * classes that each hold three or four forms of data. First, each
     * class holds the table name that they refer to. Second, each hold
     * a static class 'Column' that holds each of the column names as
     * described in the database. Next, the Data class holds temporary
     * data regarding the current instance being used. That could be
     * transfering user input to the database, or querying data from
     * the database to be displayed on a screen. Lastly, which is
     * dependent on constructed queries, is the Alias class that holds
     * the name of an alias used as temporary tables after a join or
     * query has taken place.
     */

    public static class Employee implements DBTable{
        public final static String table_name = "EMP";
        
        public static class Column {
            public final static String EmpId           = "Emp_ID";
            public final static String SSN             = "SSN";
            public final static String Fname           = "Fname";
            public final static String Minit           = "Minit";
            public final static String Lname           = "Lname";
            public final static String Salary          = "Salary";
        }

        public class Data implements ColumnData {
            public String emp_id; // PK
            public String ssn;
            public String fname;
            public String minit;
            public String lname;
            public int salary;
        }
    }

    public static class Company implements DBTable {
        public final static String table_name = "COMPANY";
        
        public static class Column implements ColumnName {
            public final static String Name               = "Name";
            public final static String Address            = "Address";
            public final static String Contact            = "Contact";
            public final static String Fax                = "Fax";
            public final static String EPA_IP_no          = "EPA_IP_no";
            public final static String Generator_no       = "Generator_no";
        }

        public class Data implements ColumnData {
            public String name; // PK
            public String address;
            public String contact;
            public String fax;
            public String epa_id;
            public String gen_id;
            
            // Update table COMPANY_PHONE
            public ArrayList<String> phone;
            
            public Data() { // Not for processing new updates
                phone = new ArrayList<String>();
            }
        }
    }

    public static class Company_Phone implements DBTable {
        public final static String table_name = "COMPANY_PHONE";

        public static class Column implements ColumnName {
            public final static String Name         = "Name";
            public final static String Phone_number = "Phone_number";
        }

        public static class Alias {
            public final static String Phone_numbers = "phone_numbers";
        }

        public class Data implements ColumnData {
            public String company_name; // PK
            public String phone; // PK
        }
    }

    public static class ACV_Enviro implements DBTable {
        public final static String table_name = "ACV_ENVIRO";

        public static class Column implements ColumnName {
            public final static String Code            = "Code";
            public final static String UOM             = "UOM";
            public final static String Com             = "Com";
            public final static String Product         = "Product";
            public final static String Disposal_method = "Disposal_method";
            public final static String Comments        = "Comments";
        }

        public class Data implements ColumnData {
            public String code; // PK
            public String uom; // PK
            public double com;
            public String product;
            public String disposal_method;
            public String comments;
        }
    }

    public static class Disposal_Profile implements DBTable {
        public final static String table_name = "DISPOSAL_PROFILE";

        public static class Column implements ColumnName {
            public final static String ACV_Code          = "ACV_Code";
            public final static String WSN_ID            = "WSN_ID";
            public final static String WasteStreamName   = "WasteStreamName";
            public final static String Company_ID        = "Company_ID";
            public final static String Disposal_Date     = "Disposal_Date";
            public final static String Shipping_info     = "Shipping_info";
        }

        public static class Alias {
            public final static String Profile_Number = "profile_number";
        }

        public class Data implements ColumnData {
            public String acv_code; // PK
            public String wsn_id; // PK
            public String wastestreamname;
            public String company_id;
            public String disposal_date;
            public String shipping_info;

            // Update table WASTE_CODE
            public ArrayList<String> waste_code;

            public Data() {
                waste_code = new ArrayList<String>();
            }
        }
    }

    public static class TObject implements DBTable {
        public final static String table_name = "OBJECT";

        public static class Column implements ColumnName {
            public final static String Quote       = "Quote";
            public final static String PO_no       = "PO_no_2";
            public final static String Quantity    = "Quantity";
            public final static String Price       = "Price";
            public final static String Cost        = "Cost";
        }
        
        public class Data implements ColumnData {
            // Update table OBJECT
            public String quote; // PK
            public String po_no; // PK
            public int quantity;
            public double price;
            public double cost;
        }
    }

    public static class Waste extends TObject {
        public final static String table_name = "WASTE";

        public static class Column implements ColumnName {
            public final static String Quote                = "Quote_W";
            public final static String ACV_Code             = "ACV_Code";
            public final static String UOM                  = "UOM_1";
        }

        public class Data extends TObject.Data implements ColumnData {
            public String acv_code;
            public String uom;
        }
    }

    public static class Material extends TObject {
        public final static String table_name = "MATERIAL";
        
        public static class Column implements ColumnName {
            public final static String Quote     = "Quote_M";
            public final static String Name      = "Name";
        }

        public class Data extends TObject.Data implements ColumnData {
            public String name;
        }
    }

    public static class Labor extends TObject {
        public final static String table_name = "LABOR";
        
        public static class Column implements ColumnName {
            public final static String Quote       = "Quote_L";
            public final static String Position    = "Position";
        }

        public class Data extends TObject.Data implements ColumnData {
            public String position;
        }
    }

    public static class Equipment extends TObject {
        public final static String table_name = "EQUIPMENT";

        public static class Column implements ColumnName {
            public final static String Quote        = "Quote_E";
            public final static String Name         = "Name";
        }

        public class Data extends TObject.Data implements ColumnData {
            public String name;
        }
    }

    public static class Order implements DBTable {
        public final static String table_name = "`ORDER`";

        public static class Column implements ColumnName {
            public final static String PO_no                    = "PO_no";
            public final static String Company_name             = "Company_name";
            public final static String Description              = "Description";
            public final static String Salesperson              = "Salesperson";
            public final static String Order_date               = "Order_date";
            public final static String Client_billing_address   = "Client_billing_address";
            public final static String Transport_type           = "Transport_type";
            public final static String Transport_duration       = "Transport_duration";
            public final static String Manifest_fee             = "Manifest_fee";
            public final static String Canceled                 = "Canceled";
            public final static String Cancelation_fee          = "Cancelation_fee";
        }

        public static class Alias {
            public final static String Cancel_fee = "cancel_fee";
        }
        
        public class Data implements ColumnData {
            public String po_no; // PK
            public String company_name;
            public String description;
            public String salesperson;
            public String order_date;
            public String client_billing_address;
            public String transport_type;
            public String transport_duration;
            public double manifest_fee;
            public boolean canceled;
            public double cancelation_fee;
            
            // Update table ORDER_STOP_FEES
            public ArrayList<Integer> stop_no;
            public ArrayList<Double> stop_fee;
            
            public Data() {
                stop_no = new ArrayList<Integer>();
                stop_fee = new ArrayList<Double>();
            }
        }
    }

    public static class Order_Stop_Fees implements DBTable {
        public final static String table_name = "ORDER_STOP_FEES";

        public static class Column implements ColumnName {
            public final static String PO_no       = "PO_no_1";
            public final static String Stop_no     = "Stop_no";
            public final static String Stop_fee    = "Stop_fee";
        }

        public static class Alias {
            public final static String Stop_fees = "stop_fees";
        }

        public class Data implements ColumnData {
            public String po_no; // PK
            public int stop_no; // PK
            public double stop_fee;
        }
    }

    public static class Picksup_Emp_Po implements DBTable {
        public final static String table_name = "PICKSUP_EMP_PO";

        public static class Column implements ColumnName {
            public final static String Emp_ID      = "Emp_ID_2";
            public final static String PO_ID       = "PO_ID_3";
        }

        public class Data implements ColumnData {
            public String emp_id; // PK
            public String po_id; // PK
        }
    }

    public static class Ships_Emp_DP implements DBTable {
        public final static String table_name = "SHIPS_EMP_DP";

        public static class Column implements ColumnName {
            public final static String Emp_ID        = "Emp_ID_3";
            public final static String DispProf_Code = "DispProf_Code";
            public final static String DispProf_WSN  = "DispProf_WSN";
        }

        public class Data implements ColumnData {
            public String emp_id; // PK
            public String dispprof_code; // PK
            public String dispprof_wsn; // PK
        }
    }


    public static class Waste_Characteristic implements DBTable {
        public final static String table_name = "WASTE_CHARACTERISTIC";

        public static class Column implements ColumnName {
            public final static String EPA_no         = "EPA_no";
            public final static String Description    = "Description";
        }

        public class Data implements ColumnData {
            public String epa_no; // PK
            public String description;
        }
    }

    public static class Waste_Code implements DBTable {
        public final static String table_name = "WASTE_CODE";

        public static class Column implements ColumnName {
            public final static String DispProf_Code   = "DispProf_Code";
            public final static String DispProf_WSN    = "DispProf_WSN";
            public final static String Waste_code      = "Waste_code";
        }

        public static class Alias {
            public final static String Waste_Codes = "waste_codes";
        }

        public class Data implements ColumnData {
            public String dispprof_code; // PK
            public String dispprof_wsn; // PK
            public String waste_code; // PK
        }
    }

    public static class Waste_Char_Lookup implements DBTable { // Currently not used
        public final static String table_name = "WASTE_CHAR_LOOKUP";

        public static class Column implements ColumnName {
            public final static String DispProf_Code    = "DispProf_Code";
            public final static String DispProf_WSN     = "DispProf_WSN";
            public final static String WC_EPA           = "WC_EPA";
        }

        public class Data implements ColumnData {
            public String dispprof_code; // PK
            public String dispprof_wsn; // PK
            public String wc_epa; // PK
        }
    }
    
}
