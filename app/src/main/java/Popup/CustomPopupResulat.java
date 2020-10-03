package Popup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.polytoic.R;

import Model.Questionnaire;

public class CustomPopupResulat  extends Dialog {
    private ImageView fermer;
    private ImageView recommencer;
    private TextView resultat;
    private TextView affiche_grille;
    private TextView recompense;
    private TextView reading;
    private TextView listening;
    private TextView str_reading;
    private TextView str_listening;
    Questionnaire questionnaire;
    int note;
    int global;


    public ImageView getFermer() {
        return fermer;
    }

    public ImageView getRecommencer() { return recommencer; }


    public  String interpretationResultat(){
        double result = this.note*100/this.global;
        Log.i("RESULTAT ",result+"");
        Log.i("NOTE ",this.note+"");
        Log.i("GLOBAL ",this.global+"");
        if (result>=95)return "Excellent";
        else if (result>=80) return "Très bien";
        else if (result>=70) return " bien";
        else if (result>60) return "Assez bien";
        else return "Echec , veuillez recommencer";
    }

    public CustomPopupResulat(Activity activity , Questionnaire q) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.fragment_resultat);
        fermer=findViewById(R.id.fermer_resultat);
        resultat=findViewById(R.id.resultat_text);
        recommencer=findViewById(R.id.recommencer_resultat);
        recompense = findViewById(R.id.recompense_text);
        reading = findViewById(R.id.resultat_reading);
        listening = findViewById(R.id.resultat_listening);
        str_reading = findViewById(R.id.str_reading_score);
        str_listening = findViewById(R.id.str_listening_score);
        //affiche_grille=findViewById(R.id.resultat_afficher);
        this.questionnaire = q ;
        this.note = questionnaire.Corriger();
        this.global=questionnaire.getNOMBRE_QUESTION();
        this.resultat.setText(this.note+"/"+this.global);
        this.recompense.setText(interpretationResultat());

        if (questionnaire.getReading().size()>0){
            this.listening.setVisibility(View.VISIBLE);
            this.str_listening.setVisibility(View.VISIBLE);
            this.listening.setText(questionnaire.getListening_resultat()+"");
            this.reading.setVisibility(View.VISIBLE);
            this.str_reading.setVisibility(View.VISIBLE);
            this.reading.setText(questionnaire.getReading_resultat()+"");
        }
        else {
            this.listening.setVisibility(View.INVISIBLE);
            this.str_listening.setVisibility(View.INVISIBLE);
            this.str_reading.setVisibility(View.INVISIBLE);
            this.reading.setVisibility(View.INVISIBLE);
        }

    }


    public void build(){
        show();
    }

}
