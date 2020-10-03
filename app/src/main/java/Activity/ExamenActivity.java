package Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.polytoic.R;
import Helper.MySave;
import Model.Etat;
import Model.Mode;
import Model.Questionnaire;
import Popup.CustomPopupModifier;
import Popup.CustomPopupResulat;
import adapter.QuestionAdapter;

public class ExamenActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    Questionnaire questionnaire;
    ListView listView;
    QuestionAdapter adapter;
    MySave mySave;
    Mode mode;
    Etat etat;
    int position;
    Button commencer;
    ImageView pause;
    ImageView resume;
    ImageButton retour;
    TextView nombre_select;
    Context context=this;
    private Chronometer chronometer;
    private long pauseOffset;
    ImageButton goListening;
    ImageButton goReading;
    TextView letterL;
    TextView letterR;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examen);
        listView = findViewById(R.id.examen);
        commencer = findViewById(R.id.terminer_test);
        pause = findViewById(R.id.matrice_pause);
        resume = findViewById(R.id.matrice_resume);
        retour = findViewById(R.id.retour_test);
        nombre_select = findViewById(R.id.matrice_nombre_select);
        goListening = findViewById(R.id.iconListening);
        goReading = findViewById(R.id.iconReading);
        letterL = findViewById(R.id.lettre_listening);
        letterR = findViewById(R.id.lettre_reading);
        etat = new Etat(Etat.DEBUT_RESULT);



        final Bundle extras = getIntent().getExtras();
        assert extras != null;
        this.mode = new Mode(extras.getInt("mode"));
        this.position = extras.getInt("position");

        //Json charger
        mySave = new MySave(this);
        questionnaire = mySave.getQuestionnaire(this.mode.getMODE(), position);

        adapter = new QuestionAdapter(this, questionnaire.getListening());
        listView.setAdapter(adapter);
        nombre_select.setText(questionnaire.nombreSelectionner(etat.getResult_code())+"/" + (questionnaire.getNOMBRE_QUESTION()));
        if (this.mode.getTHE_MODE() == Mode.ENTRAINEMENT){
            goListening.setVisibility(View.INVISIBLE);
            goReading.setVisibility(View.INVISIBLE);
            letterL.setVisibility(View.INVISIBLE);
            letterR.setVisibility(View.INVISIBLE);
        }

        adapter.registerDataSetObserver(new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (etat.getResult_code()==Etat.EN_COURS_RESULT)
                    nombre_select.setText(questionnaire.nombreSelectionner(etat.EN_COURS_RESULT)+"/" + questionnaire.getNOMBRE_QUESTION());

                    else if (etat.getResult_code()==Etat.CORRECTION_RESULT)
                    nombre_select.setText(questionnaire.nombreSelectionner(etat.CORRECTION_RESULT)+"/" + questionnaire.getNOMBRE_QUESTION());

                    else nombre_select.setText("34/4");
                }
            }
        );



        //chronometre
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime() - questionnaire.getChronometre());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                 if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 30000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                }
            }
        });

        commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (etat.getResult_code()) {

                    case Etat.DEBUT_RESULT:
                        commencer_test();
                        break;

                    case Etat.AFFICHE_RESULTAT:
                        recommencer_test();
                        break;

                    case Etat.EN_COURS_RESULT: // on termine le test
                        Terminer_test();
                        break;

                    case Etat.CORRECTION_RESULT: // on termine la correction

                        if (questionnaire.nombreSelectionner(Etat.CORRECTION_RESULT) == questionnaire.getNOMBRE_QUESTION()) {
                            TerminerCorrection();
                        } else
                            Toast.makeText(ExamenActivity.this, "veuillez terminer la correction ", Toast.LENGTH_SHORT).show();
                        break;


                    default:
                        Toast.makeText(ExamenActivity.this, "ERREUR", Toast.LENGTH_SHORT).show();
                        break;
                }
                headerANDfooter();
                Sauvegarde();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseChronometer();
                adapter.setActive(false);
                adapter.notifyDataSetChanged();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startChronometer();
                adapter.setActive(true);
                adapter.notifyDataSetChanged();
            }
        });

        goReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etat.getResult_code() == Etat.CORRECTION_RESULT) {
                    adapter.setListe_question(questionnaire.getReading_correction());
                }
                else  adapter.setListe_question(questionnaire.getReading());
                adapter.notifyDataSetChanged();
            }
        });

        goListening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etat.getResult_code() == Etat.CORRECTION_RESULT) {
                    adapter.setListe_question(questionnaire.getListening_correction());
                }
                else  adapter.setListe_question(questionnaire.getListening());
                adapter.notifyDataSetChanged();
            }
        });

        final Context co = this;
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sauvegarde();
                ((ExamenActivity) co).finish();
            }
        });

        headerANDfooter();
        changeEtat(Etat.DEBUT_RESULT);

    }



    private void TerminerCorrection() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamenActivity.this);
        builder1.setMessage("Voulez vous vraiment terminer la correction du test.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        changeEtat(Etat.AFFICHE_RESULTAT);
                        afficheResultat();
                        headerANDfooter();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    public void Terminer_test() {
        //on termine le teste
        //on affiche la correction si elle existe
        //ou on propose d'en créer une

        AlertDialog.Builder builder1 = new AlertDialog.Builder(ExamenActivity.this);
        builder1.setMessage("Voulez vous vraiment terminer le test.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        pauseChronometer();
                        if (questionnaire.correctionExiste()) {
                            changeEtat(Etat.AFFICHE_RESULTAT);
                            afficheResultat();
                        }
                        else {
                            changeEtat(Etat.CORRECTION_RESULT);
                            nombre_select.setText(questionnaire.nombreSelectionner(etat.getResult_code())+"/" + (questionnaire.getNOMBRE_QUESTION()));
                        }
                        headerANDfooter();
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "Non",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startChronometer();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    public void recommencer_test() {
        resetChronometer();
        pauseChronometer();
        questionnaire.reinitTest();
        changeEtat(Etat.DEBUT_RESULT);
    }

    public void commencer_test() {
        startChronometer();
        changeEtat(Etat.EN_COURS_RESULT);
    }


    public void Sauvegarde() {
        //sauvegarde des donnée
        if(mode.getTHE_MODE() == Mode.EXAMEN){
            questionnaire.setChronometre(SystemClock.elapsedRealtime() - chronometer.getBase());
        }
        questionnaire.setEtat(this.etat.getResult_code());
        questionnaire.setMode(this.mode.getTHE_MODE());
        this.mySave.update(this.mode.getMODE(), this.position, this.questionnaire.transformer());
    }


    public void startChronometer() {
        pause.setVisibility(View.VISIBLE);
        resume.setVisibility(View.INVISIBLE);
        chronometer.setBase(SystemClock.elapsedRealtime() - questionnaire.getChronometre());
        chronometer.start();
    }

    public void pauseChronometer() {
        pause.setVisibility(View.INVISIBLE);
        resume.setVisibility(View.VISIBLE);
        chronometer.stop();
        pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

    public void afficheResultat() {
        if (!questionnaire.correctionExiste())
            Toast.makeText(ExamenActivity.this, "pas de correction disponible pour se test", Toast.LENGTH_SHORT).show();
        else {
            final CustomPopupResulat customPopup = new CustomPopupResulat(this, questionnaire);

            customPopup.getFermer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //afficher resultat grille
                    customPopup.dismiss();
                }
            });

            customPopup.getRecommencer().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recommencer_test();
                    customPopup.dismiss();
                }
            });


            customPopup.build();
        }
    }

    public void Modifier() {
        //popup customiser modifier
        pauseChronometer();
        final CustomPopupModifier customPopup = new CustomPopupModifier(this,questionnaire);
        //injection des valeurs ligne et colonne dans les editText
        customPopup.setNom(questionnaire.getNom());
        customPopup.setLigne(questionnaire.getListening().size());
        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                questionnaire.modifier(customPopup.getN().getText().toString(),
                        Integer.parseInt(customPopup.getL().getText().toString()));

                adapter.notifyDataSetChanged();
                customPopup.dismiss();
            }
        });


        customPopup.getAnnuler().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopup.dismiss();
            }
        });
        Sauvegarde();
        customPopup.build();
    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_matrice);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_paramètre:
                Toast.makeText(this, "parametre non defini", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_modifier:
                if (mode.equalMode(Mode.ENTRAINEMENT)) {
                    pauseChronometer();
                    Modifier();
                }

                return true;
            case R.id.action_correction:
                etat.setResult_code(Etat.CORRECTION_RESULT);
                commencer.setBackgroundColor(Color.argb(255, 255, 0, 0));
                commencer.setText("Fin correction");
                adapter.setListe_question(questionnaire.getListening_correction());
                return true;

            case R.id.action_quitter:
                Sauvegarde();
                this.finish();
                return true;

            case R.id.action_reset:
                recommencer_test();
                return true;

            case R.id.action_afficher_resultat:
                afficheResultat();
                return true;

            case R.id.action_historique_exam:
                Log.i("ACTION HISTORIQUE","historique des test");
                return true;

            case R.id.action_affiche_correction:
                questionnaire.Corriger();
                etat.setResult_code(Etat.AFFICHE_RESULTAT);
                adapter.setListe_question(questionnaire.getListening());
                adapter.setResultat(true);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


    public void headerANDfooter(){
        switch (etat.getResult_code()){

            case Etat.DEBUT_RESULT:
                pause.setVisibility(View.INVISIBLE);
                resume.setVisibility(View.INVISIBLE);
                nombre_select.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);
                commencer.setBackgroundColor(Color.argb(255, 0, 255, 0));
                commencer.setText("Commencer");
                break;
            case Etat.EN_COURS_RESULT:
                nombre_select.setVisibility(View.VISIBLE);
                resume.setVisibility(View.INVISIBLE);
                if (mode.getTHE_MODE()==Mode.EXAMEN){
                    pause.setVisibility(View.VISIBLE);
                    chronometer.setVisibility(View.VISIBLE);
                }
                else{
                    pause.setVisibility(View.INVISIBLE);
                    chronometer.setVisibility(View.INVISIBLE);
                }
                commencer.setBackgroundColor(Color.argb(255, 255, 0, 0));
                commencer.setText("Terminer");
                nombre_select.setText(questionnaire.nombreSelectionner(etat.getResult_code())+"/" + (questionnaire.getNOMBRE_QUESTION()));
                break;

            case Etat.CORRECTION_RESULT:
                nombre_select.setVisibility(View.VISIBLE);
                resume.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);
                commencer.setBackgroundColor(Color.argb(255, 0, 0, 255));
                commencer.setText("Terminer correction");
                nombre_select.setText(questionnaire.nombreSelectionner(etat.getResult_code())+"/" + (questionnaire.getNOMBRE_QUESTION()));
                break;
            case Etat.AFFICHE_RESULTAT:
                pause.setVisibility(View.INVISIBLE);
                resume.setVisibility(View.INVISIBLE);
                nombre_select.setVisibility(View.INVISIBLE);
                chronometer.setVisibility(View.INVISIBLE);
                commencer.setBackgroundColor(Color.argb(255, 0, 255, 0));
                commencer.setText("Recommencer");
                break;
            default:
                listView.setVisibility(View.INVISIBLE);
        }

    }

    public void changeEtat(int state){

        switch (state){
            case Etat.DEBUT_RESULT:
                etat.setResult_code(Etat.DEBUT_RESULT);
                adapter.setActive(false);
                adapter.setCorrection(false);
                adapter.setResultat(false);
                break;
            case Etat.EN_COURS_RESULT:
                etat.setResult_code(Etat.EN_COURS_RESULT);
                adapter.setActive(true);
                adapter.setCorrection(false);
                adapter.setResultat(false);
                adapter.setListe_question(questionnaire.getListening());
                break;
            case Etat.CORRECTION_RESULT:
                etat.setResult_code(Etat.CORRECTION_RESULT);
                adapter.setActive(true);
                adapter.setCorrection(true);
                adapter.setResultat(false);
                adapter.setListe_question(questionnaire.getListening_correction());
                break;

            case Etat.AFFICHE_RESULTAT:
                etat.setResult_code(Etat.AFFICHE_RESULTAT);
                adapter.setActive(false);
                adapter.setCorrection(false);
                adapter.setResultat(true);
                adapter.setListe_question(questionnaire.getListening());
                break;
            default:
                Toast.makeText(this,"ERREUR CHANGEMENT ETAT",Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();

    }



}
