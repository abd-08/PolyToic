package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.polytoic.R;

import java.util.ArrayList;

import Model.Etat;
import Model.Mode;
import Model.Questionnaire;

public class MyspaceAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public ArrayList<Questionnaire> liste_question;
    Context context;




    public MyspaceAdapter(Context context, ArrayList<Questionnaire> liste_question ) {

        this.liste_question = liste_question;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    public void setListe_question(ArrayList<Questionnaire> liste_question) {
        this.liste_question = liste_question;
    }

    public int getCount() {
        return this.liste_question.size();
    }

    public Questionnaire getItem(int position) {
        return this.liste_question.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }


    public String transformeString(String string , int n){
        if (string.length() == n) return string;
        else if (string.length()>n) return string.substring(0,n);
        else {
            String res = string;
            for (int i=0;i < n-string.length()-1 ; i++){
                res=res+" ";
            }
            return res;
        }

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_item_myspace,null);

        final Questionnaire questionnaire = getItem(position);
        TextView itemNameView = convertView.findViewById(R.id.nom_du_test);
        TextView score = convertView.findViewById(R.id.score_du_test);
        TextView type = convertView.findViewById(R.id.type_du_test);
        TextView etat = convertView.findViewById(R.id.etat_du_test);

        itemNameView.setText(transformeString(questionnaire.getNom(),10));
        type.setText(transformeString( Mode.getMODE(questionnaire.getType()),6));
        etat.setText(transformeString( Etat.Affiche( questionnaire.getEtat()),6));



        if ( questionnaire.correctionExiste()){
            score.setText(transformeString( questionnaire.Corriger()+"",3)+" / "+ transformeString(questionnaire.getNOMBRE_QUESTION()+"",3));
        }
        else  score.setText(" - / "+transformeString( questionnaire.getNOMBRE_QUESTION()+"",3));

        return convertView;
    }
}
