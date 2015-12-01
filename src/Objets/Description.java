
package Objets;

import java.sql.SQLData;
import java.sql.SQLException;
import java.sql.SQLInput;
import java.sql.SQLOutput;

/**
 *
 * @author Ferhat
 */
public class Description implements SQLData{
    private String Classement;
    private String constructeur;
    private String adresse;
    private String commentaire;

    public Description(String clas,String adr,String constr,String comment){
        this.Classement=clas;
        this.adresse=adr;
        this.constructeur=constr;
        this.commentaire=comment;
    }
    public Description(){
        this.Classement = "Non Renseigné";
        this.adresse = "Non Renseigné";
        this.constructeur = "Non Renseigné";
        this.commentaire = "Non Renseigné";
    }
    
    public String get_adresse(){
        return this.adresse;
    }
    public void set_adresse(String adr){
        this.adresse=adr;
    }

    public String get_classement(){
        return this.Classement;
    }

    public void set_classement(String cla){
        this.Classement=cla;
    }
    
    public String get_constructeur(){
        return this.constructeur;
    }

    public void set_constructeur(String cla){
        this.constructeur=cla;
    }
    
    public String get_commentaire(){
        return this.commentaire;
    }

    public void set_commentaire(String cla){
        this.commentaire=cla;
    }
    
    @Override
    public String toString(){
        return this.constructeur+" "+this.Classement+" "+this.commentaire+" "+this.adresse;
    }
    
   @Override
    public String getSQLTypeName(){
        return "DESCRIPTION_TYPE";
    }
    
    @Override
    public void readSQL(SQLInput stream,String typeName) throws SQLException{
        this.set_classement(stream.readString());
        this.set_constructeur(stream.readString());
        this.set_adresse(stream.readString());
        this.set_commentaire(stream.readString());
    }
    
    @Override
    public void writeSQL(SQLOutput stream) throws SQLException{
        stream.writeString(this.get_classement());
        stream.writeString(this.get_constructeur());
        stream.writeString(this.get_adresse());
        stream.writeString(this.get_commentaire());
    }
}
