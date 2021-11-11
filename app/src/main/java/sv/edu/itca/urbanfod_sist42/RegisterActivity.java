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
import com.android.volley.toolbox.JsonObjectRequest;
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

public class RegisterActivity extends AppCompatActivity {
    EditText etNombres, etApellidos, etTelefono, etUsuarioAdd, etClaveAdd;
    RequestQueue requestQueue;

    private static final String url = "http://192.168.0.6/UrbanFood/insert2.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etTelefono = findViewById(R.id.etTelefono);
        etUsuarioAdd = findViewById(R.id.etUsuarioAdd);
        etClaveAdd = findViewById(R.id.etClaveAdd);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void Registrarme(View view) {
        if (etNombres.getText().toString().isEmpty() || etApellidos.getText().toString().isEmpty() || etTelefono.getText().toString().isEmpty() || etUsuarioAdd.getText().toString().isEmpty() || etClaveAdd.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.ToastRelleneTodos, Toast.LENGTH_SHORT).show();
        }
        else {
            String nombres = etNombres.getText().toString();
            String apellidos = etApellidos.getText().toString();
            String telefono = etTelefono.getText().toString();
            String nombre_us = etUsuarioAdd.getText().toString();
            String clave = etClaveAdd.getText().toString();
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
                                Toast.makeText(getApplicationContext(), R.string.registradoOk, Toast.LENGTH_SHORT).show();
                                Intent ventana = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(ventana);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), R.string.UsuarioYaExiste, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), R.string.ToastErrorJson + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), R.string.ToastErrorServidor, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void Iniciar(View view) {
        Intent ventana = new Intent(RegisterActivity.this, Login.class);
        startActivity(ventana);
        finish();
    }
}