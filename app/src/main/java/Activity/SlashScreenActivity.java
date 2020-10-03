package Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.polytoic.R;

public class SlashScreenActivity extends Activity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slashscreen);



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),AcceuilActivity.class);
                startActivity(intent);
                finish();
            }
        };

        new Handler().postDelayed(runnable,30);
    }
}
