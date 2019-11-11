package Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.polytoic.R;

import org.json.JSONException;

import java.io.IOException;

import Helper.MyJSONFile;
import Helper.Other;
import Model.Matrice;
import Popup.CustomPopupModifier;
import Popup.CustomPopupResulat;
import adapter.CelluleItemAdapter;

public class MatriceActivity extends Activity implements PopupMenu.OnMenuItemClickListener {


    GridView gridView;
    Matrice matrice;
    Matrice matriceCorrection;
    Button commencer;
    CelluleItemAdapter adapter;
    ImageView pause ;
    ImageView resume ;

    private MyJSONFile myJSONFile;
    int position_parent;

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private boolean acommencer = false;
    private boolean normal = true ;
    boolean corrige = false ;


    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrice);
        gridView=findViewById(R.id.MatriceGridView);
        commencer=findViewById(R.id.terminer_test);
        pause=findViewById(R.id.matrice_pause);
        resume=findViewById(R.id.matrice_resume);


        //Json charger
        Bundle extras = getIntent().getExtras();
        position_parent = extras.getInt("position");
        myJSONFile = new MyJSONFile(this);
        matrice = new Matrice("test",3,3);
        try {
            matrice=myJSONFile.getMatrice(position_parent);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        //adapter gridview
        gridView.setNumColumns(matrice.getColonne());
        if (normal)   adapter = new CelluleItemAdapter(this, matrice.getListeCellule(), matrice.getColonne());
        else  adapter = new CelluleItemAdapter(this, matriceCorrection.getListeCellule(), matriceCorrection.getColonne());

        gridView.setAdapter(adapter);
        registerForContextMenu(this.gridView);


        //chronometre
        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    pauseChronometer();
                    Toast.makeText(MatriceActivity.this, "Test Fini", Toast.LENGTH_SHORT).show();
                }
            }
        });




        //start activity
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int ligne = Other.convertLigne(position,matrice.getColonne());
                int column = Other.convertColonne(position,matrice.getColonne());

                if (normal){
                    //on fait le test
                    matrice.getLignePosition(ligne).deselect(position);
                    matrice.getCellulePosition(ligne,column).Clicked();
                    adapter.setCelluleList(matrice.getListeCellule());
                    gridView.setAdapter(adapter);
                }
                else{
                    //on corrige le test
                    matriceCorrection = new Matrice("correction",matrice.getLigne(),matrice.getColonne());
                    matriceCorrection.getLignePosition(ligne).deselect(position);
                    matriceCorrection.getCellulePosition(ligne,column).Clicked();
                    adapter.setCelluleList(matriceCorrection.getListeCellule());
                    gridView.setAdapter(adapter);
                }

            }
        });



        commencer.setBackgroundColor(Color.argb(255, 0, 255, 0));
        commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acommencer==false){
                    //on ouvre le test
                    acommencer=true;
                    commencer.setBackgroundColor(Color.argb(255, 255, 0, 0));
                    commencer.setText("Terminer");
                }

                if (!normal)  TerminerCorrection();
                else if (!running ) startChronometer();
                else if (running) Terminer();

            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pause.setVisibility(View.INVISIBLE);
                resume.setVisibility(View.VISIBLE);
                pauseChronometer();
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resume.setVisibility(View.INVISIBLE);
                pause.setVisibility(View.VISIBLE);
                startChronometer();
            }
        });
    }

    private void TerminerCorrection() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatriceActivity.this);
        builder1.setMessage("Voulez vous vraiment terminer la correction du test.");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Oui",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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


    public void Terminer(){
        if (normal) {
            //on termine le teste avant la fin du temps
            pauseChronometer();
            AlertDialog.Builder builder1 = new AlertDialog.Builder(MatriceActivity.this);
            builder1.setMessage("Voulez vous vraiment terminer le test.");
            builder1.setCancelable(true);
            builder1.setPositiveButton(
                    "Oui",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Fini();
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
        else{

        }
    }
    public void Fini(){
        pauseChronometer();
        normal=false;
        pause.setVisibility(View.INVISIBLE);
        resume.setVisibility(View.INVISIBLE);
    }


    public void Sauvegarde(){
        //sauvegarde des donnée

        try {
            myJSONFile.update(myJSONFile.getMydatas(),position_parent,myJSONFile.transformer(matrice));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void startChronometer() {
        if (!running) {

            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }


    public void showPopup(View v){
        PopupMenu popup=new PopupMenu(this,v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.menu_matrice);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_paramètre:
                Toast.makeText(this,"parametre non defini",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_modifier:
                Modifier();
                adapter.setCelluleList(matrice.getListeCellule());
                gridView.setNumColumns(matrice.getColonne());
                this.adapter.notifyDataSetChanged();
                gridView.invalidate();
                //adapter = new CelluleItemAdapter(this, matrice.getListeCellule(), matrice.getColonne());
                //gridView.setAdapter(adapter);

                return true;
            case R.id.action_correction:
                Toast.makeText(this,"parametre non defini",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_quitter:
                this.finish();
                return true;
            case R.id.action_reset:
                matrice=new Matrice(matrice.getNom());matrice.getLigne();matrice.getColonne();
                resetChronometer();
                return true;
            case R.id.action_afficher_resultat:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void Modifier(){
        //popup customiser modifier
        final CustomPopupModifier customPopup= new CustomPopupModifier(this);
        //injection des valeurs ligne et colonne dans les editText
        customPopup.setNom(matrice.getNom());
        customPopup.setLigne(matrice.getLigne());
        customPopup.setColonne(matrice.getColonne());

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("nmobre ligne",customPopup.getL().getText().toString());
                Log.i("nmobre colonnne",customPopup.getC().getText().toString());
                matrice.modifier(Integer.parseInt(customPopup.getL().getText().toString()),
                        Integer.parseInt(customPopup.getC().getText().toString()));
                matrice.setNom(customPopup.getN().getText().toString());
                customPopup.dismiss();
            }
        });


        customPopup.getAnnuler().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopup.dismiss();
            }
        });

        customPopup.build();
    }


    public void Resultat(){
        final CustomPopupResulat customPopup= new CustomPopupResulat(this);
        int resultat = Other.resultat(matrice,matriceCorrection);
        int total = matrice.getColonne()*matrice.getLigne() ;
        String mark = resultat +"/"+total;
        customPopup.setNote(mark);

        customPopup.getFermer().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customPopup.dismiss();
            }
        });

        customPopup.getAffiche_grille().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //afficher le resultat du test
                if (corrige){
                    afficheCorrection();
                }
                else Toast.makeText(MatriceActivity.this,"Pas de correction pour se test" , Toast.LENGTH_SHORT).show();
                customPopup.dismiss();
            }
        });

    }

    private void afficheCorrection() {
        for(int i=0;i<matrice.getLigne();i++){
            if (matrice.getLignePosition(i).Compare(matriceCorrection.getLignePosition(i))){

            }
            else{

            }
        }
    }
}
