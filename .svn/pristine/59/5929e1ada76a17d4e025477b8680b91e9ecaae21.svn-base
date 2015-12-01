
package Objets;

import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;

/**
 *
 * @author Ferhat
 */
public class MotCle implements SQLData{
    private int id_mot;
    private String mot;
    
    
    public MotCle(){
        this.id_mot=0;
        this.mot="";
    }
    
    public MotCle(int id, String mot){
        this.id_mot=id;
        this.mot=mot;
    }
    
    public void set_mot(String mot){
        this.mot=mot;
    }
    
    public String get_mot(){
        return this.mot;
    }
    
    public void set_id(int id){
        this.id_mot=id;
    }
    
    public int get_id(){
        return this.id_mot;
    }
    
    @Override
    public String toString(){
        return this.id_mot+" "+this.mot;
    }
    
    @Override
    public String getSQLTypeName(){
        return "MOTCLE_TYPE";
    }
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.id_mot=stream.readInt();
        this.mot=stream.readString();
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeInt(id_mot);
        stream.writeString(mot);
    }
    
    public ArrayList<MotCle> load() throws SQLException, ClassNotFoundException{
        
        ArrayList<MotCle> liste = new ArrayList<>();
        
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery("SELECT value(t) from MOTCLE t ");
            MotCle mots;
            while(rs.next()){
                mots = (MotCle)  rs.getObject(1,DB.getInstance().getMap());
                liste.add(mots);
                //System.out.println(mot.toString());
            }
        }
        rs.close();
        return liste;
        
    }
    
    public void save() throws SQLException{
        PreparedStatement pst = DB.getInstance().setPreparedStatement("INSERT INTO MOTCLE VALUES(?)");
        pst.setObject(1, this);
        DB.getInstance().executePreparedStatement(pst);
        DB.getInstance().commit();
    }
    
}

