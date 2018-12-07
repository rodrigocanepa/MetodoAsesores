package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionFinalizarConvActivity extends AppCompatActivity {


    FirebaseStorage storage = FirebaseStorage.getInstance();

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtCarrera;
        private RatingBar ratingBar;
        private Button btnCalificar;
        private CircleImageView circleImageView;

        private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<ConsultorConv> postulados) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.postulados = postulados;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.txtConsultoresNombreCalificarConsultores);
            txtCarrera = (TextView) itemView.findViewById(R.id.txtConsultoresEspecialidadCalificarConsultores);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.imgAvatarConsultoresCalificarConsultores);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingCalificarConsultores);
            btnCalificar = (Button) itemView.findViewById(R.id.btnCalificarConsultores);

            /*ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    postulados.get(getAdapterPosition()).setCalificacion(rating);
                    ratingBar.setRating(rating);
                }
            });*/

            ratingBar.setVisibility(View.INVISIBLE);

            btnCalificar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(InstitucionFinalizarConvActivity.this);

                    // Get the layout inflater
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.dialog_calificar,
                            null, false);

                    dialog_comentarios = (TextInputEditText) formElementsView.findViewById(R.id.dialog_comentariosCalificarConsultores);
                    dialog_ratinBar = (RatingBar) formElementsView.findViewById(R.id.dialog_ratingCalificarConsultores);

                    dialog_ratinBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            dialog_ratinBar.setRating(rating);
                        }
                    });
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String comentarios = dialog_comentarios.getText().toString();
                            float calificacion = dialog_ratinBar.getRating();
                            boolean error = false;

                            if(comentarios.length() < 2){
                                error = true;
                            }


                            if(error == true){
                                Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                            }
                            else{
                                postulados.get(getAdapterPosition()).setCalificacion(calificacion);
                                postulados.get(getAdapterPosition()).setComentarios(comentarios);
                                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                adapter = new InstitucionFinalizarConvActivity.DataConfigAdapter(postulados, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }


                        }
                    });

                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    // Inflate and set the layout for the dialog
                    // Pass null as the parent view because its going in the dialog layout
                    builder.setView(formElementsView);
                    // Add action buttons
                    builder.create();
                    builder.show();
                }
            });
        }

        public void bindConfig(final ConsultorConv postulado) {
            txtNombre.setText(postulado.getNombreCompleto());
            txtCarrera.setText(postulado.getEspecialidad());

            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(postulado.getId() + "-profile");
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    circleImageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

            if(postulado.getComentarios().length() < 2){
                btnCalificar.setText("Calificar");
                btnCalificar.setTextColor(Color.BLACK);
            }
            else{
                btnCalificar.setTextColor(Color.rgb(0,130,0));
                btnCalificar.setText("Calificado");
            }
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionFinalizarConvActivity.DataConfigHolder> {

        private List<ConsultorConv> postulados;
        Context ctx;

        public DataConfigAdapter(List<ConsultorConv> postulados, Context ctx ){
            this.postulados = postulados;
            this.ctx = ctx;
        }

        @Override
        public InstitucionFinalizarConvActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_calificar_consultores, parent, false);
            return new InstitucionFinalizarConvActivity.DataConfigHolder(view, ctx, postulados);
        }

        @Override
        public void onBindViewHolder(final InstitucionFinalizarConvActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(postulados.get(position));

        }

        @Override
        public int getItemCount() {
            return postulados.size();
        }

    }

    private InstitucionFinalizarConvActivity.DataConfigAdapter adapter;
    private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();

    private TextInputEditText editParticipantes;
    private TextInputEditText editComentarios;
    private ImageView img1;
    private ImageView img2;
    private ImageView img3;
    private RecyclerView recyclerView;
    private Button btnFinalizar;

    private boolean isSelectedPhoto1 = false;
    private boolean isSelectedPhoto2 = false;
    private boolean isSelectedPhoto3 = false;

    private ProgressDialog progressDialog;
    private String url1 = "";
    private String url2 = "";
    private String url3 = "";

    public static final String ID_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_ID_CONVOCATORIA";
    private String id_conv = "";
    private DatabaseReference caucelAppRef;

    private RatingBar dialog_ratinBar;
    private TextInputEditText dialog_comentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institucion_finalizar_conv);
        setTitle("Finalizar Convocatoria");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerFinalizarConv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        editParticipantes = (TextInputEditText)findViewById(R.id.txtFinalizarConvocatoriaAsistentes);
        editComentarios = (TextInputEditText)findViewById(R.id.txtFinalizarConvocatoriaComentarios);
        img1 = (ImageView) findViewById(R.id.imgFinalizarConvocatoria1);
        img2 = (ImageView)findViewById(R.id.imgFinalizarConvocatoria2);
        img3 = (ImageView)findViewById(R.id.imgFinalizarConvocatoria3);
        btnFinalizar = (Button)findViewById(R.id.btnFinalizarFinConvocatoria);

        progressDialog = new ProgressDialog(InstitucionFinalizarConvActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");

        Intent i = getIntent();
        id_conv = i.getStringExtra(ID_CONVOCATORIA);

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("consultorConvs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ConsultorConv postulado = snapshot.getValue(ConsultorConv.class);
                    postulados.add(postulado);
                }

                if (postulados.size() == 0){
                    Toast.makeText(getApplicationContext(), "No hay consultores confirmados", Toast.LENGTH_LONG).show();
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new InstitucionFinalizarConvActivity.DataConfigAdapter(postulados, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                        .setCameraButtonText("Cámara")
                        .setGalleryButtonText("Galería"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                img1.setImageBitmap(r.getBitmap());
                                isSelectedPhoto1 = true;
                                url1 = id_conv + "-foto1.png";
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

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelectedPhoto1){
                    PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                            .setCameraButtonText("Cámara")
                            .setGalleryButtonText("Galería"))
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    //TODO: do what you have to...
                                    img2.setImageBitmap(r.getBitmap());
                                    isSelectedPhoto2 = true;
                                    url2 = id_conv + "-foto2.png";

                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show(getSupportFragmentManager());
                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes seleccionar primero la imagen 1", Toast.LENGTH_LONG).show();
                }
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSelectedPhoto1 && isSelectedPhoto2){
                    PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                            .setCameraButtonText("Cámara")
                            .setGalleryButtonText("Galería"))
                            .setOnPickResult(new IPickResult() {
                                @Override
                                public void onPickResult(PickResult r) {
                                    //TODO: do what you have to...
                                    img3.setImageBitmap(r.getBitmap());
                                    isSelectedPhoto3 = true;
                                    url3 = id_conv + "-foto3.png";
                                }
                            })
                            .setOnPickCancel(new IPickCancel() {
                                @Override
                                public void onCancelClick() {
                                    //TODO: do what you have to if user clicked cancel
                                }
                            }).show(getSupportFragmentManager());
                }

                else{
                    Toast.makeText(getApplicationContext(), "Debes seleccionar primero la imagen 1 y 2", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int asistentes = 0;
                final String comentarioFinal = editComentarios.getText().toString();

                boolean calificados = true;

                for(int i = 0; i<postulados.size(); i++){
                    if(postulados.get(i).getComentarios().length() < 2){
                        calificados = false;
                    }
                }

                if(calificados == false){
                    Toast.makeText(getApplicationContext(), "Debes calificar a tus consultores para finalizar", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(editParticipantes.getText().toString().length() == 0){
                    editParticipantes.setError("Debes introducir un valor");
                    return;
                }
                else{
                    asistentes = Integer.valueOf(editParticipantes.getText().toString());
                }
                if(editComentarios.getText().toString().length() < 2){
                    editComentarios.setError("Debes introducir tus comentarios");
                    return;
                }
                if(isSelectedPhoto1 == false){
                    Toast.makeText(getApplicationContext(), "Debes subir por lo menos una foto", Toast.LENGTH_LONG).show();
                    return;
                }

                progressDialog.show();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                final String UUIDUser = user.getUid();

                if (isSelectedPhoto1){
                    img1.setDrawingCacheEnabled(true);
                    img1.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                    img1.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(img1.getDrawingCache());

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias/").child(id_conv + "-foto1.png");

                    UploadTask uploadTask = refSubirImagen.putBytes(data);

                    final int finalAsistentes = asistentes;
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {


                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if(isSelectedPhoto2 == false){
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();

                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("comentarios").setValue(comentarioFinal);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("asistentes").setValue(finalAsistentes);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto1").setValue(url1);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto2").setValue(url2);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto3").setValue(url3);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("status").setValue("finalizada");

                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("consultorConvs").setValue(postulados).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            Intent i = new Intent(InstitucionFinalizarConvActivity.this, InstitucionesMain2Activity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

                if (isSelectedPhoto2){
                    img2.setDrawingCacheEnabled(true);
                    img2.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                    img2.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(img2.getDrawingCache());
                    final int finalAsistentes = asistentes;

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias/").child(id_conv + "-foto2.png");

                    UploadTask uploadTask = refSubirImagen.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {


                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (isSelectedPhoto3 == false){
                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();

                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("comentarios").setValue(comentarioFinal);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("asistentes").setValue(finalAsistentes);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto1").setValue(url1);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto2").setValue(url2);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto3").setValue(url3);
                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("status").setValue("finalizada");

                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("consultorConvs").setValue(postulados).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(progressDialog.isShowing()){
                                                progressDialog.dismiss();
                                            }
                                            Intent i = new Intent(InstitucionFinalizarConvActivity.this, InstitucionesMain2Activity.class);
                                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i);
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

                if (isSelectedPhoto3){
                    img3.setDrawingCacheEnabled(true);
                    img3.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                    img3.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(img3.getDrawingCache());
                    final int finalAsistentes = asistentes;


                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias/").child(id_conv + "-foto3.png");

                    UploadTask uploadTask = refSubirImagen.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();

                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("comentarios").setValue(comentarioFinal);
                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("asistentes").setValue(finalAsistentes);
                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto1").setValue(url1);
                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto2").setValue(url2);
                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("urlFoto3").setValue(url3);
                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("status").setValue("finalizada");

                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_conv).child("consultorConvs").setValue(postulados).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(progressDialog.isShowing()){
                                            progressDialog.dismiss();
                                        }
                                        Intent i = new Intent(InstitucionFinalizarConvActivity.this, InstitucionesMain2Activity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                });
                            }
                        }
                    });
                }




            }
        });

    }
}
