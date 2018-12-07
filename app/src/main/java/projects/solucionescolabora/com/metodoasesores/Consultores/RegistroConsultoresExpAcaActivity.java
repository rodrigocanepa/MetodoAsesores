package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.LogOrRegActivity;
import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class RegistroConsultoresExpAcaActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtGrado;
        private TextView txtMateria;
        private TextView txtEscuela;
        private TextView txtGeneracion;
        private ImageView btnDelete;

        private List<ExperienciaAcademica> experienciaAcademicas = new ArrayList<ExperienciaAcademica>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<ExperienciaAcademica> experienciaAcademicas) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.experienciaAcademicas = experienciaAcademicas;
            this.ctx = ctx;

            txtGrado = (TextView) itemView.findViewById(R.id.item_exp_aca_grado);
            txtMateria = (TextView) itemView.findViewById(R.id.item_exp_aca_materia);
            txtEscuela = (TextView) itemView.findViewById(R.id.item_exp_aca_escuela);
            txtGeneracion = (TextView) itemView.findViewById(R.id.item_exp_aca_generacion);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_exp_aca_delete);
        }

        public void bindConfig(final ExperienciaAcademica experienciaAcademica) {
            txtGrado.setText(experienciaAcademica.getGrado());
            txtMateria.setText(experienciaAcademica.getMateria());
            txtEscuela.setText(experienciaAcademica.getEscuela());
            txtGeneracion.setText(experienciaAcademica.getGeneracion());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultoresExpAcaActivity.this);
                    builder.setTitle("Eliminar experiencia académica");
                    builder.setMessage("¿Estas seguro de querer elimnar esta experiencia académica?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    experienciaAcademicas.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new RegistroConsultoresExpAcaActivity.DataConfigAdapter(experienciaAcademicas, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<RegistroConsultoresExpAcaActivity.DataConfigHolder> {

        private List<ExperienciaAcademica> experienciaAcademicas;
        Context ctx;

        public DataConfigAdapter(List<ExperienciaAcademica> experienciaAcademicas, Context ctx ){
            this.experienciaAcademicas = experienciaAcademicas;
            this.ctx = ctx;
        }

        @Override
        public RegistroConsultoresExpAcaActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.exp_aca_items, parent, false);
            return new RegistroConsultoresExpAcaActivity.DataConfigHolder(view, ctx, experienciaAcademicas);
        }

        @Override
        public void onBindViewHolder(final RegistroConsultoresExpAcaActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(experienciaAcademicas.get(position));

        }

        @Override
        public int getItemCount() {
            return experienciaAcademicas.size();
        }

    }

    private RegistroConsultoresExpAcaActivity.DataConfigAdapter adapter;
    private List<ExperienciaAcademica> experienciaAcademicas = new ArrayList<ExperienciaAcademica>();

    // ******************************************************************

    private RecyclerView recyclerView;
    private Button btnAnterior;
    private Button btnSiguiente;
    private FloatingActionButton fabButton;

    private Spinner spinnerGrado;
    private TextInputEditText editMateria;
    private TextInputEditText editEscuela;
    private TextInputEditText editGeneracion;

    private List<String> grados = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_consultores_exp_aca);
        setTitle("Registro Consultor");

        fabButton = (FloatingActionButton)findViewById(R.id.fabRegConsultoresExpAca);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerRegistroConsultoresExpAca);
        btnSiguiente = (Button)findViewById(R.id.btnRegistroConsultorAcademicaSiguiente);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(RegistroConsultoresExpAcaActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");

        grados.add("Seleccione un grado académico");
        grados.add("Licenciatura");
        grados.add("Maestría");
        grados.add("Doctorado");
        grados.add("Otros");

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                final String UUIDUser = user.getUid();

                if(experienciaAcademicas.size() == 0){
                    Toast.makeText(getApplicationContext(), "Debes introducir tu experiencia académica antes de seguir", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    progressDialog.show();
                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).child("ExperienciaAcademica").setValue(experienciaAcademicas).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent i = new Intent(RegistroConsultoresExpAcaActivity.this, RegistroConsultoresExpProActivity.class);
                            startActivity(i);
                        }
                    });
                }

            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultoresExpAcaActivity.this);

                // Get the layout inflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.registro_exp_aca,
                        null, false);

                spinnerGrado = (Spinner) formElementsView.findViewById(R.id.spinnerRegistroConsultorAcaGrado);
                editMateria = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorCarrera);
                editEscuela = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorEscuela);
                editGeneracion = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorGeneracion);


                ArrayAdapter<String> adapterCategoria = new ArrayAdapter<String>(
                        RegistroConsultoresExpAcaActivity.this, R.layout.support_simple_spinner_dropdown_item, grados);
                spinnerGrado.setAdapter(adapterCategoria);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String materia = editMateria.getText().toString();
                        String escuela = editEscuela.getText().toString();
                        String generacion = editGeneracion.getText().toString();
                        boolean error = false;

                        if(spinnerGrado.getSelectedItem().toString().equals("Seleccione un grado académico")){
                            Toast.makeText(getApplicationContext(), "Debes seleccionar un grado académico", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(materia.length() < 2){
                            error = true;
                        }
                        else if(escuela.length() < 2){
                            error = true;
                        }
                        else if(generacion.length() < 2){
                            error = true;
                        }

                        if(error == true){
                            Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                        }
                        else{
                            ExperienciaAcademica experienciaAcademica = new ExperienciaAcademica(spinnerGrado.getSelectedItem().toString(), materia, escuela, generacion);
                            experienciaAcademicas.add(experienciaAcademica);

                            // *********** LLENAMOS EL RECYCLER VIEW *****************************
                            adapter = new RegistroConsultoresExpAcaActivity.DataConfigAdapter(experienciaAcademicas, getApplicationContext());
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
}
