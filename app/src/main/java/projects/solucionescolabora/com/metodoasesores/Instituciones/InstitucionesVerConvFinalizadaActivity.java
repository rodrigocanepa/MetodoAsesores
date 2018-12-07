package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresVerConvocatoriasActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesVerConvFinalizadaActivity extends AppCompatActivity {

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

            ratingBar.setEnabled(false);
            btnCalificar.setVisibility(View.INVISIBLE);
        }

        public void bindConfig(final ConsultorConv postulado) {
            txtNombre.setText(postulado.getNombreCompleto());
            txtCarrera.setText(postulado.getEspecialidad() + "\nComentarios: " + postulado.getComentarios());
            ratingBar.setRating((float)postulado.getCalificacion());

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
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionesVerConvFinalizadaActivity.DataConfigHolder> {

        private List<ConsultorConv> postulados;
        Context ctx;

        public DataConfigAdapter(List<ConsultorConv> postulados, Context ctx ){
            this.postulados = postulados;
            this.ctx = ctx;
        }

        @Override
        public InstitucionesVerConvFinalizadaActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_calificar_consultores, parent, false);
            return new InstitucionesVerConvFinalizadaActivity.DataConfigHolder(view, ctx, postulados);
        }

        @Override
        public void onBindViewHolder(final InstitucionesVerConvFinalizadaActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(postulados.get(position));

        }

        @Override
        public int getItemCount() {
            return postulados.size();
        }

    }

    private InstitucionesVerConvFinalizadaActivity.DataConfigAdapter adapter;
    private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();

    private TextView txtTitulo;
    private TextView txtDescripcion;
    private TextView txtUbicacion;
    private TextView txtAsistentes;
    private TextView txtDia;
    private TextView txtLocacion;
    private TextView txtComentarios;
    private RecyclerView recyclerView;

    private ImageView img1;
    private ImageView img2;
    private ImageView img3;

    private List<ConsultorConv> confirmados;

    public static final String ID_CONVOCATORIA = "ID_CONVOCATORIA";
    private String id_conv = "";
    private DatabaseReference caucelAppRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_ver_conv_finalizada);

        txtTitulo = (TextView)findViewById(R.id.verConvFinTitutlo);
        txtDescripcion = (TextView)findViewById(R.id.verConvFinDescripcion);
        txtUbicacion = (TextView)findViewById(R.id.verConvFinUbicacion);
        txtAsistentes = (TextView)findViewById(R.id.verConvFinAsisentes);
        txtDia = (TextView)findViewById(R.id.verConvFinDia);
        txtLocacion = (TextView)findViewById(R.id.verConvFinLocacion);
        txtComentarios = (TextView)findViewById(R.id.verConvFinComentarios);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerVerConvFinalizada);
        img1 = (ImageView) findViewById(R.id.imgConvFinalizada1);
        img2 = (ImageView)findViewById(R.id.imgConvFinalizada2);
        img3 = (ImageView)findViewById(R.id.imgConvFinalizada3);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent i = getIntent();
        id_conv = i.getStringExtra(ID_CONVOCATORIA);


        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************
        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String tit = "";
                String descr = "";
                String ubica = "";
                String asist = "";
                String dia = "";
                String loca = "";
                String comen = "";
                String url1 = "";
                String url2 = "";
                String url3 = "";

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);

                    if(convocatoria.getId().equals(id_conv)){
                        tit = convocatoria.getTitulo();
                        descr = convocatoria.getDescripcion();
                        ubica = convocatoria.getUbicacion();
                        asist = String.valueOf(convocatoria.getAsistentes());
                        dia = convocatoria.getDiaEvento();
                        loca = convocatoria.getLocacion();
                        comen = convocatoria.getComentarios();
                        url1 = convocatoria.getUrlFoto1();
                        url2 = convocatoria.getUrlFoto2();
                        url3 = convocatoria.getUrlFoto3();
                        confirmados = convocatoria.getConsultorConvs();
                    }


                }

                txtTitulo.setText(tit);
                txtDescripcion.setText(descr);
                txtAsistentes.setText(asist);
                txtDia.setText(dia);
                txtLocacion.setText(loca);
                txtComentarios.setText(comen);

                if(confirmados.size()> 0){
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerConvFinalizadaActivity.DataConfigAdapter(confirmados, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }

                if(url1.length() > 0){
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias").child(url1);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            img1.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }

                if(url2.length() > 0){
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias").child(url2);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            img2.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }

                if(url3.length() > 0){
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Convocatorias").child(url3);
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            img3.setImageBitmap(bitmap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
