
package Objets;

import java.sql.*;
import oracle.jdbc.pool.*;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.*;
import oracle.sql.*;

public class Connection1 {
    
    private OracleConnection conn=null;
    private OracleDataSource ods = null;
    private String req = "SELECT VALUE (t) FROM THEME t where t.id_theme= ?";
    private Statement st=null;
    private PreparedStatement ps;
    private OracleResultSet rs;
    private String result="";
    
    
    public OracleConnection connect() throws Exception{
        //conn = (OracleConnection) DriverManager.getConnection("jdbc:oracle:thin:@ufrsciencestech.u-bourgogne.fr:25561:ensb2014","fy358838","fy358838");
        ods = new OracleDataSource();
        //ods.setURL("jdbc:oracle:thin:@butor:25561:ensb2014");
        ods.setDriverType("thin");
        ods.setDatabaseName("ensb2014");
        ods.setServerName("ufrsciencestech.u-bourgogne.fr");
        ods.setPortNumber(25561);
        ods.setUser("fy358838");
        ods.setPassword("fy358838");
        conn = (OracleConnection)ods.getConnection();
        conn.setAutoCommit(false);
        return conn;
    }
    
    public void close() throws Exception {
        conn.close();
    }
    
    public String affiche_test(String req) throws Exception{
	result="";
        ps = conn.prepareStatement(req);
        rs =  (OracleResultSet) ps.executeQuery();
        while(rs.next()){
            oracle.sql.STRUCT st = rs.getSTRUCT(1);
            Object tab[]= st.getAttributes();
            System.out.println(tab[1].toString());
            result+=tab[1].toString();
            result+="\n";
        }
        return result;
    }
    
    public Statement createStmt() throws SQLException{
        return conn.createStatement();
    }
    
    
    
    
}
