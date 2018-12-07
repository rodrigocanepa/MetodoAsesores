package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class RegistroConsultoresExpProActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombreEmpresa;
        private TextView txtPeriodo;
        private TextView txtPuesto;
        private TextView txtDescripcion;
        private TextView txtUbicacion;
        private ImageView btnDelete;

        private List<ExperienciaProfesional> experienciaProfesionals = new ArrayList<ExperienciaProfesional>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<ExperienciaProfesional> experienciaProfesionals) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.experienciaProfesionals = experienciaProfesionals;
            this.ctx = ctx;

            txtNombreEmpresa = (TextView) itemView.findViewById(R.id.item_exp_prof_nombre);
            txtPeriodo = (TextView) itemView.findViewById(R.id.item_exp_prof_periodo);
            txtPuesto = (TextView) itemView.findViewById(R.id.item_exp_pro_puesto);
            txtDescripcion = (TextView) itemView.findViewById(R.id.item_exp_prof_descripcion);
            txtUbicacion = (TextView)itemView.findViewById(R.id.item_exp_prof_ubicacion);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_exp_prof_delete);
        }

        public void bindConfig(final ExperienciaProfesional experienciaProfesional) {
            txtNombreEmpresa.setText(experienciaProfesional.getNombreEmpresa());
            txtPeriodo.setText(experienciaProfesional.getPeriodo());
            txtPuesto.setText(experienciaProfesional.getPuesto());
            txtDescripcion.setText(experienciaProfesional.getDescripcion());
            txtUbicacion.setText(experienciaProfesional.getUbicacion());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultoresExpProActivity.this);
                    builder.setTitle("Eliminar experiencia profesional");
                    builder.setMessage("¿Estas seguro de querer elimnar esta experiencia profesional?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    experienciaProfesionals.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new RegistroConsultoresExpProActivity.DataConfigAdapter(experienciaProfesionals, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<RegistroConsultoresExpProActivity.DataConfigHolder> {

        private List<ExperienciaProfesional> experienciaProfesionals;
        Context ctx;

        public DataConfigAdapter(List<ExperienciaProfesional> experienciaProfesionals, Context ctx ){
            this.experienciaProfesionals = experienciaProfesionals;
            this.ctx = ctx;
        }

        @Override
        public RegistroConsultoresExpProActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.items_exp_prof, parent, false);
            return new RegistroConsultoresExpProActivity.DataConfigHolder(view, ctx, experienciaProfesionals);
        }

        @Override
        public void onBindViewHolder(final RegistroConsultoresExpProActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(experienciaProfesionals.get(position));

        }

        @Override
        public int getItemCount() {
            return experienciaProfesionals.size();
        }

    }

    private RegistroConsultoresExpProActivity.DataConfigAdapter adapter;
    private List<ExperienciaProfesional> experienciaProfesionals = new ArrayList<ExperienciaProfesional>();

    // ******************************************************************

    private RecyclerView recyclerView;
    private Button btnAnterior;
    private Button btnFinalizar;
    private FloatingActionButton fabButton;

    private TextInputEditText editNombreEmpresa;
    private TextInputEditText editPeriodo;
    private TextInputEditText editPuesto;
    private TextInputEditText editDescripcion;
    private TextInputEditText editUbicacion;

    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_consultores_exp_pro);
        setTitle("Registro Consultor");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerRegistroConsultoresExpPro);
        btnFinalizar = (Button)findViewById(R.id.btnRegistroConsultorProfesionalFinalizar);
        fabButton = (FloatingActionButton)findViewById(R.id.fabRegConsultoresExpPro);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(RegistroConsultoresExpProActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");


        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                final String UUIDUser = user.getUid();

                if(experienciaProfesionals.size() == 0){
                    Toast.makeText(getApplicationContext(), "Debes introducir tu experiencia profesional antes de finalizar", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    progressDialog.show();
                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).child("ExperienciaProfesional").setValue(experienciaProfesionals).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            /*
                            Intent i = new Intent(RegistroConsultoresExpProActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("typeCurrentUser", "Consultor");
                            editor.putString("registroCompleto", "Completo");
                            editor.commit();*/
                            // ******************************************************************************
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent i = new Intent(RegistroConsultoresExpProActivity.this, RegistroReconocimientosConsulActivity.class);
                            startActivity(i);
                        }
                    });
                }

            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultoresExpProActivity.this);

                // Get the layout inflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.registro_cons_exp_prof,
                        null, false);

                editNombreEmpresa = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorNombreEmpresa);
                editPeriodo = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorPeriodoEmpresa);
                editPuesto = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorPuestoEmpresa);
                editDescripcion = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorDescripcionEmpresa);
                editUbicacion = (TextInputEditText) formElementsView.findViewById(R.id.txtRegistroConsultorUbicacionEmpresa);


                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String nombreEmpresa = editNombreEmpresa.getText().toString();
                        String periodo = editPeriodo.getText().toString();
                        String puesto = editPuesto.getText().toString();
                        String descripcion = editDescripcion.getText().toString();
                        String ubicacion = editUbicacion.getText().toString();

                        boolean error = false;


                        if(nombreEmpresa.length() < 2){
                            error = true;
                        }
                        else if(periodo.length() < 2){
                            error = true;
                        }
                        else if(puesto.length() < 2){
                            error = true;
                        }
                        else if(descripcion.length() < 2){
                            error = true;
                        }
                        else if(ubicacion.length() < 2){
                            error = true;
                        }

                        if(error == true){
                            Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                        }
                        else{
                            ExperienciaProfesional experienciaProfesional = new ExperienciaProfesional(nombreEmpresa,periodo,puesto,descripcion,ubicacion);
                            experienciaProfesionals.add(experienciaProfesional);

                            // *********** LLENAMOS EL RECYCLER VIEW *****************************
                            adapter = new RegistroConsultoresExpProActivity.DataConfigAdapter(experienciaProfesionals, getApplicationContext());
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
