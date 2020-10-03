package Activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.polytoic.R;

import java.util.ArrayList;

import Helper.MySave;
import Model.Mode;
import Model.Questionnaire;
import Popup.CustomPopupModifier;
import Popup.PopupTriFiltre;
import adapter.MyspaceAdapter;

public class MyspaceActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    ListView listView;
    private ArrayList<Questionnaire> list_exam;
    private ArrayList<Questionnaire> list_train;
    private ArrayList<Questionnaire> list;
    int checkidcroissance=R.id.radio_croissant;
    int checkidtri=R.id.radio_nom;
    int checkidfiltre=R.id.radio_filtre_aucun;
    TextView textView;
    MySave mySave;
    MyspaceAdapter adapter;
    ImageView _imageview_filtre_tri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myspace);
        listView = findViewById(R.id.info_donne_variable);
        _imageview_filtre_tri=findViewById(R.id.tri_filtre);
        textView = findViewById(R.id.textView9);


        //Json charger
        mySave = new MySave(this);
        list = new ArrayList<>();
        list_exam=new ArrayList<>();
        list_train=new ArrayList<>();
        Log.i("MYSAVE" , mySave.getMydatas().toString());
        list_exam.addAll( mySave.getListeQuestionnaire("examen"));
        list_train.addAll(mySave.getListeQuestionnaire("entrainement"));
        list.addAll(list_exam);
        list.addAll(list_train);

        mySave.TriFiltreRestore(checkidcroissance,checkidtri,checkidfiltre);



        adapter = new MyspaceAdapter(this, list);
        listView.setAdapter(adapter);
        registerForContextMenu(this.listView);
        _imageview_filtre_tri.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final PopupTriFiltre popupTriFiltre = new PopupTriFiltre(MyspaceActivity.this,adapter, list,list_exam,list_train, checkidcroissance,checkidtri,checkidfiltre);
                popupTriFiltre.getValide().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkidtri = popupTriFiltre.getCheckidtri();
                        checkidcroissance = popupTriFiltre.getCheckidcroissance();
                        checkidfiltre = popupTriFiltre.getCheckidfiltre();
                        mySave.TriFiltreSauvegarde(checkidcroissance , checkidtri , checkidfiltre);
                        Log.i("TEST VALEUR",checkidcroissance +" "+checkidtri+" "+checkidfiltre);
                        popupTriFiltre.dismiss();
                    }
                });
                popupTriFiltre.build();
            }
        });


    }

    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("croissant_decroissant",this.checkidcroissance);
        savedInstanceState.putInt("triage",this.checkidtri);
        savedInstanceState.putInt("fitre",this.checkidfiltre);
        super.onSaveInstanceState(savedInstanceState);
        Log.e("RESTORATION","donnée sauver");
    }




    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.checkidcroissance= savedInstanceState.getInt("croissant_decroissant");
        this.checkidtri = savedInstanceState.getInt("triage");
        this.checkidfiltre = savedInstanceState.getInt("fitre");

        Log.e("RESTORATION","donnée restorer");
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    //menu long press item gridview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_all_questionnaire,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Questionnaire modifi = this.list.get(info.position);

        switch(item.getItemId()){

            case R.id.all_questionnaire_supprimer:
               this.list.remove(info.position);
                this.adapter.notifyDataSetChanged();
                if (modifi.getType() == Mode.EXAMEN){
                    mySave.remove(Mode.getMODE(modifi.getType()),list_exam.indexOf(modifi));
                }
                else if (modifi.getType() == Mode.ENTRAINEMENT){
                    mySave.remove(Mode.getMODE(modifi.getType()),list_train.indexOf(modifi));
                }
                else Log.i("SUPPRIMER " , "echec de la supresison");

                return true;

            case R.id.all_questionnaire_modifier:
                //on modifie notre matrice
                Modifier(modifi);
                mySave.update(Mode.getMODE( modifi.getType()), info.position, modifi.transformer());
                adapter.notifyDataSetChanged();
                return true;


            default:
                return super.onContextItemSelected(item);
        }


    }

    public void Modifier(final Questionnaire questionnaire) {
        //popup customiser modifier
        final CustomPopupModifier customPopup = new CustomPopupModifier(this,questionnaire);
        //injection des valeurs ligne et colonne dans les editText
        customPopup.setNom(questionnaire.getNom());
        customPopup.setLigne(questionnaire.getListening().size());
        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionnaire.getType() == Mode.ENTRAINEMENT){
                    questionnaire.modifier(customPopup.getN().getText().toString() , Integer.parseInt(customPopup.getL().getText().toString()));
                }
                else questionnaire.setNom(customPopup.getN().getText().toString());
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




}
