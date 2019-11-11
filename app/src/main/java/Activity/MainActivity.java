package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.GridView;

import com.example.polytoic.R;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import Activity.MatriceActivity;
import Helper.MyJSONFile;
import Model.Matrice;
import Popup.CustomPopupAjouter;
import Popup.CustomPopupModifier;
import adapter.MatriceItemAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    GridView mainGridView;
    ArrayList<Matrice> matriceList;
    MatriceItemAdapter adapter;
    MyJSONFile myJSONFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
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



        //Json charger
        myJSONFile = new MyJSONFile(this);
        try {
            this.matriceList = myJSONFile.getListeMatrices();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        //grid view
        this.mainGridView = findViewById(R.id.mainGridView);
        this.adapter=new MatriceItemAdapter(this,matriceList);
        this.mainGridView.setAdapter(this.adapter);
        registerForContextMenu(this.mainGridView);

        //start activity
        this.mainGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MatriceActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ajouter();
                Sauvegarde();
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
        final Matrice modifi = matriceList.get(info.position);

        switch(item.getItemId()){

            case R.id.supprimer_matrice:
                matriceList.remove(info.position);
                this.adapter.notifyDataSetChanged();
                Sauvegarde();
                return true;

            case R.id.modifier_matrice:
                //on modifie notre matrice
                Modifier(modifi,info.position);
                Sauvegarde();
                return true;

            default:
                return super.onContextItemSelected(item);
        }


    }


    public void Ajouter(){
        final CustomPopupAjouter customPopup= new CustomPopupAjouter(this);

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matrice Ma=new Matrice(customPopup.getN().getText().toString(),
                        Integer.parseInt(customPopup.getL().getText().toString()),
                        Integer.parseInt(customPopup.getC().getText().toString())
                );
                matriceList.add(Ma);
                adapter.notifyDataSetChanged();
                Sauvegarde();
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

    public void Modifier(final Matrice matrice , final int position){
        //popup customiser modifier
        final CustomPopupModifier customPopup= new CustomPopupModifier(this);
        //injection des valeurs ligne et colonne dans les editText
        customPopup.setNom(matrice.getNom());
        customPopup.setLigne(matrice.getLigne());
        customPopup.setColonne(matrice.getColonne());

        customPopup.getValider().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                matrice.modifier(Integer.parseInt(customPopup.getL().getText().toString()),
                        Integer.parseInt(customPopup.getC().getText().toString()));
                matrice.setNom(customPopup.getN().getText().toString());
                matriceList.set(position,matrice);
                adapter.notifyDataSetChanged();
                Sauvegarde();
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

    public void Sauvegarde(){
        //sauvegarde des donnée
        myJSONFile.setMydatas(myJSONFile.Transforme(matriceList));
        try {
            myJSONFile.saveData(myJSONFile.getMydatas());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
