package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.R;
import projects.solucionescolabora.com.metodoasesores.RegistroActivity;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

public class RegistroConsultorActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtInteres;
        private ImageView btnDelete;

        private List<String> intereses = new ArrayList<String >();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<String > intereses) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.intereses = intereses;
            this.ctx = ctx;

            txtInteres = (TextView) itemView.findViewById(R.id.txtItemNuevaConvEsp);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_nuev_conv_delete);
        }

        public void bindConfig(final String interes) {
            txtInteres.setText(interes);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultorActivity.this);
                    builder.setTitle("Eliminar interés");
                    builder.setMessage("¿Estas seguro de querer elimnar este interés?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    intereses.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new RegistroConsultorActivity.DataConfigAdapter(intereses, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<RegistroConsultorActivity.DataConfigHolder> {

        private List<String> intereses;
        Context ctx;

        public DataConfigAdapter(List<String> intereses, Context ctx ){
            this.intereses = intereses;
            this.ctx = ctx;
        }

        @Override
        public RegistroConsultorActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_nueva_conv_esp, parent, false);
            return new RegistroConsultorActivity.DataConfigHolder(view, ctx, intereses);
        }

        @Override
        public void onBindViewHolder(final RegistroConsultorActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(intereses.get(position));

        }

        @Override
        public int getItemCount() {
            return intereses.size();
        }

    }

    private RegistroConsultorActivity.DataConfigAdapter adapter;
    // ******************************************************************

    private ImageView circleImageViewAvatar;
    private TextInputEditText editNombres;
    private TextInputEditText editApellidos;
    private TextInputEditText editRFC;
    private TextInputEditText editEdad;
    private TextInputEditText editUbicacion;
    private TextInputEditText editAnioMentor;
    private TextInputEditText editFrase;
    private TextInputEditText editCorreo;
    private RadioButton radioMasc;
    private RadioButton radioFem;
    private Button btnSiguiente;
    private RecyclerView recyclerView;
    private Button btnAgregarEsp;

    private SharedPreferences sharedPreferences;
    private String urlFoto = "";
    private String nombreFace = "";
    private String apellidoFace = "";
    private String idUser = "";
    private boolean fotoSelected = false;

    private String txtEspecialidad = "";
    private String esp = "";
    private List<String> intereses = new ArrayList<>();

    private String genero = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private ProgressDialog progressDialog;

    private List<String> estados = new ArrayList<>();
    private List<String> municipios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_consultor);
        setTitle("Registro Consultor");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        circleImageViewAvatar = (ImageView) findViewById(R.id.imgRegistroConsultorAvatar);
        editNombres = (TextInputEditText)findViewById(R.id.txtRegistroConsultorNombre);
        editApellidos = (TextInputEditText)findViewById(R.id.txtRegistroConsultorApellidos);
        editRFC = (TextInputEditText)findViewById(R.id.txtRegistroConsultorRFC);
        editEdad = (TextInputEditText)findViewById(R.id.txtRegistroConsultorEdad);
        editUbicacion = (TextInputEditText)findViewById(R.id.txtRegistroConsultorUbicacion);
        editAnioMentor = (TextInputEditText)findViewById(R.id.txtRegistroConsultorAnioMentor);
        editFrase = (TextInputEditText)findViewById(R.id.txtRegistroConsultorFrase);
        radioMasc = (RadioButton) findViewById(R.id.radioRegistroConsultorGMasc);
        radioFem = (RadioButton) findViewById(R.id.radioRegistroConsultorGFem);
        btnSiguiente = (Button)findViewById(R.id.btnRegistroConsultorPersonalesSiguiente);
        btnAgregarEsp = (Button)findViewById(R.id.btnRegistroConsultorAgregarEsp);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerRegistroConsultorEspecialidad);
        editCorreo = (TextInputEditText)findViewById(R.id.txtRegistroConsultorEmail);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        progressDialog = new ProgressDialog(RegistroConsultorActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");

        // ********************************************************************************************************
        estados.add("Yucatán");

        municipios.add("Abalá");
        municipios.add("Acancéh");
        municipios.add("Akil");
        municipios.add("Baca");
        municipios.add("Bokobá");
        municipios.add("Buctzotz");
        municipios.add("Cacalchén");
        municipios.add("Calotmul");
        municipios.add("Cansahcab");
        municipios.add("Cantamayec");
        municipios.add("Celestún");
        municipios.add("Cenotillo");
        municipios.add("Conkal");
        municipios.add("Cuncunul");
        municipios.add("Cuzamá");
        municipios.add("Chacsinkín");
        municipios.add("Chankom");
        municipios.add("Chapab");
        municipios.add("Chemax");
        municipios.add("Chicxulub Pueblo");
        municipios.add("Chichimilá");
        municipios.add("Chikindzonot");
        municipios.add("Chocholá");
        municipios.add("Chumayel");
        municipios.add("Dzan");
        municipios.add("Dzemul");
        municipios.add("Dzidzantún");
        municipios.add("Dzilam de Bravo");
        municipios.add("Dzilam González");
        municipios.add("Dzitás");
        municipios.add("Dzoncauich");
        municipios.add("Espita");
        municipios.add("Halachó");
        municipios.add("Hocabá");
        municipios.add("Hoctún");
        municipios.add("Homún");
        municipios.add("Huní");
        municipios.add("Hunucmá");
        municipios.add("Ixil");
        municipios.add("Izamal");
        municipios.add("Kanasín");
        municipios.add("Kantunil");
        municipios.add("Kaua");
        municipios.add("Kinchil");
        municipios.add("Kopomá");
        municipios.add("Mama");
        municipios.add("Maní");
        municipios.add("Maxcanú");
        municipios.add("Mayapán");
        municipios.add("Mérida");
        municipios.add("Mocochá");
        municipios.add("Motul");
        municipios.add("Muna");
        municipios.add("Muxupip");
        municipios.add("Opichén");
        municipios.add("Oxkutzcab");
        municipios.add("Panabá");
        municipios.add("Peto");
        municipios.add("Progreso");
        municipios.add("Quintana Roo");
        municipios.add("Río Lagartos");
        municipios.add("Sacalum");
        municipios.add("Samahil");
        municipios.add("Sanahcat");
        municipios.add("San Felipe");
        municipios.add("Seyé");
        municipios.add("Sinanché");
        municipios.add("Sotuta");
        municipios.add("Sucilá");
        municipios.add("Sudzal");
        municipios.add("Suma de Hidalgo");
        municipios.add("Tahziú");
        municipios.add("Tahmek");
        municipios.add("Teabo");
        municipios.add("Tecóh");
        municipios.add("Tekal de Venegas");
        municipios.add("Tekantó");
        municipios.add("Tekax");
        municipios.add("Tekit");
        municipios.add("Tekom");
        municipios.add("Telchac Pueblo");
        municipios.add("Telchac Puerto");
        municipios.add("Temax");
        municipios.add("Temozón");
        municipios.add("Tepakán");
        municipios.add("Tetiz");
        municipios.add("Teya");
        municipios.add("Ticul");
        municipios.add("Timucuy");
        municipios.add("Tinum");
        municipios.add("Tixcacalcupul");
        municipios.add("Tixkokob");
        municipios.add("Tixméhuac");
        municipios.add("Tixpéhual");
        municipios.add("Tizimín");
        municipios.add("Tunkás");
        municipios.add("Tzucacab");
        municipios.add("Uayma");
        municipios.add("Ucú");
        municipios.add("Umán");
        municipios.add("Valladolid");
        municipios.add("Xocchel");
        municipios.add("Yaxcabá");
        municipios.add("Yaxkukul");

        editUbicacion.setOnTouchListener(new View.OnTouchListener() {
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

        editUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final NumberPicker numberPicker = new NumberPicker(RegistroConsultorActivity.this);

                int size = municipios.size();
                numberPicker.setMaxValue(size - 1);
                numberPicker.setMinValue(0);

                String[] mStringArray = new String[municipios.size()];
                mStringArray = municipios.toArray(mStringArray);

                //numberPicker.setDisplayedValues(new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal y administración", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"});
                //final String[] arrayPicker= new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal y administración", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"};

                final String[] arrayPicker= mStringArray;
                numberPicker.setDisplayedValues(mStringArray);

                esp = "Abalá";

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        esp = arrayPicker[i1];
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultorActivity.this);
                builder.setView(numberPicker);
                builder.setTitle("Municipios");
                builder.setMessage("Seleccione su municipio");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editUbicacion.setText(esp + ", Yucatán");
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
        // ********************************************************************************************************
        btnAgregarEsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker numberPicker = new NumberPicker(RegistroConsultorActivity.this);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(0);
                numberPicker.setDisplayedValues(new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"});
                final String[] arrayPicker= new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"};

                esp = "Planeación estratégica";

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        esp = arrayPicker[i1];
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroConsultorActivity.this);
                builder.setView(numberPicker);
                builder.setTitle("Especialidades");
                builder.setMessage("Selecciona tu especialidad");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intereses.add(esp);
                        // *********** LLENAMOS EL RECYCLER VIEW *****************************
                        adapter = new RegistroConsultorActivity.DataConfigAdapter(intereses, getApplicationContext());
                        recyclerView.setAdapter(adapter);
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

       /* sharedPreferences = getSharedPreferences("misDatos", 0);
        urlFoto = sharedPreferences.getString("fotoCurrentUser","");
        nombreFace = sharedPreferences.getString("firstNameCurrentUser","");
        apellidoFace = sharedPreferences.getString("lastNameCurrentUser","");

        // Si el usuario de logueó por Facebook entonces cargamos su imagen de perfil y los demás datos personales
        if(urlFoto.length()>2){
            Picasso.with(getApplicationContext())
                    .load(urlFoto).into(circleImageViewAvatar, new Callback() {
                @Override
                public void onSuccess() {
                    fotoSelected = true;
                }

                @Override
                public void onError() {
                    Toast.makeText(getApplicationContext(), "No se pudo cargar la foto de Facebook", Toast.LENGTH_LONG).show();
                }
            });
        }

        if(nombreFace.length() > 2){
            editNombres.setText(nombreFace);
        }

        if(apellidoFace.length()>2){
            editApellidos.setText(apellidoFace);
        }*/
        // **************************************************************************

        final List<String> especialidades = new ArrayList<>();
        // Llenamos el spinner con los datos necesarios
        especialidades.add("Seleccione una especialidad");
        especialidades.add("Diseño digital");
        especialidades.add("Marketing");
        especialidades.add("Tecnología e innovación");
        especialidades.add("Finanzas");
        especialidades.add("Administración");
        especialidades.add("Protección intelectual");
        especialidades.add("Diseño industrial");
        especialidades.add("Turismo");
        especialidades.add("Desarrollo social");


        // Tomamos el control del cambio en el estado de los radioButtons
        radioFem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    radioMasc.setChecked(false);
                    genero = "Femenino";
                }
            }
        });

        radioMasc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b == true){
                    radioFem.setChecked(false);
                    genero = "Masculino";
                }
            }
        });

        circleImageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                        .setCameraButtonText("Cámara")
                        .setGalleryButtonText("Galería"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                circleImageViewAvatar.setImageBitmap(r.getBitmap());
                                fotoSelected = true;
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

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txtApellidos = editApellidos.getText().toString();
                final String txtNombres = editNombres.getText().toString();
                final String txtRFC = editRFC.getText().toString();
                final String txtEdad = editEdad.getText().toString();
                final String txtUbicacion = editUbicacion.getText().toString();
                final String txtAnioMentor = editAnioMentor.getText().toString();
                final String txtFrase = editFrase.getText().toString();
                final String txtCorreo = editCorreo.getText().toString();

                boolean error = false;

                if(intereses.size() == 0){
                    Toast.makeText(getApplicationContext(), "Debes seleccionar por lo menos una especialidad", Toast.LENGTH_LONG).show();
                    return;
                }

                if(txtNombres.length() < 2){
                    error = true;
                    editNombres.setError("Debes llenar este campo");
                }
                else if(txtApellidos.length() < 2){
                    error = true;
                    editApellidos.setError("Debes llenar este campo");
                }
                else if(txtRFC.length() < 2){
                    error = true;
                    editRFC.setError("Debes llenar este campo");
                }
                else if(txtEdad.length() < 2){
                    error = true;
                    editEdad.setError("Debes llenar este campo");
                }
                else if(txtUbicacion.length() < 2){
                    error = true;
                    editUbicacion.setError("Debes llenar este campo");
                }
                else if(genero.length() < 2){
                    error = true;
                }
                else if(txtCorreo.length() < 2){
                    error = true;
                    editCorreo.setError("Debes llenar este campo");
                }
                else if(fotoSelected == false){
                    error = true;
                    Toast.makeText(getApplicationContext(), "Debes subir una foto", Toast.LENGTH_LONG).show();
                    return;
                }

                if(error == true){
                    Toast.makeText(getApplicationContext(), "Debes llenar los campos obligatorios para continuar", Toast.LENGTH_LONG).show();
                }
                else{

                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                    final String UUIDUser = user.getUid();

                    circleImageViewAvatar.setDrawingCacheEnabled(true);
                    circleImageViewAvatar.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                    circleImageViewAvatar.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(circleImageViewAvatar.getDrawingCache());

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 30, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(UUIDUser + "-profile");

                    UploadTask uploadTask = refSubirImagen.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            for (int i = 0; i < intereses.size(); i++){
                                if (intereses.get(i).equals("Tecnología e innovación")){
                                    txtEspecialidad = "TecnologiaInnovacion";
                                }
                                else if (intereses.get(i).equals("Planeación estratégica")){
                                    txtEspecialidad = "PlaneacionEstrategica";
                                }
                                else if (intereses.get(i).equals("Diseño")){
                                    txtEspecialidad = "Diseno";
                                }
                                else if (intereses.get(i).equals("Marketing")){
                                    txtEspecialidad = "Marketing";
                                }
                                else if (intereses.get(i).equals("Finanzas")){
                                    txtEspecialidad = "Finanzas";
                                }
                                else if (intereses.get(i).equals("Administración")){
                                    txtEspecialidad = "Administracion";
                                }
                                else if (intereses.get(i).equals("Legal y administración")){
                                    txtEspecialidad = "Legal";
                                }
                                else if (intereses.get(i).equals("Turismo")){
                                    txtEspecialidad = "Turismo";
                                }
                                else if (intereses.get(i).equals("Responsabilidad social")){
                                    txtEspecialidad = "Responsabilidad";
                                }
                                else if (intereses.get(i).equals("Procuración de recursos")){
                                    txtEspecialidad = "Procuracion";
                                }
                                else if (intereses.get(i).equals("Comercio")){
                                    txtEspecialidad = "Comercio";
                                }

                                FirebaseMessaging.getInstance().subscribeToTopic("consultor_" + txtEspecialidad);

                            }

                            FirebaseMessaging.getInstance().subscribeToTopic("consultor");
                            Toast.makeText(getApplicationContext(), "Foto subida con exito", Toast.LENGTH_SHORT).show();
                            List<ExperienciaProfesional> experienciaProfesional = new ArrayList<>();
                            List<ExperienciaAcademica> experienciaAcademica = new ArrayList<>();
                            List<Reconocimientos> reconocimientos = new ArrayList<>();
                            Consultor newConsultor = new Consultor(UUIDUser,txtNombres,txtApellidos,txtRFC,txtAnioMentor,genero,txtEdad,intereses,UUIDUser + "-profile",txtUbicacion,txtFrase,experienciaAcademica,experienciaProfesional,reconocimientos,0.0,"", txtCorreo);

                            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).child(UUIDUser).setValue(newConsultor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Intent i = new Intent(RegistroConsultorActivity.this, RegistroConsultoresExpAcaActivity.class);
                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("typeCurrentUser", "Consultor");
                                    editor.putString("registroCompleto", "Incompleto");
                                    editor.commit();
                                    // ******************************************************************************
                                    startActivity(i);
                                }
                            });
                        }
                    });
                }
            }
        });

    }


}
