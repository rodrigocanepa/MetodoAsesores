package projects.solucionescolabora.com.metodoasesores;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresMainActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesMain2Activity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesMainActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.RegistroInstitucionActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText editCorreo;
    private EditText editContrasena;
    private TextView txtForgotPassword;
    private Button btnBackFace;
    private RadioButton radioButtonConsultor;
    private RadioButton radioButtonStartup;
    private RadioButton radioButtonInstitucion;

    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    private LoginButton loginButton;
    private CallbackManager mCallbackManager;

    private ProgressDialog progressDialog;
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String profileFoto = "";
    private boolean thisUserExists = false;
    private boolean error = false;

    private SharedPreferences sharedPreferences;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Iniciar Sesión");

        btnLogin = (Button)findViewById(R.id.btnLogin);
        editCorreo = (EditText)findViewById(R.id.editEmailLogin);
        editContrasena = (EditText)findViewById(R.id.editPasswordLogin);
        loginButton = (LoginButton)findViewById(R.id.login_buttonFace);
        txtForgotPassword = (TextView)findViewById(R.id.txtForgotPassword);
        btnBackFace = (Button)findViewById(R.id.btnFaceBack);
        radioButtonConsultor = (RadioButton)findViewById(R.id.radioConsultorLogin);
        radioButtonInstitucion = (RadioButton)findViewById(R.id.radioInstuticionLogin);
        radioButtonStartup = (RadioButton)findViewById(R.id.radioStartupLogin);

        progressDialog = new ProgressDialog(LoginActivity.this);

        progressDialog.setTitle("Validando");
        progressDialog.setMessage("Espere un momento mientras el sistema inicia sesión");


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(LoginActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_LOCATION);
        }

        radioButtonStartup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b == true){
                    radioButtonConsultor.setChecked(false);
                    radioButtonInstitucion.setChecked(false);
                }
            }
        });

        radioButtonInstitucion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b == true){
                    radioButtonConsultor.setChecked(false);
                    radioButtonStartup.setChecked(false);
                }
            }
        });

        radioButtonConsultor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(b==true){
                    radioButtonStartup.setChecked(false);
                    radioButtonInstitucion.setChecked(false);
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;

                if(user != null){
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                    }
                    Log.i("SESION", "Sesión iniciada con email: " + user.getEmail());
                    // LANZAMOS SEGUNDA ACTIVITY, ESTO SE QUEDA GUARDADO
                    final String UUIDUser = user.getUid();

                    sharedPreferences = getSharedPreferences("misDatos", 0);
                    String tipoUsuario = sharedPreferences.getString("typeCurrentUser","");
                    String registro = sharedPreferences.getString("registroCompleto","");

                    if(tipoUsuario.equals("Institucion")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LoginActivity.this, InstitucionesMain2Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LoginActivity.this, RegistroInstitucionActivity.class);
                            startActivity(i);
                            return;
                        }
                    }
                    else if(tipoUsuario.equals("Consultor")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LoginActivity.this, ConsultoresMainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LoginActivity.this, RegistroConsultorActivity.class);
                            startActivity(i);
                            return;
                        }
                    }
                    else if(tipoUsuario.equals("Startup")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LoginActivity.this, RegistroStartupActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                    }

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);


                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Consultor consultor = snapshot.getValue(Consultor.class);
                                if(consultor.getId().equals(UUIDUser)){
                                    if (consultor.getNombres().length() > 2){
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Consultor");
                                        editor.putString("registroCompleto", "Completo");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, ConsultoresMainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Consultor");
                                        editor.putString("registroCompleto", "Incompleto");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, RegistroConsultorActivity.class);
                                        startActivity(i);
                                    }
                                }

                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Institucion institucion = snapshot.getValue(Institucion.class);
                                if(institucion.getId().equals(UUIDUser)){
                                    if(institucion.getNombre().length() > 2){
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Institucion");
                                        editor.putString("registroCompleto", "Completo");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, InstitucionesMain2Activity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Institucion");
                                        editor.putString("registroCompleto", "Incompleto");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, RegistroInstitucionActivity.class);
                                        startActivity(i);
                                    }
                                }

                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.STARTUP_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final Startup startup = snapshot.getValue(Startup.class);
                                if(startup.getId().equals(UUIDUser)){
                                    if(startup.getNombreStartup().length() > 2){
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Startup");
                                        editor.putString("registroCompleto", "Completo");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }
                                    else{
                                        // *********** Guardamos los principales datos de los nuevos usuarios *************
                                        sharedPreferences = getSharedPreferences("misDatos", 0);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("typeCurrentUser", "Institucion");
                                        editor.putString("registroCompleto", "Incompleto");
                                        editor.commit();
                                        // ******************************************************************************
                                        Intent i = new Intent(LoginActivity.this, RegistroStartupActivity.class);
                                        startActivity(i);
                                    }
                                }

                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



                }else{
                    Log.i("SESION", "Sesión cerrada");
                }
            }
        };

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("¿Desea reestablecer su contraseña?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(editCorreo.getText().toString().isEmpty()){
                                    editCorreo.setError("Escribe tu correo");
                                }
                                else{
                                    progressDialog = ProgressDialog.show(LoginActivity.this, "Enviando correo", "Espere un momento mientras el equipo envía un correo para restablecer la contraseña de su cuenta");

                                    FirebaseAuth.getInstance().sendPasswordResetEmail(editCorreo.getText().toString())
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Email enviado", Toast.LENGTH_LONG).show();
                                                        if (progressDialog.isShowing()){
                                                            progressDialog.dismiss();
                                                        }
                                                    } else{
                                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        if (progressDialog.isShowing()){
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                }
                                            });
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                checkBlanckSpaces();

                /*if(radioButtonConsultor.isChecked() == true || radioButtonStartup.isChecked() == true || radioButtonInstitucion.isChecked() == true){
                    hideKeyboard();
                    checkBlanckSpaces();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes seleccionar si eres STARTUP, CONSULTOR o INSTITUCIÓN", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        btnBackFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(radioButtonInstitucion.isChecked() == true){
                    Toast.makeText(getApplicationContext(), "Las instituciones sólo pueden registrarse vía correo electrónico", Toast.LENGTH_LONG).show();
                    return;
                }

                if(radioButtonConsultor.isChecked() == true || radioButtonStartup.isChecked() == true ){
                    loginButton.performClick();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes seleccionar si eres STARTUP o CONSULTOR", Toast.LENGTH_LONG).show();
                }

            }
        });

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                System.out.println("onSuccess");

                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

                Log.d("FACEBOOK", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("FACEBOOK", "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }


    private void IniciarSesion(String email, String password){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.i("SESION", "Sesión iniciada correctamente");
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

    private void checkBlanckSpaces(){
        boolean error = false;

        if(editCorreo.getText().toString().isEmpty()){
            error = true;
            editCorreo.setError("No se permiten campos vacíos");
        }

        else if(editContrasena.getText().toString().isEmpty()){
            error = true;
            editContrasena.setError("No se permiten campos vacíos");
        }


        if(error == false){
            progressDialog.show();
            IniciarSesion(editCorreo.getText().toString(), editContrasena.getText().toString());
        }

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("FACEBOOK", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FACEBOOK", "signInWithCredential:success");
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                            final String UUIDUser = user.getUid();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                            if(radioButtonConsultor.isChecked() == true){
                                // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
                                tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                            Consultor consultor = snapshot.getValue(Consultor.class);
                                            if(consultor.getId().equals(UUIDUser)){
                                                thisUserExists = true;

                                                if(consultor.getNombres().length() < 2){
                                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("typeCurrentUser", "Consultor");
                                                    editor.putString("registroCompleto", "Incompleto");
                                                    editor.commit();
                                                    // ******************************************************************************
                                                }
                                                else{
                                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("typeCurrentUser", "Consultor");
                                                    editor.putString("registroCompleto", "Completo");
                                                    editor.commit();
                                                    // ******************************************************************************
                                                }

                                            }
                                        }
                                        if(thisUserExists == false){
                                            List<ExperienciaProfesional> experienciaProfesional = new ArrayList<>();
                                            List<ExperienciaAcademica> experienciaAcademica = new ArrayList<>();
                                            List<Reconocimientos> reconocimientos = new ArrayList<>();
                                            List<String> especialidades = new ArrayList<>();
                                            Consultor newConsultor = new Consultor(UUIDUser,firstName,lastName,"","","","",especialidades,profileFoto,"","",experienciaAcademica,experienciaProfesional,reconocimientos,0.0,"", "");
                                            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).setValue(newConsultor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("fotoCurrentUser", profileFoto);
                                                    editor.putString("idCurrentUser", UUIDUser);
                                                    editor.putString("firstNameCurrentUser", firstName);
                                                    editor.putString("lastNameCurrentUser", lastName);
                                                    editor.putString("typeCurrentUser", "Consultor");
                                                    editor.putString("registroCompleto", "Incompleto");
                                                    editor.commit();
                                                    // ******************************************************************************
                                                    Intent i = new Intent(LoginActivity.this, RegistroConsultorActivity.class);
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                            else if(radioButtonStartup.isChecked() == true){

                            }

                            // ******************************************************************************************


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FACEBOOK", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=150&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());
                profileFoto = profile_pic.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name")){
                bundle.putString("first_name", object.getString("first_name"));
                firstName = object.getString("first_name");
            }
            if (object.has("last_name")){
                bundle.putString("last_name", object.getString("last_name"));
                lastName = object.getString("last_name");
            }
            if (object.has("email")){
                bundle.putString("email", object.getString("email"));
                email = object.getString("email");
            }
            if (object.has("gender")){
                bundle.putString("gender", object.getString("gender"));
            }
            if (object.has("birthday")){
                bundle.putString("birthday", object.getString("birthday"));
            }
            if (object.has("location")){
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            }

            return bundle;
        }
        catch(JSONException e) {
            Log.d("JSON","Error parsing JSON");
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
