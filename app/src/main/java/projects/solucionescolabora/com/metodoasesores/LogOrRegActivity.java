package projects.solucionescolabora.com.metodoasesores;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresMainActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesMain2Activity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.RegistroInstitucionActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

public class LogOrRegActivity extends Activity {

    private Button btnGoToLogin;
    private Button btnGoToRegister;
    private TextView txtBievenido;
    private TextView txtIntro;
    private TextView txtSolCol;
    private ImageView imgGoToLogo;

    private NotificationHelper notificationHelper;

    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_or_reg);

        btnGoToLogin = (Button)findViewById(R.id.btnGoToLogin);
        btnGoToRegister = (Button)findViewById(R.id.btnGoToRegister);
        txtBievenido = (TextView)findViewById(R.id.txtGoToBienvenido);
        txtIntro = (TextView)findViewById(R.id.txtGoToIntro);
        txtSolCol = (TextView)findViewById(R.id.txtGoToSolCol);
        imgGoToLogo = (ImageView)findViewById(R.id.imgGoToLogo);

        btnGoToRegister.setVisibility(View.INVISIBLE);
        btnGoToLogin.setVisibility(View.INVISIBLE);
        txtSolCol.setVisibility(View.INVISIBLE);
        txtIntro.setVisibility(View.INVISIBLE);
        txtBievenido.setVisibility(View.INVISIBLE);

        txtSolCol.setMovementMethod(LinkMovementMethod.getInstance());

        //notificationHelper = new NotificationHelper(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;

                if(user != null){
                    Log.i("SESION", "Sesión iniciada con email: " + user.getEmail());
                    // LANZAMOS SEGUNDA ACTIVITY, ESTO SE QUEDA GUARDADO
                    final String UUIDUser = user.getUid();

                    sharedPreferences = getSharedPreferences("misDatos", 0);
                    String tipoUsuario = sharedPreferences.getString("typeCurrentUser","");
                    String registro = sharedPreferences.getString("registroCompleto","");

                    if(tipoUsuario.equals("Institucion")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LogOrRegActivity.this, InstitucionesMain2Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LogOrRegActivity.this, RegistroInstitucionActivity.class);
                            startActivity(i);
                            return;
                        }
                    }
                    else if(tipoUsuario.equals("Consultor")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LogOrRegActivity.this, ConsultoresMainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LogOrRegActivity.this, RegistroConsultorActivity.class);
                            startActivity(i);
                            return;
                        }
                    }
                    else if(tipoUsuario.equals("Startup")){
                        if(registro.equals("Completo")){
                            Intent i = new Intent(LogOrRegActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                        else if (registro.equals("Incompleto")){
                            Intent i = new Intent(LogOrRegActivity.this, RegistroStartupActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            return;
                        }
                    }

                }else{
                    Log.i("SESION", "Sesión cerrada");
                }
            }
        };


        Animation animation = AnimationUtils.loadAnimation(LogOrRegActivity.this, R.anim.slide_up);
        final Animation animation1 = AnimationUtils.loadAnimation(LogOrRegActivity.this, R.anim.transparentar);
        imgGoToLogo.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btnGoToRegister.setVisibility(View.VISIBLE);
                btnGoToLogin.setVisibility(View.VISIBLE);
                txtSolCol.setVisibility(View.VISIBLE);
                txtIntro.setVisibility(View.VISIBLE);
                txtBievenido.setVisibility(View.VISIBLE);

                btnGoToRegister.setAnimation(animation1);
                btnGoToLogin.setAnimation(animation1);
                txtSolCol.setAnimation(animation1);
                txtIntro.setAnimation(animation1);
                txtBievenido.setAnimation(animation1);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        btnGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LogOrRegActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FirebaseMessaging.getInstance().subscribeToTopic("news");
                Intent i = new Intent(LogOrRegActivity.this, RegistroActivity.class);
                startActivity(i);
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
}
