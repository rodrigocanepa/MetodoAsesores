package projects.solucionescolabora.com.metodoasesores.Startups;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Consultores.PDFCVConsultoresActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroReconocimientosConsulActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.CrearConvocatoriaMainActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.RegistroInstitucionActivity;
import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.R;

public class RegistroStartupActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtInteres;
        private ImageView btnDelete;

        private List<String> intereses = new ArrayList<String >();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<String > intereses) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.intereses = intereses;
            this.ctx = ctx;

            txtInteres = (TextView) itemView.findViewById(R.id.txtItemNuevaConvEsp);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_nuev_conv_delete);
        }

        public void bindConfig(final String interes) {
            txtInteres.setText(interes);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroStartupActivity.this);
                    builder.setTitle("Eliminar interés");
                    builder.setMessage("¿Estas seguro de querer elimnar este interés?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    intereses.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new RegistroStartupActivity.DataConfigAdapter(intereses, getApplicationContext());
                                    recyclerView.setAdapter(adapter);
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

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<RegistroStartupActivity.DataConfigHolder> {

        private List<String> intereses;
        Context ctx;

        public DataConfigAdapter(List<String> intereses, Context ctx ){
            this.intereses = intereses;
            this.ctx = ctx;
        }

        @Override
        public RegistroStartupActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_nueva_conv_esp, parent, false);
            return new RegistroStartupActivity.DataConfigHolder(view, ctx, intereses);
        }

        @Override
        public void onBindViewHolder(final RegistroStartupActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(intereses.get(position));

        }

        @Override
        public int getItemCount() {
            return intereses.size();
        }

    }

    private RegistroStartupActivity.DataConfigAdapter adapter;
    // ******************************************************************


    private ImageView imgFoto;
    private TextInputEditText txtNombreStartup;
    private TextInputEditText txtNombreEmprendedor;
    private TextInputEditText txtApellidosEmprendeodr;
    private TextInputEditText txtFechaInicio;
    private TextInputEditText txtCorreo;
    private TextInputEditText txtTelefono;
    private TextInputEditText txtUbicacion;
    private List<String> intereses = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button btnAgregarInteres;
    private Button btnFinalizar;

    private String nombreStartup;
    private String nombreEmprendedor;
    private String apellidosEmprendedor;
    private String fechaInicio;
    private String correo;
    private String telefono;
    private String ubicacion;
    private SharedPreferences sharedPreferences;

    FirebaseStorage storage = FirebaseStorage.getInstance();

    private String esp = "";
    private boolean fotoSelected = false;

    private ProgressDialog progressDialog;
    private String UUIDUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_startup);
        setTitle("Registro Startup");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imgFoto = (ImageView)findViewById(R.id.imgRegistroStartupAvatar);
        txtNombreEmprendedor = (TextInputEditText)findViewById(R.id.txtRegistroStartupNombreEmprendedor);
        txtApellidosEmprendeodr = (TextInputEditText)findViewById(R.id.txtRegistroStartupApellidosEmprendedor);
        txtNombreStartup = (TextInputEditText)findViewById(R.id.txtRegistroStartupNombredelaStart);
        txtFechaInicio = (TextInputEditText)findViewById(R.id.txtRegistroStartupFechaInicio);
        txtCorreo = (TextInputEditText)findViewById(R.id.txtRegistroStartupCorreo);
        txtTelefono = (TextInputEditText)findViewById(R.id.txtRegistroStartupTelefono);
        txtUbicacion = (TextInputEditText)findViewById(R.id.txtRegistroStartupUbicacion);
        btnAgregarInteres = (Button)findViewById(R.id.btnRegistroStartupAgregarInteres);
        btnFinalizar = (Button)findViewById(R.id.btnRegistroStartupFinalizar);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerRegistroStartup);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(RegistroStartupActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDUser = user.getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.STARTUP_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Startup startup = snapshot.getValue(Startup.class);

                    if(startup.getId().equals(UUIDUser)){
                        if(startup.getNombreStartup() != null){
                            txtNombreStartup.setText(startup.getNombreStartup());
                            txtApellidosEmprendeodr.setText(startup.getApellidos());
                            txtCorreo.setText(startup.getCorreo());
                            txtFechaInicio.setText(startup.getFechaInicio());
                            txtTelefono.setText(startup.getTelefono());
                            txtUbicacion.setText(startup.getUbicacion());
                            txtNombreEmprendedor.setText(startup.getNombres());
                            fotoSelected = true;

                            // ********* IMPORTANTE CAMBIAR ************
                            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(UUIDUser + "-profile");
                            final long ONE_MEGABYTE = 1024 * 1024;
                            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    imgFoto.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            });
                        }

                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        imgFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                        .setCameraButtonText("Cámara")
                        .setGalleryButtonText("Galería"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                imgFoto.setImageBitmap(r.getBitmap());
                                fotoSelected = true;
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(getSupportFragmentManager());
            }
        });

        btnAgregarInteres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker numberPicker = new NumberPicker(RegistroStartupActivity.this);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(0);
                numberPicker.setDisplayedValues(new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"});
                final String[] arrayPicker= new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"};

                esp = "Diseño digital";

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        esp = arrayPicker[i1];
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroStartupActivity.this);
                builder.setView(numberPicker);
                builder.setTitle("Especialidades");
                builder.setMessage("Selecciona la especialidad requerida");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intereses.add(esp);
                        // *********** LLENAMOS EL RECYCLER VIEW *****************************
                        adapter = new RegistroStartupActivity.DataConfigAdapter(intereses, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create();
                builder.show();
            }
        });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;

                nombreEmprendedor = txtNombreEmprendedor.getText().toString();
                nombreStartup = txtNombreStartup.getText().toString();
                apellidosEmprendedor = txtApellidosEmprendeodr.getText().toString();
                telefono = txtTelefono.getText().toString();
                correo = txtCorreo.getText().toString();
                fechaInicio = txtFechaInicio.getText().toString();
                ubicacion = txtUbicacion.getText().toString();

                if (fotoSelected == false){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar una foto de perfil para continuar", Toast.LENGTH_LONG).show();
                    return;
                }

                if (nombreStartup.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (nombreEmprendedor.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (apellidosEmprendedor.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (correo.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (fechaInicio.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (ubicacion.length() < 2){
                    Toast.makeText(getApplicationContext(), "Debes completar todos los datos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                if (fotoSelected == false){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar una foto para finalizar", Toast.LENGTH_LONG).show();
                    return;
                }
                if (intereses.size() == 0){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar por lo menos un interés para finalizar", Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                final String UUIDUser = user.getUid();

                imgFoto.setDrawingCacheEnabled(true);
                imgFoto.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                imgFoto.buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(imgFoto.getDrawingCache());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                byte[] data = outputStream.toByteArray();

                StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(UUIDUser + "-profile");

                UploadTask uploadTask = refSubirImagen.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String topico = "";
                        for(int i = 0; i < intereses.size(); i++){

                            if (intereses.get(i).equals("Tecnología e innovación")){
                                topico = "TecnologiaInnovacion";
                            }
                            else if (intereses.get(i).equals("Planeación estratégica")){
                                topico = "PlaneacionEstrategica";
                            }
                            else if (intereses.get(i).equals("Diseño")){
                                topico = "Diseno";
                            }
                            else if (intereses.get(i).equals("Marketing")){
                                topico = "Marketing";
                            }
                            else if (intereses.get(i).equals("Finanzas")){
                                topico = "Finanzas";
                            }
                            else if (intereses.get(i).equals("Administración")){
                                topico = "Administracion";
                            }
                            else if (intereses.get(i).equals("Legal y administración")){
                                topico = "Legal";
                            }
                            else if (intereses.get(i).equals("Turismo")){
                                topico = "Turismo";
                            }
                            else if (intereses.get(i).equals("Responsabilidad social")){
                                topico = "Responsabilidad";
                            }
                            else if (intereses.get(i).equals("Procuración de recursos")){
                                topico = "Procuracion";
                            }
                            else if (intereses.get(i).equals("Comercio")){
                                topico = "Comercio";
                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(topico);
                        }

                        Toast.makeText(getApplicationContext(), "Foto subida con exito", Toast.LENGTH_SHORT).show();
                        Startup startup = new Startup(UUIDUser,nombreEmprendedor,apellidosEmprendedor,correo,telefono,UUIDUser + "-profile",ubicacion,intereses,nombreStartup,fechaInicio);
                        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.STARTUP_REFERENCE).child(UUIDUser).setValue(startup).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                FirebaseMessaging.getInstance().subscribeToTopic("startup");
                                Intent i = new Intent(RegistroStartupActivity.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("typeCurrentUser", "Startup");
                                    editor.putString("registroCompleto", "Completo");
                                    editor.putString("idActual", "");
                                    editor.commit();
                                // ******************************************************************************
                                startActivity(i);
                            }
                        });
                    }
                });
            }
        });
    }
}
