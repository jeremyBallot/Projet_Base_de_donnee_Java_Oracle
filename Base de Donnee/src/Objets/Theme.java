
package Objets;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import oracle.sql.ARRAY;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.ArrayDescriptor;
import oracle.sql.REF;

/**
 *
 * @author Ferhat
 */
public class Theme implements SQLData {
    private int id_theme;
    private String theme;
    private oracle.sql.ArrayDescriptor desc;
    private oracle.sql.ARRAY monuments=null;
    private ArrayList<Monument> MonListe ;

    
    public Theme() throws SQLException{
        this.id_theme=9999;
        this.theme="Autre";
        this.desc = new ArrayDescriptor("MONUMENTS_TYPE", DB.getInstance().get_connection());
        this.monuments=null;
        this.MonListe = new ArrayList<>();
    }
    public Theme(String mot) throws SQLException{
        this.id_theme=0;
        this.theme=mot;
        this.desc = new ArrayDescriptor("MONUMENTS_TYPE", DB.getInstance().get_connection());
        this.monuments=null;
        this.MonListe = new ArrayList<>();
    }
    
    public Theme(int id,String mot) throws SQLException{
        this.desc = new ArrayDescriptor("MONUMENTS_TYPE", DB.getInstance().get_connection());
        this.id_theme=id;
        this.theme=mot;
        this.MonListe = new ArrayList<>();
    }
    
    public Theme(int id, String s , ArrayList<Monument> mon) throws SQLException{
        this.desc = new ArrayDescriptor("MONUMENTS_TYPE", DB.getInstance().get_connection());
        this.id_theme=id;
        this.theme=s;
        this.monuments= new ARRAY(desc,DB.getInstance().get_connection(),mon.toArray(new Monument[mon.size()]));
        this.MonListe = mon;
    }
    
    @Override
    public String toString(){
        return  this.theme;
    }
    
    public void set_theme(String s){
        this.theme=s;
       
    }
    
    public void set_id(int id){
        this.id_theme=id;
    }
    
    public String get_theme(){
        return theme;
    }
    public int get_id()
    {
        return id_theme;
    }
    
    public void set_monuments(ARRAY mon){
        this.monuments=mon;
    }
    
    public Array get_monuments(){
        return monuments;
    }
    
    
    
    @Override
    public String getSQLTypeName(){
        return "THEME_TYPE";
    }
        
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.set_id(stream.readInt());
        this.set_theme(stream.readString());
        this.monuments = (ARRAY) stream.readArray();
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeInt(this.id_theme);
        stream.writeString(this.theme);
        stream.writeArray(monuments);
    }
    
    public ArrayList<Theme> loadThemeListe() throws SQLException, ClassNotFoundException{
        
        ArrayList<Theme> liste = new ArrayList<>();
        OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
        OracleResultSet rs = (OracleResultSet) st.executeQuery("SELECT value(t) from THEME t");
        while(rs.next()){
            liste.add((Theme)  rs.getObject(1,DB.getInstance().getMap()));
        }
        st.close();
        rs.close();
        return liste;
    }
    
    public void save() throws SQLException{
        
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO THEME VALUES(?)");
        ps.setObject(1,this);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        
        /*Mettre à jour le constructeur par defaut de la collection monument*/
        
        PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE THEME T SET MONUMENTS =( MONUMENTS_TYPE() ) WHERE T.THEME = ? ");
        pu.setString(1, this.theme);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
    }
    
}
