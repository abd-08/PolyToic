package Model;

import android.graphics.Color;
import android.graphics.ColorSpace;

import static java.lang.Math.sqrt;

public class Cellule {
    public boolean selected;
    public float x,y;
    public ColorSpace couleur ;


    public Cellule(float x,float y) {
        this.x=x;
        this.y=y;
        this.selected = false;
    }
    public Cellule(float x,float y,boolean b) {
        this.x=x;
        this.y=y;
        this.selected = b;
    }

    public Cellule(boolean b) {
        this.selected = b;
    }



    public boolean esDansCellule(float i ,float j,float rayon){
        double distance = sqrt((this.x-i)*(this.x-i) + (this.y-j)*(this.y-j));
        if (distance<=rayon ) {
            return true;
        }
        else return false;

    }

    public boolean isSelected() {
        return this.selected;
    }


    public void Clicked(){
        if (isSelected()) deselect();
        else select();
    }

    public void select(){
        this.selected=true;
    }

    public void deselect(){
        this.selected=false;
    }

    public void setCouleur(ColorSpace couleur) {
        this.couleur = couleur;
    }
}
