/**
 * 
 */

import java.sql.*;

public class Database {
    private static Database db_instance = null;
    DatabaseProfile profile;
    DatabaseCredentials credentials;
    Connection connection;
    boolean showExceptions = true;
    
    public Database() {}

    public static synchronized Database getDatabase() {
        if (db_instance == null)
            db_instance = new Database();
        return db_instance;
    }

    public void setProfile(DatabaseProfile profile) { this.profile = profile; }
    public void setCredentials(DatabaseCredentials credentials) { this.credentials = credentials; }
    public DatabaseProfile getProfile() { return profile; }
    public DatabaseCredentials getCredentials() { return credentials; }

    public boolean connect()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://" +
                profile.toString(),
                credentials.getUsername(),
                credentials.getPassword());
        } catch (Exception e) {
            System.out.println("SQL Connection Exception " + e);
            return false;
        }
        return true;
    }


    /**
     * QueryResult, UpdateResult
     * Structure-like classes to contain ResultSet and result boolean data.
     */
    public class QueryResult {
        public ResultSet results;
        public boolean success;
        public SQLException exception;
    }

    public class UpdateResult {
        public boolean success;
        public SQLException exception;
    }

    /**
     * execUpdate
     * Handles general insertion and update commands.
     * Handles the Statement and Exception objects.
     */
    UpdateResult execUpdate(String query) {
        UpdateResult result = new UpdateResult();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            result.success = true;
        } catch (SQLException e) {
            if(showExceptions)
                System.out.println("SQL Update Exception: Cannot update\n>" + query + "\n" + e);
            result.exception = e;
        }
        return result;
    }
    
    /**
    * execQuery
    * Handles general query commands that return ResetSet objects.
    * Handles Exception objects.
    */
    QueryResult execQuery(String query) {
        QueryResult result = new QueryResult();
        try {
            Statement statement = connection.createStatement();
            result.results = statement.executeQuery(query);
            result.success = true;
        } catch (SQLException e) {
            if(showExceptions)
                System.out.println("SQL Query Exception: Cannot query\n>" + query + "\n" + e);
            result.exception = e;
            result.success = false;
        }
        return result;
    }




    // QUERIES HERE


    /**
     * createEmployee
     * table Employee
     */
    UpdateResult createEmployee(Tables.Employee.Data data) {
        return execUpdate(
            "INSERT INTO " + Tables.Employee.table_name   + " ( " +
                Tables.Employee.Column.EmpId             + ", " +
                Tables.Employee.Column.SSN                + ", " +
                Tables.Employee.Column.Fname              + ", " +
                Tables.Employee.Column.Minit              + ", " +
                Tables.Employee.Column.Lname              + ", " +
                Tables.Employee.Column.Salary             + " ) " +
            "VALUES (" +
                "'" + data.emp_id   + "'," +
                "'" + data.ssn      + "'," +
                "'" + data.fname    + "'," +
                "'" + data.minit    + "'," +
                "'" + data.lname    + "'," +
                "'" + data.salary   + "');"
        );
    }

    /**
     * createCompany
     * table COMPANY
     */
    UpdateResult createCompany(Tables.Company.Data data) {
        // Handle COMPANY table
        return execUpdate(
            "INSERT INTO " + Tables.Company.table_name    + " ( " +
                Tables.Company.Column.Name                + ", " +
                Tables.Company.Column.Address             + ", " +
                Tables.Company.Column.Contact             + ", " +
                Tables.Company.Column.Fax                 + ", " +
                Tables.Company.Column.EPA_IP_no           + ", " +
                Tables.Company.Column.Generator_no        + " ) " +
            "VALUES (" +
                "'" + data.name     + "'," +
                "'" + data.address  + "'," +
                "'" + data.contact  + "'," +
                "'" + data.fax      + "'," +
                "'" + data.epa_id   + "'," +
                "'" + data.gen_id   + "');"
        );
    }

    /**
     * createCompanyPhone
     * table COMPANY_PHONE
     */
    UpdateResult createCompanyPhone(Tables.Company_Phone.Data data) { // 
        // COMPANY_PHONE table
        return execUpdate(
            "INSERT INTO " + Tables.Company_Phone.table_name  + " ( " +
                Tables.Company_Phone.Column.Name              + ", " +
                Tables.Company_Phone.Column.Phone_number      + " ) " +
            "VALUES (" +
                "'" + data.company_name     + "'," +
                "'" + data.phone            + "');"
        );
    }
    
    /**
     * updateCompany
     * table COMPANY
     */
    UpdateResult updateCompany(Tables.Company.Data data) {
        return execUpdate(
            "UPDATE " + Tables.Company.table_name + " " +
            "SET " +    Tables.Company.Column.Name           + "='" + data.name    + "', " +
                        Tables.Company.Column.Address        + "='" + data.address + "', " +
                        Tables.Company.Column.Contact        + "='" + data.contact + "', " +
                        Tables.Company.Column.Fax            + "='" + data.fax     + "', " +
                        Tables.Company.Column.EPA_IP_no      + "='" + data.epa_id  + "', " +
                        Tables.Company.Column.Generator_no   + "='" + data.gen_id  + "' " +
            "WHERE " + Tables.Company.Column.Name + "='" + data.name + "';"
        );
    }

    /**
     * createDisposalProfile
     * table DISPOSAL_PROFILE
     */
    UpdateResult createDisposalProfile(Tables.Disposal_Profile.Data data) {
        // Update table DISPOSAL_PROFILE
        return execUpdate(
            "INSERT INTO " + Tables.Disposal_Profile.table_name   + " ( " +
                Tables.Disposal_Profile.Column.ACV_Code           + ", " +
                Tables.Disposal_Profile.Column.WSN_ID             + ", " +
                Tables.Disposal_Profile.Column.WasteStreamName    + ", " +
                Tables.Disposal_Profile.Column.Company_ID         + ", " +
                Tables.Disposal_Profile.Column.Disposal_Date      + ", " +
                Tables.Disposal_Profile.Column.Shipping_info      + " ) " +
            "VALUES (" +
                "'" + data.acv_code             + "', " +
                "'" + data.wsn_id               + "', " +
                "'" + data.wastestreamname      + "', " +
                "'" + data.company_id           + "', " +
                "'" + data.disposal_date        + "', " +
                "'" + data.shipping_info        + "');"
        );
    }

    /**
     * createWasteCode
     * table WASTE_CODE
     */
    UpdateResult createWasteCode(Tables.Waste_Code.Data data) {
        return execUpdate(
            "INSERT INTO " + Tables.Waste_Code.table_name + " ( " +
                Tables.Waste_Code.Column.DispProf_Code    + ", " +
                Tables.Waste_Code.Column.DispProf_WSN     + ", " +
                Tables.Waste_Code.Column.Waste_code       + " ) " +
            "VALUES (" +
                "'" + data.dispprof_code  + "', " +
                "'" + data.dispprof_wsn   + "', " +
                "'" + data.waste_code     + "'); "
        );
    }

    /**
     * createWasteCharacteristic
     * table WASTE_CHARACTERSTIC
     */
    UpdateResult createWasteCharacteristic(Tables.Waste_Characteristic.Data data) {
        return execUpdate(
            "INSERT INTO " + Tables.Waste_Characteristic.table_name   + " ( " +
                Tables.Waste_Characteristic.Column.EPA_no             + ", " +
                Tables.Waste_Characteristic.Column.Description        + " ) " +
            "VALUES (" +
                "'" + data.epa_no       + "', " +
                "'" + data.description  + "');"
        );
    }

    /**
     * createOrder
     * table ORDER
     */
    UpdateResult createOrder(Tables.Order.Data data) {
        return execUpdate(
            "INSERT INTO " + Tables.Order.table_name          + " ( " +  
                Tables.Order.Column.PO_no                     + ", " +
                Tables.Order.Column.Company_name              + ", " +
                Tables.Order.Column.Description               + ", " +
                Tables.Order.Column.Salesperson               + ", " +
                Tables.Order.Column.Order_date                + ", " +
                Tables.Order.Column.Client_billing_address    + ", " +
                Tables.Order.Column.Transport_type            + " ) " +
            "VALUES (" +
                "'" + data.po_no                    + "'," +
                "'" + data.company_name             + "'," +
                "'" + data.description              + "'," +
                "'" + data.salesperson              + "'," +
                "'" + data.order_date               + "'," +
                "'" + data.client_billing_address   + "'," +
                "'" + data.transport_type           + "');"
        );
    }

    /**
     * queryEmployees
     * 
     */
    QueryResult queryEmployees()
    {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Employee.table_name + ";"
        );
    }

    /**
     * queryEmployee
     * 
     */
    QueryResult queryEmployee(String emp_id)
    {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Employee.table_name + " " +
            "WHERE " + Tables.Employee.Column.EmpId + "='" + emp_id + "';"
        );
    }

    /**
     * queryCompanies
     */
    QueryResult queryCompanies() {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Company.table_name + ";"
        );
    }

    /**
     * queryCompany
     */
    QueryResult queryCompany(String company_id) {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Company.table_name + " " +
            "WHERE " + Tables.Company.Column.Name + "='" + company_id + "';"
        );
    }

    /**
     * queryCompanyPhone
     */
    QueryResult queryCompanyPhone(String company_id) {
        return execQuery(
            "SELECT GROUP_CONCAT(" + Tables.Company_Phone.Column.Phone_number + " SEPARATOR '\n'" + ") AS '" + Tables.Company_Phone.Alias.Phone_numbers + "' " +
            "FROM " + Tables.Company_Phone.table_name + " " +
            "WHERE " + Tables.Company_Phone.Column.Name + "='" + company_id + "';"
        );
    }

   /**
     * queryProfile
     * 
     */
    QueryResult queryProfiles() {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Disposal_Profile.table_name + ";"
        );
    }

    /**
     * queryProfile
     * 
     */
    QueryResult queryProfile(String acv_code, String wsn_id) {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Disposal_Profile.table_name + " " +
            "WHERE " + Tables.Disposal_Profile.Column.ACV_Code + "='" + acv_code + "' " +
            "AND "   + Tables.Disposal_Profile.Column.WSN_ID + "='" + wsn_id + "';"
        );
    }










    // ----------------------------------------------------------------
    // ------------------- DATABASE QUERYING METHODS ------------------
    // ----------------------------------------------------------------
    
    /**
     * queryOrderForObjects_[Waste,Material,Labor,Equipment]
     */
    QueryResult queryOrderForObjects_Waste(String po_no) {
        return execQuery(
            "SELECT " + Tables.TObject.Column.Quantity + ", " +
                        Tables.TObject.Column.Quote    + ", " +
                        Tables.TObject.Column.Price    + ", " +
                        Tables.TObject.Column.Cost     + ", " +
                        Tables.Waste.Column.ACV_Code  + " " +
            "FROM " + Tables.TObject.table_name + "," + Tables.Waste.table_name + " " +
            "WHERE " + Tables.TObject.Column.Quote + "=" + Tables.Waste.Column.Quote + " " +
            "AND " + Tables.TObject.Column.PO_no + "='" + po_no + "';"
        );
    }

    QueryResult queryOrderForObjects_Material(String po_no) {
        return execQuery(
            "SELECT " + Tables.TObject.Column.Quantity + ", " +
                        Tables.TObject.Column.Quote    + ", " +
                        Tables.TObject.Column.Price    + ", " +
                        Tables.TObject.Column.Cost     + " " +
            "FROM " + Tables.TObject.table_name + "," + Tables.Material.table_name + " " +
            "WHERE " + Tables.TObject.Column.Quote + "=" + Tables.Material.Column.Quote + " " +
            "AND " + Tables.TObject.Column.PO_no + "='" + po_no + "';"
        );
    }
    
    QueryResult queryOrderForObjects_Labor(String po_no) {
        return execQuery(
            "SELECT " + Tables.TObject.Column.Quantity + ", " +
                        Tables.TObject.Column.Quote    + ", " +
                        Tables.TObject.Column.Price    + ", " +
                        Tables.TObject.Column.Cost     + " " +
            "FROM " + Tables.TObject.table_name + "," + Tables.Labor.table_name + " " +
            "WHERE " + Tables.TObject.Column.Quote + "=" + Tables.Labor.Column.Quote + " " +
            "AND " + Tables.TObject.Column.PO_no + "='" + po_no + "';"
        );
    }
    
    QueryResult queryOrderForObjects_Equipment(String po_no) {
        return execQuery(
            "SELECT " + Tables.TObject.Column.Quantity + ", " +
                        Tables.TObject.Column.Quote    + ", " +
                        Tables.TObject.Column.Price    + ", " +
                        Tables.TObject.Column.Cost     + " " +
            "FROM " + Tables.TObject.table_name + "," + Tables.Equipment.table_name + " " +
            "WHERE " + Tables.TObject.Column.Quote + "=" + Tables.Equipment.Column.Quote + " " +
            "AND " + Tables.TObject.Column.PO_no + "='" + po_no + "';"
        );
    }

    /**
     * queryOrderForParams
     * Itemed order independent of objects,
     * but includes stop fees.
     */
    QueryResult queryOrderForParams(String po_no) {
        return execQuery(
            "SELECT " + Tables.Order.Column.Transport_type + ", " + Tables.Order.Column.Transport_duration + ", " + Tables.Order.Column.Manifest_fee + ", " +
                "(SELECT " + Tables.Order.Column.Cancelation_fee + " " +
                " FROM " + Tables.Order.table_name + " " +
                " WHERE " + Tables.Order.Column.PO_no + "='" + po_no + "' " +
                " AND " + Tables.Order.Column.Canceled + "=1) AS '" + Tables.Order.Alias.Cancel_fee + "', " +
                "(SELECT SUM(" + Tables.Order_Stop_Fees.Column.Stop_fee + ") " +
                " FROM " + Tables.Order_Stop_Fees.table_name + " " +
                " WHERE " + Tables.Order_Stop_Fees.Column.PO_no + "='" + po_no + "') AS '" + Tables.Order_Stop_Fees.Alias.Stop_fees + "' " +
                "FROM " + Tables.Order.table_name + " " +
                "WHERE " + Tables.Order.Column.PO_no + "='" + po_no + "';"
        );

        // SELECT Transport_type, Transport_duration, Manifest_fee,
        //     (SELECT Cancelation_fee
        //      FROM `ORDER`
        //      WHERE PO_no='po_no'
        //      AND Canceled=1) AS cancel_fee,
        //      (SELECT SUM(Stop_fee)
        //       FROM ORDER_STOP_FEES
        //       WHERE PO_no='po_no') AS stop_fees
        //     FROM `ORDER`
        //     WHERE PO_no='po_no';
    }

    /**
     * queryCompany_DPs
     */
    QueryResult queryCompany_DPs(String co_name) {
        // Complex query. Took about two hours to solve.
        return execQuery(
            "SELECT " + 
                Tables.Disposal_Profile.Column.ACV_Code       + ", " +
                Tables.Disposal_Profile.Column.WSN_ID         + ", " +
                Tables.Disposal_Profile.Column.Disposal_Date  + ", " +
                "CONCAT(" + Tables.Disposal_Profile.Column.ACV_Code + ", '-', " + Tables.Disposal_Profile.Column.WSN_ID + ") AS '" + Tables.Disposal_Profile.Alias.Profile_Number + "', " +
                Tables.Disposal_Profile.Column.WasteStreamName    + ", " +
                Tables.Disposal_Profile.Column.Shipping_info      + ", " +
                "GROUP_CONCAT(" + Tables.Waste_Code.Column.Waste_code + " SEPARATOR ', ') AS '" + Tables.Waste_Code.Alias.Waste_Codes + "' " +
            "FROM " + Tables.Disposal_Profile.table_name + "," + Tables.Waste_Code.table_name + " " +
            "WHERE " + Tables.Disposal_Profile.Column.Company_ID + "='" + co_name + "' " +
                "AND " + Tables.Disposal_Profile.Column.ACV_Code + "=" + Tables.Waste_Code.Column.DispProf_Code + " " +
                "AND " + Tables.Disposal_Profile.Column.WSN_ID + "=" + Tables.Waste_Code.Column.DispProf_WSN + " " +
            "GROUP BY CONCAT (" + Tables.Disposal_Profile.Column.ACV_Code + ", '-', " + Tables.Disposal_Profile.Column.WSN_ID + ");"
        );
        // SELECT 
        //     Disposal_Date AS 'Date',
        //     CONCAT(ACV_Code, '-', WSN_ID) AS 'Profile Number',
        //     WasteStreamName AS 'Waste Stream Name',
        //     Shipping_info AS 'Shipping Information',
        //     GROUP_CONCAT (Waste_code separator ', ') as 'Waste Codes',
        // FROM DISPOSAL_PROFILE,WASTE_CODE
        // WHERE Company_ID='Joes Transport'
        // AND DISPOSAL_PROFILE.ACV_Code=WASTE_CODE.DispProf_Code
        // AND DISPOSAL_PROFILE.WSN_ID=WASTE_CODE.DispProf.WSN
        // GROUP BY CONCAT(ACV_code, '-', WSN_ID);
    }


    /**
     * queryOrdersPo_no
     * table ORDER
     * Queries and retrieves only the information of the primary key, po_no
     */
    QueryResult queryOrdersPo_no(String po_no) {
        return execQuery(
            "SELECT * " +
            "FROM " + Tables.Order.table_name + " " +
            "WHERE " + Tables.Order.Column.PO_no + "='" + po_no + "';"
        );
    }

    /**
     * querySearchOrders
     * table ORDER
     * Queries and retrieves all Date and Company Names with Company Names like specified term
     */
    QueryResult querySearchOrders(String term) {
        return execQuery(
            "SELECT " + Tables.Order.Column.PO_no         + ", " + 
                        Tables.Order.Column.Order_date    + ", " + 
                        Tables.Order.Column.Company_name  + " " +
            "FROM " + Tables.Order.table_name + " " +
            "WHERE " + Tables.Order.Column.Company_name + " LIKE '%" + term + "%';"
        );
    }
    
    /**
     * querySearchOrders
     * table ORDER
     * Queries and retrieves all Date and Company Names with Company Names like specified term
     */
    QueryResult querySearchProfile(String term) {
        return execQuery(
            "SELECT DISTINCT " + Tables.Disposal_Profile.Column.Company_ID + " " +
            "FROM " + Tables.Disposal_Profile.table_name + " " +
            "WHERE " + Tables.Disposal_Profile.Column.Company_ID + " LIKE '%" + term + "%';"
            );
            // Returns multiple instances of Company_ID (unwanted)
            // "SELECT DISTINCT " + TDisposal_Profile.Column.ACV_Code + ", " + TDisposal_Profile.Column.WSN_ID + ", " + TDisposal_Profile.Column.Company_ID + " " +
    }
    
    
    
    /**
     * queryOrder
     * 
     */
    QueryResult queryOrders() {
        return execQuery("");
    }

    
    // ----------------------------------------------------------------
    // --------------- DATABASE INSERT/UPDATE METHODS -----------------
    // ----------------------------------------------------------------
    
    /**
     * The DATABASE INSERT/UPDATE METHODS section contains all
     * methods used to insert or update tables. They are all void
     * return type and do not query. A method exists for every table.
     * Some methods update a table, then call another method to
     * update a different table. This can also happen iteratively,
     * such as when a table can have a multiple value attribute.
     */

    /**
     * updateCancelOrder
     */
    UpdateResult updateCancelOrder(Tables.Order.Data data) {
        return execUpdate(
            "UPDATE " + Tables.Order.table_name + " " +
            "SET " + Tables.Order.Column.Canceled + "=" + (data.canceled ? 1 : 0) + ", " +
                     Tables.Order.Column.Cancelation_fee + "=" + data.cancelation_fee + " " +
            "WHERE " + Tables.Order.Column.PO_no + "='" + data.po_no + "';"
        );
    }

    /**
     * Object Deletion update command
     * Must delete from OBJECT, not from subclasses
     */
    UpdateResult updateDeleteObject(String po_no, String quote) {
        return execUpdate(
            "DELETE FROM " + Tables.TObject.table_name + " " +
            "WHERE " + Tables.TObject.Column.Quote + "='" + quote + "' " +
            "AND " + Tables.TObject.Column.PO_no + "='" + po_no + "';"
        );
    }

    /**
     * updateEmployee
     * table Emp (EMPLOYEE)
     */
    UpdateResult updateEmployee(Tables.Employee.Data data) {
        return execUpdate(
            "UPDATE " + Tables.Employee.table_name + " " +
            "SET " +    Tables.Employee.Column.SSN      + "='" + data.ssn    + "', " +
                        Tables.Employee.Column.Fname    + "='" + data.fname  + "', " +
                        Tables.Employee.Column.Minit    + "='" + data.minit  + "', " +
                        Tables.Employee.Column.Lname    + "='" + data.lname  + "', " +
                        Tables.Employee.Column.Salary   + "='" + data.salary + "' "  +
            "WHERE " + Tables.Employee.Column.EmpId + "='" + data.emp_id + "';"
        );
    }


    


    /**
     * updateAddStopToOrder
     * table ORDER_STOP_FEES
     */
    UpdateResult updateAddStopToOrder(Tables.Order_Stop_Fees.Data data) {
        // Table ORDER_STOP_FEES
        return execUpdate(
            "INSERT INTO " + Tables.Order_Stop_Fees.table_name    + " ( " +
                Tables.Order_Stop_Fees.Column.PO_no               + ", " +
                Tables.Order_Stop_Fees.Column.Stop_no             + ", " +
                Tables.Order_Stop_Fees.Column.Stop_fee            + " ) " +
            "VALUES (" +
                "'" + data.po_no                + "', " +
                "" + data.stop_no              + ", " +
                "" + data.stop_fee           + ");"
        );
    }
    
    /**
     * createPickup
     * table PICKSUP_EMP_PO
     */
    UpdateResult createPickup(Tables.Picksup_Emp_Po.Data data) {
        // Table PICKSUP_EMP_PO
        return execUpdate(
            "INSERT INTO " + Tables.Picksup_Emp_Po.table_name + " ( " +
                Tables.Picksup_Emp_Po.Column.Emp_ID           + ", " +
                Tables.Picksup_Emp_Po.Column.PO_ID            + " ) " +
            "VALUES ( " +
                "'" + data.emp_id   + "', " +
                "'" + data.po_id    + "' );"
        );
    }

    /**
     * createACV_Enviro
     * table ACV_ENVIRO
     */
    UpdateResult createACV_Enviro(Tables.ACV_Enviro.Data data) {
        return execUpdate(
            "INSERT INTO " + Tables.ACV_Enviro.table_name     + " ( " +
                Tables.ACV_Enviro.Column.Code                 + ", " +
                Tables.ACV_Enviro.Column.UOM                  + ", " +
                Tables.ACV_Enviro.Column.Com                  + ", " +
                Tables.ACV_Enviro.Column.Product              + ", " +
                Tables.ACV_Enviro.Column.Disposal_method      + ", " +
                Tables.ACV_Enviro.Column.Comments             + " ) " +
            "VALUES (" +
                "'" + data.code             + "', " +
                "'" + data.uom              + "', " +
                "'" + data.com              + "', " +
                "'" + data.product          + "', " +
                "'" + data.disposal_method  + "', " +
                "'" + data.comments         + "');"
        );
    }

    /**
     * createObject
     * table OBJECT
     */
    UpdateResult createObject(Tables.TObject.Data data) { // superclass: do not create objects
        // Update table Object
        return execUpdate(
            "INSERT INTO " + Tables.TObject.table_name + " ( " +
                Tables.TObject.Column.Quote            + ", " +
                Tables.TObject.Column.Quantity         + ", " +
                Tables.TObject.Column.PO_no            + ", " +
                Tables.TObject.Column.Price            + ", " +
                Tables.TObject.Column.Cost             + " ) " +
            "VALUES (" +
                "'" + data.quote        + "', " +
                "'" + data.po_no        + "', " +
                "'" + data.quantity     + "', " +
                "'" + data.cost*1.3     + "', " +
                "'" + data.cost         + "');"
        );
    }

    /**
     * createWaste
     * table WASTE
     */
    UpdateResult[] createWaste(Tables.Waste.Data data) {
        UpdateResult[] results = new UpdateResult[2];
        // Update table OBJECT
        results[0] = createObject((Tables.TObject.Data)data);

        // Update table Waste
        
        results[1] = execUpdate(
            "INSERT INTO " + Tables.Waste.table_name  + " ( " + 
                Tables.Waste.Column.Quote             + ", " +
                Tables.Waste.Column.ACV_Code          + ", " +
                Tables.Waste.Column.UOM               + " ) " +
            "VALUES (" +
                "'" + ((Tables.TObject.Data)data).quote    + "', " +
                "'" + data.acv_code + "', " +
                "'" + data.uom      + "');"
        );

        return results;
    }

    /**
     * createMaterial
     * table MATERIAL
     */
    UpdateResult[] createMaterial(Tables.Material.Data data) {
        UpdateResult[] results = new UpdateResult[2];
        // Update table OBJECT
        results[0] = createObject((Tables.TObject.Data)data);

        // Update table MATERIAL
        results[1] = execUpdate(
            "INSERT INTO " + Tables.Material.table_name   + " ( " + 
                Tables.Material.Column.Quote              + ", " +
                Tables.Material.Column.Name               + " ) " +
            "VALUES (" +
                "'" + ((Tables.TObject.Data)data).quote    + "', " +
                "'" + data.name     + "');"
        );

        return results;
    }

    /**
     * createLabor
     * table LABOR
     */
    UpdateResult[] createLabor(Tables.Labor.Data data) {
        UpdateResult[] results = new UpdateResult[2];
        // Update table OBJECT
        results[0] = createObject((Tables.TObject.Data)data);
        
        // Update table LABOR
        results[1] = execUpdate(
            "INSERT INTO " + Tables.Labor.table_name  + " ( " + 
                Tables.Labor.Column.Quote             + ", " +
                Tables.Labor.Column.Position          + " ) " +
            "VALUES (" +
                "'" + ((Tables.TObject.Data)data).quote    + "', " +
                "'" + data.position + "');"
        );

        return results;
    }

    /**
     * createEquipment
     * table EQUIPMENT
     */
    UpdateResult[] createEquipment(Tables.Equipment.Data data) {
        UpdateResult[] results = new UpdateResult[2];
        // Update table OBJECT
        results[0] = createObject((Tables.TObject.Data)data);

        // Update table EQUIPMENT
        results[1] = execUpdate(
            "INSERT INTO " + Tables.Equipment.table_name  + " ( " + 
                Tables.Equipment.Column.Quote             + ", " +
                Tables.Equipment.Column.Name              + " ) " +
            "VALUES (" +
                "'" + ((Tables.TObject.Data)data).quote    + "', " +
                "'" + data.name     + "');"
        );

        return results;
    }

    /**
     * createShipsEmpDP
     * table SHIPS_EMP_DP
     */    
    UpdateResult createShipsEmpDP(Tables.Ships_Emp_DP.Data data) {
        // Update table SHIPS_EMP_DP
        return execUpdate(
            "INSERT INTO " + Tables.Ships_Emp_DP.table_name   + " ( " +
                Tables.Ships_Emp_DP.Column.Emp_ID             + ", " +
                Tables.Ships_Emp_DP.Column.DispProf_Code      + ", " +
                Tables.Ships_Emp_DP.Column.DispProf_WSN       + " ) " +
            "VALUES (" +
                "'" + data.emp_id           + "', " +
                "'" + data.dispprof_code    + "', " +
                "'" + data.dispprof_wsn     + "' );"
        );
    }



}
