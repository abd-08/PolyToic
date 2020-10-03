package adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polytoic.R;
import java.util.ArrayList;

import Model.Question;

public class QuestionAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public ArrayList<Question> liste_question;
    Context context;
    boolean active,correction,resultat;



    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int nbselect(){
        int res=0;
        for(int i=0;i<liste_question.size();i++){
            if ( liste_question.get(i).isSelect()) res++;
        }
        return  res;
    }


    public boolean isCorrection() {
        return correction;
    }

    public void setCorrection(boolean correction) {
        this.correction = correction;
    }

    public boolean isResultat() {
        return resultat;
    }

    public void setResultat(boolean resultat) {
        this.resultat = resultat;
    }

    public QuestionAdapter(Context context, ArrayList<Question> liste_question ) {

        this.liste_question = liste_question;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.active=false;
        this.correction=false;
        this.resultat=false;
    }


    public void setListe_question(ArrayList<Question> liste_question) {
        this.liste_question = liste_question;
    }

    @Override
    public int getCount() {
        return this.liste_question.size();
    }

    @Override
    public Question getItem(int position) {
        return this.liste_question.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_question,null);

        final Question question = getItem(position);
        TextView itemNameView = convertView.findViewById(R.id.question_numero);
        itemNameView.setText(String.valueOf(question.getNumero()));

        int img = R.drawable.disque;
        if (correction ) img = R.drawable.disque_green;


        final ImageButton a = convertView.findViewById(R.id.reponse_A);
        final ImageButton b = convertView.findViewById(R.id.reponse_B);
        final ImageButton c = convertView.findViewById(R.id.reponse_C);
        final ImageButton d = convertView.findViewById(R.id.reponse_D);
        final TextView lettreA = convertView.findViewById(R.id.lettreA);
        final TextView lettreB = convertView.findViewById(R.id.lettreB);
        final TextView lettreC = convertView.findViewById(R.id.lettreC);
        final TextView lettreD = convertView.findViewById(R.id.lettreD);
        final LinearLayout couleur_question = convertView.findViewById(R.id.couleur_question);
        couleur_question.setBackgroundColor(Color.rgb(255,255,255));
        if (question.isA()) a.setBackgroundResource(img);
        if (question.isB()) b.setBackgroundResource(img);
        if (question.isC()) c.setBackgroundResource(img);
        if (question.isD()) d.setBackgroundResource(img);





        if (resultat){
            int correct = Color.argb(255,0,255 ,0);
            couleur_question.setBackgroundColor(correct);
            switch (question.getBonneReponse()){
                case "a" :
                    lettreA.setText("V");
                    lettreA.setTextColor(correct);
                    break;

                case "b" :
                    lettreB.setText("V");
                    lettreB.setTextColor(correct);
                    break;
                case "c" :
                    lettreC.setText("V");
                    lettreC.setTextColor(correct);
                    break;
                case "d" :
                    lettreD.setText("V");
                    lettreD.setTextColor(correct);
                    break;
                default:
                    Log.i("ADAPTER CORRECTION:","QUESTION SANS REPONSE");break;
            }
            if (!question.isCorrect()){ // si la reponse est bonne
                int incorrect = Color.argb(255,255,0 ,0);
                couleur_question.setBackgroundColor(incorrect);
                switch (question.whoIs()){
                    case "a" :
                        lettreA.setText("X");
                        lettreA.setTextColor(incorrect);
                        break;

                    case "b" :
                        lettreB.setText("X");
                        lettreB.setTextColor(incorrect);
                        break;
                    case "c" :
                        lettreC.setText("X");
                        lettreC.setTextColor(incorrect);
                        break;
                    case "d" :
                        lettreD.setText("X");
                        lettreD.setTextColor(incorrect);
                        break;
                    default:
                        Log.i("ADAPTER CORRECTION:","QUESTION SANS REPONSE");break;

                }
            }
        }



        if (active){
            final int finalImg = img;
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    question.setA(true);
                    a.setBackgroundResource(finalImg);
                    b.setBackgroundResource(R.drawable.cercle);
                    c.setBackgroundResource(R.drawable.cercle);
                    d.setBackgroundResource(R.drawable.cercle);
                    notifyDataSetChanged();

                }
            });

            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    question.setB(true);
                    a.setBackgroundResource(R.drawable.cercle);
                    b.setBackgroundResource(finalImg);
                    c.setBackgroundResource(R.drawable.cercle);
                    d.setBackgroundResource(R.drawable.cercle);
                    notifyDataSetChanged();
                }
            });
            c.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    question.setC(true);
                    a.setBackgroundResource(R.drawable.cercle);
                    b.setBackgroundResource(R.drawable.cercle);
                    c.setBackgroundResource(finalImg);
                    d.setBackgroundResource(R.drawable.cercle);
                    notifyDataSetChanged();
                }
            });
            d.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    question.setD(true);
                    a.setBackgroundResource(R.drawable.cercle);
                    b.setBackgroundResource(R.drawable.cercle);
                    c.setBackgroundResource(R.drawable.cercle);
                    d.setBackgroundResource(finalImg);
                    notifyDataSetChanged();
                }
            });
        }




        return convertView;
    }
   }
