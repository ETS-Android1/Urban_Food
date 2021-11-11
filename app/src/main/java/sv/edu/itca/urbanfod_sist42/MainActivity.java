package sv.edu.itca.urbanfod_sist42;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    EditText etFecha, etHora, etGrupo;
    String result;
    TextView txtID, txtNombre, txtTelefono, txtReserva1, txtReserva2, txtReserva3, txtid1, txtid2, txtid3, txtError;
    private int dia, mes, anio, hora, minutos;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        boolean sesion = preferences.getBoolean("sesion", false);
        if(sesion)
        {
            Button boton = (Button) findViewById(R.id.btnSesion);
            boton.setText(R.string.btnCerrarSesion);
        }
        else
        {
            Button boton = (Button) findViewById(R.id.btnSesion);
            boton.setText(R.string.LoginTitle);
        }

        etGrupo = findViewById(R.id.etGrupo);
        etFecha = findViewById(R.id.etFecha);
        etHora = findViewById(R.id.etHora);
        txtID = findViewById(R.id.txtID);
        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtReserva1 = findViewById(R.id.txtReserva1);
        txtReserva2 = findViewById(R.id.txtReserva2);
        txtReserva3 = findViewById(R.id.txtReserva3);
        txtid1 = findViewById(R.id.txtid1);
        txtid2 = findViewById(R.id.txtid2);
        txtid3 = findViewById(R.id.txtid3);
        txtError = findViewById(R.id.txtError);


        Resources recurso=getResources();

        TabHost tabControl = findViewById(R.id.miTabHost);
        tabControl.setup();

        TabHost.TabSpec spec1, spec2, spec3;

        spec1=tabControl.newTabSpec("INI");
        spec1.setContent(R.id.inicio);
        spec1.setIndicator(getString(R.string.tab1));
        tabControl.addTab(spec1);

        spec2=tabControl.newTabSpec("RES");
        spec2.setContent(R.id.reservas);
        spec2.setIndicator(getString(R.string.tab2));
        tabControl.addTab(spec2);

        spec3=tabControl.newTabSpec("HIST");
        spec3.setContent(R.id.historial);
        spec3.setIndicator(getString(R.string.tab3));
        tabControl.addTab(spec3);

        ConsultaUsuario();
        ConsultaReserva();
    }

    public void SelectHora(View view) {
        final Calendar c = Calendar.getInstance();
        hora = c.get(Calendar.HOUR_OF_DAY);
        minutos = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                c.set(Calendar.HOUR_OF_DAY, i);
                c.set(Calendar.MINUTE, i1);
                SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");
                String formatedDate = timeformat.format(c.getTime());
                etHora.setText(formatedDate);
            }
        }, hora, minutos, true);
        timePickerDialog.show();
    }

    public void SelectFecha(View view) {
        final Calendar c = Calendar.getInstance();
        dia = c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH);
        anio = c.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                etFecha.setText(i2+"/"+(i1+1)+"/"+i);
            }
        }, dia, mes, anio);
        c.add(c.DATE, 1);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    public void SobreNosotros(View view) {
        Intent objVentana = new Intent(MainActivity.this,SobreNosotros.class);
        startActivity(objVentana);
    }

    public void MenuPrincipales(View view) {
        Intent objVentana = new Intent(MainActivity.this,MenuPrincipal.class);
        startActivity(objVentana);
    }

    public void MenuBebidas(View view) {
        Intent objVentana = new Intent(MainActivity.this,MenuBebidas.class);
        startActivity(objVentana);
    }

    public void MenuPostres(View view) {
        Intent objVentana = new Intent(MainActivity.this,MenuPostres.class);
        startActivity(objVentana);
    }

    public void MenuEntradas(View view) {
        Intent objVentana = new Intent(MainActivity.this,MenuEntradas.class);
        startActivity(objVentana);
    }

    public void Hecho(View view) {
        if (etGrupo.getText().toString().isEmpty() || etHora.getText().toString().isEmpty() || etFecha.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.ToastRelleneTodos, Toast.LENGTH_SHORT).show();
        }
        else {
            SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
            String usuario = preferences.getString("usu","");
            boolean sesion = preferences.getBoolean("sesion", false);
            if(sesion)
            {
                String grupo = etGrupo.getText().toString();
                String fecha = etFecha.getText().toString();
                String hora = etHora.getText().toString();

                realizarReserva(usuario, grupo, fecha, hora);
                Intent objVentana = new Intent(MainActivity.this,MainActivity.class);
                startActivity(objVentana);
            }
            else
            {
                Intent objVentana = new Intent(MainActivity.this,Login.class);
                startActivity(objVentana);
                Toast.makeText(getApplicationContext(), R.string.ToastPrimeroSesion, Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void realizarReserva(final String usuario, final String grupo, final String fecha, final String hora) {
        String url = "http://192.168.0.6/UrbanFood/reservas.php";
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), R.string.ToastReservaOK, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("usuario", usuario);
                params.put("fecha", fecha);
                params.put("hora", hora);
                params.put("grupo", grupo);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void ConsultaUsuario() {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usu","");
        boolean sesion = preferences.getBoolean("sesion", false);
        if(sesion)
        {
            String IP = "192.168.0.6";

            String user = usuario;

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
                                txtID.setText(objetoJSON.getString("id"));
                                txtNombre.setText(objetoJSON.getString("usuario"));
                                txtTelefono.setText(objetoJSON.getString("telefono"));
                            } else {
                                result = objetoJSON.getString("error");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), getString(R.string.ToastErrorJson) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), R.string.ToastErrorServidor, Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            txtError.setText(R.string.ToastPrimeroSesion);
        }
    }

    public void ConsultaReserva() {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        String usuario = preferences.getString("usu","");
        boolean sesion = preferences.getBoolean("sesion", false);
        if(sesion)
        {
            String IP = "192.168.0.6";

            String user = usuario;

            String url = "http://" + IP + "/UrbanFood/consultaReservas.php";

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
                                if (objetoJSON.getInt("rows") == 1)
                                {
                                    txtid1.setText("#"+objetoJSON.getString("id0"));
                                    txtReserva1.setText(objetoJSON.getString("date0"));
                                }
                                else if (objetoJSON.getInt("rows") == 2)
                                {
                                    txtid1.setText("#"+objetoJSON.getString("id0"));
                                    txtReserva1.setText(objetoJSON.getString("date0"));
                                    txtid2.setText("#"+objetoJSON.getString("id1"));
                                    txtReserva2.setText(objetoJSON.getString("date1"));
                                }
                                else if (objetoJSON.getInt("rows") == 3)
                                {
                                    txtid1.setText("#"+objetoJSON.getString("id0"));
                                    txtReserva1.setText(objetoJSON.getString("date0"));
                                    txtid2.setText("#"+objetoJSON.getString("id1"));
                                    txtReserva2.setText(objetoJSON.getString("date1"));
                                    txtid3.setText("#"+objetoJSON.getString("id2"));
                                    txtReserva3.setText(objetoJSON.getString("date2"));
                                }
                            } else {
                                result = objetoJSON.getString("error");
                            }

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(), R.string.ToastErrorJson+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            txtError.setText(R.string.ToastPrimeroSesion);
        }
    }

    public void Sesion(View view) {
        SharedPreferences preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        boolean sesion = preferences.getBoolean("sesion", false);
        if(sesion)
        {
            preferences.edit().clear().commit();
            Intent objVentana = new Intent(MainActivity.this,MainActivity.class);
            startActivity(objVentana);
            Toast.makeText(getApplicationContext(), R.string.ToastSesionFin, Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent objVentana = new Intent(MainActivity.this,Login.class);
            startActivity(objVentana);
        }
    }

    public void Llamar(View view) {
        String phone = "+50376313421";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }
}