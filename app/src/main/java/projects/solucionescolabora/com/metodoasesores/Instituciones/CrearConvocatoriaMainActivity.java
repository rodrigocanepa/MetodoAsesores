package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class CrearConvocatoriaMainActivity extends AppCompatActivity{

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtEspecialidad;
        private ImageView btnDelete;

        private List<String> especialidades = new ArrayList<String>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, List<String> especialidades) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.especialidades = especialidades;
            this.ctx = ctx;

            txtEspecialidad = (TextView) itemView.findViewById(R.id.txtItemNuevaConvEsp);
            btnDelete = (ImageView) itemView.findViewById(R.id.item_nuev_conv_delete);
        }

        public void bindConfig(final String especialidad) {
            txtEspecialidad.setText(especialidad);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CrearConvocatoriaMainActivity.this);
                    builder.setTitle("Eliminar experiencia profesional");
                    builder.setMessage("¿Estas seguro de querer elimnar esta experiencia profesional(" + especialidades.get(position) + ")?")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    especialidades.remove(position);
                                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                    adapter = new CrearConvocatoriaMainActivity.DataConfigAdapter(especialidades, getApplicationContext());
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


    private class DataConfigAdapter extends RecyclerView.Adapter<CrearConvocatoriaMainActivity.DataConfigHolder> {

        private List<String> especialidades;
        Context ctx;

        public DataConfigAdapter(List<String> especialidades, Context ctx ){
            this.especialidades = especialidades;
            this.ctx = ctx;
        }

        @Override
        public CrearConvocatoriaMainActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_nueva_conv_esp, parent, false);
            return new CrearConvocatoriaMainActivity.DataConfigHolder(view, ctx, especialidades);
        }

        @Override
        public void onBindViewHolder(final CrearConvocatoriaMainActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(especialidades.get(position));

        }

        @Override
        public int getItemCount() {
            return especialidades.size();
        }

    }

    private CrearConvocatoriaMainActivity.DataConfigAdapter adapter;
    private List<String> especialidades = new ArrayList<String>();
    private RecyclerView recyclerView;

    // ******************************************************************
    private Button btnAgregar;
    private Button btnFinalizar;
    private TextInputEditText editTitulo;
    private TextInputEditText editDescripcion;
    private TextInputEditText editUbicacion;
    private TextInputEditText editLocacion;
    private TextInputEditText editDiaConfirmar;
    private TextInputEditText editDiaEvento;
    private TextInputEditText editPrecio;

    private String titulo;
    private String descripcion;
    private String ubicacion;
    private String locacion;
    private String diaConfirmar;
    private String diaEvento;
    private String precio;

    private String idInstitucion;
    private String urlInstitucion;
    private String nombreInstitucion;
    private String esp = "";

    private List<String> estados = new ArrayList<>();
    private List<String> municipios = new ArrayList<>();
    private ProgressDialog progressDialog;

    public static final String INTENT_ID_INSTITUCION = "projects.solucionescolabora.com.metodoasesores.Instituciones.ID_INSTITUCION";
    public static final String INTENT_URL_INSTITUCION = "projects.solucionescolabora.com.metodoasesores.Instituciones.URL_INSTITUCION";
    public static final String INTENT_NOMBRE_INSTITUCION = "projects.solucionescolabora.com.metodoasesores.Instituciones.NOMBRE_INSTITUCION";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();
    private static final String CERO = "0";
    private static final String BARRA = "/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_convocatoria_main);
        setTitle("Nueva Convocatoria");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        btnAgregar = (Button)findViewById(R.id.btnNuevaConvAgregarEsp);
        btnFinalizar = (Button)findViewById(R.id.btnNuevaConvFinalizar);
        editTitulo = (TextInputEditText)findViewById(R.id.txtNuevaConvTitulo);
        editDescripcion = (TextInputEditText)findViewById(R.id.txtNuevaConvDescripcion);
        editUbicacion = (TextInputEditText)findViewById(R.id.txtNuevaConvUbicacion);
        editLocacion = (TextInputEditText)findViewById(R.id.txtNuevaConvLocacion);
        editDiaConfirmar = (TextInputEditText)findViewById(R.id.txtNuevaConvFechaConfirmar);
        editDiaEvento = (TextInputEditText)findViewById(R.id.txtNuevaConvDiaEvento);
        editPrecio = (TextInputEditText)findViewById(R.id.txtNuevaConvPrecio);
        recyclerView = (RecyclerView)findViewById(R.id.recycleNuevaConvocatoriaEsp);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent i = getIntent();
        idInstitucion = i.getStringExtra(INTENT_ID_INSTITUCION);
        urlInstitucion = i.getStringExtra(INTENT_URL_INSTITUCION);
        nombreInstitucion = i.getStringExtra(INTENT_NOMBRE_INSTITUCION);

        progressDialog = new ProgressDialog(CrearConvocatoriaMainActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");


        //Variables para obtener la hora hora
        final int hora = c.get(Calendar.HOUR_OF_DAY);
        final int minuto = c.get(Calendar.MINUTE);

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


        editDiaConfirmar.setOnTouchListener(new View.OnTouchListener() {
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

        editDiaEvento.setOnTouchListener(new View.OnTouchListener() {
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
                final NumberPicker numberPicker = new NumberPicker(CrearConvocatoriaMainActivity.this);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(CrearConvocatoriaMainActivity.this);
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

        editDiaConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Calendario para obtener fecha & hora
                final Calendar c = Calendar.getInstance();

                //Variables para obtener la fecha
                final int mes = c.get(Calendar.MONTH);
                final int dia = c.get(Calendar.DAY_OF_MONTH);
                final int anio = c.get(Calendar.YEAR);

                DatePickerDialog recogerFecha = new DatePickerDialog(CrearConvocatoriaMainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                        final int mesActual = month + 1;
                        //Formateo el día obtenido: antepone el 0 si son menores de 10
                        String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        //Formateo el mes obtenido: antepone el 0 si son menores de 10
                        String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                        //Muestro la fecha con el formato deseado
                        editDiaConfirmar.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);


                    }
                    //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                    /**
                     *También puede cargar los valores que usted desee
                     */
                },anio, mes, dia);
                //Muestro el widget
                recogerFecha.show();
            }
        });

        editDiaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editDiaConfirmar.getText().toString().length() > 0){

                    //Calendario para obtener fecha & hora
                    final Calendar c = Calendar.getInstance();

                    //Variables para obtener la fecha
                    final int mes = c.get(Calendar.MONTH);
                    final int dia = c.get(Calendar.DAY_OF_MONTH);
                    final int anio = c.get(Calendar.YEAR);

                    DatePickerDialog recogerFecha = new DatePickerDialog(CrearConvocatoriaMainActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, final int year, int month, int dayOfMonth) {
                            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                            final int mesActual = month + 1;
                            //Formateo el día obtenido: antepone el 0 si son menores de 10
                            final String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                            //Formateo el mes obtenido: antepone el 0 si son menores de 10
                            final String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                            //Muestro la fecha con el formato deseado
                            //editDiaConfirmar.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);

                            TimePickerDialog recogerHora = new TimePickerDialog(CrearConvocatoriaMainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    //Formateo el hora obtenido: antepone el 0 si son menores de 10
                                    String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                                    //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                                    String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                                    //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                                    //Muestro la hora con el formato deseado
                                    editDiaEvento.setText(diaFormateado + BARRA + mesFormateado + BARRA + year + " a las " + horaFormateada + ":" + minutoFormateado + " horas");
                                }
                                //Estos valores deben ir en ese orden
                                //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
                                //Pero el sistema devuelve la hora en formato 24 horas
                            }, hora, minuto, false);

                            recogerHora.show();


                        }
                        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
                        /**
                         *También puede cargar los valores que usted desee
                         */
                    },anio, mes, dia);
                    //Muestro el widget

                    Date date;
                    long startDate = 0;
                    try {
                        String dateString = editDiaConfirmar.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        date = sdf.parse(dateString);

                        startDate = date.getTime();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    recogerFecha.getDatePicker().setMinDate(startDate);
                    recogerFecha.show();

                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes seleccionar primero la fecha máxima de postulación", Toast.LENGTH_LONG).show();
                }

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker numberPicker = new NumberPicker(CrearConvocatoriaMainActivity.this);
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(0);
                numberPicker.setDisplayedValues(new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal y administración", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"});
                final String[] arrayPicker= new String[]{"Planeación estratégica", "Diseño", "Marketing", "Tecnología e innovación", "Finanzas","Administración","Legal y administración", "Turismo", "Responsabilidad social", "Procuración de recursos", "Comercio"};

                esp = "Planeación estratégica";

                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        esp = arrayPicker[i1];
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(CrearConvocatoriaMainActivity.this);
                builder.setView(numberPicker);
                builder.setTitle("Especialidades");
                builder.setMessage("Selecciona la especialidad requerida");
                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        especialidades.add(esp);
                        // *********** LLENAMOS EL RECYCLER VIEW *****************************
                        adapter = new CrearConvocatoriaMainActivity.DataConfigAdapter(especialidades, getApplicationContext());
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

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titulo = editTitulo.getText().toString();
                descripcion = editDescripcion.getText().toString();
                ubicacion = editUbicacion.getText().toString();
                locacion = editLocacion.getText().toString();
                diaConfirmar = editDiaConfirmar.getText().toString();
                diaEvento = editDiaEvento.getText().toString();
                precio = editPrecio.getText().toString();

                boolean error = false;

                if(titulo.length() < 2){
                    error = true;
                }
                else if(descripcion.length() < 2){
                    error = true;
                }
                else if(ubicacion.length() < 2){
                    error = true;
                }
                else if(diaConfirmar.length() < 2){
                    error = true;
                }
                else if(diaEvento.length() < 2){
                    error = true;
                }
                else if(especialidades.size() == 0){
                    error = true;
                }

                if (error == true){
                    Toast.makeText(getApplicationContext(), "Debes llenar los campos obligatorios", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                    final String UUIDUser = user.getUid();

                    List<ConsultorConv> postulados = new ArrayList<>();
                    List<ConsultorConv> consultores = new ArrayList<>();

                    final String id = tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).push().getKey();

                    Convocatoria convocatoria = new Convocatoria(id, titulo, descripcion, ubicacion, locacion, 0, "","","",consultores,"",especialidades,diaConfirmar,diaEvento,"abierta",idInstitucion,urlInstitucion,nombreInstitucion,postulados, precio);
                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id).setValue(convocatoria).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            for(int i = 0; i < especialidades.size(); i++){
                                if (especialidades.get(i).equals("Tecnología e innovación")){
                                    esp = "TecnologiaInnovacion";
                                }
                                else if (especialidades.get(i).equals("Planeación estratégica")){
                                    esp = "PlaneacionEstrategica";
                                }
                                else if (especialidades.get(i).equals("Diseño")){
                                    esp = "Diseno";
                                }
                                else if (especialidades.get(i).equals("Marketing")){
                                    esp = "Marketing";
                                }
                                else if (especialidades.get(i).equals("Finanzas")){
                                    esp = "Finanzas";
                                }
                                else if (especialidades.get(i).equals("Administración")){
                                    esp = "Administracion";
                                }
                                else if (especialidades.get(i).equals("Legal y administración")){
                                    esp = "Legal";
                                }
                                else if (especialidades.get(i).equals("Turismo")){
                                    esp = "Turismo";
                                }
                                else if (especialidades.get(i).equals("Responsabilidad social")){
                                    esp = "Responsabilidad";
                                }
                                else if (especialidades.get(i).equals("Procuración de recursos")){
                                    esp = "Procuracion";
                                }
                                else if (especialidades.get(i).equals("Comercio")){
                                    esp = "Comercio";
                                }

                                String body = "¡Se ha registrado una nueva convocatoria por parte de '" + nombreInstitucion + "' donde se solicita personal con tu perfil!";
                                enviarNotificaciones("consultor_" + esp, body);
                            }
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            FirebaseMessaging.getInstance().subscribeToTopic(id);
                            Intent i = new Intent(CrearConvocatoriaMainActivity.this, InstitucionesMain2Activity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });

                }

            }
        });
    }

    public void enviarNotificaciones(String condicion, String body){
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            String URL = "https://fcm.googleapis.com/fcm/send";
            JSONObject jsonBody = new JSONObject();
            JSONObject jsonNotif = new JSONObject();

            jsonBody.put("condition", "'" + condicion + "' in topics");

            jsonNotif.put("title","Consultor");
            jsonNotif.put("body", body);

            jsonBody.put("notification", jsonNotif);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "key=AIzaSyD6YzweBfQVWxSuiW-C9nkPJ2jr8I_JW8Q");
                    return headers;
                }
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}
