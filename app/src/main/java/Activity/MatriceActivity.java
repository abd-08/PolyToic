package Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.polytoic.R;

import org.json.JSONException;

import java.io.IOException;

import Helper.MyJSONFile;
import Helper.Other;
import Model.Matrice;
import adapter.CelluleItemAdapter;

public class MatriceActivity extends Activity {


    GridView gridView;
    Matrice matrice;
    Button commencer;
    CelluleItemAdapter adapter;
    LinearLayout afficheMenu;
    ImageView pause ;
    ImageView resume ;

    private MyJSONFile myJSONFile;
    int position_parent;

    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;


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
        matrice=myJSONFile.getMatrice(position_parent);



        //adapter gridview
        gridView.setNumColumns(matrice.getColonne());
        adapter=new CelluleItemAdapter(this,matrice.getListeCellule(),matrice.getColonne());
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

                matrice.getLignePosition(ligne).deselect(position);
                matrice.getCellulePosition(ligne,column).Clicked();

                adapter.setCelluleList(matrice.getListeCellule());
                gridView.setAdapter(adapter);
            }
        });



        commencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Terminer();
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


    public void Terminer(){
        pauseChronometer();
        AlertDialog.Builder builder1 = new AlertDialog.Builder(MatriceActivity.this);
        builder1.setMessage("Voulez vous vraiment terminer le test.");
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
                        startChronometer();
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

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

    public void resetChronometer(View v) {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

}
