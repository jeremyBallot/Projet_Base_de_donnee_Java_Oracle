
package Objets;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.sql.Date;
import java.sql.Struct;
import java.util.ArrayList;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.ARRAY;
import oracle.sql.BLOB;
import oracle.sql.REF;
import oracle.sql.STRUCT;

/**
 *
 * @author Ferhat
 */
public class Monument implements SQLData {
    private int id_monument;
    private String nom;
    private REF ville_ref;
    private String ville;
    private String type;
    private Date date_construction;
    private Description description;
    private REF theme_ref=null;
    private String theme;
    private Theme th;
    private ARRAY Medias;
    private ArrayList<Media> listeMedia;
    
    /*Constructeur Vide*/
    public Monument(){
        this.id_monument=-1;
        this.nom=" ";
        this.ville=" ";
        this.type=" ";
        this.date_construction=null;
        this.description=null;
        this.theme_ref=null;
        this.Medias=null;
        this.th=null;
        this.listeMedia=new ArrayList<>();
    }
        /*Constructeur avec parametres*/
    public Monument(int id,String nom,String ville,String type,Date date,Description desc,String theme,Array list){
        this.id_monument=id;
        this.nom=nom;
        this.ville=ville;
        this.type=type;
        this.date_construction=date;
        this.description=desc;
        this.theme=theme;
        this.Medias=(ARRAY) list;
        this.listeMedia=new ArrayList<>();
    }

        /*Accesseurs et setteur*/
    public void set_id(int id){
        this.id_monument=id;
    }

    public int get_id(){
        return this.id_monument;
    }
    public ArrayList<Media> get_MediaList(){
        return this.listeMedia;
    }
    public void set_ville(String input){
        this.ville=input;
    }

    public String get_ville(){
        return this.ville;
    }

    public void set_theme(String input){
        this.theme=input;
    }

    public String get_theme(){
        return this.theme;
    }
    
    private void set_ville_ref(REF input){
        this.ville_ref=input;
    }

    private REF get_ville_ref() throws SQLException{
       return ville_ref;
    }

    private void set_theme_ref(REF input){
        this.theme_ref=input;
    }

    private REF get_theme_ref(String input) throws SQLException{
        /*System.out.println(getThemeFromName(input).toString());
        this.theme_ref.setValue(getThemeFromName(input));*/
        return this.theme_ref;
    }
    public void set_Date(java.sql.Date in){
        this.date_construction=in;
    }
    public void set_nom(String input){
        this.nom=input;
    }
    public String get_nom(){
        return this.nom;
    }

    public void set_type(String input){
        this.type=input;
    }
    public String get_type(){
        return this.type;
    }

   

    public Date get_date(){
        return this.date_construction;
    }

    public void set_description(Description desc){
        this.description=desc;
    }

    public Description get_description(){
        return this.description;
    }


    public void set_medias(ARRAY list){
        this.Medias=list;
    }
    
    public void set_theme(Theme t){
        this.th=t;
    }
    
    public Theme get_th(){
        return this.th;
    }

    public Array get_medias(){
        return Medias;
    }

    public void setListeMedia(ArrayList<Media> in){
        this.listeMedia=in;
    }
    public ArrayList<Media> getMediaList(){
        return this.listeMedia;
    }
    
    @Override
    public String toString(){
        return this.nom+" "+this.type;
    }

    @Override
    public String getSQLTypeName(){
        return "MONUMENT_TYPE";
    }
    
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.set_id(stream.readInt());
        this.set_nom(stream.readString());
        this.set_ville_ref((REF) stream.readRef());
        this.set_type(stream.readString());
        this.set_Date(stream.readDate());
        this.set_description((Description) stream.readObject());
        this.set_theme_ref((REF) stream.readRef());
        this.set_medias((ARRAY) stream.readArray());
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeInt(get_id());
        stream.writeString(get_nom());
        stream.writeRef( get_ville_ref() );
        stream.writeString(get_type());
        stream.writeDate( get_date());
        stream.writeObject((SQLData) description);
        stream.writeRef(get_theme_ref(theme));
        stream.writeArray((Array) Medias);
    }
    
    
    public ArrayList<Monument> loadAll() throws SQLException, ClassNotFoundException{
        
        ArrayList<Monument> tmp = new ArrayList<>();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery("SELECT VALUE(M) FROM MONUMENT M");
            while(rs.next()){
                Monument mon;
                mon = (Monument) rs.getObject(1, DB.getInstance().getMap());
                mon.set_theme(mon.theme_ref.getValue().toString());
                mon.set_ville(mon.ville_ref.getValue().toString());
                mon.setListeMedia(LoadMediaFromArray((ARRAY) mon.get_medias()));
                System.out.println(mon.get_nom()+" Chargé");
                System.out.println("Medias Chargés "+mon.getMediaList().size());
                tmp.add(mon);
            }
        }
        rs.close();
        return tmp;
    }
    
    public Monument loadFromId(int id) throws SQLException, ClassNotFoundException{
        
        Monument tmp = new Monument();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery("SELECT VALUE(M) FROM MONUMENT M WHERE ID_MONUMENT="+id);
            while(rs.next()){
               
                tmp = (Monument) rs.getObject(1, DB.getInstance().getMap());
                tmp.set_theme(tmp.theme_ref.getValue().toString());
                tmp.set_ville(tmp.ville_ref.getValue().toString());
                tmp.setListeMedia(LoadMediaFromArray((ARRAY) tmp.get_medias()));
                System.out.println(tmp.get_nom()+" Chargé");
                System.out.println("Medias Chargés "+tmp.getMediaList().size());
                
            }
        }
        rs.close();
        return tmp;
    }
    
    @SuppressWarnings("ConvertToTryWithResources")
    public void save() throws SQLException{
     
        /*Insertion du Monumeent */
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO MONUMENT VALUES(?)");
        ps.setObject(1,this);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        ps.close();

        /*Mettre à jour la REF Ville*/
        
        PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE MONUMENT M SET VILLE = (SELECT REF(R) FROM VILLE R WHERE R.NOM_VILLE = ?) WHERE M.ID_MONUMENT = ? ");
        pu.setString(1, this.ville);
        pu.setInt(2, this.id_monument);
        DB.getInstance().executePreparedStatement(pu);

        /*Mettre à jour la REF Theme*/
        
        pu=DB.getInstance().setPreparedStatement("UPDATE MONUMENT M SET THEME =(SELECT REF(R) FROM THEME R WHERE R.THEME = ?),VILLE = (SELECT REF(R) FROM VILLE R WHERE R.NOM_VILLE = ?) WHERE M.ID_MONUMENT = ?");
        pu.setString(1, this.theme);
        pu.setString(2, this.ville);
        pu.setInt(3, this.id_monument);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        
        /*Inserer le Monument dans Collection de Theme*/
        
        pu=DB.getInstance().setPreparedStatement("INSERT INTO THE (SELECT T.MONUMENTS FROM THEME T WHERE T.THEME = ?) SELECT REF(M) FROM MONUMENT M WHERE M.ID_MONUMENT = ?");
        pu.setString(1,this.theme);
        pu.setInt(2, this.id_monument);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        
        /*Inserer le Monument dans Collection de Ville*/
        
        pu=DB.getInstance().setPreparedStatement("INSERT INTO THE ( SELECT V.MONUMENTS FROM VILLE V WHERE V.NOM_VILLE = ? ) SELECT REF(M) FROM MONUMENT M WHERE M.ID_MONUMENT = ?");
        pu.setString(1,this.ville);
        pu.setInt(2, this.id_monument);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        
        /*Mettre à jour le constructeur Par defaut de la collection Medias*/
        
        pu = DB.getInstance().setPreparedStatement("UPDATE MONUMENT M SET MEDIAS =( MEDIAS_TYPE() ) WHERE M.NOM = ? ");
        pu.setString(1, this.nom);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        /*Rebuild de l'index sur les noms*/
        pu = DB.getInstance().setPreparedStatement("ALTER index monument_index_name rebuild");
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
    
    }
    
    public ArrayList<Monument> loadInterMedia(String str) throws SQLException, ClassNotFoundException{
       ArrayList<Monument> tmp = new ArrayList<>();
       OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()){
            rs = (OracleResultSet) st.executeQuery("SELECT M.id_monument,M.NOM FROM MONUMENT M WHERE CONTAINS (M.NOM,'?"+str+"')>0");
            while(rs.next()){
                Monument mon = new Monument();
                mon.set_id((int) rs.getInt(1));
                mon.set_nom((String) rs.getString(2));
                tmp.add(mon);
            }
        }
        rs.close();
        return tmp;
    }
    
    public ArrayList<Monument> loadFromAdvanced(String theme,String ville, String nom) throws SQLException, ClassNotFoundException{
       
        String Query="SELECT M.id_monument,M.NOM FROM MONUMENT M WHERE ";
        Boolean prev_cri = false;
        if(!theme.equals("Sélectionner")){
            Query  = Query+"M.THEME=(SELECT REF(T) FROM THEME T WHERE T.THEME='"+theme+"') ";
            prev_cri=true;
        }
        if(!ville.equals("Sélectionner")){
            if(prev_cri){
                Query = Query+" AND ";
            }
            Query= Query+" M.VILLE = (SELECT REF(V) FROM VILLE V WHERE V.NOM_VILLE='"+ville+"') ";
            prev_cri=true;
        }
        if(!nom.equals("")){
            if(prev_cri){
                Query = Query+" AND ";
            }
            Query= Query+" CONTAINS (M.NOM,'?"+nom+"')>0";
        }
        System.out.println(Query);
        ArrayList<Monument> tmp = new ArrayList<>();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery(Query);
            while(rs.next()){
                Monument mon = new Monument();
                mon.set_id((int) rs.getInt(1));
                mon.set_nom((String) rs.getString(2));
                tmp.add(mon);
            }
        }
        rs.close();
        return tmp;
    }
    
    public ArrayList<Media> LoadMediaFromArray(ARRAY ar) throws SQLException{
        ArrayList<Media> list = new ArrayList<>();
        OracleResultSet meds =(OracleResultSet) ar.getResultSet();
        while(meds.next()){
            Struct t2 = ((REF) ((meds.getSTRUCT(2)).getAttributes())[0]).getSTRUCT();
            if((t2.getAttributes())[5].toString().equals("IMAGE")){
                Photo p = new Photo();
                p.set_id(Integer.parseInt(t2.getAttributes()[0].toString()));
                p.set_nom((String) (t2.getAttributes())[1]);
                p.set_date((t2.getAttributes())[2].toString());
                p.set_legende((String) (t2.getAttributes())[3]);
                p.set_mon_ref((REF)(t2.getAttributes())[4]);
                p.set_blob((BLOB) ((STRUCT)((STRUCT) t2.getAttributes()[6]).getAttributes()[0]).getAttributes()[0]);
                list.add(p);
                System.out.println("Photo chargé"+p.get_id());
            }
            if((t2.getAttributes())[5].toString().equals("AUDIO")){
                
                System.out.println("Audio detecté");
                Audio au = new Audio();
                au.set_id(Integer.parseInt(t2.getAttributes()[0].toString()));
                au.set_nom((String) (t2.getAttributes())[1]);
                au.set_date((t2.getAttributes())[2].toString());
                au.set_legende((String) (t2.getAttributes())[3]);
                au.set_mon_ref((REF)(t2.getAttributes())[4]);
                au.set_duree(Integer.parseInt(t2.getAttributes()[6].toString()));                
                au.set_blob((BLOB) ((STRUCT)((STRUCT) t2.getAttributes()[7]).getAttributes()[1]).getAttributes()[0]);
                list.add(au);
                System.out.println("AUdio chargé"+au.get_id());
            }
            
            if((t2.getAttributes())[5].toString().equals("VIDEO")){
                
                System.out.println("Video detecté");
                Video au = new Video();
                au.set_id(Integer.parseInt(t2.getAttributes()[0].toString()));
                au.set_nom((String) (t2.getAttributes())[1]);
                au.set_date((t2.getAttributes())[2].toString());
                au.set_legende((String) (t2.getAttributes())[3]);
                au.set_mon_ref((REF)(t2.getAttributes())[4]);
                au.set_duree(Integer.parseInt(t2.getAttributes()[6].toString()));
                au.set_blob((BLOB) ((STRUCT)((STRUCT) t2.getAttributes()[7]).getAttributes()[1]).getAttributes()[0]);
                list.add(au);
                System.out.println("Video chargé"+au.get_id());
            }
        }
        return list;
    }
  
}
