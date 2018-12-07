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
import android.widget.Button;
import android.widget.ImageView;
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
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.R;

public class RegistroReconocimientosConsulActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtAnio;
        private TextView txtOtorgado;
        private TextView txtNombre;
        private ImageView btnDelete;

        private List<Reconocimientos> reconocimientos = new ArrayList<Reconocimientos>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<Reconocimientos> reconocimientos) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.reconocimientos = reconocimientos;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.item_reconocimientos_nombre);
            txtOtorgado = (TextView) itemView.findViewById(R.id.item_reconocimientos_otorgado);
            txtAnio = (TextView) itemView.findViewById(R.id.item_reconocimientos_anio);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_reconocimientos_delete);
        }

        public void bindConfig(final Reconocimientos reconocimientos) {
            txtNombre.setText(reconocimientos.getDescripcion());
            txtOtorgado.setText(reconocimientos.getOtorgadoPor());
            txtAnio.setText(reconocimientos.getAnio());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroReconocimientosConsulActivity.this);
                    builder.setTitle("Eliminar experiencia profesional");
                    builder.setMessage("¿Estas seguro de querer elimnar este reconocimiento?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    reconocimientos.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new RegistroReconocimientosConsulActivity.DataConfigAdapter(reconocimientos, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<RegistroReconocimientosConsulActivity.DataConfigHolder> {

        private List<Reconocimientos> reconocimientos;
        Context ctx;

        public DataConfigAdapter(List<Reconocimientos> reconocimientos, Context ctx ){
            this.reconocimientos = reconocimientos;
            this.ctx = ctx;
        }

        @Override
        public RegistroReconocimientosConsulActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_reconocimientos, parent, false);
            return new RegistroReconocimientosConsulActivity.DataConfigHolder(view, ctx, reconocimientos);
        }

        @Override
        public void onBindViewHolder(final RegistroReconocimientosConsulActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(reconocimientos.get(position));

        }

        @Override
        public int getItemCount() {
            return reconocimientos.size();
        }

    }

    private RegistroReconocimientosConsulActivity.DataConfigAdapter adapter;
    private List<Reconocimientos> reconocimientos = new ArrayList<Reconocimientos>();

    // ******************************************************************

    private RecyclerView recyclerView;
    private Button btnAnterior;
    private Button btnFinalizar;
    private FloatingActionButton fabButton;

    private TextInputEditText editNombreReconocimiento;
    private TextInputEditText editAnio;
    private TextInputEditText editOtorgadoPor;
    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_reconocimientos_consul);
        setTitle("Reconocimientos");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerRegistroConsultoresReconocimientos);
        btnFinalizar = (Button)findViewById(R.id.btnRegistroConsultorReconocimientosFinalizar);
        fabButton = (FloatingActionButton)findViewById(R.id.fabRegConsultoresReconocimientos);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog = new ProgressDialog(RegistroReconocimientosConsulActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");




        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                final String UUIDUser = user.getUid();

                if(reconocimientos.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroReconocimientosConsulActivity.this);
                    builder.setTitle("Reconocimientos");
                    builder.setMessage("¿Estas seguro de querer finalizar sin agregar algún reconocimiento?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(RegistroReconocimientosConsulActivity.this, PDFCVConsultoresActivity.class);
                                    /*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("typeCurrentUser", "Consultor");
                                    editor.putString("registroCompleto", "Completo");
                                    editor.commit();*/
                                    // ******************************************************************************
                                    startActivity(i);
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
                else{
                    progressDialog.show();
                    tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).child("Reconocimientos").setValue(reconocimientos).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Intent i = new Intent(RegistroReconocimientosConsulActivity.this, PDFCVConsultoresActivity.class);
                            /*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("typeCurrentUser", "Consultor");
                            editor.putString("registroCompleto", "Completo");
                            editor.commit();*/
                            // ******************************************************************************
                            startActivity(i);
                        }
                    });
                }

            }
        });


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegistroReconocimientosConsulActivity.this);

                // Get the layout inflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.dialog_reconocimientos,
                        null, false);

                editNombreReconocimiento = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoReconocimientoDescripcion);
                editOtorgadoPor = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoReconocimientoOtorgadoPor);
                editAnio = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoRenocimientoAnio);


                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String nombre = editNombreReconocimiento.getText().toString();
                        String otorgadoPor = editOtorgadoPor.getText().toString();
                        String anio = editAnio.getText().toString();

                        boolean error = false;


                        if(nombre.length() < 2){
                            error = true;
                        }
                        else if(otorgadoPor.length() < 2){
                            error = true;
                        }
                        else if(anio.length() < 2){
                            error = true;
                        }

                        if(error == true){
                            Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Reconocimientos reconocimiento = new Reconocimientos(anio,otorgadoPor,nombre);
                            reconocimientos.add(reconocimiento);

                            // *********** LLENAMOS EL RECYCLER VIEW *****************************
                            adapter = new RegistroReconocimientosConsulActivity.DataConfigAdapter(reconocimientos, getApplicationContext());
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
