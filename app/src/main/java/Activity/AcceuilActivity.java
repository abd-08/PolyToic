package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.polytoic.R;

import Model.Mode;
import adapter.MyspaceAdapter;

public class AcceuilActivity extends AppCompatActivity {
    Button examen;
    Button training;
    Button espace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        examen = findViewById(R.id.mode_exam);
        training = findViewById(R.id.mode_training);
        espace = findViewById(R.id.mon_espace);

        examen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mode", Mode.EXAMEN);
                Log.i("DEBUT ACTIVITE EXAMEN" ,"examen");
                startActivity(intent);

            }
        });
        training.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("mode", Mode.ENTRAINEMENT);
                startActivity(intent);

            }
        });
        espace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyspaceActivity.class);
                startActivity(intent);
                intent.putExtra("mode", Mode.TERMINEE);
                //Toast.makeText(AcceuilActivity.this , "mon espace", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

