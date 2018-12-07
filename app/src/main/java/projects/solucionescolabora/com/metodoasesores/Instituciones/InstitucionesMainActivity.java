package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.LogOrRegActivity;
import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesMainActivity extends AppCompatActivity {

    private CircleImageView imgAvatar;
    private ImageView imgFondo;
    private TextView txtNombre;
    private TextView txtRFC;
    private ImageView imgCrear;
    private ImageView imgConsultores;
    private ImageView imgHistorial;
    private ImageView imgCerrar;

    private String url = "";
    private String nombre = "";
    private String rfc = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_instituciones);

        imgAvatar = (CircleImageView)findViewById(R.id.imgInstitucionAvatarHome);
        imgFondo = (ImageView)findViewById(R.id.imgInstitucionHomeFondo);
        txtNombre = (TextView)findViewById(R.id.txtInstitucionNameHome);
        txtRFC = (TextView)findViewById(R.id.txtInstitucionRFCHome);
        imgCrear = (ImageView)findViewById(R.id.imgInstitucionHomeCrearConv);
        imgConsultores = (ImageView)findViewById(R.id.imgInstitucionHomeVerConsul);
        imgHistorial = (ImageView)findViewById(R.id.imgInstitucionVerHist);
        imgCerrar = (ImageView)findViewById(R.id.imgInstitucionHomeCerrarSes);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        final String UUIDUser = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Institucion institucion = snapshot.getValue(Institucion.class);

                    if(institucion.getId().equals(UUIDUser)){
                        url = institucion.getUrlLogo();
                        nombre = institucion.getNombre();
                        rfc = institucion.getRfc();
                    }

                }

                if(url.length() > 2){
                    Picasso.with(getApplicationContext())
                            .load(url).into(imgAvatar, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getApplicationContext(), "No se pudo cargar la foto", Toast.LENGTH_LONG).show();
                        }
                    });

                    Picasso.with(getApplicationContext())
                            .load(url).into(imgFondo, new Callback() {
                        @Override
                        public void onSuccess() {
                            imgFondo.setColorFilter(Color.argb(180,10,10,10), PorterDuff.Mode.DARKEN);
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getApplicationContext(), "No se pudo cargar la foto", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                if(nombre.length() > 1){
                    txtNombre.setText(nombre);
                }
                if(rfc.length() > 1){
                    txtRFC.setText(rfc);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(InstitucionesMainActivity.this);
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Estas seguro de querer cerrar sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                Intent i = new Intent(InstitucionesMainActivity.this, LogOrRegActivity.class);
                                startActivity(i);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }

}
