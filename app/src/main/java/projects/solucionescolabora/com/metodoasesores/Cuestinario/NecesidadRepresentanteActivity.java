package projects.solucionescolabora.com.metodoasesores.Cuestinario;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.R;

public class NecesidadRepresentanteActivity extends AppCompatActivity {

    private TextInputEditText editNombres;
    private TextInputEditText editEdad;
    private Button btnGeneroFemenino;
    private Button btnGeneroMasculino;
    private TextInputEditText editOcupacion;
    private TextInputEditText editTelefono;
    private TextInputEditText editCorreo;
    private Button btnSiguiente;

    private String nombres = "";
    private String edad = "";
    private String ocupacion = "";
    private String telefono = "";
    private String correo = "";
    private String genero = "";

    private SharedPreferences misDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_necesidad_representante);
        setTitle("Diagnóstico: Datos generales");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editNombres = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad1);
        editEdad = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad2);
        editOcupacion = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad3);
        editTelefono = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad4);
        editCorreo = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad5);
        btnGeneroFemenino = (Button)findViewById(R.id.btnDiagnosticoNecesidad6Femenino);
        btnGeneroMasculino = (Button)findViewById(R.id.btnDiagnosticoNecesidad6Masc);
        btnSiguiente = (Button)findViewById(R.id.btnDiagnosticoNecesidadSiguiente1);

        // ******************** SECCION DE REQ FONDOS ************************
        btnGeneroFemenino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGeneroFemenino.setBackgroundColor(Color.GREEN);
                btnGeneroMasculino.setBackgroundColor(Color.GRAY);
                genero = btnGeneroFemenino.getText().toString();
            }
        });
        btnGeneroMasculino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGeneroFemenino.setBackgroundColor(Color.GRAY);
                btnGeneroMasculino.setBackgroundColor(Color.GREEN);
                genero = btnGeneroMasculino.getText().toString();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEdits();
            }
        });

    }

    private void checkEdits() {

        boolean isOk = true;

        nombres = editNombres.getText().toString();
        edad = editEdad.getText().toString();
        ocupacion = editOcupacion.getText().toString();
        telefono = editTelefono.getText().toString();
        correo = editCorreo.getText().toString();

        if (nombres.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (edad.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (ocupacion.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (telefono.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (correo.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (genero.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }

        if (isOk == true) {

            // *********** Guardamos el dato de la ubicación actual del usuario *************

            misDatos = getSharedPreferences("misDatos", 0);
            SharedPreferences.Editor editor = misDatos.edit();
            editor.putString("DiagnosticoNecesidad1", nombres);
            editor.putString("DiagnosticoNecesidad2", edad);
            editor.putString("DiagnosticoNecesidad3", genero);
            editor.putString("DiagnosticoNecesidad4", ocupacion);
            editor.putString("DiagnosticoNecesidad5", telefono);
            editor.putString("DiagnosticoNecesidad6", correo);

            editor.commit();
            // ******************************************************************************

            Intent i = new Intent(NecesidadRepresentanteActivity.this, NecesidadDatosEmpresaActivity.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NecesidadRepresentanteActivity.this);
        builder.setMessage("¿Estás seguro de querer salir del test diagnóstico? Si lo haces tendrás que iniciar de nuevo")
                .setPositiveButton("Salir del diagnóstico", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(NecesidadRepresentanteActivity.this, MainActivity.class);
                        finish();
                        startActivity(i);
                    }
                })
                .setNegativeButton("Quedarme aqui", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }
}
