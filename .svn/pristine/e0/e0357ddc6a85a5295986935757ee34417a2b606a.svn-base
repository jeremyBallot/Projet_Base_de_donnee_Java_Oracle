package Objets;

import java.awt.Image;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.BLOB;
import oracle.sql.REF;
import oracle.sql.STRUCT;
import static projet_test.Projet_test.getBlobFromStream;

/**
 *
 * @author Ferhat
 */
public class Photo extends Media implements SQLData{
    private Image img;
    private STRUCT StillImage;
    private BLOB lob;
    private File file;
    private InputStream input;
    
    private int id_monument;
    
    public Photo(){
        super();
        this.img = null;
        this.type="IMAGE";
        this.id_monument=-1;
    }
    
    public Photo(int id, String nom, String legende,String monument,File file) throws IOException{
        super(id,nom,legende,monument);
        this.file=file;
        this.type="IMAGE";
        this.id_monument=-1;
    }
    public Photo(int id, String nom, String legende,String monument,BLOB file) throws IOException{
        super(id,nom,legende,monument);
        this.lob=file;
        this.type="IMAGE";
    }
    
    public Image get_image(){
        return img;
    }
    
    public void set_image(File img){
        try {
            this.img = ImageIO.read(img);
        } catch (IOException ex) {
            Logger.getLogger(Photo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void set_monument(int in){
        this.id_monument=in;
    }
        
    @Override
    public String getSQLTypeName(){
        return "IMAGE_TYPE";
    }
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.idMedia = stream.readInt();
        this.nom = stream.readString();
        this.DateAjout = stream.readDate();
        this.legende = stream.readString();
        this.Mon = (REF) stream.readRef();
        this.type=stream.readString();
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
       stream.writeInt(this.idMedia);
       stream.writeString(this.nom);
       stream.writeDate(this.DateAjout);
       stream.writeString(this.legende);
       stream.writeRef(null);
       stream.writeString(type);
    }
    
    public void load() throws SQLException{
        
        String query = "SELECT value(t) FROM image t";
        OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
        OracleResultSet rs = (OracleResultSet) st.executeQuery(query);
        this.StillImage = null;
        while(rs.next()){
            this.idMedia = (int)rs.getInt(1);
            this.nom = (String) rs.getString(2);
            this.DateAjout = rs.getDate(3);
            this.legende= rs.getString(4);
            this.Mon = (REF) rs.getREF(5);
            this.StillImage = (STRUCT) rs.getSTRUCT(6);
        }
        if(input == null)
            System.out.println("NULL");
        //System.out.println(this.lob.length());
        //this.lob = (BLOB) ((STRUCT) stillimage.getAttributes()[0]).getAttributes()[0];
        System.out.println("ok");
    }
    
    public void save() throws FileNotFoundException, SQLException, ClassNotFoundException, IOException{
        
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO IMAGE VALUES(?)");
        ps.setObject(1, this);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        /* Insertion de l'image */
        if(this.file != null){
        InputStream stream = new FileInputStream(this.file);
        this.lob = getBlobFromStream(stream);
         /*Si l'image est bien chargée on insert*/
            ps = DB.getInstance().setPreparedStatement("UPDATE IMAGE I set I.image = ORDSYS.SI_STILLIMAGE(?) where I.id_media = ?");
            ps.setBlob(1, this.lob);
            ps.setInt(2, idMedia);
            DB.getInstance().executePreparedStatement(ps);
            DB.getInstance().commit();
            /*executer init_features()*/
            CallableStatement stmt = (DB.getInstance().get_connection()).prepareCall("{call calprop(?)}");
            stmt.setInt(1, idMedia);
            stmt.execute();
        }
        else{/*Sinon on insert Rien*/
           // ps = DB.getInstance().setPreparedStatement("UPDATE IMAGE I set I.image = ORDSYS.SI_STILLIMAGE() where I.id_media = ?");
            //ps.setInt(1, idMedia);
            //DB.getInstance().executePreparedStatement(ps);
            //DB.getInstance().commit();
            System.out.println("Fichier NON lu, Blob Vide");
        }
        if(id_monument != -1){
            
        /* Mettre à jour les references */
        PreparedStatement pu = DB.getInstance().setPreparedStatement("UPDATE IMAGE M SET MONUMENT = (SELECT REF(R) FROM MONUMENT R WHERE R.id_monument=?) WHERE M.ID_MEDIA = ? ");
        pu.setInt(1, this.id_monument);
        pu.setInt(2, this.idMedia);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        /*Ajouter le média dans la Collection de Monument*/
        pu=DB.getInstance().setPreparedStatement("INSERT INTO THE (SELECT M.MEDIAS FROM MONUMENT M WHERE M.id_monument = ?) SELECT REF(I) FROM IMAGE I WHERE I.ID_MEDIA = ?");
        pu.setInt(1,this.id_monument);
        pu.setInt(2, this.idMedia);
        DB.getInstance().executePreparedStatement(pu);
        DB.getInstance().commit();
        }
        else{
            System.out.println("Attention Aucune reference n'est à jour");
        }
    }
    
    public void delete() throws SQLException{
        
        /*Supression dans la collection de medias dans monument*/        
        PreparedStatement ps=DB.getInstance().setPreparedStatement("DELETE THE (SELECT p.Medias FROM MONUMENT p WHERE p.id_monument=?) ph WHERE ph.media=(select ref(i) from IMAGE i where i.id_media=?)");
        ps.setInt(1, id_monument);
        ps.setInt(2, idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        
        /*Supression du media*/
        ps = DB.getInstance().setPreparedStatement("DELETE IMAGE where id_media=?");
        ps.setInt(1, this.idMedia);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
    }
    
    public ArrayList<Photo> comparTo(double coefav,double coefht,double coefpos,double coeftex,File f,int precision) throws SQLException, FileNotFoundException, ClassNotFoundException, IOException{
        
        ArrayList<Photo> result = new ArrayList<>();
        InputStream stream = new FileInputStream(f);
        this.lob = getBlobFromStream(stream);
        
        PreparedStatement ps = DB.getInstance().setPreparedStatement("SELECT value(t) FROM image t WHERE t.compareto(?,?,?,?,ORDSYS.SI_STILLIMAGE(?)) <="+precision);
        ps.setDouble(1, coefav);
        ps.setDouble(2, coefht);
        ps.setDouble(3, coefpos);
        ps.setDouble(4, coeftex);
        ps.setBlob(5, lob);
        OracleResultSet rs = (OracleResultSet) DB.getInstance().executePreparedStatementSelect(ps);
        while(rs.next()){
            Photo ph;
            ph =(Photo) rs.getObject(1, DB.getInstance().getMap());
            result.add(ph);
            System.out.println(ph.get_legende()+" Chargée "+ph.get_id());
            if(ph!=null){
                result.add(ph);
            }
        }
        /*Chargement de L'image*/
        if(result.size()>0){
            for(Photo ph : result){
                OracleStatement st2 = (OracleStatement) DB.getInstance().getstatement();
                rs = (OracleResultSet) st2.executeQuery("SELECT IMAGE FROM IMAGE I WHERE I.ID_MEDIA="+ph.get_id());
                rs.next();
                STRUCT struct = (STRUCT) rs.getSTRUCT(1);
                ph.set_blob((BLOB) ((STRUCT) struct.getAttributes()[0]).getAttributes()[0]);
            }
                  
        }
        return result;
    }
    
    
    
}
























/* PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO imgst VALUES(?,?,ORDSYS.SI_STILLIMAGE(?))");
        ps.setInt(1, 2);
        ps.setString(2, "lol");
        ps.setBlob(3, lob);
        System.out.println(lob.toString()+" "+lob.length()+"--"+imgdata.length);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();*/