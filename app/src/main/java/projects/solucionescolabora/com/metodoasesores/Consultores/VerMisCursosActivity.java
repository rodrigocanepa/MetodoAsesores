package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesVerConsultoresActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.Curso;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class VerMisCursosActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitulo;
        private TextView txtDescripcion;
        private TextView txtDuracion;
        private Button btnEditar;

        private List<Curso> cursos = new ArrayList<Curso>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<Curso> cursos) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.cursos = cursos;
            this.ctx = ctx;

            txtTitulo = (TextView) itemView.findViewById(R.id.item_txtCursoNombre);
            txtDescripcion = (TextView) itemView.findViewById(R.id.item_txtCursoDescripcion);
            txtDuracion = (TextView) itemView.findViewById(R.id.item_txtCursoDuracion);
            btnEditar = (Button) itemView.findViewById(R.id.btnEditarCursos);

            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    String titulo = cursos.get(pos).getTitulo();
                    String descripcion = cursos.get(pos).getDescripcion();
                    String duracion = cursos.get(pos).getDuracion();
                    String objetivo = cursos.get(pos).getObjetivos();
                    String requerimientos = cursos.get(pos).getRequerimientos();
                    String id = cursos.get(pos).getId();

                    Intent i = new Intent(VerMisCursosActivity.this, CrearCursosConsulActivity.class);
                    i.putExtra(CrearCursosConsulActivity.INTENT_TITULO, titulo);
                    i.putExtra(CrearCursosConsulActivity.INTENT_DESCRIPCION, descripcion);
                    i.putExtra(CrearCursosConsulActivity.INTENT_DURACION, duracion);
                    i.putExtra(CrearCursosConsulActivity.INTENT_OBJETIVO, objetivo);
                    i.putExtra(CrearCursosConsulActivity.INTENT_REQUERIMIENTOS, requerimientos);
                    i.putExtra(CrearCursosConsulActivity.INTENT_IDCURSO, id);
                    i.putExtra(CrearCursosConsulActivity.INTENT_ID_CONSULTOR, idActual);
                    i.putExtra(CrearCursosConsulActivity.INTENT_NOMBRE_CONSULTOR, nombre);
                    i.putExtra(CrearCursosConsulActivity.INTENT_URL_CONSULTOR, url);
                    startActivity(i);
                }
            });
        }

        public void bindConfig(final Curso curso) {
            txtTitulo.setText(curso.getTitulo());
            txtDescripcion.setText(curso.getDescripcion());
            txtDuracion.setText(curso.getDuracion());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();


        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<VerMisCursosActivity.DataConfigHolder> {

        private List<Curso> cursos;
        Context ctx;

        public DataConfigAdapter(List<Curso> cursos, Context ctx ){
            this.cursos = cursos;
            this.ctx = ctx;
        }

        @Override
        public VerMisCursosActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.items_cursos, parent, false);
            return new VerMisCursosActivity.DataConfigHolder(view, ctx, cursos);
        }

        @Override
        public void onBindViewHolder(final VerMisCursosActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(cursos.get(position));

        }

        @Override
        public int getItemCount() {
            return cursos.size();
        }

    }

    private VerMisCursosActivity.DataConfigAdapter adapter;
    private List<Curso> cursos = new ArrayList<Curso>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;
    private String UUIDUser = "";
    private FloatingActionButton fab;

    public static final String INTENT_ID_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.VER.ID_CONSULTOR";
    public static final String INTENT_URL_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.VER.URL_CONSULTOR";
    public static final String INTENT_NOMBRE_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.VER.NOMBRE_CONSULTOR";

    private String nombre;
    private String id;
    private String url;

    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private String idActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mis_cursos);
        setTitle("Mis cursos");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewCursos);
        fab = (FloatingActionButton)findViewById(R.id.fabAgregarCursos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");

        progressDialog = new ProgressDialog(VerMisCursosActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");

        progressDialog.show();

        Intent i = getIntent();
        nombre = i.getStringExtra(INTENT_NOMBRE_CONSULTOR);
        id = i.getStringExtra(INTENT_ID_CONSULTOR);
        url = i.getStringExtra(INTENT_URL_CONSULTOR);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VerMisCursosActivity.this, CrearCursosConsulActivity.class);
                i.putExtra(CrearCursosConsulActivity.INTENT_ID_CONSULTOR, UUIDUser);
                i.putExtra(CrearCursosConsulActivity.INTENT_NOMBRE_CONSULTOR, nombre);
                i.putExtra(CrearCursosConsulActivity.INTENT_URL_CONSULTOR, url);
                startActivity(i);
            }
        });

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDUser = user.getUid();

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        cursos.clear();
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CURSOS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cursos.clear();
                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Curso curso = snapshot.getValue(Curso.class);
                    if(curso.getIdConsultor().equals(idActual)){
                        cursos.add(curso);
                    }

                }
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new VerMisCursosActivity.DataConfigAdapter(cursos, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(VerMisCursosActivity.this, ConsultoresMainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
/*
        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        cursos.clear();
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CURSOS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Curso curso = snapshot.getValue(Curso.class);
                    if(curso.getIdConsultor().equals(UUIDUser)){
                        cursos.add(curso);
                    }
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new VerMisCursosActivity.DataConfigAdapter(cursos, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */
    }
}
