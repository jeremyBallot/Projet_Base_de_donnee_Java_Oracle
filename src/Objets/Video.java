package Objets;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.ord.im.OrdVideo;
import oracle.sql.BLOB;

/**
 *
 * @author Ferhat
 */
public class Video extends Media {
    private int duree;
    private int id_monument;
    
    public Video()
    {
        super();
        this.type="VIDEO";
        this.idMedia=(int) (Math.random() * 1000);
        this.monument="";
        this.duree=0;
        this.id_monument=-1;
    }
    
    public void set_duree(int i){
        this.duree=i;
    }
    
    
    public void set_monument(int s){
        this.id_monument=s;
    }
    
    public void save(String PATH) throws SQLException, FileNotFoundException, IOException, Exception{
        /*Insertion de l'objet*/
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO VIDEO VALUES(?,?,?,?,NULL,?,?,ORDSYS.ORDVIDEO.init())");
        ps.setInt(1,this.idMedia);
        ps.setString(2, this.nom);
        ps.setDate(3, this.DateAjout);
        ps.setString(4, this.legende);
        ps.setString(5, type);
        ps.setInt(6, 0);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        System.out.println("Fichier inserer ");
        /*Insertion ddu Son */
        if(PATH != null ){ /*SI le fichier est plain on insert*/
            OracleResultSet rset = (OracleResultSet) DB.getInstance().executeOracleQuery("SELECT A.VIDEO FROM VIDEO A WHERE A.id_media = "+this.idMedia+" FOR UPDATE");
            rset.next();
            OrdVideo obj  = (OrdVideo) rset.getORAData(1, OrdVideo.getORADataFactory());
            System.out.println("Get file for upadte");
            byte[] ctx[] = new byte[4000][64];
           
            System.out.println(" Loading into ORDVIDEO");
            obj.loadDataFromFile(PATH);
            System.out.println("set proerties");
            this.duree=obj.getVideoDuration();
            obj.setProperties(ctx,false);
            System.out.println(obj.getVideoDuration());
            try {
                OraclePreparedStatement pstm = DB.getInstance().executeOraclePreparedStatement("UPDATE VIDEO A SET A.VIDEO = ?, A.DUREE = ? WHERE A.ID_MEDIA= ?");
                pstm.setORAData(1, obj);
                pstm.setInt(2, duree);
                pstm.setInt(3, this.idMedia);
                pstm.execute();
                System.out.println("Insertion vids ok");
                
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }

            if(id_monument!=-1)
            {            
                /* Mettre à jour les references */
                PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE VIDEO AU SET MONUMENT = (SELECT REF(R) FROM MONUMENT R WHERE R.ID_MONUMENT = ?) WHERE AU.ID_MEDIA = ? ");
                pu.setInt(1, this.id_monument);
                pu.setInt(2, this.idMedia);
                DB.getInstance().executePreparedStatement(pu);

                /*Ajouter le média dans la Collection de Monument*/
                pu=DB.getInstance().setPreparedStatement("INSERT INTO THE (SELECT M.MEDIAS FROM MONUMENT M WHERE M.ID_MONUMENT = ?) SELECT REF(I) FROM VIDEO I WHERE I.ID_MEDIA = ?");
                pu.setInt(1,this.id_monument);
                pu.setInt(2, this.idMedia);
                DB.getInstance().executePreparedStatement(pu);
                DB.getInstance().commit();
            }
            else{
                System.out.println("Aucun ref n'est liées");
            }
        
        
        }           
    }
    
    public void delete() throws SQLException{
        
        /*Supression dans la collection de medias dans monument*/        
        PreparedStatement ps=DB.getInstance().setPreparedStatement("DELETE THE (SELECT p.Medias FROM MONUMENT p WHERE p.id_monument=?) ph WHERE ph.media=(select ref(i) from VIDEO i where i.id_media=?)");
        ps.setInt(1, id_monument);
        ps.setInt(2, idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        
        /*Supression du media*/
        ps = DB.getInstance().setPreparedStatement("DELETE VIDEO where id_media=?");
        ps.setInt(1, this.idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
    }
    
    
}
