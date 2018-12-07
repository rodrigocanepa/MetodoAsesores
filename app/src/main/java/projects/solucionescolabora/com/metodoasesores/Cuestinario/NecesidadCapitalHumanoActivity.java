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

public class NecesidadCapitalHumanoActivity extends AppCompatActivity {

    private Button btn27Si;
    private Button btn27No;

    private Button btn29Si;
    private Button btn29No;

    private Button btn30Si;
    private Button btn30No;

    private Button btn31Si;
    private Button btn31No;

    private Button btn32Si;
    private Button btn32No;

    private Button btn33Si;
    private Button btn33No;

    private Button btnSiguiente;
    private TextInputEditText editNombreComercial;

    private String _27 = "";
    private String _28 = "";
    private String _29 = "";
    private String _30 = "";
    private String _31 = "";
    private String _32 = "";
    private String _33 = "";

    private SharedPreferences misDatos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_necesidad_capital_humano);
        setTitle("Diagnóstico: Datos generales");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btn27No = (Button)findViewById(R.id.btnDiagnosticoNecesidad27No);
        btn27Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad27Si);

        btn29No = (Button)findViewById(R.id.btnDiagnosticoNecesidad29No);
        btn29Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad29Si);

        btn30No = (Button)findViewById(R.id.btnDiagnosticoNecesidad30No);
        btn30Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad30Si);

        btn31No = (Button)findViewById(R.id.btnDiagnosticoNecesidad31No);
        btn31Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad31Si);

        btn32No = (Button)findViewById(R.id.btnDiagnosticoNecesidad32No);
        btn32Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad32Si);

        btn33No = (Button)findViewById(R.id.btnDiagnosticoNecesidad33No);
        btn33Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad33Si);

        editNombreComercial = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad28);

        btnSiguiente = (Button)findViewById(R.id.btnSiguienteNecesidad3);


        btn27No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn27No.setBackgroundColor(Color.GREEN);
                btn27Si.setBackgroundColor(Color.GRAY);
                _27 = btn27No.getText().toString();
            }
        });
        btn27Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn27No.setBackgroundColor(Color.GRAY);
                btn27Si.setBackgroundColor(Color.GREEN);
                _27 = btn27Si.getText().toString();
            }
        });

        btn29No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn29No.setBackgroundColor(Color.GREEN);
                btn29Si.setBackgroundColor(Color.GRAY);
                _29 = btn29No.getText().toString();
            }
        });
        btn29Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn29No.setBackgroundColor(Color.GRAY);
                btn29Si.setBackgroundColor(Color.GREEN);
                _29 = btn29Si.getText().toString();
            }
        });

        btn30No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn30No.setBackgroundColor(Color.GREEN);
                btn30Si.setBackgroundColor(Color.GRAY);
                _30 = btn30No.getText().toString();
            }
        });
        btn30Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn30No.setBackgroundColor(Color.GRAY);
                btn30Si.setBackgroundColor(Color.GREEN);
                _30 = btn30Si.getText().toString();
            }
        });

        btn31No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn31No.setBackgroundColor(Color.GREEN);
                btn31Si.setBackgroundColor(Color.GRAY);
                _31 = btn31No.getText().toString();
            }
        });
        btn31Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn31No.setBackgroundColor(Color.GRAY);
                btn31Si.setBackgroundColor(Color.GREEN);
                _31 = btn31Si.getText().toString();
            }
        });

        btn32No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn32No.setBackgroundColor(Color.GREEN);
                btn32Si.setBackgroundColor(Color.GRAY);
                _32 = btn32No.getText().toString();
            }
        });
        btn32Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn32No.setBackgroundColor(Color.GRAY);
                btn32Si.setBackgroundColor(Color.GREEN);
                _32 = btn32Si.getText().toString();
            }
        });

        btn33No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn33No.setBackgroundColor(Color.GREEN);
                btn33Si.setBackgroundColor(Color.GRAY);
                _33 = btn33No.getText().toString();
            }
        });
        btn33Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn33No.setBackgroundColor(Color.GRAY);
                btn33Si.setBackgroundColor(Color.GREEN);
                _33 = btn33Si.getText().toString();
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

        _28 = editNombreComercial.getText().toString();

        if (_27.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        if (_28.length() < 1) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_29.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_30.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_31.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_32.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_33.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }

        if(editNombreComercial.getText().toString().isEmpty()){
            isOk = false;
            editNombreComercial.setError("Debes llenar este campo");
        }

        if (isOk == true) {

            // *********** Guardamos el dato de la ubicación actual del usuario *************

            misDatos = getSharedPreferences("misDatos", 0);
            SharedPreferences.Editor editor = misDatos.edit();
            editor.putString("DiagnosticoNecesidad27", _27);
            editor.putString("DiagnosticoNecesidad28", _28);
            editor.putString("DiagnosticoNecesidad29", _29);
            editor.putString("DiagnosticoNecesidad30", _30);
            editor.putString("DiagnosticoNecesidad31", _31);
            editor.putString("DiagnosticoNecesidad32", _32);
            editor.putString("DiagnosticoNecesidad33", _33);

            editor.commit();


            Intent i = new Intent(NecesidadCapitalHumanoActivity.this, NecesidadTestActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NecesidadCapitalHumanoActivity.this);
        builder.setMessage("¿Estás seguro de querer salir del test diagnóstico? Si lo haces tendrás que iniciar de nuevo")
                .setPositiveButton("Salir del diagnóstico", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(NecesidadCapitalHumanoActivity.this, MainActivity.class);
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
