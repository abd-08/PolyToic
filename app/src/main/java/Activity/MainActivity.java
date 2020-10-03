package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.example.polytoic.R;
import java.util.ArrayList;
import Helper.MySave;
import Model.Mode;
import Model.Questionnaire;
import Popup.CustomPopupModifier;
import Popup.PopupAjouterQuestionnaire;
import adapter.QuestionnaireAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    GridView mainGridView;
    Mode mode;
    ArrayList<Questionnaire> questionnaire_list;
    QuestionnaireAdapter adapter_q;
    MySave mySave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        Bundle extras = getIntent().getExtras();
        assert extras != null;
        this.mode = new Mode(extras.getInt("mode"));

        //Json charger
        mySave = new MySave(this);
        questionnaire_list = mySave.getListeQuestionnaire(this.mode.getMODE());

        //grid view
        this.mainGridView = findViewById(R.id.mainGridView);
        adapter_q = new QuestionnaireAdapter(this,questionnaire_list);
        this.mainGridView.setAdapter(this.adapter_q);
        registerForContextMenu(this.mainGridView);

        //start activity
        this.mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ExamenActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("mode", mode.getTHE_MODE());
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajouter();

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    //menu long press item gridview
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_item_matrice,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Questionnaire modifi = questionnaire_list.get(info.position);

        switch(item.getItemId()){

            case R.id.supprimer_matrice:
                questionnaire_list.remove(info.position);
                adapter_q.notifyDataSetChanged();
                mySave.remove(mode.getMODE(),info.position);
                return true;

            case R.id.modifier_matrice:
                //on modifie notre matrice
                Modifier(modifi,info.position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }


    }


    public void Ajouter(){
        final Mode mode = this.mode ;
        final PopupAjouterQuestionnaire customPopup= new PopupAjouterQuestionnaire(this , mode);

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Questionnaire q;
                if (mode.equalMode(Mode.EXAMEN)){
                    q= new Questionnaire(customPopup.getN().getText().toString());
                }
                else{
                    q=new Questionnaire(customPopup.getN().getText().toString(),
                            Integer.parseInt(customPopup.getLigne().getText().toString()));}
                questionnaire_list.add(q);
                adapter_q.notifyDataSetChanged();
                mySave.ajouter(mode.getMODE() , q.transformer());
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

    public void Modifier(final Questionnaire questionnaire , final int position){
        //popup customiser modifier
        final CustomPopupModifier customPopup= new CustomPopupModifier(this , questionnaire);
        //injection des valeurs ligne et colonne dans les editText
        customPopup.setNom(questionnaire.getNom());
        customPopup.setLigne(questionnaire.size());

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                questionnaire.modifier(customPopup.getN().getText().toString(),
                        Integer.parseInt(customPopup.getL().getText().toString()));

                questionnaire_list.set(position,questionnaire);
                adapter_q.notifyDataSetChanged();
                mySave.update(mode.getMODE(),position,questionnaire.transformer());
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
