package projects.solucionescolabora.com.metodoasesores;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.RegistroInstitucionActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

public class RegistroActivity extends AppCompatActivity {

    private EditText editUsername;
    private EditText editContrasena;
    private EditText editContrasenaAgain;
    private RadioButton radioStartup;
    private RadioButton radioConsultor;
    private RadioButton radioInstitucion;
    private Button btnRegistrar;
    private TextView txtTerminos;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private SharedPreferences sharedPreferences;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        setTitle("Registro");

        editUsername = (EditText)findViewById(R.id.editEmailRegistro);
        editContrasena = (EditText)findViewById(R.id.editPasswordRegistro);
        editContrasenaAgain = (EditText)findViewById(R.id.editPasswordAgainRegistro);
        btnRegistrar = (Button)findViewById(R.id.btnRegistro);
        radioConsultor = (RadioButton)findViewById(R.id.radioConsultor);
        radioInstitucion = (RadioButton)findViewById(R.id.radioInstuticion);
        radioStartup = (RadioButton)findViewById(R.id.radioStartup);
        txtTerminos = (TextView)findViewById(R.id.txtTerminos);

        progressDialog = new ProgressDialog(RegistroActivity.this);

        progressDialog.setTitle("Validando");
        progressDialog.setMessage("Espere un momento mientras el sistema registra su usuario");


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(RegistroActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        txtTerminos.setMovementMethod(LinkMovementMethod.getInstance());

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                if(user != null){
                    // LANZAMOS SEGUNDA ACTIVITY, ESTO SE QUEDA GUARDADO
                    if(radioConsultor.isChecked() == true){
                        Intent i = new Intent(RegistroActivity.this, RegistroConsultorActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else if (radioInstitucion.isChecked() == true){
                        Intent i = new Intent(RegistroActivity.this, RegistroInstitucionActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                    else if (radioStartup.isChecked() == true){
                        Intent i = new Intent(RegistroActivity.this, RegistroStartupActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }

                }else{
                    Log.i("SESION", "Sesión cerrada");
                }
            }
        };

        radioStartup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true){
                    radioConsultor.setChecked(false);
                    radioInstitucion.setChecked(false);
                }
            }
        });

        radioInstitucion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b == true){
                    radioConsultor.setChecked(false);
                    radioStartup.setChecked(false);
                }
            }
        });

        radioConsultor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b==true){
                    radioStartup.setChecked(false);
                    radioInstitucion.setChecked(false);
                }
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(radioConsultor.isChecked() == false && radioStartup.isChecked() == false && radioInstitucion.isChecked() == false){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar si eres Consultor, Startup o Institución", Toast.LENGTH_LONG).show();
                    return;
                }

                hideKeyboard();
                checkBlanckSpaces();
            }
        });
    }

    private void Registrar(final String email, String password){
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegistroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.i("SESION", "Usuario creado correctamente");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                    final String UUIDUser = user.getUid();

                    if(radioConsultor.isChecked() == true){
                        List<ExperienciaProfesional> experienciaProfesional = new ArrayList<>();
                        List<ExperienciaAcademica> experienciaAcademica = new ArrayList<>();
                        List<Reconocimientos> reconocimientos = new ArrayList<>();
                        List<String> especialidades = new ArrayList<>();
                        Consultor newConsultor = new Consultor(UUIDUser,"","","","","","",especialidades,"","","",experienciaAcademica,experienciaProfesional,reconocimientos,0.0,"", "");
                        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).setValue(newConsultor).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("typeCurrentUser", "Consultor");
                                editor.putString("registroCompleto", "Incompleto");
                                editor.commit();
                                // ******************************************************************************
                            }
                        });
                    }
                    else if(radioInstitucion.isChecked() == true){
                        Institucion institucion = new Institucion(UUIDUser, "","","","","","","","","");
                        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).child(UUIDUser).setValue(institucion).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("typeCurrentUser", "Institucion");
                                editor.putString("registroCompleto", "Incompleto");
                                editor.commit();
                                // ******************************************************************************
                            }
                        });

                    }
                    else if(radioStartup.isChecked() == true){
                        List<String> intereses = new ArrayList<>();
                        Startup startup = new Startup(UUIDUser, "","","","","","",intereses,"","");
                        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.STARTUP_REFERENCE).child(UUIDUser).setValue(startup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("typeCurrentUser", "Startup");
                                editor.putString("registroCompleto", "Incompleto");
                                editor.commit();
                                // ******************************************************************************
                            }
                        });
                    }



                } else{
                    Log.e("SESION", task.getException().getMessage() + "");
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

    private void checkBlanckSpaces(){
        boolean error = false;

        if(editContrasena.getText().toString().equals(editContrasenaAgain.getText().toString())){
            error = false;
        }
        else{
            error = true;
            editContrasena.setError("Las contraseñas no coinciden");
            editContrasenaAgain.setError("Las contreseñas no coinciden");
        }

        if(editUsername.getText().toString().isEmpty()){
            error = true;
            editUsername.setError("No se permiten campos vacíos");
        }

        else if(editContrasena.getText().toString().isEmpty()){
            error = true;
            editContrasena.setError("No se permiten campos vacíos");
        }
        else if(editContrasenaAgain.getText().toString().isEmpty()){
            error = true;
            editContrasenaAgain.setError("No se permiten campos vacíos");
        }

        if(error == false){
            Registrar(editUsername.getText().toString(), editContrasena.getText().toString());
        }
        else{
            Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos correctamente", Toast.LENGTH_LONG).show();
        }

    }

    public void hideKeyboard() {
        // Check if no view has focus:
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    new AlertDialog.Builder(this)
                            .setTitle("Aviso")
                            .setMessage("Si no concede los permisos la app se cerrará")
                            .setPositiveButton("Cerrar aplicación", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            })
                            .create()
                            .show();

                }
                return;
            }

        }
    }
}
