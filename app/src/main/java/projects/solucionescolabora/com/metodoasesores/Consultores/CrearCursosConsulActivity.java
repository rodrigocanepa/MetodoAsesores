package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Instituciones.CrearConvocatoriaMainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.Curso;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.Modelos.Temas;
import projects.solucionescolabora.com.metodoasesores.Modelos.TemplatePDF;
import projects.solucionescolabora.com.metodoasesores.R;

public class CrearCursosConsulActivity extends AppCompatActivity {


    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTema;
        private TextView txtSubtemas;
        private TextView txtDuracion;
        private TextView txtMateriales;
        private ImageView btnDelete;
        private ImageView btnEdit;

        private List<Temas> temas = new ArrayList<Temas>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<Temas> temas) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.temas = temas;
            this.ctx = ctx;

            txtTema = (TextView) itemView.findViewById(R.id.item_txtNuevoCursoTema);
            txtSubtemas = (TextView) itemView.findViewById(R.id.item_txtNuevoCursoSubtemas);
            txtDuracion = (TextView) itemView.findViewById(R.id.item_txtNuevoCursoDuracion);
            txtMateriales = (TextView) itemView.findViewById(R.id.item_txtNuevoCursoMateriales);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_nuevo_curso_delete);
            btnEdit = (ImageView) itemView.findViewById(R.id.item_nuevo_curso_editar);
        }

        public void bindConfig(final Temas temas) {
            txtTema.setText(temas.getTema());
            txtSubtemas.setText(temas.getSubtemas());
            txtMateriales.setText(temas.getMateriales());
            txtDuracion.setText(temas.getDuracion());
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrearCursosConsulActivity.this);
                    builder.setTitle("Eliminar tema");
                    builder.setMessage("¿Estas seguro de querer elimnar este tema?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    temas.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new CrearCursosConsulActivity.DataConfigAdapter(temas, getApplicationContext());
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

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CrearCursosConsulActivity.this);

                    // Get the layout inflater
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View formElementsView = inflater.inflate(R.layout.dialog_temas,
                            null, false);

                    editTema = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoTema);
                    editSubtema = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoSubtema);
                    editDuracion = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoDuracion);
                    editMateriales = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoMateriales);

                    editTema.setText(temas.get(position).getTema());
                    editSubtema.setText(temas.get(position).getSubtemas());
                    editDuracion.setText(temas.get(position).getDuracion());
                    editMateriales.setText(temas.get(position).getMateriales());

                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            String tema = editTema.getText().toString();
                            String subtema = editSubtema.getText().toString();
                            String duracion = editDuracion.getText().toString();
                            String materiales = editMateriales.getText().toString();

                            boolean error = false;

                            if(tema.length() < 2){
                                error = true;
                            }
                            else if(subtema.length() < 2){
                                error = true;
                            }
                            else if(duracion.length() < 1){
                                error = true;
                            }
                            else if(materiales.length() < 2){
                                error = true;
                            }

                            if(error == true){
                                Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Temas tema_ = new Temas(tema,subtema,duracion,materiales);
                                temas.remove(position);
                                temas.add(tema_);

                                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                adapter = new CrearCursosConsulActivity.DataConfigAdapter(temas, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<CrearCursosConsulActivity.DataConfigHolder> {

        private List<Temas> temas;
        Context ctx;

        public DataConfigAdapter(List<Temas> temas, Context ctx ){
            this.temas = temas;
            this.ctx = ctx;
        }

        @Override
            public CrearCursosConsulActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_nuevo_curso, parent, false);
            return new CrearCursosConsulActivity.DataConfigHolder(view, ctx, temas);
        }

        @Override
        public void onBindViewHolder(final CrearCursosConsulActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(temas.get(position));

        }

        @Override
        public int getItemCount() {
            return temas.size();
        }

    }

    private CrearCursosConsulActivity.DataConfigAdapter adapter;
    // ******************************************************************

    private TextInputEditText editTitutlo;
    private TextInputEditText editDescripcion;
    private TextInputEditText editDuracionTotal;
    private TextInputEditText editObjetivo;
    private TextInputEditText editRequerimientos;
    private Spinner spinnerEspecialidad;
    private List<Temas> temas = new ArrayList<>();
    private Button btnAgregarTemas;
    private Button btnFinalizar;
    private RecyclerView recyclerView;

    private String titulo;
    private String descripcion;
    private String duracion;
    private String objetivo;
    private String requerimientos;
    private String especialidad;
    private List<String> especialidades = new ArrayList<>();
    private String urlConsultor;
    private String idConsultor;
    private String nombreConsultor;

    private TextInputEditText editTema;
    private TextInputEditText editSubtema;
    private TextInputEditText editDuracion;
    private TextInputEditText editMateriales;

    public static final String INTENT_ID_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.ID_CONSULTOR";
    public static final String INTENT_URL_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.URL_CONSULTOR";
    public static final String INTENT_NOMBRE_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Consultores.NOMBRE_CONSULTOR";

    public static final String INTENT_TITULO = "projects.solucionescolabora.com.metodoasesores.Consultores.TITULO";
    public static final String INTENT_DESCRIPCION = "projects.solucionescolabora.com.metodoasesores.Consultores.DESCRIPCION";
    public static final String INTENT_DURACION = "projects.solucionescolabora.com.metodoasesores.Consultores.DURACION";
    public static final String INTENT_OBJETIVO = "projects.solucionescolabora.com.metodoasesores.Consultores.OBJETIVO";
    public static final String INTENT_REQUERIMIENTOS = "projects.solucionescolabora.com.metodoasesores.Consultores.REQUERIMIENTOS";

    public static final String INTENT_IDCURSO = "projects.solucionescolabora.com.metodoasesores.Consultores.IDCURSO";

    private String titulo_;
    private String descripcion_;
    private String duracion_;
    private String objetivo_;
    private String requerimientos_;
    private String id_;

    // GENERACION DE PDF
    private TemplatePDF templatePDF;
    private String formattedDate ="";
    private String[]headerPreguntas = {"Temas", "Subtemas", "Materiales","Duración"};

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cursos_consul);
        setTitle("Nuevo Curso");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editTitutlo = (TextInputEditText)findViewById(R.id.txtNuevoCursoTitulo);
        editDescripcion = (TextInputEditText)findViewById(R.id.txtNuevoCursoDescripcion);
        editDuracionTotal = (TextInputEditText)findViewById(R.id.txtNuevoCursoDuracionTotal);
        editObjetivo = (TextInputEditText)findViewById(R.id.txtNuevoCursoObjetivo);
        editRequerimientos = (TextInputEditText)findViewById(R.id.txtNuevoCursoRequerimientos);
        spinnerEspecialidad = (Spinner)findViewById(R.id.spinnerNuevoCursoEspecialidad);
        btnAgregarTemas = (Button)findViewById(R.id.btnNuevoCursoTema);
        btnFinalizar = (Button)findViewById(R.id.btnNuevoCursoFinalizar);
        recyclerView = (RecyclerView)findViewById(R.id.recycleNuevoCursoTemas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(CrearCursosConsulActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");

        final List<String> especialidades = new ArrayList<>();
        // Llenamos el spinner con los datos necesarios
        especialidades.add("Seleccione una especialidad");
        especialidades.add("Planeación estratégica");
        especialidades.add("Diseño");
        especialidades.add("Marketing");
        especialidades.add("Tecnología e innovación");
        especialidades.add("Finanzas");
        especialidades.add("Administración");
        especialidades.add("Legal");
        especialidades.add("Turismo");
        especialidades.add("Desarrollo social");
        especialidades.add("Procuración de recursos");
        especialidades.add("Comercio");

        Intent i = getIntent();
        urlConsultor = i.getStringExtra(INTENT_URL_CONSULTOR);
        idConsultor = i.getStringExtra(INTENT_ID_CONSULTOR);
        nombreConsultor = i.getStringExtra(INTENT_NOMBRE_CONSULTOR);

        titulo_ = i.getStringExtra(INTENT_TITULO);
        descripcion_ = i.getStringExtra(INTENT_DESCRIPCION);
        duracion_ = i.getStringExtra(INTENT_DURACION);
        objetivo_ = i.getStringExtra(INTENT_OBJETIVO);
        requerimientos_ = i.getStringExtra(INTENT_REQUERIMIENTOS);
        id_ = i.getStringExtra(INTENT_IDCURSO);

        if (titulo_ != null){
            editTitutlo.setText(titulo_);
            editDescripcion.setText(descripcion_);
            editDuracionTotal.setText(duracion_);
            editObjetivo.setText(objetivo_);
            editRequerimientos.setText(requerimientos_);
        }

        android.widget.SpinnerAdapter adapterSpinner = new projects.solucionescolabora.com.metodoasesores.Consultores.SpinnerAdapter (especialidades, CrearCursosConsulActivity.this);
        spinnerEspecialidad.setAdapter(adapterSpinner);

        editDuracionTotal.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.onTouchEvent(motionEvent);
                InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }

        });

        editDuracionTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker numberPicker = new NumberPicker(CrearCursosConsulActivity.this);
                numberPicker.setMaxValue(19);
                numberPicker.setMinValue(0);
                numberPicker.setDisplayedValues(new String[]{"1 hora", "2 horas", "3 horas", "4 horas","5 horas","6 horas","7 horas", "8 horas", "9 horas", "10 horas", "11 horas", "12 horas", "13 horas", "14 horas", "15 horas", "16 horas", "17 horas", "18 horas", "19 horas", "20 horas"});
                final String[] arrayPicker= new String[]{"1 hora", "2 horas", "3 horas", "4 horas","5 horas","6 horas","7 horas", "8 horas", "9 horas", "10 horas", "11 horas", "12 horas", "13 horas", "14 horas", "15 horas", "16 horas", "17 horas", "18 horas", "19 horas", "20 horas"};

                duracion = "1 hora";

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        duracion = arrayPicker[i1];
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(CrearCursosConsulActivity.this);
                builder.setView(numberPicker);
                builder.setTitle("Curso");
                builder.setMessage("Selecciona la duración de tu curso");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editDuracionTotal.setText(duracion);
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

        btnAgregarTemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(CrearCursosConsulActivity.this);

                // Get the layout inflater
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View formElementsView = inflater.inflate(R.layout.dialog_temas,
                        null, false);

                editTema = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoTema);
                editSubtema = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoSubtema);
                editDuracion = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoDuracion);
                editMateriales = (TextInputEditText) formElementsView.findViewById(R.id.dialog_txtNuevoCursoMateriales);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String tema = editTema.getText().toString();
                        String subtema = editSubtema.getText().toString();
                        String duracion = editDuracion.getText().toString();
                        String materiales = editMateriales.getText().toString();

                        boolean error = false;

                        if(tema.length() < 2){
                            error = true;
                        }
                        else if(subtema.length() < 2){
                            error = true;
                        }
                        else if(duracion.length() < 2){
                            error = true;
                        }
                        else if(materiales.length() < 2){
                            error = true;
                        }

                        if(error == true){
                            Toast.makeText(getApplicationContext(), "Debes llenar los campos requeridos", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Temas tema_ = new Temas(tema,subtema,duracion,materiales);
                            temas.add(tema_);

                            // *********** LLENAMOS EL RECYCLER VIEW *****************************
                            adapter = new CrearCursosConsulActivity.DataConfigAdapter(temas, getApplicationContext());
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

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean error = false;

                titulo = editTitutlo.getText().toString();
                descripcion = editDescripcion.getText().toString();
                duracion = editDuracionTotal.getText().toString();
                objetivo = editObjetivo.getText().toString();
                requerimientos = editRequerimientos.getText().toString();
                especialidad = spinnerEspecialidad.getSelectedItem().toString();

                if (titulo.length() < 2){
                    error = true;
                }
                else if(descripcion.length() < 2){
                    error = true;
                }
                else if(duracion.length() < 1){
                    error = true;
                }
                else if(objetivo.length() < 2){
                    error = true;
                }
                else if(requerimientos.length() < 2){
                    error = true;
                }
                else if(temas.size() == 0){
                    error = true;
                }
                else if(especialidad.equals("Seleccione una especialidad")){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar una especialidad", Toast.LENGTH_LONG).show();
                    return;
                }

                if(error == true){
                    Toast.makeText(getApplicationContext(), "Debes llenar los campos obligatorios para finalizar", Toast.LENGTH_LONG).show();
                }

                else{
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                    final String UUIDUser = user.getUid();

                    String id = tutorialRef.child(FirebaseReferences.CURSOS_REFERENCE).push().getKey();

                    Curso curso = new Curso(id,titulo,descripcion,duracion,especialidad,requerimientos,objetivo,temas,idConsultor,nombreConsultor,urlConsultor);

                    if (titulo_ != null){
                        tutorialRef.child(FirebaseReferences.CURSOS_REFERENCE).child(id_).setValue(curso).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());

                                ArrayList<String[]> temas_ = new ArrayList<>();
                                for(int i = 0; i < temas.size(); i ++){
                                    temas_.add(new String[]{temas.get(i).getTema(), temas.get(i).getSubtemas(), temas.get(i).getDuracion(), temas.get(i).getMateriales()});
                                }
                                templatePDF = new TemplatePDF(getApplicationContext());
                                templatePDF.openDocument();
                                templatePDF.addMetaData("Curso", "Metodo Asesores", "Soluciones Colabora");
                                templatePDF.addImage(getApplicationContext());
                                templatePDF.addTitles("Curso: " + titulo, descripcion, especialidad);

                                templatePDF.addSections("Objetivo");
                                templatePDF.addParagraph(objetivo);

                                templatePDF.addSections("Temas");
                                templatePDF.createTableWith4cell(headerPreguntas, temas_);

                                templatePDF.addSections("Duración total");
                                templatePDF.addParagraph(duracion);

                                templatePDF.addSections("Requerimientos");
                                templatePDF.addParagraph(requerimientos);

                                templatePDF.closeDocument();

                                templatePDF.viewPDF("Curso%" + titulo);
                                finish();
                            /*Intent i = new Intent(CrearCursosConsulActivity.this, VerMisCursosActivity.class);
                            finish();
                            startActivity(i);*/
                            }
                        });
                    }
                    else{
                        tutorialRef.child(FirebaseReferences.CURSOS_REFERENCE).child(id).setValue(curso).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());

                                ArrayList<String[]> temas_ = new ArrayList<>();
                                for(int i = 0; i < temas.size(); i ++){
                                    temas_.add(new String[]{temas.get(i).getTema(), temas.get(i).getSubtemas(), temas.get(i).getDuracion(), temas.get(i).getMateriales()});
                                }
                                templatePDF = new TemplatePDF(getApplicationContext());
                                templatePDF.openDocument();
                                templatePDF.addMetaData("Curso", "Metodo Asesores", "Soluciones Colabora");
                                templatePDF.addImage(getApplicationContext());
                                templatePDF.addTitles("Curso: " + titulo, descripcion, especialidad);

                                templatePDF.addSections("Objetivo");
                                templatePDF.addParagraph(objetivo);

                                templatePDF.addSections("Temas");
                                templatePDF.createTableWith4cell(headerPreguntas, temas_);

                                templatePDF.addSections("Duración total");
                                templatePDF.addParagraph(duracion);

                                templatePDF.addSections("Requerimientos");
                                templatePDF.addParagraph(requerimientos);

                                templatePDF.closeDocument();

                                templatePDF.viewPDF("Curso%" + titulo);
                                finish();
                            /*Intent i = new Intent(CrearCursosConsulActivity.this, VerMisCursosActivity.class);
                            finish();
                            startActivity(i);*/
                            }
                        });
                    }

                    }


            }
        });


    }
}
