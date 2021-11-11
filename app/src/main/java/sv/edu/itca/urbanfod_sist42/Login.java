package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {
    private EditText etUsuario, etClave;
    public String user, pasw, url, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsuario = findViewById(R.id.etUsuario);
        etClave = findViewById(R.id.etClave);
    }


    public void Ingresar(View view) {
        if (etUsuario.getText().toString().isEmpty() || etClave.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.ToastRelleneTodos, Toast.LENGTH_SHORT).show();
        }
        else {
            String IP = "192.168.0.6";

            user = etUsuario.getText().toString();
            pasw = etClave.getText().toString();

            url = "http://" + IP + "/UrbanFood/consulta.php";

            RequestParams parametros = new RequestParams();
            parametros.put("usu", user);
            parametros.put("pas", pasw);

            AsyncHttpClient cliente = new AsyncHttpClient();

            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        String respuesta = new String(responseBody);
                        try {
                            JSONObject objetoJSON = new JSONObject(respuesta);

                            if (objetoJSON.names().get(0).equals("exito")) {
                                guardarSesion();
                                result = getString(R.string.Bienvenido)+objetoJSON.getString("usuario");
                            } else {
                                result = objetoJSON.getString("error");
                            }
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                            if (objetoJSON.getString("tipo").equals("Administrador")) {
                                Intent ventana = new Intent(Login.this, AdminActivity.class);
                                startActivity(ventana);
                            } else {
                                Intent ventana = new Intent(Login.this, MainActivity.class);
                                startActivity(ventana);
                            }

                            finish();
                        } catch (JSONException e) {

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

    private void guardarSesion()
    {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("usu", user);
        editor.putBoolean("sesion", true);
        editor.commit();
    }

    public void Registro(View view) {
        Intent objVentana = new Intent(Login.this,RegisterActivity.class);
        startActivity(objVentana);
        finish();
    }
}