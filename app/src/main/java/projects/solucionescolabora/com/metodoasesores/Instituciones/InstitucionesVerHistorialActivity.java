package projects.solucionescolabora.com.metodoasesores.Instituciones;

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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresHistorialActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresVerConvocatoriasActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.SpinnerAdapter;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesVerHistorialActivity extends AppCompatActivity {

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtTitulo;
        private TextView txtDescripcion;
        private TextView txtFechaPos;
        private TextView txtFechaEvento;
        private TextView txtEstatus;
        private Button btnOpcion1;
        private Button btnOpcion2;

        private List<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<Convocatoria> convocatorias) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.convocatorias = convocatorias;
            this.ctx = ctx;

            txtTitulo = (TextView) itemView.findViewById(R.id.txtConvocatoriaTituloINST_HISTORIAL);
            txtDescripcion = (TextView) itemView.findViewById(R.id.txtConvocatoriaDescripcionINST_HISTORIAL);
            txtFechaPos = (TextView) itemView.findViewById(R.id.txtConvocatoriaFechaPostulacionINST_HISTORIAL);
            txtFechaEvento = (TextView) itemView.findViewById(R.id.txtConvocatoriaFechaEventoINST_HISTORIAL);
            txtEstatus = (TextView) itemView.findViewById(R.id.txtConvocatoriaStatusINST_HISTORIAL);
            btnOpcion1 = (Button) itemView.findViewById(R.id.btnINST_HISTORIAL_opcion1);
            btnOpcion2 = (Button) itemView.findViewById(R.id.btnINST_HISTORIAL_opcion2);

            btnOpcion1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();

                    if(convocatorias.get(pos).getStatus().equals("abierta")){

                        boolean estaEnCurso = true;

                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(convocatorias.get(pos).getDiaConfirmar() + " 23:59:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {
                            estaEnCurso = false;
                        }
                        // ***********************************************************

                        if(estaEnCurso){
                            Intent i = new Intent(InstitucionesVerHistorialActivity.this, InstitucionesVerPostuladosActivity.class);
                            i.putExtra(InstitucionesVerPostuladosActivity.ID_CONVOCATORIA,convocatorias.get(pos).getId());
                            i.putExtra(InstitucionesVerPostuladosActivity.NOMBRE_CONVOCATORIA,convocatorias.get(pos).getTitulo());
                            i.putExtra(InstitucionesVerPostuladosActivity.INSTITUCION_CONVOCATORIA,convocatorias.get(pos).getNombreInstitucion());
                            startActivity(i);
                        }
                        else{
                            Intent i = new Intent(InstitucionesVerHistorialActivity.this, InstitucionesVerConfirmadosActivity.class);
                            i.putExtra(InstitucionesVerConfirmadosActivity.ID_CONVOCATORIA,convocatorias.get(pos).getId());
                            i.putExtra(InstitucionesVerConfirmadosActivity.NOMBRE_CONVOCATORIA,convocatorias.get(pos).getTitulo());
                            i.putExtra(InstitucionesVerConfirmadosActivity.INSTITUCION_CONVOCATORIA,convocatorias.get(pos).getNombreInstitucion());
                            startActivity(i);
                        }
                    }

                    else{
                        Intent i = new Intent(InstitucionesVerHistorialActivity.this, InstitucionesVerConvFinalizadaActivity.class);
                        i.putExtra(InstitucionesVerConvFinalizadaActivity.ID_CONVOCATORIA,convocatorias.get(pos).getId());
                        startActivity(i);
                    }


                }
            });

            btnOpcion2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if(convocatorias.get(pos).getStatus().equals("abierta")){
                        boolean estaEnCurso = true;

                        // ********** VALIDAMOS SI LA CONVOCATORIA AUN ESTA EN CURSO O NO
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(convocatorias.get(pos).getDiaConfirmar() + " 23:59:00");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (new Date().after(strDate)) {
                            estaEnCurso = false;
                        }
                        // ***********************************************************

                        // ***********************************************************

                        if(estaEnCurso){
                            Intent i = new Intent(InstitucionesVerHistorialActivity.this, InstitucionesVerConfirmadosActivity.class);
                            i.putExtra(InstitucionesVerConfirmadosActivity.ID_CONVOCATORIA,convocatorias.get(pos).getId());
                            i.putExtra(InstitucionesVerConfirmadosActivity.NOMBRE_CONVOCATORIA,convocatorias.get(pos).getTitulo());
                            i.putExtra(InstitucionesVerConfirmadosActivity.INSTITUCION_CONVOCATORIA,convocatorias.get(pos).getNombreInstitucion());
                            startActivity(i);
                        }
                        else{
                            Intent i = new Intent(InstitucionesVerHistorialActivity.this, InstitucionFinalizarConvActivity.class);
                            i.putExtra(InstitucionFinalizarConvActivity.ID_CONVOCATORIA,convocatorias.get(pos).getId());
                            startActivity(i);

                        }
                    }
                }
            });
        }

        public void bindConfig(final Convocatoria convocatoria) {
            txtDescripcion.setText(convocatoria.getDescripcion());
            txtTitulo.setText(convocatoria.getTitulo());
            txtFechaEvento.setText("Fecha del evento: " + convocatoria.getDiaEvento());
            txtFechaPos.setText("Fecha límite para postularse: " + convocatoria.getDiaConfirmar());

            if (convocatoria.getStatus().equals("finalizada")){
                btnOpcion1.setText("Ver detalles");
                btnOpcion2.setText("finalizada");
                txtEstatus.setText("Estatus: Convocatoria finalizada");
                txtEstatus.setTextColor(Color.rgb(255,255,255));
            }
            else if(convocatoria.getStatus().equals("abierta")){
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
                // ***********************************************************

                if(estaEnCurso){
                    btnOpcion1.setText("Ver consultores postulados");
                    btnOpcion2.setText("Ver consultores confirmados");
                    txtEstatus.setText("Estatus: Convocatoria abierta");
                    txtEstatus.setTextColor(Color.rgb(20,200,20));
                }
                else{

                    btnOpcion1.setText("Consultores confirmados");
                    btnOpcion2.setText("Finalizar Convocatoria");
                    txtEstatus.setText("Estatus: Convocatoria en curso");
                    txtEstatus.setTextColor(Color.rgb(254,203,47));
                }
            }
           /*
            if(convocatoria.getPostulados() != null){
                List<ConsultorConv> postulados = convocatoria.getPostulados();
                for(int i = 0; i < postulados.size(); i++){
                    if(postulados.get(i).getId().equals(idActual)){
                        btnPostularme.setText("POSTULADO");
                        btnPostularme.setTextColor(Color.rgb(254,203,47));
                    }
                }
            }*/


        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionesVerHistorialActivity.DataConfigHolder> {

        private List<Convocatoria> convocatorias;
        Context ctx;

        public DataConfigAdapter(List<Convocatoria> convocatorias, Context ctx ){
            this.convocatorias = convocatorias;
            this.ctx = ctx;
        }

        @Override
        public InstitucionesVerHistorialActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_historial_institucion_convocatorias, parent, false);
            return new InstitucionesVerHistorialActivity.DataConfigHolder(view, ctx, convocatorias);
        }

        @Override
        public void onBindViewHolder(final InstitucionesVerHistorialActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(convocatorias.get(position));

        }

        @Override
        public int getItemCount() {
            return convocatorias.size();
        }

    }

    private InstitucionesVerHistorialActivity.DataConfigAdapter adapter;
    private List<Convocatoria> convocatorias = new ArrayList<Convocatoria>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;

    // ******************************************************************

    private SharedPreferences sharedPreferences;
    private String idActual = "";
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Spinner spinner;
    private List<String> filtros = new ArrayList<>();

    private List<Convocatoria> convocatoriasFiltradas = new ArrayList<Convocatoria>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_ver_historial);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerInstitucionHistorialConvocatorias);
        spinner = (Spinner)findViewById(R.id.spinnerFiltrosHistorialInstituciones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(InstitucionesVerHistorialActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");

        progressDialog.show();

        filtros.add("Ver todas las convocatorias");
        filtros.add("Ver convocatorias abiertas");
        filtros.add("Ver convocatorias finalizadas");

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(filtros,InstitucionesVerHistorialActivity.this);
        spinner.setAdapter(spinnerAdapter);


        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Convocatoria convocatoria = snapshot.getValue(Convocatoria.class);
                    if(convocatoria.getIdInstitucion().equals(idActual)){
                        convocatorias.add(convocatoria);
                    }
                }
                Collections.reverse(convocatorias);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new InstitucionesVerHistorialActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    adapter = new InstitucionesVerHistorialActivity.DataConfigAdapter(convocatorias, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                if(position == 1){
                    convocatoriasFiltradas.clear();

                    for(int i = 0; i < convocatorias.size(); i++){
                        if(convocatorias.get(i).getStatus().equals("abierta")){
                           convocatoriasFiltradas.add(convocatorias.get(i));
                        }
                    }
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerHistorialActivity.DataConfigAdapter(convocatoriasFiltradas, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                if(position == 2){
                    convocatoriasFiltradas.clear();

                    for(int i = 0; i < convocatorias.size(); i++){
                        if(convocatorias.get(i).getStatus().equals("finalizada")){
                            convocatoriasFiltradas.add(convocatorias.get(i));
                        }
                    }
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerHistorialActivity.DataConfigAdapter(convocatoriasFiltradas, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
