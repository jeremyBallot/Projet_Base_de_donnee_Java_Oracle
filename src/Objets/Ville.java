package Objets;

import java.sql.Array;
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
public class Ville implements SQLData {
    private int code;
    private String nom_Ville;
    private String Region;
    private String Pays;
    private ArrayList<Monument>  monuments;
    
    public Ville(){
        this.code=0;
        this.nom_Ville="";
        this.Region="";
        this.Pays="";
        this.monuments = new ArrayList<>();
    }
    public Ville(String nom, String region, String pays){
        this.nom_Ville=nom;
        this.Region=region;
        this.Pays=pays;
        this.monuments = new ArrayList<>();
    }
    public Ville(int code,String nom, String region, String pays){
        this.code=code;
        this.nom_Ville=nom;
        this.Region=region;
        this.Pays=pays;
        this.monuments = new ArrayList<>();
    }
    
    
    public void set_ville(String nom){
        this.nom_Ville=nom;
    }
    public String get_ville(){
        return this.nom_Ville;
    }
    
    public void set_region(String reg){
        this.Region=reg;
    }
    
    public String get_region(){
        return Region;
    }
    
    public void set_pays(String pays){
        this.Pays=pays;
    }
    public String get_pays(){
        return this.Pays;
    }
    public void set_code(int code){
        this.code=code;
    }
    
    public int get_code(){
        return this.code;
    }
    
    public void set_monuments(Array monument){
        
        //this.monuments=;
    }
     
    public Array get_monument() throws SQLException{
        Object[] in = monuments.toArray( new Object[monuments.size()+1]);
        return DB.getInstance().getArrayFromList("Monument", in);
    }
    
    @Override
    public String toString(){
        return this.nom_Ville+this.Region+this.Pays+this.code;
    }
    @Override
    public String getSQLTypeName(){
        return "VILLE_TYPE";
    }
    
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.set_code(stream.readInt());
        this.set_ville(stream.readString());
        this.set_region(stream.readString());
        this.set_pays(stream.readString());
        //this.set_monuments(stream.readArray());
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeInt(this.get_code());
        stream.writeString(this.get_ville());
        stream.writeString(this.get_region());
        stream.writeString(this.get_pays());
        //stream.writeArray(this.get_monument());
        
       
    }
    
    public ArrayList<Ville> loadVilleListe() throws SQLException, ClassNotFoundException{
        ArrayList<Ville> liste = new ArrayList<>();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery("SELECT value(t) from Ville t");
            while(rs.next()){
                liste.add((Ville)  rs.getObject(1,DB.getInstance().getMap()));
            }
        }
        rs.close();
        return liste;
    }
    
    public void save() throws SQLException{

        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO VILLE VALUES(?)");
        ps.setObject(1,this);
        DB.getInstance().executePreparedStatement(ps);    
        DB.getInstance().commit();
        
        /*Mettre à jour le constructeur par defaut de la collection monument*/
        
        PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE VILLE T SET MONUMENTS =( MONUMENTS_TYPE() ) WHERE T.NOM_VILLE = ? ");
        pu.setString(1, this.nom_Ville);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        
    }
    
}
