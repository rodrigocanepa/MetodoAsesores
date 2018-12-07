package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import javax.mail.Session;

import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesVerConfirmadosActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtCarrera;
        private Button btnVerCurriculum;
        private Button btnVerTemarios;

        private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<ConsultorConv> postulados) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.postulados = postulados;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.txtConsultoresNombreCONFIRMADOS);
            txtCarrera = (TextView) itemView.findViewById(R.id.txtConsultoresEspecialidadCONFIRMADOS);
            btnVerCurriculum = (Button)itemView.findViewById(R.id.item_instituciones_ver_curriculumCONFIRMADOS);
            btnVerTemarios = (Button)itemView.findViewById(R.id.item_instituciones_ver_temariosCONFIRMADOS);

            btnVerCurriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerConfirmadosActivity.this, VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, postulados.get(getAdapterPosition()).getId());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "curriculum");
                    startActivity(i);
                }
            });

            btnVerTemarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerConfirmadosActivity.this, VerTemariosActivity.class);
                    i.putExtra(VerTemariosActivity.ID_CONSULTOR, postulados.get(getAdapterPosition()).getId());
                    startActivity(i);
                }
            });

        }

        public void bindConfig(final ConsultorConv postulado) {
            txtNombre.setText(postulado.getNombreCompleto());
            txtCarrera.setText(postulado.getEspecialidad());

        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionesVerConfirmadosActivity.DataConfigHolder> {

        private List<ConsultorConv> postulados;
        Context ctx;

        public DataConfigAdapter(List<ConsultorConv> postulados, Context ctx ){
            this.postulados = postulados;
            this.ctx = ctx;
        }

        @Override
        public InstitucionesVerConfirmadosActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_confirmados, parent, false);
            return new InstitucionesVerConfirmadosActivity.DataConfigHolder(view, ctx, postulados);
        }

        @Override
        public void onBindViewHolder(final InstitucionesVerConfirmadosActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(postulados.get(position));

        }

        @Override
        public int getItemCount() {
            return postulados.size();
        }

    }

    private InstitucionesVerConfirmadosActivity.DataConfigAdapter adapter;
    private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;
    private TextInputEditText editCorreo;
    private TextInputEditText editResponsable;


    public static final String ID_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_ID_CONVOCATORIA";
    public static final String NOMBRE_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_NOMBRE_CONVOCATORIA";
    public static final String INSTITUCION_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_INSTITUCION_CONVOCATORIA";

    private String id_convocatoria = "";
    private String nombre_convocatoria = "";
    private String institucion_convocatoria = "";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_ver_confirmados);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerVerConfirmados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        id_convocatoria = intent.getStringExtra(ID_CONVOCATORIA);
        nombre_convocatoria = intent.getStringExtra(NOMBRE_CONVOCATORIA);
        institucion_convocatoria = intent.getStringExtra(INSTITUCION_CONVOCATORIA);

        progressDialog = new ProgressDialog(InstitucionesVerConfirmadosActivity.this);

        progressDialog.setTitle("Enviando correo");
        progressDialog.setMessage("Espere un momento mientras el sistema envia el correo");

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("consultorConvs").addListenerForSingleValueEvent(new ValueEventListener() {
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
                adapter = new InstitucionesVerConfirmadosActivity.DataConfigAdapter(postulados, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
