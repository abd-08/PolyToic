package Popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.polytoic.R;

public class CustomPopupResulat  extends Dialog {
    private Button fermer;
    private TextView resultat;
    private TextView affiche_grille;
    String note;

    public void setNote(String note) {
        this.note = note;
        resultat.setText(note);
    }



    public Button getFermer() {
        return fermer;
    }

    public TextView getResultat() {
        return resultat;
    }

    public TextView getAffiche_grille() {
        return affiche_grille;
    }


    public CustomPopupResulat(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.popup_resultat);
        resultat=findViewById(R.id.resultat_text);
        affiche_grille=findViewById(R.id.resultat_afficher);
    }


    public void build(){
        show();
    }

}
