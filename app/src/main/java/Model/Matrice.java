package Model;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

public class Matrice  {
    String Nom;
    Boolean fini;

    String Etat;
    ArrayList<Ligne> List;
    private final static char [] alphabet = {'a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    public char getAlphabet(int position){
        return alphabet[position];
    }

    public Matrice(String nom){
        this.Nom=nom;
        List=new ArrayList<Ligne>();
        fini=false;
    }

    public Matrice(String nom,ArrayList<Ligne> list){
        this.Nom=nom;
        this.List=list;
        fini=false;
    }


    public Matrice(String nom,int ligne,int colonne){
        Ligne line;
        this.Nom=nom;
        this.List=new ArrayList<Ligne>();
        for (int i=0;i<ligne;i++){
            line = new Ligne(colonne);
            this.List.add(line);
        }
        fini=false;
    }




    public boolean SelectCellule(float x,float y){
        for (int i=0;i<this.getList().size();i++){
            for (int j=0;j<this.getList().get(i).taille();j++){
                if (getCellulePosition(i,j).esDansCellule(x,y,50)){
                    if ( getCellulePosition(i,j).isSelected()){
                        getCellulePosition(i,j).selected=false;
                    }
                    else  getCellulePosition(i,j).selected=true;

                    return true;
                }
            }
        }
        return false;
    }

    public void modifier(int line,int column){
        //lorsqu'on doit reduire le nombre de ligne
        if (getLigne()>line) this.reduire(this.List.size()-line);
        else
            //lorsqu'on doit augmenter le nombre de ligne
            if (getLigne() <line) this.augmenter(line-this.List.size());

        for (int i = 0; i<this.List.size();i++){
            getLignePosition(i).modifier(column);
        }
    }

    public void reduire(int quantite){
        int last = this.List.size();
        for (int i=0;i<quantite;i++) {
            getList().remove(last - 1);
            last--;
        }
    }

    public void augmenter(int quantite){
        Ligne l;
        for (int i=0;i<quantite;i++){
            l=new Ligne(getColonne());
            getList().add(l);
        }
    }


    //fonction qui transforme une matrice en objet JSon
    public JSONObject transformer(){
        JSONObject jsonObject=new JSONObject();
        JSONArray Liste=new JSONArray();


        //pour chaque ligne
        for (int i=0;i<this.getLigne();i++){
            JSONArray ligne=new JSONArray();

            //Pour chaque cellule
            for (int j=0;i<this.getColonne();j++){
                JSONObject cellule = new JSONObject();
                try {
                    cellule.put("x",i);
                    cellule.put("y",j);
                    cellule.put("selected",this.getCellulePosition(i,j).isSelected());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ligne.put(cellule);
            }
            Liste.put(ligne);
        }

        try{
            jsonObject.put("nom",this.Nom);
            jsonObject.put("tableau",Liste);
        }
        catch
        (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }


    //fonction qui renvoi une ligne de position i
    public Ligne getLignePosition(int i){
        return List.get(i);
    }

    //fonction qui renvoie une cellule de poosition i,j
    public Cellule getCellulePosition(int i , int j){
        return getLignePosition(i).getCellule(j);
    }


    public void setNom(String nom) {
        this.Nom = nom;
    }

    public String getNom() {
        return this.Nom;
    }

    public int getLigne(){
        return this.List.size();
    }

    public int getColonne(){
        return this.List.get(0).taille();
    }

    public ArrayList<Ligne> getList() {
        return List;
    }

    public void setList(ArrayList<Ligne> list) {
        List = list;
    }

    public ArrayList<Cellule> getListeCellule(){
        //fonction qui renvoi la matrice sous forme de list

        ArrayList<Cellule> list = new ArrayList<>();
        Cellule cellule;
        for (int i=0;i<getLigne();i++) {
            for (int j = 0; j < getColonne(); j++) {
                cellule=getCellulePosition(i,j);
                list.add(cellule);
            }
        }
        return list;
    }




}
