package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultorVerPDFActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.SpinnerAdapter;
import projects.solucionescolabora.com.metodoasesores.Fragments.ConsultoresFragment;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesVerConsultoresActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtCarrera;
        private TextView txtFrase;
        private Button btnVerCurriculum;
        private Button btnVerTemarios;
        private CircleImageView circleImageView;

        private List<Consultor> consultores = new ArrayList<Consultor>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<Consultor> consultores) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.consultores = consultores;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.txtConsultoresNombre);
            txtCarrera = (TextView) itemView.findViewById(R.id.txtConsultoresEspecialidad);
            txtFrase = (TextView) itemView.findViewById(R.id.txtConsultoresFrase);
            //txtGeneracion = (TextView) itemView.findViewById(R.id.item_exp_aca_generacion);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.imgAvatarConsultores);
            btnVerCurriculum = (Button)itemView.findViewById(R.id.item_instituciones_ver_curriculum);
            btnVerTemarios = (Button)itemView.findViewById(R.id.item_instituciones_ver_temarios);

            btnVerCurriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerConsultoresActivity.this, VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, consultores.get(getAdapterPosition()).getId());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "curriculum");
                    startActivity(i);
                }
            });

            btnVerTemarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerConsultoresActivity.this, VerTemariosActivity.class);
                    i.putExtra(VerTemariosActivity.ID_CONSULTOR, consultores.get(getAdapterPosition()).getId());
                    startActivity(i);
                }
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerConsultoresActivity.this, VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, consultores.get(getAdapterPosition()).getId());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "curriculum");

                    startActivity(i);
                }
            });
        }

        public void bindConfig(final Consultor consultor) {
            txtNombre.setText(consultor.getNombres() + " " + consultor.getApellidos());
            List<String> especialidades = consultor.getEspecialidad();
            String carreras = "";
            for (int i = 0; i < especialidades.size(); i ++){
                if(i == especialidades.size() - 1){
                    carreras += especialidades.get(i);
                }
                else{
                    carreras += especialidades.get(i) + ", ";
                }
            }
            txtCarrera.setText(carreras);
            txtFrase.setText(consultor.getUbicacion());

            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(consultor.getUrlFoto());
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

        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionesVerConsultoresActivity.DataConfigHolder> {

        private List<Consultor> consultors;
        Context ctx;

        public DataConfigAdapter(List<Consultor> consultors, Context ctx ){
            this.consultors = consultors;
            this.ctx = ctx;
        }

        @Override
        public InstitucionesVerConsultoresActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.items_consultores, parent, false);
            return new InstitucionesVerConsultoresActivity.DataConfigHolder(view, ctx, consultors);
        }

        @Override
        public void onBindViewHolder(final InstitucionesVerConsultoresActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(consultors.get(position));

        }

        @Override
        public int getItemCount() {
            return consultors.size();
        }

    }

    private InstitucionesVerConsultoresActivity.DataConfigAdapter adapter;
    private List<Consultor> consultores = new ArrayList<Consultor>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;

    private List<String> filtrosEspecialidad = new ArrayList<>();
    private List<String> municipios = new ArrayList<>();

    private Spinner spinnerEspecialidad;
    private Spinner spinnerUbicacion;
    private List<Consultor> consultoresFiltradosEsp = new ArrayList<Consultor>();

    // ******************************************************************

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_ver_consultores);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewInstVerConsultores);
        spinnerEspecialidad = (Spinner)findViewById(R.id.spinnerFiltrosInstVerConsultoresEsp);
        spinnerUbicacion = (Spinner)findViewById(R.id.spinnerFiltrosInstVerConsultoresUbi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        progressDialog = new ProgressDialog(InstitucionesVerConsultoresActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");

        progressDialog.show();

        filtrosEspecialidad.add("Todas las especialidades");
        filtrosEspecialidad.add("Planeación estratégica");
        filtrosEspecialidad.add("Diseño");
        filtrosEspecialidad.add("Marketing");
        filtrosEspecialidad.add("Tecnología e innovación");
        filtrosEspecialidad.add("Finanzas");
        filtrosEspecialidad.add("Administración");
        filtrosEspecialidad.add("Legal");
        filtrosEspecialidad.add("Turismo");
        filtrosEspecialidad.add("Responsabilidad social");
        filtrosEspecialidad.add("Procuración de recursos");
        filtrosEspecialidad.add("Comercio");

        municipios.add("Ver todos los municipios");
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

        SpinnerAdapter spinnerAdapterEspecialidad = new SpinnerAdapter(filtrosEspecialidad, InstitucionesVerConsultoresActivity.this);
        SpinnerAdapter spinnerAdapterUbicacion = new SpinnerAdapter(municipios, InstitucionesVerConsultoresActivity.this);

        spinnerUbicacion.setAdapter(spinnerAdapterUbicacion);
        spinnerEspecialidad.setAdapter(spinnerAdapterEspecialidad);


        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Consultor consultor = snapshot.getValue(Consultor.class);
                    consultores.add(consultor);
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new InstitucionesVerConsultoresActivity.DataConfigAdapter(consultores, getApplicationContext());
                recyclerView.setAdapter(adapter);

                if (progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        spinnerEspecialidad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                consultoresFiltradosEsp.clear();
                String filtro = filtrosEspecialidad.get(position);
                String filtroUbicacion = "";
                if(spinnerUbicacion.getSelectedItem().toString().equals("Ver todos los municipios")){
                    filtroUbicacion = spinnerUbicacion.getSelectedItem().toString();
                }
                else{
                    filtroUbicacion = spinnerUbicacion.getSelectedItem().toString() + ", Yucatán";
                }

                if(position > 0){
                    for(Consultor consultor : consultores){
                        List<String> especialidades = consultor.getEspecialidad();

                        for(int i = 0; i<especialidades.size(); i++){
                            if(especialidades.get(i).equals(filtro)){
                                if(filtroUbicacion.equals("Ver todos los municipios")){
                                    consultoresFiltradosEsp.add(consultor);
                                }
                                else{
                                    if(consultor.getUbicacion().equals(filtroUbicacion)){
                                        consultoresFiltradosEsp.add(consultor);
                                    }
                                }
                            }
                        }
                    }
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerConsultoresActivity.DataConfigAdapter(consultoresFiltradosEsp, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerConsultoresActivity.DataConfigAdapter(consultores, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerUbicacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                consultoresFiltradosEsp.clear();
                String filtro = municipios.get(position) + ", Yucatán";
                String filtroEsp = spinnerEspecialidad.getSelectedItem().toString();

                if(position > 0){
                    for(Consultor consultor : consultores){
                        String ubicacion = consultor.getUbicacion();

                        if(ubicacion.equals(filtro)){
                            if(filtroEsp.equals("Todas las especialidades")){
                                consultoresFiltradosEsp.add(consultor);
                            }
                            else{
                                List<String> especialidades = consultor.getEspecialidad();
                                for(int i = 0; i < especialidades.size(); i++){
                                    if(especialidades.get(i).equals(filtroEsp)){
                                        consultoresFiltradosEsp.add(consultor);
                                    }
                                }
                            }

                        }
                    }
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerConsultoresActivity.DataConfigAdapter(consultoresFiltradosEsp, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new InstitucionesVerConsultoresActivity.DataConfigAdapter(consultores, getApplicationContext());
                    recyclerView.setAdapter(adapter);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
