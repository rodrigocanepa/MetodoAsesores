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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.TemplatePDF;
import projects.solucionescolabora.com.metodoasesores.R;

public class NecesidadTestActivity extends AppCompatActivity {

    private Button btnReqFondosMuyUrg;
    private Button btnReqFondosUrg;
    private Button btnReqFondosIgual;
    private Button btnReqFondosNoUrg;
    private Button btnReqFondosNadaUrg;

    private Button btnFormalizacionMuyUrg;
    private Button btnFormalizacionUrg;
    private Button btnFormalizacionIgual;
    private Button btnFormalizacionNoUrg;
    private Button btnFormalizacionNadaUrg;

    private Button btnCreacionMuyUrg;
    private Button btnCreacionUrg;
    private Button btnCreacionIgual;
    private Button btnCreacionNoUrg;
    private Button btnCreacionNadaUrg;

    private Button btnEstrategiasMuyUrg;
    private Button btnEstrategiasUrg;
    private Button btnEstrategiasIgual;
    private Button btnEstrategiasNoUrg;
    private Button btnEstrategiasNadaUrg;

    private Button btnAnalEstatusMuyUrg;
    private Button btnAnalEstatusUrg;
    private Button btnAnalEstatusIgual;
    private Button btnAnalEstatusNoUrg;
    private Button btnAnalEstatusNadaUrg;

    private Button btnProtecLegalMuyUrg;
    private Button btnProtecLegalUrg;
    private Button btnProtecLegalIgual;
    private Button btnProtecLegalNoUrg;
    private Button btnProtecLegalNadaUrg;

    private Button btnDisenioMuyUrg;
    private Button btnDisenioUrg;
    private Button btnDisenioIgual;
    private Button btnDisenioNoUrg;
    private Button btnDisenioNadaUrg;

    private Button btnEstMerMuyUrg;
    private Button btnEstMerUrg;
    private Button btnEstMerIgual;
    private Button btnEstMerNoUrg;
    private Button btnEstMerNadaUrg;

    private Button btnDesManMuyUrg;
    private Button btnDesManUrg;
    private Button btnDesManIgual;
    private Button btnDesManNoUrg;
    private Button btnDesManNadaUrg;

    private Button btnDesEstadosMuyUrg;
    private Button btnDesEstadosUrg;
    private Button btnDesEstadosIgual;
    private Button btnDesEstadosNoUrg;
    private Button btnDesEstadosNadaUrg;

    private Button btnTramSueloMuyUrg;
    private Button btnTramSueloUrg;
    private Button btnTramSueloIgual;
    private Button btnTramSueloNoUrg;
    private Button btnTramSueloNadaUrg;

    private Button btnComercializacionMuyUrg;
    private Button btnComercializacionUrg;
    private Button btnComercializacionIgual;
    private Button btnComercializacionNoUrg;
    private Button btnComercializacionNadaUrg;

    private Button btnComunnityMuyUrg;
    private Button btnComunnityUrg;
    private Button btnComunnityIgual;
    private Button btnComunnityNoUrg;
    private Button btnComunnityNadaUrg;

    private TextInputEditText editOtros;
    private TextInputEditText editComentarios;
    private Button btnSiguiente;

    private String reqFondos = "";
    private String formalizacion = "";
    private String creacion = "";
    private String estrategias = "";
    private String analEstatus = "";
    private String protectLegal = "";
    private String disenio = "";
    private String estMer = "";
    private String desMan = "";
    private String desEstados = "";
    private String tramSuelo = "";
    private String comercializacion = "";
    private String comunnity = "";
    private SharedPreferences misDatos;

    // GENERACION DE PDF
    private TemplatePDF templatePDF;
    private String formattedDate ="";
    private String[]headerPreguntas = {"Pregunta", "Respuesta"};

    private String UUIDuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_necesidad_test);
        setTitle("Diagnóstico: Datos generales");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnReqFondosMuyUrg = (Button)findViewById(R.id.btnReqFondosMuyUrg);
        btnReqFondosUrg = (Button)findViewById(R.id.btnReqFondosUrg);
        btnReqFondosIgual = (Button)findViewById(R.id.btnReqFondosIgual);
        btnReqFondosNoUrg = (Button)findViewById(R.id.btnReqFondosNoUrg);
        btnReqFondosNadaUrg = (Button)findViewById(R.id.btnReqFondosNadaUrg);

        btnFormalizacionMuyUrg = (Button)findViewById(R.id.btnFormalizacionMuyUrg);
        btnFormalizacionUrg = (Button)findViewById(R.id.btnFormalizacionUrg);
        btnFormalizacionIgual = (Button)findViewById(R.id.btnFormalizacionIgual);
        btnFormalizacionNoUrg = (Button)findViewById(R.id.btnFormalizacionNoUrg);
        btnFormalizacionNadaUrg = (Button)findViewById(R.id.btnFormalizacionNadaUrg);

        btnCreacionMuyUrg = (Button)findViewById(R.id.btnCreacionMuyUrg);
        btnCreacionUrg = (Button)findViewById(R.id.btnCreacionUrg);
        btnCreacionIgual = (Button)findViewById(R.id.btnCreacionIgual);
        btnCreacionNoUrg = (Button)findViewById(R.id.btnCreacionNoUrg);
        btnCreacionNadaUrg = (Button)findViewById(R.id.btnCreacionNadaUrg);

        btnEstrategiasMuyUrg = (Button)findViewById(R.id.btnEstrategiasMuyUrg);
        btnEstrategiasUrg = (Button)findViewById(R.id.btnEstrategiasUrg);
        btnEstrategiasIgual = (Button)findViewById(R.id.btnEstrategiasIgual);
        btnEstrategiasNoUrg = (Button)findViewById(R.id.btnEstrategiasNoUrg);
        btnEstrategiasNadaUrg = (Button)findViewById(R.id.btnEstrategiasNadaUrg);

        btnAnalEstatusMuyUrg = (Button)findViewById(R.id.btnAnalEstatusMuyUrg);
        btnAnalEstatusUrg = (Button)findViewById(R.id.btnAnalEstatusUrg);
        btnAnalEstatusIgual = (Button)findViewById(R.id.btnAnalEstatusIgual);
        btnAnalEstatusNoUrg = (Button)findViewById(R.id.btnAnalEstatusNoUrg);
        btnAnalEstatusNadaUrg = (Button)findViewById(R.id.btnAnalEstatusNadaUrg);

        btnProtecLegalMuyUrg = (Button)findViewById(R.id.btnProtecLegalMuyUrg);
        btnProtecLegalUrg = (Button)findViewById(R.id.btnProtecLegalUrg);
        btnProtecLegalIgual = (Button)findViewById(R.id.btnProtecLegalIgual);
        btnProtecLegalNoUrg = (Button)findViewById(R.id.btnProtecLegalNoUrg);
        btnProtecLegalNadaUrg = (Button)findViewById(R.id.btnProtecLegalNadaUrg);

        btnDisenioMuyUrg = (Button)findViewById(R.id.btnDisenioMuyUrg);
        btnDisenioUrg = (Button)findViewById(R.id.btnDisenioUrg);
        btnDisenioIgual = (Button)findViewById(R.id.btnDisenioIgual);
        btnDisenioNoUrg = (Button)findViewById(R.id.btnDisenioNoUrg);
        btnDisenioNadaUrg = (Button)findViewById(R.id.btnDisenioNadaUrg);

        btnEstMerMuyUrg = (Button)findViewById(R.id.btnEstMerMuyUrg);
        btnEstMerUrg = (Button)findViewById(R.id.btnEstMerUrg);
        btnEstMerIgual = (Button)findViewById(R.id.btnEstMerIgual);
        btnEstMerNoUrg = (Button)findViewById(R.id.btnEstMerNoUrg);
        btnEstMerNadaUrg = (Button)findViewById(R.id.btnEstMerNadaUrg);

        btnDesManMuyUrg = (Button)findViewById(R.id.btnDesManMuyUrg);
        btnDesManUrg = (Button)findViewById(R.id.btnDesManUrg);
        btnDesManIgual = (Button)findViewById(R.id.btnDesManIgual);
        btnDesManNoUrg = (Button)findViewById(R.id.btnDesManNoUrg);
        btnDesManNadaUrg = (Button)findViewById(R.id.btnDesManNadaUrg);

        btnDesEstadosMuyUrg = (Button)findViewById(R.id.btnDesEstadosMuyUrg);
        btnDesEstadosUrg = (Button)findViewById(R.id.btnDesEstadosUrg);
        btnDesEstadosIgual = (Button)findViewById(R.id.btnDesEstadosIgual);
        btnDesEstadosNoUrg = (Button)findViewById(R.id.btnDesEstadosNoUrg);
        btnDesEstadosNadaUrg = (Button)findViewById(R.id.btnDesEstadosNadaUrg);

        btnTramSueloMuyUrg = (Button)findViewById(R.id.btnTramSueloMuyUrg);
        btnTramSueloUrg = (Button)findViewById(R.id.btnTramSueloUrg);
        btnTramSueloIgual = (Button)findViewById(R.id.btnTramSueloIgual);
        btnTramSueloNoUrg = (Button)findViewById(R.id.btnTramSueloNoUrg);
        btnTramSueloNadaUrg = (Button)findViewById(R.id.btnTramSueloNadaUrg);

        btnComercializacionMuyUrg = (Button)findViewById(R.id.btnComercializacionMuyUrg);
        btnComercializacionUrg = (Button)findViewById(R.id.btnComercializacionUrg);
        btnComercializacionIgual = (Button)findViewById(R.id.btnComercializacionIgual);
        btnComercializacionNoUrg = (Button)findViewById(R.id.btnComercializacionNoUrg);
        btnComercializacionNadaUrg = (Button)findViewById(R.id.btnComercializacionNadaUrg);

        btnComunnityMuyUrg = (Button)findViewById(R.id.btnComunnityMuyUrg);
        btnComunnityUrg = (Button)findViewById(R.id.btnComunnityUrg);
        btnComunnityIgual = (Button)findViewById(R.id.btnComunnityIgual);
        btnComunnityNoUrg = (Button)findViewById(R.id.btnComunnityNoUrg);
        btnComunnityNadaUrg = (Button)findViewById(R.id.btnComunnityNadaUrg);

        editOtros = (TextInputEditText) findViewById(R.id.editDiagnosticoNecesidad48);
        editComentarios = (TextInputEditText) findViewById(R.id.editDiagnosticoNecesidad49);
        btnSiguiente = (Button)findViewById(R.id.btnDiagnosticoNecesidadFinalizar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDuser = user.getUid();
        // ******************** SECCION DE REQ FONDOS ************************
        btnReqFondosMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReqFondosMuyUrg.setBackgroundColor(Color.GREEN);
                btnReqFondosUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosIgual.setBackgroundColor(Color.GRAY);
                btnReqFondosNoUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosNadaUrg.setBackgroundColor(Color.GRAY);
                reqFondos = btnReqFondosMuyUrg.getText().toString();
            }
        });

        btnReqFondosUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReqFondosMuyUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosUrg.setBackgroundColor(Color.GREEN);
                btnReqFondosIgual.setBackgroundColor(Color.GRAY);
                btnReqFondosNoUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosNadaUrg.setBackgroundColor(Color.GRAY);
                reqFondos = btnReqFondosUrg.getText().toString();
            }
        });

        btnReqFondosIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReqFondosMuyUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosIgual.setBackgroundColor(Color.GREEN);
                btnReqFondosNoUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosNadaUrg.setBackgroundColor(Color.GRAY);
                reqFondos = btnReqFondosIgual.getText().toString();
            }
        });

        btnReqFondosNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReqFondosMuyUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosIgual.setBackgroundColor(Color.GRAY);
                btnReqFondosNoUrg.setBackgroundColor(Color.GREEN);
                btnReqFondosNadaUrg.setBackgroundColor(Color.GRAY);
                reqFondos = btnReqFondosNoUrg.getText().toString();
            }
        });

        btnReqFondosNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnReqFondosMuyUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosIgual.setBackgroundColor(Color.GRAY);
                btnReqFondosNoUrg.setBackgroundColor(Color.GRAY);
                btnReqFondosNadaUrg.setBackgroundColor(Color.GREEN);
                reqFondos = btnReqFondosNadaUrg.getText().toString();
            }
        });


        // ******************** SECCION DE FORMALIZACION ************************
        btnFormalizacionMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFormalizacionMuyUrg.setBackgroundColor(Color.GREEN);
                btnFormalizacionUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionIgual.setBackgroundColor(Color.GRAY);
                btnFormalizacionNoUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionNadaUrg.setBackgroundColor(Color.GRAY);
                formalizacion = btnFormalizacionMuyUrg.getText().toString();
            }
        });

        btnFormalizacionUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFormalizacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionUrg.setBackgroundColor(Color.GREEN);
                btnFormalizacionIgual.setBackgroundColor(Color.GRAY);
                btnFormalizacionNoUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionNadaUrg.setBackgroundColor(Color.GRAY);
                formalizacion = btnFormalizacionUrg.getText().toString();
            }
        });

        btnFormalizacionIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFormalizacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionIgual.setBackgroundColor(Color.GREEN);
                btnFormalizacionNoUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionNadaUrg.setBackgroundColor(Color.GRAY);
                formalizacion = btnFormalizacionIgual.getText().toString();
            }
        });

        btnFormalizacionNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFormalizacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionIgual.setBackgroundColor(Color.GRAY);
                btnFormalizacionNoUrg.setBackgroundColor(Color.GREEN);
                btnFormalizacionNadaUrg.setBackgroundColor(Color.GRAY);
                formalizacion = btnFormalizacionNoUrg.getText().toString();
            }
        });

        btnFormalizacionNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFormalizacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionIgual.setBackgroundColor(Color.GRAY);
                btnFormalizacionNoUrg.setBackgroundColor(Color.GRAY);
                btnFormalizacionNadaUrg.setBackgroundColor(Color.GREEN);
                formalizacion = btnFormalizacionNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE CREACION ************************
        btnCreacionMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreacionMuyUrg.setBackgroundColor(Color.GREEN);
                btnCreacionUrg.setBackgroundColor(Color.GRAY);
                btnCreacionIgual.setBackgroundColor(Color.GRAY);
                btnCreacionNoUrg.setBackgroundColor(Color.GRAY);
                btnCreacionNadaUrg.setBackgroundColor(Color.GRAY);
                creacion = btnCreacionMuyUrg.getText().toString();
            }
        });

        btnCreacionUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnCreacionUrg.setBackgroundColor(Color.GREEN);
                btnCreacionIgual.setBackgroundColor(Color.GRAY);
                btnCreacionNoUrg.setBackgroundColor(Color.GRAY);
                btnCreacionNadaUrg.setBackgroundColor(Color.GRAY);
                creacion = btnCreacionUrg.getText().toString();
            }
        });

        btnCreacionIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnCreacionUrg.setBackgroundColor(Color.GRAY);
                btnCreacionIgual.setBackgroundColor(Color.GREEN);
                btnCreacionNoUrg.setBackgroundColor(Color.GRAY);
                btnCreacionNadaUrg.setBackgroundColor(Color.GRAY);
                creacion = btnCreacionIgual.getText().toString();
            }
        });

        btnCreacionNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnCreacionUrg.setBackgroundColor(Color.GRAY);
                btnCreacionIgual.setBackgroundColor(Color.GRAY);
                btnCreacionNoUrg.setBackgroundColor(Color.GREEN);
                btnCreacionNadaUrg.setBackgroundColor(Color.GRAY);
                creacion = btnCreacionNoUrg.getText().toString();
            }
        });

        btnCreacionNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCreacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnCreacionUrg.setBackgroundColor(Color.GRAY);
                btnCreacionIgual.setBackgroundColor(Color.GRAY);
                btnCreacionNoUrg.setBackgroundColor(Color.GRAY);
                btnCreacionNadaUrg.setBackgroundColor(Color.GREEN);
                creacion = btnCreacionNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE ESTRATEGIAS COMERCIALES ************************
        btnEstrategiasMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstrategiasMuyUrg.setBackgroundColor(Color.GREEN);
                btnEstrategiasUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasIgual.setBackgroundColor(Color.GRAY);
                btnEstrategiasNoUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasNadaUrg.setBackgroundColor(Color.GRAY);
                estrategias = btnEstrategiasMuyUrg.getText().toString();
            }
        });

        btnEstrategiasUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstrategiasMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasUrg.setBackgroundColor(Color.GREEN);
                btnEstrategiasIgual.setBackgroundColor(Color.GRAY);
                btnEstrategiasNoUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasNadaUrg.setBackgroundColor(Color.GRAY);
                estrategias = btnEstrategiasUrg.getText().toString();
            }
        });

        btnEstrategiasIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstrategiasMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasIgual.setBackgroundColor(Color.GREEN);
                btnEstrategiasNoUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasNadaUrg.setBackgroundColor(Color.GRAY);
                estrategias = btnEstrategiasIgual.getText().toString();
            }
        });

        btnEstrategiasNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstrategiasMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasIgual.setBackgroundColor(Color.GRAY);
                btnEstrategiasNoUrg.setBackgroundColor(Color.GREEN);
                btnEstrategiasNadaUrg.setBackgroundColor(Color.GRAY);
                estrategias = btnEstrategiasNoUrg.getText().toString();
            }
        });

        btnEstrategiasNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstrategiasMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasIgual.setBackgroundColor(Color.GRAY);
                btnEstrategiasNoUrg.setBackgroundColor(Color.GRAY);
                btnEstrategiasNadaUrg.setBackgroundColor(Color.GREEN);
                estrategias = btnEstrategiasNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE ANAL ESTATUS ************************
        btnAnalEstatusMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAnalEstatusMuyUrg.setBackgroundColor(Color.GREEN);
                btnAnalEstatusUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusIgual.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNoUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNadaUrg.setBackgroundColor(Color.GRAY);
                analEstatus = btnAnalEstatusMuyUrg.getText().toString();
            }
        });

        btnAnalEstatusUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAnalEstatusMuyUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusUrg.setBackgroundColor(Color.GREEN);
                btnAnalEstatusIgual.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNoUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNadaUrg.setBackgroundColor(Color.GRAY);
                analEstatus = btnAnalEstatusUrg.getText().toString();
            }
        });

        btnAnalEstatusIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAnalEstatusMuyUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusIgual.setBackgroundColor(Color.GREEN);
                btnAnalEstatusNoUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNadaUrg.setBackgroundColor(Color.GRAY);
                analEstatus = btnAnalEstatusIgual.getText().toString();
            }
        });

        btnAnalEstatusNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAnalEstatusMuyUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusIgual.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNoUrg.setBackgroundColor(Color.GREEN);
                btnAnalEstatusNadaUrg.setBackgroundColor(Color.GRAY);
                analEstatus = btnAnalEstatusNoUrg.getText().toString();
            }
        });

        btnAnalEstatusNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnAnalEstatusMuyUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusIgual.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNoUrg.setBackgroundColor(Color.GRAY);
                btnAnalEstatusNadaUrg.setBackgroundColor(Color.GREEN);
                analEstatus = btnAnalEstatusNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE PROTEC LEGAL ************************
        btnProtecLegalMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProtecLegalMuyUrg.setBackgroundColor(Color.GREEN);
                btnProtecLegalUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalIgual.setBackgroundColor(Color.GRAY);
                btnProtecLegalNoUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalNadaUrg.setBackgroundColor(Color.GRAY);
                protectLegal = btnProtecLegalMuyUrg.getText().toString();
            }
        });

        btnProtecLegalUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProtecLegalMuyUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalUrg.setBackgroundColor(Color.GREEN);
                btnProtecLegalIgual.setBackgroundColor(Color.GRAY);
                btnProtecLegalNoUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalNadaUrg.setBackgroundColor(Color.GRAY);
                protectLegal = btnProtecLegalUrg.getText().toString();
            }
        });

        btnProtecLegalIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProtecLegalMuyUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalIgual.setBackgroundColor(Color.GREEN);
                btnProtecLegalNoUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalNadaUrg.setBackgroundColor(Color.GRAY);
                protectLegal = btnProtecLegalIgual.getText().toString();
            }
        });

        btnProtecLegalNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProtecLegalMuyUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalIgual.setBackgroundColor(Color.GRAY);
                btnProtecLegalNoUrg.setBackgroundColor(Color.GREEN);
                btnProtecLegalNadaUrg.setBackgroundColor(Color.GRAY);
                protectLegal = btnProtecLegalNoUrg.getText().toString();
            }
        });

        btnProtecLegalNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnProtecLegalMuyUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalIgual.setBackgroundColor(Color.GRAY);
                btnProtecLegalNoUrg.setBackgroundColor(Color.GRAY);
                btnProtecLegalNadaUrg.setBackgroundColor(Color.GREEN);
                protectLegal = btnProtecLegalNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE DISEÑO ************************
        btnDisenioMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisenioMuyUrg.setBackgroundColor(Color.GREEN);
                btnDisenioUrg.setBackgroundColor(Color.GRAY);
                btnDisenioIgual.setBackgroundColor(Color.GRAY);
                btnDisenioNoUrg.setBackgroundColor(Color.GRAY);
                btnDisenioNadaUrg.setBackgroundColor(Color.GRAY);
                disenio = btnDisenioMuyUrg.getText().toString();
            }
        });

        btnDisenioUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisenioMuyUrg.setBackgroundColor(Color.GRAY);
                btnDisenioUrg.setBackgroundColor(Color.GREEN);
                btnDisenioIgual.setBackgroundColor(Color.GRAY);
                btnDisenioNoUrg.setBackgroundColor(Color.GRAY);
                btnDisenioNadaUrg.setBackgroundColor(Color.GRAY);
                disenio = btnDisenioUrg.getText().toString();
            }
        });

        btnDisenioIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisenioMuyUrg.setBackgroundColor(Color.GRAY);
                btnDisenioUrg.setBackgroundColor(Color.GRAY);
                btnDisenioIgual.setBackgroundColor(Color.GREEN);
                btnDisenioNoUrg.setBackgroundColor(Color.GRAY);
                btnDisenioNadaUrg.setBackgroundColor(Color.GRAY);
                disenio = btnDisenioIgual.getText().toString();
            }
        });

        btnDisenioNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisenioMuyUrg.setBackgroundColor(Color.GRAY);
                btnDisenioUrg.setBackgroundColor(Color.GRAY);
                btnDisenioIgual.setBackgroundColor(Color.GRAY);
                btnDisenioNoUrg.setBackgroundColor(Color.GREEN);
                btnDisenioNadaUrg.setBackgroundColor(Color.GRAY);
                disenio = btnDisenioNoUrg.getText().toString();
            }
        });

        btnDisenioNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDisenioMuyUrg.setBackgroundColor(Color.GRAY);
                btnDisenioUrg.setBackgroundColor(Color.GRAY);
                btnDisenioIgual.setBackgroundColor(Color.GRAY);
                btnDisenioNoUrg.setBackgroundColor(Color.GRAY);
                btnDisenioNadaUrg.setBackgroundColor(Color.GREEN);
                disenio = btnDisenioNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE ESTUDIO MERCADO ************************
        btnEstMerMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstMerMuyUrg.setBackgroundColor(Color.GREEN);
                btnEstMerUrg.setBackgroundColor(Color.GRAY);
                btnEstMerIgual.setBackgroundColor(Color.GRAY);
                btnEstMerNoUrg.setBackgroundColor(Color.GRAY);
                btnEstMerNadaUrg.setBackgroundColor(Color.GRAY);
                estMer = btnEstMerMuyUrg.getText().toString();
            }
        });

        btnEstMerUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstMerMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstMerUrg.setBackgroundColor(Color.GREEN);
                btnEstMerIgual.setBackgroundColor(Color.GRAY);
                btnEstMerNoUrg.setBackgroundColor(Color.GRAY);
                btnEstMerNadaUrg.setBackgroundColor(Color.GRAY);
                estMer = btnEstMerUrg.getText().toString();
            }
        });

        btnEstMerIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstMerMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstMerUrg.setBackgroundColor(Color.GRAY);
                btnEstMerIgual.setBackgroundColor(Color.GREEN);
                btnEstMerNoUrg.setBackgroundColor(Color.GRAY);
                btnEstMerNadaUrg.setBackgroundColor(Color.GRAY);
                estMer = btnEstMerIgual.getText().toString();
            }
        });

        btnEstMerNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstMerMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstMerUrg.setBackgroundColor(Color.GRAY);
                btnEstMerIgual.setBackgroundColor(Color.GRAY);
                btnEstMerNoUrg.setBackgroundColor(Color.GREEN);
                btnEstMerNadaUrg.setBackgroundColor(Color.GRAY);
                estMer = btnEstMerNoUrg.getText().toString();
            }
        });

        btnEstMerNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEstMerMuyUrg.setBackgroundColor(Color.GRAY);
                btnEstMerUrg.setBackgroundColor(Color.GRAY);
                btnEstMerIgual.setBackgroundColor(Color.GRAY);
                btnEstMerNoUrg.setBackgroundColor(Color.GRAY);
                btnEstMerNadaUrg.setBackgroundColor(Color.GREEN);
                estMer = btnEstMerNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE DESARROLLO DE MANUALES ************************
        btnDesManMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesManMuyUrg.setBackgroundColor(Color.GREEN);
                btnDesManUrg.setBackgroundColor(Color.GRAY);
                btnDesManIgual.setBackgroundColor(Color.GRAY);
                btnDesManNoUrg.setBackgroundColor(Color.GRAY);
                btnDesManNadaUrg.setBackgroundColor(Color.GRAY);
                desMan = btnDesManMuyUrg.getText().toString();
            }
        });

        btnDesManUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesManMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesManUrg.setBackgroundColor(Color.GREEN);
                btnDesManIgual.setBackgroundColor(Color.GRAY);
                btnDesManNoUrg.setBackgroundColor(Color.GRAY);
                btnDesManNadaUrg.setBackgroundColor(Color.GRAY);
                desMan = btnDesManUrg.getText().toString();
            }
        });

        btnDesManIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesManMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesManUrg.setBackgroundColor(Color.GRAY);
                btnDesManIgual.setBackgroundColor(Color.GREEN);
                btnDesManNoUrg.setBackgroundColor(Color.GRAY);
                btnDesManNadaUrg.setBackgroundColor(Color.GRAY);
                desMan = btnDesManIgual.getText().toString();
            }
        });

        btnDesManNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesManMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesManUrg.setBackgroundColor(Color.GRAY);
                btnDesManIgual.setBackgroundColor(Color.GRAY);
                btnDesManNoUrg.setBackgroundColor(Color.GREEN);
                btnDesManNadaUrg.setBackgroundColor(Color.GRAY);
                desMan = btnDesManNoUrg.getText().toString();
            }
        });

        btnDesManNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesManMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesManUrg.setBackgroundColor(Color.GRAY);
                btnDesManIgual.setBackgroundColor(Color.GRAY);
                btnDesManNoUrg.setBackgroundColor(Color.GRAY);
                btnDesManNadaUrg.setBackgroundColor(Color.GREEN);
                desMan = btnDesManNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE DESARROLLO DE ESTADOS ************************
        btnDesEstadosMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesEstadosMuyUrg.setBackgroundColor(Color.GREEN);
                btnDesEstadosUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosIgual.setBackgroundColor(Color.GRAY);
                btnDesEstadosNoUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosNadaUrg.setBackgroundColor(Color.GRAY);
                desEstados = btnDesEstadosMuyUrg.getText().toString();
            }
        });

        btnDesEstadosUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesEstadosMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosUrg.setBackgroundColor(Color.GREEN);
                btnDesEstadosIgual.setBackgroundColor(Color.GRAY);
                btnDesEstadosNoUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosNadaUrg.setBackgroundColor(Color.GRAY);
                desEstados = btnDesEstadosUrg.getText().toString();
            }
        });

        btnDesEstadosIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesEstadosMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosIgual.setBackgroundColor(Color.GREEN);
                btnDesEstadosNoUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosNadaUrg.setBackgroundColor(Color.GRAY);
                desEstados = btnDesEstadosIgual.getText().toString();
            }
        });

        btnDesEstadosNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesEstadosMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosIgual.setBackgroundColor(Color.GRAY);
                btnDesEstadosNoUrg.setBackgroundColor(Color.GREEN);
                btnDesEstadosNadaUrg.setBackgroundColor(Color.GRAY);
                desEstados = btnDesEstadosNoUrg.getText().toString();
            }
        });

        btnDesEstadosNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnDesEstadosMuyUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosIgual.setBackgroundColor(Color.GRAY);
                btnDesEstadosNoUrg.setBackgroundColor(Color.GRAY);
                btnDesEstadosNadaUrg.setBackgroundColor(Color.GREEN);
                desEstados = btnDesEstadosNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE TRAMITES DE USO DE SUELO ************************
        btnTramSueloMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTramSueloMuyUrg.setBackgroundColor(Color.GREEN);
                btnTramSueloUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloIgual.setBackgroundColor(Color.GRAY);
                btnTramSueloNoUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloNadaUrg.setBackgroundColor(Color.GRAY);
                tramSuelo = btnTramSueloMuyUrg.getText().toString();
            }
        });

        btnTramSueloUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTramSueloMuyUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloUrg.setBackgroundColor(Color.GREEN);
                btnTramSueloIgual.setBackgroundColor(Color.GRAY);
                btnTramSueloNoUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloNadaUrg.setBackgroundColor(Color.GRAY);
                tramSuelo = btnTramSueloUrg.getText().toString();
            }
        });

        btnTramSueloIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTramSueloMuyUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloIgual.setBackgroundColor(Color.GREEN);
                btnTramSueloNoUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloNadaUrg.setBackgroundColor(Color.GRAY);
                tramSuelo = btnTramSueloIgual.getText().toString();
            }
        });

        btnTramSueloNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTramSueloMuyUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloIgual.setBackgroundColor(Color.GRAY);
                btnTramSueloNoUrg.setBackgroundColor(Color.GREEN);
                btnTramSueloNadaUrg.setBackgroundColor(Color.GRAY);
                tramSuelo = btnTramSueloNoUrg.getText().toString();
            }
        });

        btnTramSueloNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnTramSueloMuyUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloIgual.setBackgroundColor(Color.GRAY);
                btnTramSueloNoUrg.setBackgroundColor(Color.GRAY);
                btnTramSueloNadaUrg.setBackgroundColor(Color.GREEN);
                tramSuelo = btnTramSueloNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE COMERCIALIZACION ************************
        btnComercializacionMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComercializacionMuyUrg.setBackgroundColor(Color.GREEN);
                btnComercializacionUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionIgual.setBackgroundColor(Color.GRAY);
                btnComercializacionNoUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionNadaUrg.setBackgroundColor(Color.GRAY);
                comercializacion = btnComercializacionMuyUrg.getText().toString();
            }
        });

        btnComercializacionUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComercializacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionUrg.setBackgroundColor(Color.GREEN);
                btnComercializacionIgual.setBackgroundColor(Color.GRAY);
                btnComercializacionNoUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionNadaUrg.setBackgroundColor(Color.GRAY);
                comercializacion = btnComercializacionUrg.getText().toString();
            }
        });

        btnComercializacionIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComercializacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionIgual.setBackgroundColor(Color.GREEN);
                btnComercializacionNoUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionNadaUrg.setBackgroundColor(Color.GRAY);
                comercializacion = btnComercializacionIgual.getText().toString();
            }
        });

        btnComercializacionNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComercializacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionIgual.setBackgroundColor(Color.GRAY);
                btnComercializacionNoUrg.setBackgroundColor(Color.GREEN);
                btnComercializacionNadaUrg.setBackgroundColor(Color.GRAY);
                comercializacion = btnComercializacionNoUrg.getText().toString();
            }
        });

        btnComercializacionNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComercializacionMuyUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionIgual.setBackgroundColor(Color.GRAY);
                btnComercializacionNoUrg.setBackgroundColor(Color.GRAY);
                btnComercializacionNadaUrg.setBackgroundColor(Color.GREEN);
                comercializacion = btnComercializacionNadaUrg.getText().toString();
            }
        });

        // ******************** SECCION DE COMMUNITY ************************
        btnComunnityMuyUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComunnityMuyUrg.setBackgroundColor(Color.GREEN);
                btnComunnityUrg.setBackgroundColor(Color.GRAY);
                btnComunnityIgual.setBackgroundColor(Color.GRAY);
                btnComunnityNoUrg.setBackgroundColor(Color.GRAY);
                btnComunnityNadaUrg.setBackgroundColor(Color.GRAY);
                comunnity = btnComunnityMuyUrg.getText().toString();
            }
        });

        btnComunnityUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComunnityMuyUrg.setBackgroundColor(Color.GRAY);
                btnComunnityUrg.setBackgroundColor(Color.GREEN);
                btnComunnityIgual.setBackgroundColor(Color.GRAY);
                btnComunnityNoUrg.setBackgroundColor(Color.GRAY);
                btnComunnityNadaUrg.setBackgroundColor(Color.GRAY);
                comunnity = btnComunnityUrg.getText().toString();
            }
        });

        btnComunnityIgual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComunnityMuyUrg.setBackgroundColor(Color.GRAY);
                btnComunnityUrg.setBackgroundColor(Color.GRAY);
                btnComunnityIgual.setBackgroundColor(Color.GREEN);
                btnComunnityNoUrg.setBackgroundColor(Color.GRAY);
                btnComunnityNadaUrg.setBackgroundColor(Color.GRAY);
                comunnity = btnComunnityIgual.getText().toString();
            }
        });

        btnComunnityNoUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComunnityMuyUrg.setBackgroundColor(Color.GRAY);
                btnComunnityUrg.setBackgroundColor(Color.GRAY);
                btnComunnityIgual.setBackgroundColor(Color.GRAY);
                btnComunnityNoUrg.setBackgroundColor(Color.GREEN);
                btnComunnityNadaUrg.setBackgroundColor(Color.GRAY);
                comunnity = btnComunnityNoUrg.getText().toString();
            }
        });

        btnComunnityNadaUrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnComunnityMuyUrg.setBackgroundColor(Color.GRAY);
                btnComunnityUrg.setBackgroundColor(Color.GRAY);
                btnComunnityIgual.setBackgroundColor(Color.GRAY);
                btnComunnityNoUrg.setBackgroundColor(Color.GRAY);
                btnComunnityNadaUrg.setBackgroundColor(Color.GREEN);
                comunnity = btnComunnityNadaUrg.getText().toString();
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEdits();
            }
        });
    }

    private void checkEdits() {

        boolean isOk = true;

        if (reqFondos.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (formalizacion.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (creacion.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (estrategias.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (analEstatus.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (protectLegal.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (disenio.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (estMer.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (desMan.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (desEstados.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (tramSuelo.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (comercializacion.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }
        else if (comunnity.length() < 2) {
            isOk = false;
            Toast.makeText(getApplicationContext(), "No debes dejar alguna opción sin responder", Toast.LENGTH_SHORT).show();
        }

        if(editOtros.getText().toString().isEmpty()){
            isOk = false;
            editOtros.setError("Debes llenar este campo");
        }

        if(editComentarios.getText().toString().isEmpty()){
            isOk = false;
            editComentarios.setError("Debes llenar este campo");
        }


        if (isOk == true) {

            // *********** Guardamos el dato de la ubicación actual del usuario *************

            misDatos = getSharedPreferences("misDatos", 0);
            SharedPreferences.Editor editor = misDatos.edit();
            editor.putString("DiagnosticoNecesidad34", reqFondos);
            editor.putString("DiagnosticoNecesidad35", formalizacion);
            editor.putString("DiagnosticoNecesidad36", creacion);
            editor.putString("DiagnosticoNecesidad37", estrategias);
            editor.putString("DiagnosticoNecesidad38", analEstatus);
            editor.putString("DiagnosticoNecesidad39", protectLegal);
            editor.putString("DiagnosticoNecesidad40", disenio);
            editor.putString("DiagnosticoNecesidad41", estMer);
            editor.putString("DiagnosticoNecesidad42", desMan);
            editor.putString("DiagnosticoNecesidad43", desEstados);
            editor.putString("DiagnosticoNecesidad44", tramSuelo);
            editor.putString("DiagnosticoNecesidad45", comercializacion);
            editor.putString("DiagnosticoNecesidad46", comunnity);
            editor.putString("DiagnosticoNecesidad47", editOtros.getText().toString());
            editor.putString("DiagnosticoNecesidad48", editComentarios.getText().toString());

            DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
            String date = df.format(Calendar.getInstance().getTime());
            editor.putString(UUIDuser + "diagnosticoDatosGenerales", date);

            editor.commit();
            Toast.makeText(getApplicationContext(), "Diagnóstico completado con éxito", Toast.LENGTH_LONG).show();

            templatePDF = new TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            templatePDF.addMetaData("Diagnostico Necesidad", "Metodo Asesores", "Soluciones Colabora");
            templatePDF.addImage(getApplicationContext());
            templatePDF.addTitles("DIAGNÓSTICO: NECESIDAD", "Debido a la estadística de crecimiento de las empresas, nos preocupamos por conocer las áreas de oportunidades donde podemos aportarles un beneficio para sus negocios garantizando la mejora continua. El siguiente diagnostico está diseñado para conocer un poco más sobre su empresa y necesidades.",date);
            templatePDF.addParagraph("Las siguientes preguntas son para determinar un diagnóstico más personalizado sobre los requerimientos que considera que pueda tener en su empresa. Esto será analizado por consultores expertos. Toda la información proporcionada será únicamente para fines de análisis empresarial y no será publicado o reproducido por algún otro medio. A partir de este momento nos comprometemos a su confidencialidad.");

            ArrayList<String[]> respuestas = new ArrayList<>();
            ArrayList<String[]> respuestasDatosEmpresa = new ArrayList<>();
            ArrayList<String[]> respuestasCapitalHumano = new ArrayList<>();
            ArrayList<String[]> respuestasCapitalTestNecesidad = new ArrayList<>();

            respuestas.add(new String[]{"Nombre completo", misDatos.getString("DiagnosticoNecesidad1", "")});
            respuestas.add(new String[]{"Edad", misDatos.getString("DiagnosticoNecesidad2", "")});
            respuestas.add(new String[]{"Género", misDatos.getString("DiagnosticoNecesidad3", "")});
            respuestas.add(new String[]{"Ocupación", misDatos.getString("DiagnosticoNecesidad4", "")});
            respuestas.add(new String[]{"Teléfono", misDatos.getString("DiagnosticoNecesidad5", "")});
            respuestas.add(new String[]{"Correo", misDatos.getString("DiagnosticoNecesidad6", "")});

            respuestasDatosEmpresa.add(new String[]{"Nombre comercial", misDatos.getString("DiagnosticoNecesidad7", "")});
            respuestasDatosEmpresa.add(new String[]{"Razón social", misDatos.getString("DiagnosticoNecesidad8", "")});
            respuestasDatosEmpresa.add(new String[]{"RFC", misDatos.getString("DiagnosticoNecesidad9", "")});
            respuestasDatosEmpresa.add(new String[]{"Dirección", misDatos.getString("DiagnosticoNecesidad10", "")});
            respuestasDatosEmpresa.add(new String[]{"Teléfono", misDatos.getString("DiagnosticoNecesidad11", "")});
            respuestasDatosEmpresa.add(new String[]{"Giro del negocio", misDatos.getString("DiagnosticoNecesidad12", "")});
            respuestasDatosEmpresa.add(new String[]{"Estatus del proyecto", misDatos.getString("DiagnosticoNecesidad13", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Bajo que régimen estás dado de alta en hacienda?", misDatos.getString("DiagnosticoNecesidad14", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Qué formas de cobranza usa?", misDatos.getString("DiagnosticoNecesidad15", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuenta con local o establecimiento?", misDatos.getString("DiagnosticoNecesidad16", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuenta con página web?", misDatos.getString("DiagnosticoNecesidad17", "")});
            respuestasDatosEmpresa.add(new String[]{"Dominio", misDatos.getString("DiagnosticoNecesidad18", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuenta con redes sociales?", misDatos.getString("DiagnosticoNecesidad19", "")});
            respuestasDatosEmpresa.add(new String[]{"Redes sociales", misDatos.getString("DiagnosticoNecesidad20", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Ha realizado ventas en línea?", misDatos.getString("DiagnosticoNecesidad21", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuenta con software de facturación?", misDatos.getString("DiagnosticoNecesidad22", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuénta con correo institucional?", misDatos.getString("DiagnosticoNecesidad23", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuénta con imagen corportativa?", misDatos.getString("DiagnosticoNecesidad24", "")});
            respuestasDatosEmpresa.add(new String[]{"¿Cuenta con algún otro registro de protección intelectual e industrial?", misDatos.getString("DiagnosticoNecesidad25", "")});
            respuestasDatosEmpresa.add(new String[]{"Si la respuesta anterior fue si, mencional cuál o cuáles", misDatos.getString("DiagnosticoNecesidad26", "")});

            respuestasCapitalHumano.add(new String[]{"¿Usted o algún elemento de su proyecto tiene experiencia sobre el producto o servicio que ofrece?", misDatos.getString("DiagnosticoNecesidad27", "")});
            respuestasCapitalHumano.add(new String[]{"Número de integrantes del proyecto", misDatos.getString("DiagnosticoNecesidad28", "")});
            respuestasCapitalHumano.add(new String[]{"¿Estás dado de alta ante el IMSS como patrón?", misDatos.getString("DiagnosticoNecesidad29", "")});
            respuestasCapitalHumano.add(new String[]{"¿Tienes a tu personal dado de alta ante el IMSS?", misDatos.getString("DiagnosticoNecesidad30", "")});
            respuestasCapitalHumano.add(new String[]{"¿Cuenta con prestaciones de ley?", misDatos.getString("DiagnosticoNecesidad31", "")});
            respuestasCapitalHumano.add(new String[]{"¿Tiene manuales de puestos?", misDatos.getString("DiagnosticoNecesidad32", "")});
            respuestasCapitalHumano.add(new String[]{"¿Cuénta con reglamentos internos?", misDatos.getString("DiagnosticoNecesidad33", "")});

            respuestasCapitalTestNecesidad.add(new String[]{"Detencción de fondos y financiamiento", misDatos.getString("DiagnosticoNecesidad34", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Formalización de mi empresa", misDatos.getString("DiagnosticoNecesidad35", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Creación de mi empresa", misDatos.getString("DiagnosticoNecesidad36", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Desarrollo de estrategias comerciales", misDatos.getString("DiagnosticoNecesidad37", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Análisis del estatus de mi empresa", misDatos.getString("DiagnosticoNecesidad38", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Protección legal y fiscal de mi empresa", misDatos.getString("DiagnosticoNecesidad39", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Diseño digital o web", misDatos.getString("DiagnosticoNecesidad40", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Estudio de mercado", misDatos.getString("DiagnosticoNecesidad41", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Desarrollo de manuales del servicio o procesos de producción", misDatos.getString("DiagnosticoNecesidad42", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Desarrollo de estados y proyecciones financieras", misDatos.getString("DiagnosticoNecesidad43", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Trámites de uso de suelo y licencia de funcionamiento", misDatos.getString("DiagnosticoNecesidad44", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Comercialización de producto o servicio tradicional o digital", misDatos.getString("DiagnosticoNecesidad45", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Servicio de community manager", misDatos.getString("DiagnosticoNecesidad46", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Otro. Mencione cuál o cuáles", misDatos.getString("DiagnosticoNecesidad47", "")});
            respuestasCapitalTestNecesidad.add(new String[]{"Comentarios: Explique brevemente cual sea el problema con el que batalla sobre su proyecto", misDatos.getString("DiagnosticoNecesidad48", "")});

            templatePDF.addSections("Datos del representante legal");
            templatePDF.createTableWith2cell(headerPreguntas, respuestas);

            templatePDF.addSections("Datos de la empresa");
            templatePDF.createTableWith2cell(headerPreguntas, respuestasDatosEmpresa);

            templatePDF.addSections("Datos del capital humano");
            templatePDF.createTableWith2cell(headerPreguntas, respuestasCapitalHumano);

            templatePDF.addSections("Test de necesidad");
            templatePDF.createTableWith2cell(headerPreguntas, respuestasCapitalTestNecesidad);
            /*templatePDF.createTableWithFoto(getApplicationContext(), bitmap, nombreConsultor, especialidad, datos);
            // templatePDF.addParagraph(nombreConsultor);
            templatePDF.addSections("Experiencia Académica");
            templatePDF.createTableWithTheSameLength(headerExpAcaemica, rowsAcedemica);
            templatePDF.addSections("Experiencia Profesional");
            templatePDF.createTableWithTheSameLength(headerExpProfesional, rowsProfesional);

            if(rowsReconocimientos.size() > 0){
                templatePDF.addSections("Reconocimientos");
                templatePDF.createTableReconocimientos(headerReconocimientos, rowsReconocimientos);
            }
                            /*templatePDF.addParagraph("Contribuyente");
                            templatePDF.createTableWithTheSameLength(headerContribuyente, rows);
                            templatePDF.addParagraph("Fecha y hora del reporte");
                            templatePDF.addSections(formattedDate);
                            templatePDF.addParagraph("Estimado Cliente:");
                            templatePDF.addParagraph("Hemos realizado la búsqueda del contribuyente arriba mencionado en todas las listas que a la fecha han sido publicadas en la página del SAT y/o en el Diario Oficial de la Federación relacionadas con Empresas que Facturan Operaciones Simuladas (efos) en términos del artículo 69-B del Código Fiscal de la Federación (Listas presuntas, definitivas y desvirtuados); en la lista vigente de los contribuyentes “no localizados” a que se refiere el artículo 69 del mismo código; así como en la relación de contribuyentes notificados vías estrados vinculadas con el tema del mismo artículo 69-B antes mencionado, arrojándo el siguiente resultado:");

                            if(listaCondonados.getSUPUESTO().equals("CONDONADOS")){
                                templatePDF.addSections("Contribuyente en Lista de Condonados");
                            }

                            templatePDF.addParagraph("Detalle de la lista en la que aparece:");
                            templatePDF.createTableWithTheSameLength(headerContribuyente, rows2);
                            templatePDF.addParagraph("Advertencia. El presente reporte se elaboró a partir de los caracteres capturados en el recuadro de búsqueda del sistema, ecomendamos siempre utilizar el RFC como criterio de búsqueda preferencial, verificándo siempre que éste sea correcto. Despacho CGF® actualiza constantemente su información por lo que el resultado aquí mostrado es suceptible de ser modificado en cualquier momento posterior. Nuestra base de datos se alimenta estrictamente de la información pública que periódicamente va dando a conocer tanto el SAT como el Diario Oficial de la Federación, por lo que efosmx® no tiene control alguno sobre la seguridad, legalidad, veracidad o exactitud del contenido. La información sobre notificaciones vía estrados es a partir del día 27 de abril de 2016 a la fecha. El hecho de que el contribuyente buscado no haya sido publicado a esta fecha y hora en cualquiera de las listas mencionadas, no significa una garantía de que el mismo no será publicado en el futuro, por lo que este reporte no constituye de ninguna manera una autorización para realizar operaciones con el contribuyente antes mencionado. efos.mx® no sustituye de ninguna manera la obligación legal de revisar por cuenta propia las publicaciones que se realizan en la página del SAT así como las del Diario Oficial de la Federación a que se refiere el artículo 69-B del Código Fiscal de la Federación, por lo que Soluciones Colabora, SAPI de C.V. no serán responsable de daños, perjuicios, ni consecuencias legales derivadas de las decisiones, prácticas, acciones u omisiones en la actuación de cualquier persona como resultado de este reporte, quedando exonerada de cualquier tipo de responsabilidad por posibles daños y perjuicios, aun y cuando se demuestre que la persona actuó atendiendo a la información contenida en este reporte o en el sistema.");
        */
            templatePDF.closeDocument();

            templatePDF.viewPDF("Diagnostico1");
            finish();
            // ******************************************************************************

           // Intent i = new Intent(RequerimientosActivity.this, CuestionarioTerminadoActivity.class);
           // startActivity(i);
           // finish();

            /*templatePDF = new TemplatePDF(getApplicationContext());
            templatePDF.openDocument();
            templatePDF.addMetaData("Clientes","Ventas","Soft Listig");
            templatePDF.addTitles("CONSULTORIA GABY ANZINA", "Diagnostico", "28/03/20117");
            templatePDF.addParagraph(shortText);
            templatePDF.addParagraph(longText);
            templatePDF.addParagraph("Datos Generales");
            templatePDF.createTable(header, getDatosGenerales());
            templatePDF.addParagraph("Empresa");
            templatePDF.createTable(header, getDatosEmpresa());
            templatePDF.closeDocument();

            templatePDF.viewPDF();*/


        }
    }

    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(NecesidadTestActivity.this);
        builder.setMessage("¿Estás seguro de querer salir del test diagnóstico? Si lo haces tendrás que iniciar de nuevo")
                .setPositiveButton("Salir del diagnóstico", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(NecesidadTestActivity.this, MainActivity.class);
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
