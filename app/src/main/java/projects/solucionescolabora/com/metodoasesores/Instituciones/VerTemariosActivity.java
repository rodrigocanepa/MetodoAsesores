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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.Curso;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class VerTemariosActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitulo;
        private TextView txtObjetivo;
        private TextView txtEspecialidad;
        private Button btnVerTemarios;

        private List<Curso> cursos = new ArrayList<Curso>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<Curso> cursos) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.cursos = cursos;
            this.ctx = ctx;

            txtTitulo = (TextView) itemView.findViewById(R.id.item_ver_temarios_titulo);
            txtEspecialidad = (TextView) itemView.findViewById(R.id.item_ver_temarios_especialidad);
            txtObjetivo = (TextView) itemView.findViewById(R.id.item_ver_temarios_objetivos);
            btnVerTemarios = (Button)itemView.findViewById(R.id.btn_instituciones_ver_temario);

            btnVerTemarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(VerTemariosActivity.this, VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, cursos.get(getAdapterPosition()).getIdConsultor());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "temario");
                    i.putExtra(VerCurriculumsConsultoresActivity.TITULO, cursos.get(getAdapterPosition()).getTitulo());
                    startActivity(i);
                }
            });
        }

        public void bindConfig(final Curso curso) {
            txtTitulo.setText(curso.getTitulo());
            txtEspecialidad.setText(curso.getTipo());
            txtObjetivo.setText(curso.getObjetivos());

        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();



        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<VerTemariosActivity.DataConfigHolder> {

        private List<Curso> cursos;
        Context ctx;

        public DataConfigAdapter(List<Curso> cursos, Context ctx ){
            this.cursos = cursos;
            this.ctx = ctx;
        }

        @Override
        public VerTemariosActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_ver_temarios, parent, false);
            return new VerTemariosActivity.DataConfigHolder(view, ctx, cursos);
        }

        @Override
        public void onBindViewHolder(final VerTemariosActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(cursos.get(position));

        }

        @Override
        public int getItemCount() {
            return cursos.size();
        }

    }

    private VerTemariosActivity.DataConfigAdapter adapter;
    private List<Curso> cursos = new ArrayList<Curso>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;

    public static final String ID_CONSULTOR = "";
    private String id_cons = "";

    // ******************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_temarios);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerTemarios);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        Intent i = getIntent();
        id_cons = i.getStringExtra(ID_CONSULTOR);

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CURSOS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Curso curso = snapshot.getValue(Curso.class);
                    if(curso.getIdConsultor().equals(id_cons)){
                        cursos.add(curso);
                    }
                }

                if(cursos.size() == 0){
                    Toast.makeText(getApplicationContext(), "Este consultor todavía no tiene temarios registrados", Toast.LENGTH_LONG).show();
                }
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new VerTemariosActivity.DataConfigAdapter(cursos, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
