package Helper;

import java.util.ArrayList;

import Model.Cellule;
import Model.Matrice;

public class Other {


    private final static char [] alphabet = {'a','b','c','d','e','f','g','h','i',
            'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};


    public static char getAlphabet(int position){ return alphabet[position]; }
    public static int convertLigne(int position, int colonne){
        return position/colonne;
    }
    public static int convertColonne(int position, int colonne){
        return position%colonne;
    }






    public ArrayList<Cellule> getListeCellule(Matrice matrice){
        //fonction qui renvoi la matrice sous forme de list

        ArrayList<Cellule> list = new ArrayList<>();
        Cellule cellule;
        for (int i=0;i<matrice.getLigne();i++) {
            for (int j = 0; j < matrice.getColonne(); j++) {
                cellule=matrice.getCellulePosition(i,j);
                list.add(cellule);
            }
        }
        return list;
    }
}
