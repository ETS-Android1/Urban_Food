package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.designsplashscreen);

        Handler manejador = new Handler();

        manejador.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
                Intent objIntent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(objIntent);
            }
        }, 3000);
    }
}