
package projet_test;

import Interface.Principale;
import Objets.*;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import oracle.sql.BLOB;

/**
 *
 * @author Ferhat
 */
public class Projet_test {
    public static ArrayList<Theme> listeTheme;
    public static ArrayList<Ville> listeVille;
    public static int seq_theme;
    public static int seq_ville;
    
    public static void main(String[] args) throws Exception {
        listeVille = new Ville().loadVilleListe();
        listeTheme = new Theme().loadThemeListe();
        
        seq_theme = listeTheme.size();
        seq_ville = listeVille.size();
//        ArrayList<Monument> ls = Listes.getInstance().getMonumentList();
        Principale t = new Principale();
         t.setVisible(true);
//        System.out.println(ls.size());
//        Monument tab[] = new Monument[ls.size()];
//        for (int i = 0; i < ls.size(); i++) {
//            System.out.println("rrrr"+ls.get(i).get_nom());
//            tab[i] = ls.get(i);
//        }
//        System.out.println(tab.length);
//        oracle.sql.ArrayDescriptor  desar = new ArrayDescriptor("MONUMENTS_TYPE",DB.getInstance().get_connection());
//        oracle.sql.ARRAY ar =new ARRAY(desar, DB.getInstance().get_connection(), tab);
//        
//        System.out.println("arr= "+ar.getLength()+" list= "+listeTheme.size());
//        
//        ResultSet rs = ar.getResultSet();
//        rs.next();
//        String tx = rs.getString(1);
//        System.out.println("toz "+ rs.getObject(2));
//        Array toto = DB.getInstance().getArrayFromList("monuments_type()", ts);
  //      System.out.println(toto.toString());
        /*
        Theme theme = new Theme(seq_theme,"Pouf");
        seq_theme++;
        theme.save();
        System.out.println("ok");
        /*
        Audio pin = new Audio();
        pin.Load();
    
        
        ArrayList p = new MotCle().load();
        
        p.stream().forEach((e) -> {
            System.out.println(e.toString());
        });
         
       ArrayList t = new Theme().loadThemeListe();
       
       t.stream().forEach((_item) -> {
       System.out.println(_item.toString());});
           
       Monument mon = new Monument().load();
       Description desc = mon.get_description();
       // System.out.println("Nom = "+mon.get_nom()+" Type="+mon.get_type()+" desc= "+ desc.toString()+" "+mon.get_ville()+" "+mon.get_theme());
        
       File image = new File("moche.jpg");
        InputStream stream = new FileInputStream(image);
        byte[] imgdata = new byte[(int)image.length()];
        
         ST_Image im =  new ST_Image().getImageFromBlob();
        
        System.out.println("BLOB ==>" + im.get_blob().length()+"id ==>"+im.get_id());
        Image toto = getImageFromBlol(im.get_blob());
        System.out.println(toto.getWidth(null)+"X"+toto.getHeight(null));
              */
        }
    
        
    public static ArrayList decoupe(String Text,ArrayList result){
        if(Text.length()>1){
            String sub = Text.substring(0,Text.indexOf(","));
            result.add(sub);
            return decoupe(Text.substring(Text.indexOf(",")+1) , result);
        }
        return result;
    }

    public static BLOB getBlobFromStream(InputStream stream) throws SQLException, ClassNotFoundException, IOException {
        
        BLOB blob = BLOB.createTemporary(DB.getInstance().get_connection(), true, BLOB.DURATION_SESSION);
        blob.open(BLOB.MODE_READWRITE);
        OutputStream blobStream = blob.setBinaryStream(1L);

        int count;
        while((count = stream.available()) > 0) {
            byte[] b = new byte[count];
            stream.read(b);
            blobStream.write(b);
        }

        return blob;
    }
    
    public static BLOB getBlobFromImage(Image im) throws SQLException{
        BLOB blob = BLOB.createTemporary(DB.getInstance().get_connection(), true, BLOB.DURATION_SESSION);
        blob.open(BLOB.MODE_READWRITE);
        
        return blob;
    }
    
    public static Image getImageFromBlol(BLOB lobin) throws SQLException, IOException{
        //BufferedImage im = ImageIO.read(lobin.getBinaryStream(1L));
        return ImageIO.read(lobin.getBinaryStream(1L));
    }

   public static int getNextId(String Table) throws SQLException{
       OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
       OracleResultSet rs;
       int count =0;
       try {
        if(Table.equals("IMAGE")||Table.equals("AUDIO")||Table.equals("VIDEO")){
           rs =(OracleResultSet) st.executeQuery("select seq_media.nextval from dual");
           rs.next();
           count = rs.getInt(1);
        }
       else{
            rs = (OracleResultSet) st.executeQuery("SELECT COUNT(*) FROM "+Table+"");
            rs.next();
            count=rs.getInt(1);
        }     
        } catch (Exception ex) {
            Logger.getLogger(Projet_test.class.getName()).log(Level.SEVERE, null, ex);
        }
       return count++;
   }

}
    

 //System.out.println(desc.toString());
            //MotCle po= new MotCle(5,"pompom");
           //po.save();
           
           /*
           String req="SELECT value(t) from MOTCLE t";
           
           OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
           OracleResultSet rs = (OracleResultSet) st.executeQuery(req);
           
           while(rs.next()){
           MotCle mot = (MotCle)  rs.getObject(1,DB.getInstance().getMap());
           System.out.println(mot.toString());
           }
           */
//        try {
//           // OracleStatement st = (OracleStatement) DB.getInstance().setPreparedStatement("SELECT value(M) from MOTCLE M");
//            
//            Connection1 conn = new Connection1();
//            conn.connect();
//            OracleStatement st = (OracleStatement) conn.createStmt();
//            OracleResultSet rs = (OracleResultSet) st.executeQuery(req);
//            
//            Map<String, Class<?>> Mapping = null;
//            
////            Mapping.put("FY358838.MOTCLE_TYPE", Class.forName("Objets.MotCle"));
//            
////OracleResultSet rs = (OracleResultSet) db.executeQry(req);
////            PreparedStatement ps = DB.getInstance().setPreparedStatement(req);
////            DB.getInstance().executePreparedStatementSelect(ps);
//            
//            MotCle p = new MotCle();
//            String result="";
//            System.out.println("1");
//            while (rs.next()) {
//                
//                oracle.sql.STRUCT struct;
//                struct = rs.getSTRUCT(1);
//                Object tab[]= struct.getAttributes();
//                System.out.println(tab[1].toString());
//                result+=tab[1].toString();
//                result+="\n";
//                System.out.println(result);              
//                
//            }
//                     
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(Projet_test.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//    }
//    
    
 /*      
        BLOB lob = getBlobFromStream(stream);
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO imgst VALUES(?,?,ORDSYS.SI_STILLIMAGE(?))");
        ps.setInt(1, 2);
        ps.setString(2, "lol");
        ps.setBlob(3, lob);
        System.out.println(lob.toString()+" "+lob.length()+"--"+imgdata.length);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        */
        
        
       /*Image_ ima= new Image_();
       ima.load();
       //ima.save("moche.jpg");
       
//        String query = "SELECT * FROM imgst ph";
//        OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
//        OracleResultSet rs = (OracleResultSet) st.executeQuery(query);
//        BLOB lobs=(BLOB) DB.getInstance().getBlob() ;
//        OrdImage im=null;
//        Struct struct = null;
//       while(rs.next()){
//           struct =  rs.getSTRUCT(3);
//       }
//        System.out.println("///"+struct.getSQLTypeName());
//        //lobs = (BLOB) im.getBlobContent();
//        System.out.println(lobs.length());
//        try (FileOutputStream fi = new FileOutputStream("out2.jpg")) {
//            byte [] lo =lobs.getBytes(1L,(int) lobs.length());
//            fi.write(lo);
//            fi.close();
//        }
        
        
        //Recuperation OK
        /*
        
        
        String query = "SELECT * FROM imgst ph";
        OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
        OracleResultSet rs = (OracleResultSet) st.executeQuery(query);
        OrdImage im=null;
        STRUCT stillimage = null;
        while(rs.next()){
           stillimage = (STRUCT) rs.getSTRUCT(3);
        }
        BLOB lob = (BLOB) ((STRUCT) stillimage.getAttributes()[0]).getAttributes()[0];
        System.out.println(lob.length());
        byte[] data = lob.getBytes(1L, (int) lob.length());
        
        FileOutputStream fos = new FileOutputStream("BLOB.jpg");
        fos.write(data);
        fos.close();
        
        /*
        try (FileOutputStream fi = new FileOutputStream("out2.jpg")) {
            byte [] lo =lobs.getBytes(1L,(int) lobs.length());
            fi.write(lo);
            fi.close();
        }
        
        /*
        File image = new File("moche.jpg");
        InputStream stream = new FileInputStream(image);
        byte[] imgdata = new byte[(int)image.length()];
               
        BLOB lob = getBlobFromStream(stream);
        PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO imgst VALUES(?,?,ORDSYS.SI_STILLIMAGE(?))");
        ps.setInt(1, 2);
        ps.setString(2, "lol");
        ps.setBlob(3, lob);
        System.out.println(lob.toString()+" "+lob.length()+"--"+imgdata.length);
        DB.getInstance().executePreparedStatement(ps);
        DB.getInstance().commit();
        
        
        
        InputStream inss;        
        String query = "SELECT * FROM imgst ph";
        OracleStatement st = (OracleStatement) DB.getInstance().getstatement();
        OracleResultSet rs = (OracleResultSet) st.executeQuery(query);
        
        while(rs.next()){
        
        BLOB blob = (BLOB) DB.getInstance().getBlob();
        int i = (int) rs.getInt(1);
        String toto =(String) rs.getString(2);
        toto += rs.getString(3);
        
       // blob =  (oracle.sql.BLOB) ((OracleResultSet) rs).getBLOB("IMG");
        System.out.println(i+"...."+toto);
        }
        /*
        for (int i = 0; i < 10; i++) {
            System.out.println(blobdata[i] +"<===>"+imgdata[i] );
        }
        
        try (FileOutputStream fi = new FileOutputStream("out.jpg")) {
            fi.write(blobdata);
            fi.close();
            /*
            */
            //inblob = rset.getBLOB(1).getBinaryStream(1L);
            //Struct stru = rset.getSTRUCT(1);
            //Object tab[] = stru.getAttributes();
            //System.out.println(tab[0].toString()+" "+tab[1].toString());
            //inblob.read(B.getBytes(1L, (int) B.length()));
            
            /*
            String query = "SELECT ph.img FROM imgst ph WHERE ph.id= 1 FOR UPDATE";
            OracleResultSet rset = (OracleResultSet) DB.getInstance().executeOracleQuery(query);
            rset.next();
            /*
            
            //ImageIcon ii = new ImageIcon("moche.jpg");
            
            
            Image img = ImageIO.read(new File("moche.jpg"));
            
            JFrame pan = new JFrame();
            
            pan.setSize(img.getHeight(pan), img.getWidth(pan));
            pan.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // pan.setVisible(true);
            
            PreparedStatement ps = DB.getInstance().setPreparedStatement("INSERT INTO imgst VALUES(?,?,NULL)");
            ps.setInt(1, 1);
            ps.setString(2, "Moche");
            DB.getInstance().executePreparedStatement(ps);
            DB.getInstance().commit();
            BLOB lobs= null;
            String query = "SELECT ph.img FROM imgst ph WHERE ph.id= 1 FOR UPDATE";
            OracleResultSet rset = (OracleResultSet) DB.getInstance().executeOracleQuery(query);
            rset.next();
            try{
            
            lobs = BLOB.createTemporary(DB.getInstance().get_connection(), true, BLOB.DURATION_SESSION);
            lobs.open(BLOB.MODE_READWRITE);
            
            
            
            OraclePreparedStatement pstm = DB.getInstance().executeOraclePreparedStatement("UPDATE imgst ph SET ph.img=ORDSYS.SI_STILLIMAGE(?) WHERE ph.id=?");
            //lobs= (BLOB) set_image(imagedata);
            lobs.setBytes(1L, imagedata);
            pstm.setBlob(1, lobs);
            pstm.setInt(2, 1);
            pstm.execute();
            }
            catch(SQLException ex){
            }
            
            
            ObjectOutputStream oos;
            oos = new ObjectOutputStream(lob.setBinaryStream(1));
            oos.writeObject(ii);
            
            ps.setBlob(1, lob);
            
        */  
