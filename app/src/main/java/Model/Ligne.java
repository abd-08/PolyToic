package Model;

import java.util.ArrayList;

public class Ligne {
    ArrayList<Cellule> ligne;

    public Ligne(){
        Cellule c;
        this.ligne=new ArrayList<Cellule>();
    }

    public Ligne(int taille){
        Cellule c;
        this.ligne=new ArrayList<Cellule>();
        for (int i=0;i<taille;i++){
            c=new Cellule(0,0);
            this.ligne.add(c);
        }
    }


    public void add(Cellule c){
        this.ligne.add(c);
    }

    public void reduire(int quantite){
        int last = this.ligne.size();
        for (int i=0;i<quantite;i++) {
            this.ligne.remove(last - 1);
            last--;
        }
    }

    public void augmenter(int quantite){
        Cellule c;
        for (int i=0;i<quantite;i++){
            c=new Cellule(0,0);
            this.ligne.add(c);
        }
    }

    public void modifier(int nb){
        if (this.ligne.size()<nb) this.augmenter(nb-this.ligne.size());
        else if (this.ligne.size()>nb) this.reduire(this.ligne.size()-nb);
        else this.ligne.size();
    }

    public int taille (){
        return this.ligne.size();
    }


    public ArrayList<Cellule> getLigne() {
        return ligne;
    }

    public Cellule getCellule(int j) {
        return this.ligne.get(j);
    }

    public void deselect(int position){
        for(int i=0;i<ligne.size();i++){
            if (i!=position)  getCellule(i).deselect();

        }
    }


    public boolean Compare(Ligne l){
        for (int i = 0 ;i<ligne.size();i++){
            if (l.getCellule(i).isSelected() != this.getCellule(i).isSelected()) return  false;
        }
        return true;
    }
}
