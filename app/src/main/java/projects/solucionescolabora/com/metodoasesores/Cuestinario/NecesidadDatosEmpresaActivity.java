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

public class NecesidadDatosEmpresaActivity extends AppCompatActivity {

    private TextInputEditText edit7;
    private TextInputEditText edit8;
    private TextInputEditText edit9;
    private TextInputEditText edit10;
    private TextInputEditText edit11;
    private TextInputEditText edit18;
    private TextInputEditText edit20;

    private Button btn12Comercio;
    private Button btn12Servicio;
    private Button btn12Industria;
    private Button btn12Maquila;

    private Button btn13Idea;
    private Button btn13Proyecto;
    private Button btn13Empresa;
    private Button btn13Otro;

    private Button btn14Rif;
    private Button btn14PersonaFisica;
    private Button btn14EmpresaMoral;
    private Button btn14AsociacionCivil;

    private Button btn15Efectivo;
    private Button btn15PuntoDeVenta;
    private Button btn15Otro;

    private Button btn16Si;
    private Button btn16No;

    private Button btn17Si;
    private Button btn17No;

    private TextInputEditText editDominio18;

    private Button btn19Si;
    private Button btn19No;

    private  TextInputEditText editRedesSociales20;

    private Button btn21Si;
    private Button btn21No;

    private Button btn22Si;
    private Button btn22No;

    private Button btn23Si;
    private Button btn23No;

    private Button btn24Si;
    private Button btn24No;

    private Button btn25Si;
    private Button btn25No;

    private Button btn26Logotipo;
    private Button btn26Derecho;
    private Button btn26Patente;
    private Button btn26Otro;

    private Button btnSiguiente;
    private SharedPreferences misDatos;

    private String _7 = "";
    private String _8 = "";
    private String _9 = "";
    private String _10 = "";
    private String _11 = "";
    private String _12 = "";
    private String _13 = "";
    private String _14 = "";
    private String _15 = "";
    private String _16 = "";
    private String _17 = "";
    private String _18 = "";
    private String _19 = "";
    private String _20 = "";
    private String _21 = "";
    private String _22 = "";
    private String _23 = "";
    private String _24 = "";
    private String _25 = "";
    private String _26 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_necesidad_datos_empresa);
        setTitle("Diagnóstico: Datos generales");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        edit7 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad7);
        edit8 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad8);
        edit9 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad9);
        edit10 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad10);
        edit11 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad11);
        edit18 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad18);
        edit20 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad20);

        btn12Comercio = (Button)findViewById(R.id.btnDiagnosticoNecesidad12Comercio);
        btn12Servicio = (Button)findViewById(R.id.btnDiagnosticoNecesidad12Servicio);
        btn12Industria = (Button)findViewById(R.id.btnDiagnosticoNecesidad12Industria);
        btn12Maquila = (Button)findViewById(R.id.btnDiagnosticoNecesidad12Maquila);

        btn13Idea = (Button)findViewById(R.id.btnDiagnosticoNecesidad13Idea);
        btn13Proyecto = (Button)findViewById(R.id.btnDiagnosticoNecesidad13Proyecto);
        btn13Empresa = (Button)findViewById(R.id.btnDiagnosticoNecesidad13Empresa);
        btn13Otro = (Button)findViewById(R.id.btnDiagnosticoNecesidad13Otro);

        btn14Rif = (Button)findViewById(R.id.btnDiagnosticoNecesidad14RIF);
        btn14PersonaFisica = (Button)findViewById(R.id.btnDiagnosticoNecesidad14PersonaFisica);
        btn14EmpresaMoral = (Button)findViewById(R.id.btnDiagnosticoNecesidad14EmpresaMoral);
        btn14AsociacionCivil = (Button)findViewById(R.id.btnDiagnosticoNecesidad14Asociacion);

        btn15Efectivo = (Button)findViewById(R.id.btnDiagnosticoNecesidad15Efectivo);
        btn15PuntoDeVenta = (Button)findViewById(R.id.btnDiagnosticoNecesidad15PuntoVenta);
        btn15Otro = (Button)findViewById(R.id.btnDiagnosticoNecesidad15Otro);

        btn16Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad16Si);
        btn16No = (Button)findViewById(R.id.btnDiagnosticoNecesidad16No);

        btn17Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad17Si);
        btn17No = (Button)findViewById(R.id.btnDiagnosticoNecesidad17No);

        editDominio18 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad18);

        btn19No = (Button)findViewById(R.id.btnDiagnosticoNecesidad19No);
        btn19Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad19Si);

        editRedesSociales20 = (TextInputEditText)findViewById(R.id.editDiagnosticoNecesidad20);

        btn21No = (Button)findViewById(R.id.btnDiagnosticoNecesidad21No);
        btn21Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad21Si);

        btn22No = (Button)findViewById(R.id.btnDiagnosticoNecesidad22No);
        btn22Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad22Si);

        btn23No = (Button)findViewById(R.id.btnDiagnosticoNecesidad23No);
        btn23Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad23Si);

        btn24No = (Button)findViewById(R.id.btnDiagnosticoNecesidad24No);
        btn24Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad24Si);

        btn25No = (Button)findViewById(R.id.btnDiagnosticoNecesidad25No);
        btn25Si = (Button)findViewById(R.id.btnDiagnosticoNecesidad25Si);

        btn26Logotipo = (Button)findViewById(R.id.btnDiagnosticoNecesidad26Logotipo);
        btn26Derecho = (Button)findViewById(R.id.btnDiagnosticoNecesidad26Derecho);
        btn26Patente = (Button)findViewById(R.id.btnDiagnosticoNecesidad26Patente);
        btn26Otro = (Button)findViewById(R.id.btnDiagnosticoNecesidad26Otro);

        btnSiguiente = (Button)findViewById(R.id.btnDiagnosticoNecesidadSiguiente2);

        // *******************************************
        btn12Comercio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn12Comercio.setBackgroundColor(Color.GREEN);
                btn12Industria.setBackgroundColor(Color.GRAY);
                btn12Maquila.setBackgroundColor(Color.GRAY);
                btn12Servicio.setBackgroundColor(Color.GRAY);
                _12 = btn12Comercio.getText().toString();
            }
        });
        btn12Industria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn12Comercio.setBackgroundColor(Color.GRAY);
                btn12Industria.setBackgroundColor(Color.GREEN);
                btn12Maquila.setBackgroundColor(Color.GRAY);
                btn12Servicio.setBackgroundColor(Color.GRAY);
                _12 = btn12Industria.getText().toString();
            }
        });
        btn12Maquila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn12Comercio.setBackgroundColor(Color.GRAY);
                btn12Industria.setBackgroundColor(Color.GRAY);
                btn12Maquila.setBackgroundColor(Color.GREEN);
                btn12Servicio.setBackgroundColor(Color.GRAY);
                _12 = btn12Maquila.getText().toString();
            }
        });
        btn12Servicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn12Comercio.setBackgroundColor(Color.GRAY);
                btn12Industria.setBackgroundColor(Color.GRAY);
                btn12Maquila.setBackgroundColor(Color.GRAY);
                btn12Servicio.setBackgroundColor(Color.GREEN);
                _12 = btn12Servicio.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn13Idea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn13Idea.setBackgroundColor(Color.GREEN);
                btn13Proyecto.setBackgroundColor(Color.GRAY);
                btn13Empresa.setBackgroundColor(Color.GRAY);
                btn13Otro.setBackgroundColor(Color.GRAY);
                _13 = btn13Idea.getText().toString();
            }
        });
        btn13Proyecto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn13Idea.setBackgroundColor(Color.GRAY);
                btn13Proyecto.setBackgroundColor(Color.GREEN);
                btn13Empresa.setBackgroundColor(Color.GRAY);
                btn13Otro.setBackgroundColor(Color.GRAY);
                _13 = btn13Proyecto.getText().toString();
            }
        });
        btn13Empresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn13Idea.setBackgroundColor(Color.GRAY);
                btn13Proyecto.setBackgroundColor(Color.GRAY);
                btn13Empresa.setBackgroundColor(Color.GREEN);
                btn13Otro.setBackgroundColor(Color.GRAY);
                _13 = btn13Empresa.getText().toString();
            }
        });
        btn13Otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn13Idea.setBackgroundColor(Color.GRAY);
                btn13Proyecto.setBackgroundColor(Color.GRAY);
                btn13Empresa.setBackgroundColor(Color.GRAY);
                btn13Otro.setBackgroundColor(Color.GREEN);
                _13 = btn13Otro.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn14Rif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn14Rif.setBackgroundColor(Color.GREEN);
                btn14PersonaFisica.setBackgroundColor(Color.GRAY);
                btn14EmpresaMoral.setBackgroundColor(Color.GRAY);
                btn14AsociacionCivil.setBackgroundColor(Color.GRAY);
                _14 = btn14Rif.getText().toString();
            }
        });
        btn14PersonaFisica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn14Rif.setBackgroundColor(Color.GRAY);
                btn14PersonaFisica.setBackgroundColor(Color.GREEN);
                btn14EmpresaMoral.setBackgroundColor(Color.GRAY);
                btn14AsociacionCivil.setBackgroundColor(Color.GRAY);
                _14 = btn14PersonaFisica.getText().toString();
            }
        });
        btn14EmpresaMoral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn14Rif.setBackgroundColor(Color.GRAY);
                btn14PersonaFisica.setBackgroundColor(Color.GRAY);
                btn14EmpresaMoral.setBackgroundColor(Color.GREEN);
                btn14AsociacionCivil.setBackgroundColor(Color.GRAY);
                _14 = btn14EmpresaMoral.getText().toString();
            }
        });
        btn14AsociacionCivil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn14Rif.setBackgroundColor(Color.GRAY);
                btn14PersonaFisica.setBackgroundColor(Color.GRAY);
                btn14EmpresaMoral.setBackgroundColor(Color.GRAY);
                btn14AsociacionCivil.setBackgroundColor(Color.GREEN);
                _14 = btn14AsociacionCivil.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn15Efectivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn15Efectivo.setBackgroundColor(Color.GREEN);
                btn15PuntoDeVenta.setBackgroundColor(Color.GRAY);
                btn15Otro.setBackgroundColor(Color.GRAY);
                _15 = btn15Efectivo.getText().toString();
            }
        });
        btn15PuntoDeVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn15Efectivo.setBackgroundColor(Color.GRAY);
                btn15PuntoDeVenta.setBackgroundColor(Color.GREEN);
                btn15Otro.setBackgroundColor(Color.GRAY);
                _15 = btn15PuntoDeVenta.getText().toString();
            }
        });
        btn15Otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn15Efectivo.setBackgroundColor(Color.GRAY);
                btn15PuntoDeVenta.setBackgroundColor(Color.GRAY);
                btn15Otro.setBackgroundColor(Color.GREEN);
                _15 = btn15Otro.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn16No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn16No.setBackgroundColor(Color.GREEN);
                btn16Si.setBackgroundColor(Color.GRAY);
                _16 = btn16No.getText().toString();
            }
        });
        btn16Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn16No.setBackgroundColor(Color.GRAY);
                btn16Si.setBackgroundColor(Color.GREEN);
                _16 = btn16Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn17No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn17No.setBackgroundColor(Color.GREEN);
                btn17Si.setBackgroundColor(Color.GRAY);
                _17 = btn17No.getText().toString();
            }
        });
        btn17Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn17No.setBackgroundColor(Color.GRAY);
                btn17Si.setBackgroundColor(Color.GREEN);
                _17 = btn17Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn19No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn19No.setBackgroundColor(Color.GREEN);
                btn19Si.setBackgroundColor(Color.GRAY);
                _19 = btn19No.getText().toString();
            }
        });
        btn19Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn19No.setBackgroundColor(Color.GRAY);
                btn19Si.setBackgroundColor(Color.GREEN);
                _19 = btn19Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn21No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn21No.setBackgroundColor(Color.GREEN);
                btn21Si.setBackgroundColor(Color.GRAY);
                _21 = btn21No.getText().toString();
            }
        });
        btn21Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn21No.setBackgroundColor(Color.GRAY);
                btn21Si.setBackgroundColor(Color.GREEN);
                _21 = btn21Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn22No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn22No.setBackgroundColor(Color.GREEN);
                btn22Si.setBackgroundColor(Color.GRAY);
                _22 = btn22No.getText().toString();
            }
        });
        btn22Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn22No.setBackgroundColor(Color.GRAY);
                btn22Si.setBackgroundColor(Color.GREEN);
                _22 = btn22Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn23No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn23No.setBackgroundColor(Color.GREEN);
                btn23Si.setBackgroundColor(Color.GRAY);
                _23 = btn23No.getText().toString();
            }
        });
        btn23Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn23No.setBackgroundColor(Color.GRAY);
                btn23Si.setBackgroundColor(Color.GREEN);
                _23 = btn23Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn24No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn24No.setBackgroundColor(Color.GREEN);
                btn24Si.setBackgroundColor(Color.GRAY);
                _24 = btn24No.getText().toString();
            }
        });
        btn24Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn24No.setBackgroundColor(Color.GRAY);
                btn24Si.setBackgroundColor(Color.GREEN);
                _24 = btn24Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn25No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn25No.setBackgroundColor(Color.GREEN);
                btn25Si.setBackgroundColor(Color.GRAY);
                _25 = btn25No.getText().toString();
            }
        });
        btn25Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn25No.setBackgroundColor(Color.GRAY);
                btn25Si.setBackgroundColor(Color.GREEN);
                _25 = btn25Si.getText().toString();
            }
        });
        // -------------------------------------------

        // *******************************************
        btn26Logotipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn26Logotipo.setBackgroundColor(Color.GREEN);
                btn26Derecho.setBackgroundColor(Color.GRAY);
                btn26Patente.setBackgroundColor(Color.GRAY);
                btn26Otro.setBackgroundColor(Color.GRAY);
                _26 = btn26Logotipo.getText().toString();
            }
        });
        btn26Derecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn26Logotipo.setBackgroundColor(Color.GRAY);
                btn26Derecho.setBackgroundColor(Color.GREEN);
                btn26Patente.setBackgroundColor(Color.GRAY);
                btn26Otro.setBackgroundColor(Color.GRAY);
                _26 = btn26Derecho.getText().toString();
            }
        });
        btn26Patente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn26Logotipo.setBackgroundColor(Color.GRAY);
                btn26Derecho.setBackgroundColor(Color.GRAY);
                btn26Patente.setBackgroundColor(Color.GREEN);
                btn26Otro.setBackgroundColor(Color.GRAY);
                _26 = btn26Patente.getText().toString();
            }
        });
        btn26Otro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn26Logotipo.setBackgroundColor(Color.GRAY);
                btn26Derecho.setBackgroundColor(Color.GRAY);
                btn26Patente.setBackgroundColor(Color.GRAY);
                btn26Otro.setBackgroundColor(Color.GREEN);
                _26 = btn26Otro.getText().toString();
            }
        });
        // -------------------------------------------

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEdits();
            }
        });
    }

    private void checkEdits() {

        boolean isOk = true;

        _7 = edit7.getText().toString();
        _8 = edit8.getText().toString();
        _9 = edit9.getText().toString();
        _10 = edit10.getText().toString();
        _11 = edit11.getText().toString();
        _18 = edit18.getText().toString();
        _20 = edit20.getText().toString();

        if (_7.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_8.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_9.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_10.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_11.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_12.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_13.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_14.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_15.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_16.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_17.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_19.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_21.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_22.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_23.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_24.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (_25.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }

        if (isOk == true) {

            // *********** Guardamos el dato de la ubicación actual del usuario *************

            misDatos = getSharedPreferences("misDatos", 0);
            SharedPreferences.Editor editor = misDatos.edit();
            editor.putString("DiagnosticoNecesidad7", _7);
            editor.putString("DiagnosticoNecesidad8", _8);
            editor.putString("DiagnosticoNecesidad9", _9);
            editor.putString("DiagnosticoNecesidad10", _10);
            editor.putString("DiagnosticoNecesidad11", _11);
            editor.putString("DiagnosticoNecesidad12", _12);
            editor.putString("DiagnosticoNecesidad13", _13);
            editor.putString("DiagnosticoNecesidad14", _14);
            editor.putString("DiagnosticoNecesidad15", _15);
            editor.putString("DiagnosticoNecesidad16", _16);
            editor.putString("DiagnosticoNecesidad17", _17);
            editor.putString("DiagnosticoNecesidad18", _18);
            editor.putString("DiagnosticoNecesidad19", _19);
            editor.putString("DiagnosticoNecesidad20", _20);
            editor.putString("DiagnosticoNecesidad21", _21);
            editor.putString("DiagnosticoNecesidad22", _22);
            editor.putString("DiagnosticoNecesidad23", _23);
            editor.putString("DiagnosticoNecesidad24", _24);
            editor.putString("DiagnosticoNecesidad25", _25);
            editor.putString("DiagnosticoNecesidad26", _26);


            editor.commit();
            // ******************************************************************************

            Intent i = new Intent(NecesidadDatosEmpresaActivity.this, NecesidadCapitalHumanoActivity.class);
            startActivity(i);
            finish();
        }
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NecesidadDatosEmpresaActivity.this);
        builder.setMessage("¿Estás seguro de querer salir del test diagnóstico? Si lo haces tendrás que iniciar de nuevo")
                .setPositiveButton("Salir del diagnóstico", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(NecesidadDatosEmpresaActivity.this, MainActivity.class);
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
