package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class ConsultoresHistorialActivity extends AppCompatActivity {


    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtTitulo;
        private TextView txtUbicacion;
        private TextView txtFechaPos;
        private TextView txtFechaEvento;
        private Button btnVerDetalles;
        private Button btnPostularme;
        private ImageView circleImageView;

        private List<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<Convocatoria> convocatorias) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.convocatorias = convocatorias;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.txtConvocatoriaNombreInstitucion);
            txtTitulo = (TextView) itemView.findViewById(R.id.txtConvocatoriaTitulo);
            txtUbicacion = (TextView) itemView.findViewById(R.id.txtConvocatoriaUbicacion);
            txtFechaPos = (TextView) itemView.findViewById(R.id.txtConvocatoriaFechaPostulacion);
            txtFechaEvento = (TextView) itemView.findViewById(R.id.txtConvocatoriaFechaEvento);
            circleImageView = (ImageView) itemView.findViewById(R.id.imgAvatarConvocatorias);
            btnPostularme = (Button)itemView.findViewById(R.id.btnPortularmeConvocatoria);
            btnVerDetalles = (Button)itemView.findViewById(R.id.btnVerDescripcionConvocatoria);

            btnPostularme.setText("Ver mi calificación");

            btnVerDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    final String mensaje = "Descripción: " + convocatorias.get(pos).getDescripcion() + "\n\nLocación: " + convocatorias.get(pos).getLocacion() + "\n\nPrecio: $" + convocatorias.get(pos).getPrecio();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultoresHistorialActivity.this);
                    builder.setTitle("Detalles");
                    builder.setMessage(mensaje)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            });

            btnPostularme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    List<ConsultorConv> consultores_ = convocatorias.get(pos).getConsultorConvs();
                    String calificacion = "";
                    String comentario = "";
                    for(int i = 0; i < consultores_.size(); i++){
                        if(consultores_.get(i).getId().equals(idActual)){
                            calificacion = String.valueOf(consultores_.get(i).getCalificacion());
                            comentario = consultores_.get(i).getComentarios();
                        }
                    }
                    final String mensaje = "Calificacion: " + calificacion + "\n\nComentarios: " + comentario;
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultoresHistorialActivity.this);
                    builder.setTitle("Calificación");
                    builder.setMessage(mensaje)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            });
        }

        public void bindConfig(final Convocatoria convocatoria) {
            txtNombre.setText(convocatoria.getNombreInstitucion());
            txtUbicacion.setText(convocatoria.getUbicacion());
            txtTitulo.setText(convocatoria.getTitulo());
            txtFechaEvento.setText("Fecha del evento: " + convocatoria.getDiaEvento());

            if(convocatoria.getStatus().equals("abierta")){
                txtFechaPos.setText("En curso");
                txtFechaPos.setTextColor(Color.rgb(254,203,47));
            }
            else if(convocatoria.getStatus().equals("finalizada")){
                txtFechaPos.setText("En curso");
                txtFechaPos.setTextColor(Color.WHITE);
            }

            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones/").child(convocatoria.getUrlFotoInstitucion());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<ConsultoresHistorialActivity.DataConfigHolder> {

        private List<Convocatoria> convocatorias;
        Context ctx;

        public DataConfigAdapter(List<Convocatoria> convocatorias, Context ctx ){
            this.convocatorias = convocatorias;
            this.ctx = ctx;
        }

        @Override
        public ConsultoresHistorialActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_convocatorias, parent, false);
            return new ConsultoresHistorialActivity.DataConfigHolder(view, ctx, convocatorias);
        }

        @Override
        public void onBindViewHolder(final ConsultoresHistorialActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(convocatorias.get(position));

        }

        @Override
        public int getItemCount() {
            return convocatorias.size();
        }

    }

    private ConsultoresHistorialActivity.DataConfigAdapter adapter;
    private List<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String idActual = "";

    // ******************************************************************
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultores_historial);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerConsultoresHistorial);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(ConsultoresHistorialActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");

        progressDialog.show();

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);

                    List<ConsultorConv> consultor = convocatoria.getConsultorConvs();
                    if(consultor != null){
                        for(int i = 0; i < consultor.size(); i++){
                            if (consultor.get(i).getId().equals(idActual)){
                                convocatorias.add(convocatoria);
                            }
                        }
                    }

                    }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(convocatorias.size() == 0){
                    Toast.makeText(getApplicationContext(), "Todavía no tienes convocatorias finalizadas en tu historial", Toast.LENGTH_LONG).show();
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new ConsultoresHistorialActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
