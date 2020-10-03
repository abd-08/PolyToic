package Popup;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.polytoic.R;

import Model.Mode;

public class PopupAjouterQuestionnaire extends Dialog {

    private Button valider;
    private Button annuler;
    private EditText n;
    private EditText ligne;

    public EditText getN() {
        return n;
    }

    public EditText getLigne() {
        return ligne;
    }


    public PopupAjouterQuestionnaire(Activity activity , Mode mode ){

        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_ajouter);
        this.valider=findViewById(R.id.popup_btn_valider);
        this.annuler=findViewById(R.id.popup_btn_annuler);
        this.n=findViewById(R.id.popup_nom_matrice);
        this.ligne=findViewById(R.id.popup_ligne);

        if (mode.equalMode(Mode.EXAMEN)) {
            this.ligne.setVisibility(View.INVISIBLE);
        }
        else if (mode.equalMode(Mode.ENTRAINEMENT)) {
            this.ligne.setVisibility(View.VISIBLE);
        }
        else{
            this.valider.setVisibility(View.INVISIBLE);
        }
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
