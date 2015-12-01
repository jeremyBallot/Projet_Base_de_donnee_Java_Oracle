package Objets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.REF;
import oracle.sql.STRUCT;
import oracle.ord.im.OrdAudio;

/**
 *
 * @author Ferhat
 */
public class Audio extends Media implements SQLData {
    
    private int duree;
    private STRUCT struct ;
    private int id_monument;
        
    
    public Audio(){
        super();
        this.idMedia=(int) (Math.random() * 1000);
        this.type="AUDIO";
        this.id_monument=-1;
    }
    
     public void set_monument(String in){
        this.monument=in;
    }
     
     public void set_monument(int id){
        this.id_monument=id; 
     }
    public String get_monument(){
        return this.monument;
    }
    
   
    public void set_duree(int i){
        this.duree=i;
    }
       
    @Override
    public String getSQLTypeName(){
        return "AUDIO_TYPE";
    }
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.idMedia = stream.readInt();
        this.nom = stream.readString();
        this.DateAjout = stream.readDate();
        this.legende = stream.readString();
        this.Mon = (REF) stream.readRef();
        this.type = stream.readString();
        this.duree = (int) stream.readInt();
        
       
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
       stream.writeInt(this.idMedia);
       stream.writeString(this.nom);
       stream.writeDate(this.DateAjout);
       stream.writeString(this.legende);
       stream.writeString(type);
       stream.writeRef(Mon);
    }
    
    public void save(String PATH) throws SQLException, FileNotFoundException, IOException, Exception{
        /*Insertion de l'objet*/
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO AUDIO VALUES(?,?,?,?,NULL,?,?,ORDSYS.ORDAUDIO.init())");
        ps.setInt(1,this.idMedia);
        ps.setString(2, this.nom);
        ps.setDate(3, this.DateAjout);
        ps.setString(4, this.legende);
        this.Mon=null;
       
        ps.setString(5, type);
        ps.setInt(6, 0);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
    
        /*Insertion ddu Son */
        if(PATH != null ){ /*SI le fichier est plain on insert*/
            OracleResultSet rset = (OracleResultSet) DB.getInstance().executeOracleQuery("SELECT A.AUDIO FROM AUDIO A WHERE A.id_media = "+this.idMedia+" FOR UPDATE");
            rset.next();
            OrdAudio obj  = (OrdAudio) rset.getORAData(1, OrdAudio.getORADataFactory());
            byte[] ctx[] = new byte[4000][1];
            obj.loadDataFromFile(PATH);
            obj.setProperties(ctx);
            System.out.println(obj.getAudioDuration());
            try {
                OraclePreparedStatement pstm = DB.getInstance().executeOraclePreparedStatement("UPDATE AUDIO A SET A.AUDIO = ? WHERE A.ID_MEDIA= ?");
                pstm.setORAData(1, obj);
                pstm.setInt(2, this.idMedia);
                pstm.execute();
                System.out.println("Insertion son ok");
                
                this.duree=obj.getAudioDuration();
                PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE AUDIO A SET A.DUREE = ?  WHERE A.ID_MEDIA= ?");
                pu.setInt(1, this.duree);
                pu.setInt(2, idMedia);
                DB.getInstance().executePreparedStatement(pu);
                DB.getInstance().commit();
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
            if(id_monument!= -1){
                   
        /* Mettre à jour les references */
        PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE AUDIO AU SET MONUMENT = (SELECT REF(R) FROM MONUMENT R WHERE R.id_monument = ?) WHERE AU.ID_MEDIA = ? ");
        pu.setInt(1, this.id_monument);
        pu.setInt(2, this.idMedia);
        DB.getInstance().executePreparedStatement(pu);
        
        /*Ajouter le média dans la Collection de Monument*/
        pu=DB.getInstance().setPreparedStatement("INSERT INTO THE (SELECT M.MEDIAS FROM MONUMENT M WHERE M.id_monument = ?) SELECT REF(I) FROM AUDIO I WHERE I.ID_MEDIA = ?");
        pu.setInt(1,this.id_monument);
        pu.setInt(2, this.idMedia);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
            }
        }
    }
    
    public void Load() throws SQLException, ClassNotFoundException{
       
        Audio file = new Audio();
        OracleResultSet rs;
        try (OracleStatement st = (OracleStatement) DB.getInstance().getstatement()) {
            rs = (OracleResultSet) st.executeQuery("SELECT VALUE(M) FROM AUDIO M");
            while(rs.next()){
                struct = (STRUCT) rs.getObject(1,DB.getInstance().getMap());
            }
        }
        System.out.println((struct.getAttributes()[0]));
        System.out.println((struct.getAttributes()[1]));
        System.out.println((struct.getAttributes()[2]));
        System.out.println((struct.getAttributes()[3]));
        System.out.println((struct.getAttributes()[4]));
        System.out.println((struct.getAttributes()[5]));
        System.out.println((struct.getAttributes()[6]));
        rs.close();
       
    }
    
     public void delete() throws SQLException{
        
        /*Supression dans la collection de medias dans monument*/        
        PreparedStatement ps=DB.getInstance().setPreparedStatement("DELETE THE (SELECT p.Medias FROM MONUMENT p WHERE p.id_monument=?) ph WHERE ph.media=(select ref(i) from AUDIO i where i.id_media=?)");
        ps.setInt(1, id_monument);
        ps.setInt(2, idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        
        /*Supression du media*/
        ps = DB.getInstance().setPreparedStatement("DELETE AUIO where id_media=?");
        ps.setInt(1, this.idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
    }
}
