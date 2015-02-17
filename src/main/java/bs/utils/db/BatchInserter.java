package bs.utils.db;

import au.com.bytecode.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

/**
 * Created by shahb on 2/14/15.
 */
class BatchInserter implements Callable {

    String inputFileName;
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("d-MMM-YYYY");

    public BatchInserter(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    @Override
    public Object call() throws Exception {
        doInsert();
        return null;
    }

    public void doInsert() {
        {

            Statement sql_statement = null;
            Connection conn = null;

            try {
                Class.forName("oracle.jdbc.OracleDriver");

                String url = ResourceBundle.getBundle("db").getString("db.url");
                String user = ResourceBundle.getBundle("db").getString("db.user");
                String password = ResourceBundle.getBundle("db").getString("db.pass");
                conn = DriverManager.getConnection(url, user, password);
                conn.setAutoCommit(false);
                sql_statement = conn.createStatement();

                FileReader fileReader = new FileReader(inputFileName);
                BufferedReader br = new BufferedReader(fileReader);

                String nextLine;
                int lnNum = 0;
                //loop file , add records to batch
                while ((nextLine = br.readLine()) != null) {
                    lnNum++;
                    sql_statement.addBatch(nextLine);

                }
            } catch (Exception e) {
                System.out.println("Before Execute exception "+e.getMessage()+" in file "+inputFileName);
            }
            int[] totalRecords = new int[7];
            try {
                totalRecords = sql_statement.executeBatch();
            } catch (BatchUpdateException e) {
                System.out.println("Batch Update Exception in file "+inputFileName);
                totalRecords = e.getUpdateCounts();
            } catch (Exception e) {
                System.out.println("Other Exception in file " + inputFileName);
            } finally {
                System.out.println("Total "+totalRecords.length+" records inserted in bulk from CSV file " + inputFileName);
                try {
                    sql_statement.close();

                    conn.commit();

                    conn.close();
                } catch (Exception ex) {
                    System.out.println("Warning while closing connections in file " + inputFileName);
                }
            }
        }
    }
}
