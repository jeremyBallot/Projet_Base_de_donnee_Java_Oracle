package Objets;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ferhat
 */
public final class Listes extends Thread{
    private static Listes instance;
    private ArrayList<Monument> MonumentList;
    private ArrayList<Theme> ThemeList;
    private ArrayList<Ville> VilleList;
    private ArrayList<Photo> PhotoList;
    //private ArrayList<Media> PhotoList;
    private ArrayList<MotCle> MotCleList;
    
    public static Listes getInstance()
    {
        if(instance==null)
            instance =new Listes();
        return instance;
    }
    
    public ArrayList<Monument> getMonumentList()
    {
        return MonumentList;
    }
    
    public ArrayList<Ville> getVilleList()
    {
        return VilleList;
    }
    
    public ArrayList<Theme> getThemeList()
    {
        return ThemeList;
    }
    
    public void setMonumentList(ArrayList<Monument> newList){
        this.MonumentList = newList;
    }
    
    public void setVilleList(ArrayList<Ville> newList){
        this.VilleList  = newList;
    }
    public void setThemeList(ArrayList<Theme> newList){
        this.ThemeList=newList;
    }
    
    public int getNextMonumentId(){
        return MonumentList.size();
    }
    
    public int getNextVilleId(){
        return VilleList.size();
    }
    
    public int getNextthemeId(){
        return ThemeList.size();
    }
    
    @Override
    public void run(){
    try {
            
            //MonumentList = new Monument().loadAll();
            ThemeList = new Theme().loadThemeListe();
            VilleList = new Ville().loadVilleListe();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Listes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private Listes(){
        run();
                 
     }
}
