package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Fragments.ConsultoresFragment;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class ConsultoresVerConvocatoriasActivity extends AppCompatActivity {

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

            btnVerDetalles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    String habilidades = "";
                    List<String> especialidades = convocatorias.get(pos).getEspecialidades();
                    for (int i = 0; i < especialidades.size(); i++){
                        if(i == especialidades.size() - 1){
                            habilidades += especialidades.get(i) + ".";
                        }
                        else{
                            habilidades += especialidades.get(i) + ", ";
                        }
                    }
                    final String mensaje = "Descripción: " + convocatorias.get(pos).getDescripcion() + "\n\nLocación: " + convocatorias.get(pos).getLocacion() + "\n\nEspecialidades requeridas: " + habilidades + "\n\nPrecio: $" + convocatorias.get(pos).getPrecio();
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultoresVerConvocatoriasActivity.this);
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

                    if (btnPostularme.getText().equals("POSTULARME")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ConsultoresVerConvocatoriasActivity.this);
                        builder.setTitle("Postulación");
                        builder.setMessage("¿Desea postularse a esta convocatoria?")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        final List<ConsultorConv> postulados = new ArrayList<>();
                                        String idConvocatoria = convocatorias.get(getAdapterPosition()).getId();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                                        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(idConvocatoria).child("postulados").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    ConsultorConv postulado_ = snapshot.getValue(ConsultorConv.class);
                                                    postulados.add(postulado_);
                                                }

                                                ConsultorConv postulado = new ConsultorConv(idActual,nombreActual,especialidadActual,0.0,"", correoActual);
                                                postulados.add(postulado);
                                                tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(convocatorias.get(getAdapterPosition()).getId()).child("postulados").setValue(postulados).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        String body = "¡Se ha postulado un(a) nuevo(a) consultor(a) a tu convocatoria: '" + convocatorias.get(getAdapterPosition()).getTitulo() + "'! Puedes ver toda la información en tu perfil de INSTITUCIÓN en la sección 'Historial de convocatorias'";
                                                        enviarNotificaciones(convocatorias.get(getAdapterPosition()).getId(), body);

                                                        Toast.makeText(getApplicationContext(), "¡Postulación exitosa!", Toast.LENGTH_LONG).show();
                                                        convocatorias.clear();
                                                        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
                                                        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);
                                                                    if(convocatoria.getStatus().equals("abierta")){

                                                                        boolean estaEnCurso = true;

                                                                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                                                                        Date strDate = null;
                                                                        try {
                                                                            strDate = sdf.parse(convocatoria.getDiaConfirmar() + " 23:59:00");
                                                                        } catch (ParseException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                        if (new Date().after(strDate)) {
                                                                            estaEnCurso = false;
                                                                        }

                                                                        if(estaEnCurso){
                                                                            convocatorias.add(convocatoria);
                                                                        }
                                                                    }
                                                                }

                                                                for(int i = 0; i < misEspecialidades.size(); i++){
                                                                    for(int j = 0; j < convocatorias.size(); j++){
                                                                        List<String> especialidadesReq = convocatorias.get(j).getEspecialidades();
                                                                        for (int k = 0; k < especialidadesReq.size(); k++){
                                                                            if(especialidadesReq.get(k).equals(misEspecialidades.get(i))){
                                                                                if(convocatoriasFiltradas.size() == 0){
                                                                                    convocatoriasFiltradas.add(convocatorias.get(j));
                                                                                }
                                                                                else{
                                                                                    for(int m = 0; m < convocatoriasFiltradas.size(); m++){
                                                                                        if(convocatoriasFiltradas.get(m).getId().equals(convocatorias.get(j).getId())){

                                                                                        }
                                                                                        else{
                                                                                            convocatoriasFiltradas.add(convocatorias.get(j));
                                                                                        }
                                                                                    }
                                                                                }

                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                                                adapter = new ConsultoresVerConvocatoriasActivity.DataConfigAdapter(convocatoriasFiltradas, getApplicationContext());
                                                                recyclerView.setAdapter(adapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                });

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                })
                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // FIRE ZE MISSILES!
                                    }
                                });
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Ya te has postulado a esta convocatoria", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        public void bindConfig(final Convocatoria convocatoria) {
            txtNombre.setText(convocatoria.getNombreInstitucion());
            txtUbicacion.setText(convocatoria.getUbicacion());
            txtTitulo.setText(convocatoria.getTitulo());
            txtFechaEvento.setText("Fecha del evento: " + convocatoria.getDiaEvento());
            txtFechaPos.setText("Fecha límite para postularse: " + convocatoria.getDiaConfirmar());

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

            if(convocatoria.getPostulados() != null){
                List<ConsultorConv> postulados = convocatoria.getPostulados();
                for(int i = 0; i < postulados.size(); i++){
                    if(postulados.get(i) != null){
                        if(postulados.get(i).getId().equals(idActual)){
                            btnPostularme.setText("POSTULADO");
                            btnPostularme.setTextColor(Color.rgb(254,203,47));
                        }
                    }

                }
            }

            if(convocatoria.getConsultorConvs() != null){
                List<ConsultorConv> postulados = convocatoria.getConsultorConvs();
                for(int i = 0; i < postulados.size(); i++){
                    if(postulados.get(i).getId().equals(idActual)){
                        btnPostularme.setText("ACEPTADO");
                        btnPostularme.setTextColor(Color.rgb(254,203,47));
                    }
                }
            }


        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<ConsultoresVerConvocatoriasActivity.DataConfigHolder> {

        private List<Convocatoria> convocatorias;
        Context ctx;

        public DataConfigAdapter(List<Convocatoria> convocatorias, Context ctx ){
            this.convocatorias = convocatorias;
            this.ctx = ctx;
        }

        @Override
        public ConsultoresVerConvocatoriasActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_convocatorias, parent, false);
            return new ConsultoresVerConvocatoriasActivity.DataConfigHolder(view, ctx, convocatorias);
        }

        @Override
        public void onBindViewHolder(final ConsultoresVerConvocatoriasActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(convocatorias.get(position));

        }

        @Override
        public int getItemCount() {
            return convocatorias.size();
        }

    }

    private ConsultoresVerConvocatoriasActivity.DataConfigAdapter adapter;
    private List<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;

    // ******************************************************************
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences sharedPreferences;
    private String idActual = "";
    private String nombreActual = "";
    private String especialidadActual = "";
    private String correoActual = "";
    private boolean saveFoto = false;

    private ProgressDialog progressDialog;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Switch aSwitch;
    private List<Convocatoria> convocatoriasFiltradas = new ArrayList<Convocatoria>();
    private List<String> misEspecialidades = new ArrayList<>();

    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultores_ver_convocatorias);
        setTitle("Convocatorias");

        recyclerView = (RecyclerView)findViewById(R.id.recyclerConsultoresVerConvocatorias);
        aSwitch = (Switch)findViewById(R.id.switchConsultoresConvocatoria);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(ConsultoresVerConvocatoriasActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");

        progressDialog.show();

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");

        nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");
        correoActual = sharedPreferences.getString("correoActual","");
        saveFoto = sharedPreferences.getBoolean("saveFoto",false);

        pullToRefresh = findViewById(R.id.refreshConsultores);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                refreshData(); // your code
            }
        });

        caucelAppRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Consultor consultor = snapshot.getValue(Consultor.class);
                    if(consultor.getId().equals(idActual)){
                        misEspecialidades = consultor.getEspecialidad();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);
                    if(convocatoria.getStatus().equals("abierta")){

                        boolean estaEnCurso = true;

                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(convocatoria.getDiaConfirmar() + " 23:59:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {
                            estaEnCurso = false;
                        }

                        if(estaEnCurso){
                            convocatorias.add(convocatoria);
                        }
                    }
                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(convocatorias.size() == 0){
                    Toast.makeText(getApplicationContext(), "No hay convocatorias abiertas aún", Toast.LENGTH_LONG).show();
                }
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new ConsultoresVerConvocatoriasActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    if(convocatoriasFiltradas.size() == 0 && convocatorias.size() > 0){
                        progressDialog.show();
                        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
                        caucelAppRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Consultor consultor = snapshot.getValue(Consultor.class);
                                    if(consultor.getId().equals(idActual)){
                                        misEspecialidades = consultor.getEspecialidad();
                                    }
                                }

                                if (progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }

                                for(int i = 0; i < misEspecialidades.size(); i++){
                                    for(int j = 0; j < convocatorias.size(); j++){
                                        List<String> especialidadesReq = convocatorias.get(j).getEspecialidades();
                                        for (int k = 0; k < especialidadesReq.size(); k++){
                                            if(especialidadesReq.get(k).equals(misEspecialidades.get(i))){
                                                if(convocatoriasFiltradas.size() == 0){
                                                    convocatoriasFiltradas.add(convocatorias.get(j));
                                                }
                                                else{
                                                    for(int m = 0; m < convocatoriasFiltradas.size(); m++){
                                                        if(convocatoriasFiltradas.get(m).getId().equals(convocatorias.get(j).getId())){

                                                        }
                                                        else{
                                                            convocatoriasFiltradas.add(convocatorias.get(j));
                                                        }
                                                    }
                                                }

                                            }
                                        }
                                    }
                                }
                                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                adapter = new ConsultoresVerConvocatoriasActivity.DataConfigAdapter(convocatoriasFiltradas, getApplicationContext());
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }
                else{
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new ConsultoresVerConvocatoriasActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                    recyclerView.setAdapter(adapter);
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

            jsonNotif.put("title","Método Asesores");
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


    private void refreshData(){
        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                convocatorias.clear();
                aSwitch.setChecked(false);

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);
                    if(convocatoria.getStatus().equals("abierta")){

                        boolean estaEnCurso = true;

                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(convocatoria.getDiaConfirmar() + " 23:59:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {
                            estaEnCurso = false;
                        }

                        if(estaEnCurso){
                            convocatorias.add(convocatoria);
                        }
                    }
                }

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }

                if(convocatorias.size() == 0){
                    Toast.makeText(getApplicationContext(), "No hay convocatorias abiertas aún", Toast.LENGTH_LONG).show();
                }
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new ConsultoresVerConvocatoriasActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                recyclerView.setAdapter(adapter);
                pullToRefresh.setRefreshing(false);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pullToRefresh.setRefreshing(false);
            }
        });
    }
}
