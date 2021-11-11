package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class RegisterActivityAdm extends AppCompatActivity {
    EditText etNombresAdm, etApellidosAdm, etTelefonoAdm, etUsuarioAdm, etClaveAdm;

    RequestQueue requestQueue;

    private static final String url = "http://192.168.0.6/UrbanFood/insert2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_adm);

        etNombresAdm = findViewById(R.id.etNombresAdm);
        etApellidosAdm = findViewById(R.id.etApellidosAdm);
        etTelefonoAdm = findViewById(R.id.etTelefonoAdm);
        etUsuarioAdm = findViewById(R.id.etUsuarioAdm);
        etClaveAdm = findViewById(R.id.etClaveAdm);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void Registrar(View view) {
        if (etNombresAdm.getText().toString().isEmpty() || etApellidosAdm.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Por favor, rellene todos los campos", Toast.LENGTH_SHORT).show();
        }
        else {
            String nombres = etNombresAdm.getText().toString();
            String apellidos = etApellidosAdm.getText().toString();
            String telefono = etTelefonoAdm.getText().toString();
            String nombre_us = etUsuarioAdm.getText().toString();
            String clave = etClaveAdm.getText().toString();
            String tipo = "Usuario";

            RequestParams parametros = new RequestParams();
            parametros.put("nombres", nombres);
            parametros.put("apellidos", apellidos);
            parametros.put("telefono", telefono);
            parametros.put("nombre_us", nombre_us);
            parametros.put("clave", clave);
            parametros.put("tipo", tipo);

            AsyncHttpClient cliente = new AsyncHttpClient();

            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        String respuesta = new String(responseBody);
                        try {
                            JSONObject objetoJSON = new JSONObject(respuesta);

                            if (objetoJSON.names().get(0).equals("Success"))
                            {
                                Toast.makeText(getApplicationContext(), "Registrado correctamente", Toast.LENGTH_SHORT).show();
                                etNombresAdm.setText("");
                                etApellidosAdm.setText("");
                                etTelefonoAdm.setText("");
                                etUsuarioAdm.setText("");
                                etClaveAdm.setText("");
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "El nombre de usuario ya existe", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "Error en el JSON: " + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void Salir(View view) {
        finish();
    }
}