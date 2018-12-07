package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesMain2Activity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.RegistroInstitucionActivity;
import projects.solucionescolabora.com.metodoasesores.LogOrRegActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.R;

public class ConsultoresMainActivity extends AppCompatActivity {

    private CircleImageView imgAvatar;
    private ImageView imgFondo;
    private TextView txtNombre;
    private TextView txtRFC;
    private ImageView imgCrear;
    private ImageView imgVerConv;
    private ImageView imgHistorial;
    private ImageView imgCerrar;

    private String url = "";
    private String nombre = "";
    private String especialidad = "";
    private String UUIDUser = "";

    private String idActual = "";
    private String nombreActual = "";
    private String especialidadActual = "";
    private String urlFotoActual = "";
    private boolean saveFoto = false;
    private String correoActual = "";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences sharedPreferences;

    private boolean notificacion;
    private DatabaseReference caucelAppRef;
    private List<Convocatoria> convocatorias = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private List<String> misEspecialidades = new ArrayList<>();
    private List<Convocatoria> convocatoriasFiltradas = new ArrayList<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultores_main);

        imgAvatar = (CircleImageView)findViewById(R.id.imgConsultorAvatarHome);
        imgFondo = (ImageView)findViewById(R.id.imgConsultorHomeFondo);
        txtNombre = (TextView)findViewById(R.id.txtConsultorNameHome);
        txtRFC = (TextView)findViewById(R.id.txtConsultorEspecilidadHome);
        imgCrear = (ImageView)findViewById(R.id.imgConsultorEditarCrearCursos);
        imgVerConv = (ImageView)findViewById(R.id.imgConsultorHomeVerConv);
        imgHistorial = (ImageView)findViewById(R.id.imgConsultorVerHist);
        imgCerrar = (ImageView)findViewById(R.id.imgConsultorCerrarSesion);

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");
        nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");
        urlFotoActual = sharedPreferences.getString("urlFotoActual","");
        saveFoto = sharedPreferences.getBoolean("saveFoto",false);
        notificacion = sharedPreferences.getBoolean("notificacionConsultor",false);

        progressDialog = new ProgressDialog(ConsultoresMainActivity.this);

        progressDialog.setTitle("Buscando convocatorias");
        progressDialog.setMessage("Espere un momento mientras el sistema busca convocatorias con su perfil");


        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConsultoresMainActivity.this);
                builder.setTitle("Información");
                builder.setMessage("¿Desea actualizar su currículum? Debido a que sus datos se comparten con las instituciones si desea actualizar su foto o algún dato debe generar su CV de nuevo")
                        .setPositiveButton("Actualizar información y generar CV", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!
                                Intent i = new Intent(ConsultoresMainActivity.this, RegistroConsultorActivity.class);
                                startActivity(i);
                            }
                        });                    // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
        });

        if (idActual.length() > 1){
            txtNombre.setText(nombreActual);
            txtRFC.setText(especialidadActual);

            if (saveFoto){
                File folder = new  File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");
                if(!folder.exists())
                    folder.mkdirs();
                File file = new File(folder, "fotoMetodoApp.png");

                if(file.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    imgAvatar.setImageBitmap(myBitmap);
                    imgFondo.setImageBitmap(myBitmap);
                    imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);
                    saveFoto = false;
                }
            }
            else{
                // ********* IMPORTANTE CAMBIAR ************
                StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(idActual + "-profile");
                final long ONE_MEGABYTE = 1024 * 1024;
                storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgAvatar.setImageBitmap(bitmap);
                        imgFondo.setImageBitmap(bitmap);
                        imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                        // ***************************** GUARDAMOS LA IMAGEN ***********************
                        File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                        if(!folder.exists())
                            folder.mkdirs();
                        File file = new File(folder, "fotoMetodoApp.png");

                        if (file.exists ()) file.delete ();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("saveFoto", true);
                            editor.commit();
                            // ******************************************************************************

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }

        }
        else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
            UUIDUser = user.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Consultor consultor = snapshot.getValue(Consultor.class);

                        if(consultor.getId().equals(UUIDUser)){
                            url = consultor.getUrlFoto();
                            nombre = consultor.getNombres() + " " + consultor.getApellidos();
                            List<String> especialidades = consultor.getEspecialidad();
                            String carreras = "";
                            for (int i = 0; i < especialidades.size(); i ++){
                                if(i == especialidades.size() - 1){
                                    carreras += especialidades.get(i);
                                }
                                else{
                                    carreras += especialidades.get(i) + ", ";
                                }
                            }
                            especialidad = carreras;
                            correoActual = consultor.getCorreo();
                        }

                    }

                    if(url.length() > 2){
                        // ********* IMPORTANTE CAMBIAR ************
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(UUIDUser + "-profile");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgAvatar.setImageBitmap(bitmap);
                                imgFondo.setImageBitmap(bitmap);
                                imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                                // ***************************** GUARDAMOS LA IMAGEN ***********************
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                                if(!folder.exists())
                                    folder.mkdirs();
                                File file = new File(folder, "fotoMetodoApp.png");

                                if (file.exists ()) file.delete ();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("saveFoto", true);
                                    editor.commit();
                                    // ******************************************************************************

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("idActual", UUIDUser);
                                editor.putString("nombreActual", nombre);
                                editor.putString("especialidadActual", especialidad);
                                editor.putString("urlFotoActual", url);
                                editor.putString("correoActual", correoActual);

                                idActual = UUIDUser;
                                nombreActual = nombre;
                                especialidadActual = especialidad;
                                urlFotoActual = url;

                                editor.commit();
                                // ******************************************************************************

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    }

                    if(nombre.length() > 1){
                        txtNombre.setText(nombre);
                    }
                    if(especialidad.length() > 1){
                        txtRFC.setText(especialidad);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        imgCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConsultoresMainActivity.this);
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Estas seguro de querer cerrar sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("typeCurrentUser", "");
                                editor.putString("registroCompleto", "");
                                editor.putString("idActual", "");
                                editor.putString("nombreActual", "");
                                editor.putString("especialidadActual", "");
                                editor.putString("urlFotoActual", "");
                                editor.putString("correoActual", "");
                                editor.commit();
                                // ******************************************************************************
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                Intent i = new Intent(ConsultoresMainActivity.this, LogOrRegActivity.class);
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

        imgCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConsultoresMainActivity.this, VerMisCursosActivity.class);
                startActivity(i);
            }
        });

        imgVerConv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConsultoresMainActivity.this, ConsultoresVerConvocatoriasActivity.class);
                i.putExtra(VerMisCursosActivity.INTENT_ID_CONSULTOR, idActual);
                i.putExtra(VerMisCursosActivity.INTENT_NOMBRE_CONSULTOR, nombreActual);
                i.putExtra(VerMisCursosActivity.INTENT_URL_CONSULTOR, urlFotoActual);
                startActivity(i);
            }
        });

        imgHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ConsultoresMainActivity.this, ConsultoresHistorialActivity.class);
                startActivity(i);
            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        progressDialog.show();

        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);
                    if(convocatoria.getStatus().equals("abierta")){

                        boolean estaEnCurso = true;

                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(convocatoria.getDiaConfirmar() + " 23:59:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {
                            estaEnCurso = false;
                        }

                        if(estaEnCurso){
                            convocatorias.add(convocatoria);
                        }
                    }
                }


                caucelAppRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Consultor consultor = snapshot.getValue(Consultor.class);
                            if(consultor.getId().equals(idActual)){
                                misEspecialidades = consultor.getEspecialidad();
                            }
                        }

                        for(int i = 0; i < misEspecialidades.size(); i++){
                            for(int j = 0; j < convocatorias.size(); j++){
                                List<String> especialidadesReq = convocatorias.get(j).getEspecialidades();
                                for (int k = 0; k < especialidadesReq.size(); k++){
                                    if(especialidadesReq.get(k).equals(misEspecialidades.get(i))){
                                        if(convocatoriasFiltradas.size() == 0){
                                            convocatoriasFiltradas.add(convocatorias.get(j));
                                        }
                                        else{
                                            for(int m = 0; m < convocatoriasFiltradas.size(); m++){
                                                if(convocatoriasFiltradas.get(m).getId().equals(convocatorias.get(j).getId())){

                                                }
                                                else{
                                                    convocatoriasFiltradas.add(convocatorias.get(j));
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }

                        if(convocatoriasFiltradas.size() > 0){
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ConsultoresMainActivity.this);
                            builder.setTitle("Notificación");
                            builder.setMessage("¡Revisa en las nuevas convocatorias abiertas donde se solicita a gente con tu perfil!")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // FIRE ZE MISSILES!
                                            Intent i = new Intent(ConsultoresMainActivity.this, ConsultoresVerConvocatoriasActivity.class);
                                            i.putExtra(VerMisCursosActivity.INTENT_ID_CONSULTOR, idActual);
                                            i.putExtra(VerMisCursosActivity.INTENT_NOMBRE_CONSULTOR, nombreActual);
                                            i.putExtra(VerMisCursosActivity.INTENT_URL_CONSULTOR, urlFotoActual);
                                            startActivity(i);
                                        }
                                    });                    // Create the AlertDialog object and return it
                            builder.create();
                            builder.show();

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }
        });

    }
}
