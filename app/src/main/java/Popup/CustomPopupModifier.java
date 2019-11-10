package Popup;

import android.app.Activity;
import android.app.Dialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.polytoic.R;

public class CustomPopupModifier extends Dialog {

    private Button valider;
    private Button annuler;

    private String nom;
    private int ligne;
    private int colonne;

    private EditText n;
    private EditText l;
    private EditText c;


    public String getNom() {
        return nom;
    }

    public int getLigne() {
        return ligne;
    }

    public int getColonne() {
        return colonne;
    }

    public EditText getN() {
        return n;
    }

    public EditText getL() {
        return l;
    }

    public EditText getC() {
        return c;
    }

    public void setNom(String nom) {
        this.nom = nom;
        this.n.setText(nom.toString());
    }

    public void setLigne(int ligne) {
        this.ligne = ligne;
        this.l.setText(String.valueOf(ligne).toString());
    }

    public void setColonne(int colonne) {
        this.colonne = colonne;
        this.c.setText(String.valueOf(colonne).toString());

    }

    public void setN(EditText n) {
        this.n = n;
    }

    public void setL(EditText l) {
        this.l = l;
    }

    public void setC(EditText c) {
        this.c = c;
    }

    public CustomPopupModifier(Activity activity){
        super(activity,R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_modifier);

        this.valider=findViewById(R.id.popup_btn_valider_modifier);
        this.annuler=findViewById(R.id.popup_btn_annuler_modifier);
        this.n=findViewById(R.id.popup_nom_matrice_modifier);
        this.l= findViewById(R.id.popup_ligne_modifier);
        this.c=findViewById(R.id.popup_colonne_modifier);


    }

    public Button getValider(){
        return valider;
    }

    public Button getAnnuler(){
        return annuler;
    }

    public void build(){
        show();
    }
}
