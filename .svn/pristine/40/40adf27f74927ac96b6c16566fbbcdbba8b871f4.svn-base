/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objets;
import java.sql.*;
import java.util.Map;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.pool.OracleDataSource;
import oracle.sql.BLOB;
/**
 *
 * @author Ferhat
 */
public class DB {
    private static DB instance;
    private int timeout=40;
    private Connection con=null;
    
    private Statement statement=null;
    private OracleDataSource ods = null;
    private Map<String, Class<?>> Mapping;
    private String user="fy358838";
    //private String user="jb165099";
    
    public static DB getInstance(){
        if(instance == null)
            instance=new DB();
        return instance;
    }
    
    private DB(){
        try{
            init_connect();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void init_connect()throws Exception{
        setConnection();
        setStatement();
    }
    
    private void setConnection() throws Exception{
        ods= new OracleDataSource();
        ods.setDriverType("thin");
        ods.setDatabaseName("ensb2014");
        ods.setServerName("ufrsciencestech.u-bourgogne.fr");ods.setPortNumber(25561);
        //ods.setServerName("butor");ods.setPortNumber(1521);
        //ods.setUser(user);ods.setPassword("user");
        ods.setUser(user);ods.setPassword(user);
        con = (OracleConnection)ods.getConnection();
        con.setAutoCommit(false);
        
        
        
    }
    
    
    private void setStatement() throws Exception{
        if(con==null)
            setConnection();
        statement = con.createStatement();
        statement.setQueryTimeout(timeout);
    }
    
    public void close(){
        try{
        con.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void commit() throws SQLException{
        con.commit();
    }
    
    public String getUser(){
        return user;
    }
    
    private void init_Mapping() throws ClassNotFoundException, SQLException {
        this.Mapping=this.con.getTypeMap();
       // this.Mapping.put("PHOTO_TYPE", Class.forName("Objets.Photo"));
        this.Mapping.put("MONUMENT_TYPE", Class.forName("Objets.Monument"));
        this.Mapping.put("DESCRIPTION_TYPE", Class.forName("Objets.Description"));
        this.Mapping.put("VILLE_TYPE", Class.forName("Objets.Ville"));
        this.Mapping.put("MOTCLE_TYPE", Class.forName("Objets.MotCle"));
        this.Mapping.put("IMGST_TYPE", Class.forName("Objets.ST_Image"));
        this.Mapping.put("THEME_TYPE", Class.forName("Objets.Theme"));
        this.Mapping.put("IMAGE_TYPE", Class.forName("Objets.Photo"));
    }
    
    public Map getMap() throws ClassNotFoundException, SQLException{
        this.init_Mapping();
        return Mapping;
    }
    
    public Array getArrayFromList(String Nom,Object[] in) throws SQLException{
        return con.createArrayOf(Nom, in);
    }
    
    /*------------------------------------------------------------------------------------------------------------*/
    
     public OraclePreparedStatement executeOraclePreparedStatement(String instruction) throws SQLException {
        OraclePreparedStatement pstm = (OraclePreparedStatement) con.prepareCall(instruction);
        pstm.setQueryTimeout(timeout);
        return pstm;
    }
    
    
    public PreparedStatement setPreparedStatement(String instruction) throws SQLException {
        PreparedStatement pstm = con.prepareCall(instruction);
        pstm.setQueryTimeout(timeout);
        return pstm;
    }
    
    public ResultSet executePreparedStatementSelect(PreparedStatement pstm) throws SQLException {
        return pstm.executeQuery();
    }
    
    public void executePreparedStatement(PreparedStatement pstm) throws SQLException {
        pstm.executeUpdate();
    }
    
    public void executeStmt(String instruction) throws SQLException, Exception {
        statement.executeUpdate(instruction);
    }
    
    public ResultSet executeQry(String instruction) throws SQLException, Exception {
        return statement.executeQuery(instruction);
    } 
    
    public OracleResultSet executeOracleQuery(String instruction) throws SQLException, Exception {
        return (OracleResultSet) statement.executeQuery(instruction);
    }
    
    public Statement getstatement() throws SQLException{
        return con.createStatement();
    }
    public Blob getBlob() throws SQLException{
        BLOB blob = BLOB.createTemporary(DB.getInstance().get_connection(), true, BLOB.DURATION_SESSION);
        blob.open(BLOB.MODE_READWRITE);
        return  blob;
    }
    
    public CallableStatement getCallSatatement(String instruction) throws SQLException{
        return con.prepareCall(instruction);
    }
    public Connection get_connection(){
        return this.con;
    }
}
