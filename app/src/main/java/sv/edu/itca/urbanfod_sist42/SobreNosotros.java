package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class SobreNosotros extends AppCompatActivity {
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);

        url= "https://www.google.com/maps/place/Plaza+Real/@13.9871726,-89.6482142,14.72z/data=!4m5!3m4!1s0x0:0xcb91ee7a60aea62d!8m2!3d13.9879289!4d-89.6420806";
    }

    public void IrGoogleMaps(View view) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}