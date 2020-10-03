package Popup;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.polytoic.R;
import java.util.ArrayList;
import java.util.Comparator;
import Model.Etat;
import Model.Mode;
import Model.Questionnaire;
import adapter.MyspaceAdapter;

public class PopupTriFiltre  extends Dialog {

Activity activity;
MyspaceAdapter adapter;
ArrayList<Questionnaire> list;
ArrayList<Questionnaire> all;
RadioGroup radio_croisant_decroissant;
RadioGroup radio_tri_par;
RadioGroup radio_filtre;
RadioButton croisssant;
RadioButton decroisssant;
RadioButton radio_nom;
RadioButton radio_type;
RadioButton radio_etat;
RadioButton radio_score;
RadioButton radio_dernier_modif;
RadioButton radio_filtre_aucun;
RadioButton radio_filtre_examen;
RadioButton radio_filtre_entrainement;
RadioButton radio_filtre_en_cours;
RadioButton radio_filtre_termine;
RadioButton radio_filtre_debut;
Button valide;
int checkidcroissance;
int checkidtri;
int checkidfiltre;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public PopupTriFiltre(Activity activity , MyspaceAdapter myspaceAdapter, final ArrayList<Questionnaire> list , final ArrayList<Questionnaire> exam , final ArrayList<Questionnaire> train , int croissance , int tri, int filtre   ) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        this.activity=activity;
        this.adapter = myspaceAdapter;
        this.list=list;
        this.all=new ArrayList<>();
        this.all.addAll(exam);
        this.all.addAll(train);
        setContentView(R.layout.popup_filter);
        radio_croisant_decroissant = findViewById(R.id.radio_croissant_decroissant);
        radio_tri_par = findViewById(R.id.radio_tri_par);
        radio_filtre = findViewById(R.id.radio_filtrer);
        croisssant = findViewById(R.id.radio_croissant);
        decroisssant = findViewById(R.id.radio_decroissant);
        radio_nom = findViewById(R.id.radio_nom);
        radio_type = findViewById(R.id.radio_type);
        radio_etat = findViewById(R.id.radio_etat);
        radio_score = findViewById(R.id.radio_score);
        radio_dernier_modif = findViewById(R.id.radio_dernier_modif);
        radio_filtre_aucun = findViewById(R.id.radio_filtre_aucun);
        radio_filtre_examen = findViewById(R.id.radio_filtre_examen);
        radio_filtre_entrainement = findViewById(R.id.radio_filtre_entrainement);
        radio_filtre_en_cours = findViewById(R.id.radio_filtre_en_cours);
        radio_filtre_termine = findViewById(R.id.radio_filtre_termine);
        radio_filtre_debut = findViewById(R.id.radio_filtre_debut);
        valide = findViewById(R.id.radio_valide);
        this.checkidtri = tri;
        this.checkidcroissance = croissance;
        this.checkidfiltre = filtre;

        restoreSelection(this.checkidcroissance , this.checkidtri , this.checkidfiltre);

        this.radio_croisant_decroissant.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if ((checkedId==R.id.radio_croissant  && checkidcroissance!=R.id.radio_croissant)
                || (checkedId==R.id.radio_decroissant  && checkidcroissance!=R.id.radio_decroissant)){
                    checkidcroissance = checkedId;
                    reverse(list);
                }
                else Log.i("R CROISSANT ","FALSE");
            }
        });

        this.radio_tri_par.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doSortChanged(group,checkedId);
            }
        });
        this.radio_filtre.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doFilterChanged(group,checkedId);
            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void restoreSelection (int croissance , int tri , int filtre){

        Log.e("FILTRE","  DO FILTRE ");
        switch (filtre){
            case R.id.radio_filtre_aucun:
                this.radio_filtre_aucun.setChecked(true);
                break;
            case R.id.radio_filtre_debut:
                this.radio_filtre_debut.setChecked(true);
                break;
            case R.id.radio_filtre_en_cours:
                this.radio_filtre_en_cours.setChecked(true);
                break;
            case R.id.radio_filtre_entrainement:
                this.radio_filtre_entrainement.setChecked(true);
                break;
            case R.id.radio_filtre_examen:
                this.radio_filtre_examen.setChecked(true);
                break;
            case R.id.radio_filtre_termine:
                this.radio_filtre_termine.setChecked(true);
                break;
            default:
                Log.i("FILTRE","  TEsT NOK : ");
                break;
        }
        Log.e("FILTRE","  DO TRI ");



        switch (tri){
            case R.id.radio_nom:
                this.radio_nom.setChecked(true);
                break;
            case R.id.radio_etat:
                this.radio_etat.setChecked(true);
                break;
            case R.id.radio_type:
                this.radio_type.setChecked(true);
                break;
            case R.id.radio_dernier_modif:
                this.radio_dernier_modif.setChecked(true);
                break;
            case R.id.radio_score:
                this.radio_score.setChecked(true);
                break;
            default:
                Log.i("TRIAGE ","  TEsT NOK : ");
                break;
        }

        Log.e("tri","  DO CROISSANCE ");

        doFilterChanged(radio_filtre,filtre);
        doSortChanged(radio_tri_par,tri);
        switch (croissance){
            case R.id.radio_croissant:
                Log.i("CROISSANCE","  TEsT OK : "+croissance);
                this.croisssant.setChecked(true);
                break;
            case R.id.radio_decroissant:
                Log.i("DECROISSANCE","  TEsT OK : ");
                this.decroisssant.setChecked(true);
                reverse(this.list);
                break;

            default:
                Log.i("CROISSANCE","  NOK NOK : ");
                break;
        }

        this.adapter.notifyDataSetChanged();
    }

    public void reverse(ArrayList<Questionnaire> list){
        Questionnaire q;
        for(int i=0 ; i<list.size()/2+list.size()%2 ; i++){
            q = list.get(i);
            list.set(i,list.get(list.size()-1-i));
            list.set(list.size()-1-i,q);
        }
        adapter.notifyDataSetChanged();
    }

    public void filtreMode(int mode ){
        for (int i=list.size()-1;i>-1;i--){
            if (list.get(i).getMode()!=mode){
                list.remove(i);
                Log.i("REMOVE  : "+i," : MODE   : "+Mode.getMODE(mode)+ "taille liste: "+list.size());
            }
        }
    }

    public void filtreEtat(int etat){
        for (int i=list.size()-1;i>-1;i--){
            if (list.get(i).getEtat()!=etat){
                list.remove(i);
                Log.i("REMOVE  : "+i," : TRUE   : "+Etat.Affiche(etat)+ "taille liste: "+list.size());
            }

        }
    }

    public void filtreEtat(int correction,int resultat){
        for (int i=list.size()-1;i>-1;i--){
            if (list.get(i).getEtat()!=correction || list.get(i).getEtat()!=resultat){
                list.remove(i);
                Log.i("REMOVE  : "+i," : TRUE   : "+Etat.Affiche(correction)+"  ;"+ Etat.Affiche(resultat)   + "taille liste: "+list.size());
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doFilterChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();
        list.removeAll(list);
        list.addAll(all);
        checkidfiltre = checkedRadioId;
        switch (checkedRadioId){
            case R.id.radio_filtre_aucun:
                break;
            case R.id.radio_filtre_debut:
                filtreEtat(Etat.DEBUT_RESULT);
                break;
            case R.id.radio_filtre_en_cours:
                filtreEtat(Etat.EN_COURS_RESULT);
                break;
            case R.id.radio_filtre_termine:
                filtreEtat(Etat.AFFICHE_RESULTAT ,Etat.CORRECTION_RESULT);
                break;
            case R.id.radio_filtre_examen:
                filtreMode(Mode.EXAMEN );
                break;
            case R.id.radio_filtre_entrainement:
                filtreMode(Mode.ENTRAINEMENT );
                break;
            default:
                Log.i("DOFILTRE","PAS DE FILTRE");
                break;
        }
        this.adapter.notifyDataSetChanged();
    }


    public Activity getActivity() {
        return activity;
    }

    public int getCheckidcroissance() {
        return checkidcroissance;
    }

    public int getCheckidtri() {
        return checkidtri;
    }

    public int getCheckidfiltre() {
        return checkidfiltre;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doSortChanged(RadioGroup group, int checkedId) {
        int checkedRadioId = group.getCheckedRadioButtonId();

        checkidtri=checkedRadioId;
        switch (checkedRadioId){
            case R.id.radio_nom:
                this.list.sort(new Comparator<Questionnaire>() {
                    @Override
                    public int compare(Questionnaire o1, Questionnaire o2) {
                        return o1.getNom().toLowerCase().compareTo(o2.getNom());
                    }
                });
                break;

            case R.id.radio_type:
                this.list.sort(new Comparator<Questionnaire>() {
                    @Override
                    public int compare(Questionnaire o1, Questionnaire o2) {
                        if (o1.getMode()==o2.getMode()) {
                            return o1.getNom().toLowerCase().compareTo(o2.getNom());//on compare en deuxieme lieu par nom
                        }
                        else if (o1.getMode()>o2.getMode())return 1;
                        else return -1;
                    }
                });
                break;
                case R.id.radio_etat:
                    this.list.sort(new Comparator<Questionnaire>() {
                        @Override
                        public int compare(Questionnaire o1, Questionnaire o2) {
                            if (o1.getEtat()==o2.getEtat())  {
                                return o1.getNom().toLowerCase().compareTo(o2.getNom());//on compare en deuxieme lieu par nom
                            }
                            else if (o1.getEtat()>o2.getEtat())return 1;
                            else return -1;
                        }
                    });
                break;
                case R.id.score_du_test:
                    this.list.sort(new Comparator<Questionnaire>() {
                        @Override
                        public int compare(Questionnaire o1, Questionnaire o2) {
                            if (o1.getNOMBRE_QUESTION()==o2.getNOMBRE_QUESTION()){
                                return o1.getNom().toLowerCase().compareTo(o2.getNom());//on compare en deuxieme lieu par nom
                            }
                            else if (o1.getNOMBRE_QUESTION()>o2.getNOMBRE_QUESTION())return 1;
                            else return -1;
                        }
                    });
                break;
                case R.id.radio_dernier_modif:

                    break;
                default:
                    break;

        }
        this.adapter.notifyDataSetChanged();
    }





    public Button getValide() {
        return valide;
    }

    public void build(){
        show();
    }
}
