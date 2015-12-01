package Objets;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Struct;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.BLOB;
import oracle.sql.LobPlsqlUtil;
import oracle.sql.STRUCT;

/**
 *
 * @author Ferhat
 */
public class ST_Image implements SQLData {
    private int id;
    private String nom;
    private STRUCT struct;
    private BLOB image;
    private InputStream input;
    private STRUCT str;
    
    
    public ST_Image(){
        this.id = -1;
        this.nom = "rr";
        this.image =null;
    }
    
    public int get_id(){
        return this.id;
    }
    
    public void set_id(int input){
        this.id=input;
    }
    
    public BLOB get_blob(){
        return this.image;
    }
    
    public void set_blob(BLOB IN){
        this.image=IN;
    }
    
     @Override
    public String getSQLTypeName(){
        return "IMGST_TYPE";
    }
        
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.set_id(stream.readInt());
        this.nom=stream.readString();
        
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeInt(this.get_id());
        stream.writeBlob(this.get_blob());
        stream.writeStruct(struct);
    }
    
    public ST_Image getImageFromBlob() throws SQLException, ClassNotFoundException{
        ST_Image ob = new ST_Image();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet)st.executeQuery("select value (t) from IMGST t");
            while(rs.next()){
                ob =(ST_Image) rs.getObject(1, DB.getInstance().getMap());
            }
        }
        rs.close();
        OracleStatement st2 = (OracleStatement) DB.getInstance().getstatement();
        OracleResultSet rs2 = (OracleResultSet)st2.executeQuery("select * from IMGST ");
        while(rs2.next()){
             this.struct = (STRUCT) rs2.getSTRUCT(3);
        }
        if(struct == null)
            System.out.println("STRUCT NULL");
        else{
            ob.set_blob((BLOB)((STRUCT)this.struct.getAttributes()[0]).getAttributes()[0]);
            System.out.println(ob.get_id()+" "+ob.nom);
        }
    return ob;
    }
    
    
    public void insert(){
        
    }
    
}
