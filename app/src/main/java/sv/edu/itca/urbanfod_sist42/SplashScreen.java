package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
                SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
                String user = preferences.getString("usu", "");
                boolean sesion = preferences.getBoolean("sesion", false);
                finish();

                if (sesion)
                {
                    if (user == "administrador")
                    {
                        Intent objIntent = new Intent(SplashScreen.this,AdminActivity.class);
                        startActivity(objIntent);
                    }
                    else
                    {
                        Intent objIntent = new Intent(SplashScreen.this,MainActivity.class);
                        startActivity(objIntent);
                    }
                }
                else
                {
                    Intent objIntent = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(objIntent);
                }
            }
        }, 3000);
    }
}