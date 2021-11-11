package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class AdminActivity extends AppCompatActivity {
    TextView txtIdAdm, txtNombresAdm, txtTelefono, txtUsuario, txtTipo, txtid1Adm, txtid2Adm, txtid3Adm, txtReserva1Adm, txtReserva2Adm, txtReserva3Adm;
    EditText etUsuarioBuscar;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        txtIdAdm = findViewById(R.id.txtIDAdm);
        txtNombresAdm = findViewById(R.id.txtNombreAdm);
        txtTelefono = findViewById(R.id.txtTelefonoAdm);
        txtUsuario = findViewById(R.id.txtUsuarioAdm);
        txtTipo = findViewById(R.id.txtTipoAdm);
        etUsuarioBuscar = findViewById(R.id.etUsuarioBuscar);
        txtid1Adm = findViewById(R.id.txtid1Adm);
        txtid2Adm = findViewById(R.id.txtid2Adm);
        txtid3Adm = findViewById(R.id.txtid3Adm);
        txtReserva1Adm = findViewById(R.id.txtReserva1Adm);
        txtReserva2Adm = findViewById(R.id.txtReserva2Adm);
        txtReserva3Adm = findViewById(R.id.txtReserva3Adm);
    }

    public void CerrarSesion(View view) {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        Intent objVentana = new Intent(AdminActivity.this,MainActivity.class);
        startActivity(objVentana);
        finish();
    }

    @Override
    public void onBackPressed() {

    }

    public void AgregarUsuario(View view) {
        Intent objVentana = new Intent(AdminActivity.this,RegisterActivityAdm.class);
        startActivity(objVentana);
    }

    public void Buscar(View view) {

        if (etUsuarioBuscar.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Por favor, rellene campo de búsqueda", Toast.LENGTH_SHORT).show();
        }
        else {
            String IP = "192.168.0.6";

            String user = etUsuarioBuscar.getText().toString();

            String url = "http://" + IP + "/UrbanFood/consultaUsuario.php";

            RequestParams parametros = new RequestParams();
            parametros.put("usu", user);

            AsyncHttpClient cliente = new AsyncHttpClient();

            cliente.post(url, parametros, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    if (statusCode == 200) {
                        String respuesta = new String(responseBody);
                        try {
                            JSONObject objetoJSON = new JSONObject(respuesta);

                            if (objetoJSON.names().get(0).equals("exito")) {
                                txtIdAdm.setText(objetoJSON.getString("id"));
                                txtNombresAdm.setText(objetoJSON.getString("usuario"));
                                txtTelefono.setText(objetoJSON.getString("telefono"));
                                txtUsuario.setText(objetoJSON.getString("nombre_us"));
                                txtTipo.setText(objetoJSON.getString("tipo"));

                                if (objetoJSON.getInt("rows") == 1) {
                                    txtid1Adm.setText("#" + objetoJSON.getString("id0"));
                                    txtReserva1Adm.setText(objetoJSON.getString("date0"));
                                    txtid2Adm.setText("");
                                    txtReserva2Adm.setText("");
                                    txtid3Adm.setText("");
                                    txtReserva3Adm.setText("");
                                } else if (objetoJSON.getInt("rows") == 2) {
                                    txtid1Adm.setText("#" + objetoJSON.getString("id0"));
                                    txtReserva1Adm.setText(objetoJSON.getString("date0"));
                                    txtid2Adm.setText("#" + objetoJSON.getString("id1"));
                                    txtReserva2Adm.setText(objetoJSON.getString("date1"));
                                    txtid3Adm.setText("");
                                    txtReserva3Adm.setText("");
                                } else if (objetoJSON.getInt("rows") == 3) {
                                    txtid1Adm.setText("#" + objetoJSON.getString("id0"));
                                    txtReserva1Adm.setText(objetoJSON.getString("date0"));
                                    txtid2Adm.setText("#" + objetoJSON.getString("id1"));
                                    txtReserva2Adm.setText(objetoJSON.getString("date1"));
                                    txtid3Adm.setText("#" + objetoJSON.getString("id2"));
                                    txtReserva3Adm.setText(objetoJSON.getString("date2"));
                                }
                            } else if (objetoJSON.names().get(0).equals("error")) {
                                Toast.makeText(getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), "No hay coincidencias", Toast.LENGTH_SHORT).show();
                            txtid1Adm.setText("");
                            txtid2Adm.setText("");
                            txtid3Adm.setText("");
                            txtReserva1Adm.setText("");
                            txtReserva2Adm.setText("");
                            txtReserva3Adm.setText("");
                            txtIdAdm.setText("");
                            txtNombresAdm.setText("");
                            txtTelefono.setText("");
                            txtUsuario.setText("");
                            txtTipo.setText("");
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
}