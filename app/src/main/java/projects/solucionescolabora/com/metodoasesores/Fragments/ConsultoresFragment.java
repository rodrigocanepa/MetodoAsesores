package projects.solucionescolabora.com.metodoasesores.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultoresVerConvocatoriasActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.SpinnerAdapter;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesVerConsultoresActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.InstitucionesVerPostuladosActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.VerCurriculumsConsultoresActivity;
import projects.solucionescolabora.com.metodoasesores.Instituciones.VerTemariosActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConsultoresFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ConsultoresFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    FirebaseStorage storage = FirebaseStorage.getInstance();


    public ConsultoresFragment() {
        // Required empty public constructor
    }

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtCarrera;
        private TextView txtFrase;
        private RatingBar ratingBar;
        private CircleImageView circleImageView;
        private Button btnVerCurriculum;
        private Button btnVerTemarios;

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
                    Intent i = new Intent(getActivity(), VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, consultores.get(getAdapterPosition()).getId());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "curriculum");
                    startActivity(i);
                }
            });

            btnVerTemarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent i = new Intent(getActivity(), VerTemariosActivity.class);
                    i.putExtra(VerTemariosActivity.ID_CONSULTOR, consultores.get(getAdapterPosition()).getId());
                    startActivity(i);*/
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                    builder.setTitle("Contacto");
                    builder.setMessage("¿Desea contactar a este consultor?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final String mensaje = "Estimado consultor, la startup llamada '" +  especialidadActual + "' requiere tus servicios para el fortalecimiento de su idea o negocio, por lo cual tienes 2 días máximo para responder este mensaje y ponerte en contacto con el emprendedor";
                                    enviarCorreo(consultores.get(getAdapterPosition()).getCorreo(),"¡Se requieren tus servicios de consultoria",mensaje);
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
            });

            circleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), VerCurriculumsConsultoresActivity.class);
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

            btnVerTemarios.setText("CONTACTAR");

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


    private class DataConfigAdapter extends RecyclerView.Adapter<ConsultoresFragment.DataConfigHolder> {

        private List<Consultor> consultors;
        Context ctx;

        public DataConfigAdapter(List<Consultor> consultors, Context ctx ){
            this.consultors = consultors;
            this.ctx = ctx;
        }

        @Override
        public ConsultoresFragment.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.items_consultores, parent, false);
            return new ConsultoresFragment.DataConfigHolder(view, ctx, consultors);
        }

        @Override
        public void onBindViewHolder(final ConsultoresFragment.DataConfigHolder holder, final int position) {
            holder.bindConfig(consultors.get(position));

        }

        @Override
        public int getItemCount() {
            return consultors.size();
        }

    }

    private ConsultoresFragment.DataConfigAdapter adapter;
    private List<Consultor> consultores = new ArrayList<Consultor>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;

    // ******************************************************************

    private List<String> filtrosEspecialidad = new ArrayList<>();
    private List<String> municipios = new ArrayList<>();

    private Spinner spinnerEspecialidad;
    private Spinner spinnerUbicacion;
    private List<Consultor> consultoresFiltradosEsp = new ArrayList<Consultor>();

    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;

    private int[] calificaciones = new int[6];
    private int minimo;

    private Session session;
    private TextInputEditText editCorreo;
    private TextInputEditText editResponsable;
    private TextInputEditText editTelefono;

    private String idActual = "";
    private String nombreActual = "";
    private String especialidadActual = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_consultores, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewConsultores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        spinnerEspecialidad = (Spinner)view.findViewById(R.id.spinnerFiltrosStartVerConsultoresEsp);
        spinnerUbicacion = (Spinner)view.findViewById(R.id.spinnerFiltrosStartVerConsultoresUbi);

        sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");
        nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");

        progressDialog = new ProgressDialog(getActivity());

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


        SpinnerAdapter spinnerAdapterEspecialidad = new SpinnerAdapter(filtrosEspecialidad, getActivity());
        SpinnerAdapter spinnerAdapterUbicacion = new SpinnerAdapter(municipios, getActivity());

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

                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new ConsultoresFragment.DataConfigAdapter(consultores, getActivity());
                recyclerView.setAdapter(adapter);

                sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
                String idActual = sharedPreferences.getString("idActual", "");
                if(sharedPreferences.getString(idActual + "diagnosticoEstatusActual","").length() > 2){
                    calificaciones[0] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualContabilidad", "0"));
                    calificaciones[1] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualInnovacion", "0"));
                    calificaciones[2] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualLegal", "0"));
                    calificaciones[3] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualPlaneacion", "0"));
                    calificaciones[4] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualProcuracion", "0"));
                    calificaciones[5] = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualVentas", "0"));

                    minimo = getMinValue(calificaciones);

                    if(minimo == 0){
                        spinnerEspecialidad.setSelection(5);
                    }
                    else if (minimo == 1){
                        spinnerEspecialidad.setSelection(4);
                    }
                    else if (minimo == 2){
                        spinnerEspecialidad.setSelection(7);
                    }
                    else if (minimo == 3){
                        spinnerEspecialidad.setSelection(1);
                    }
                    else if (minimo == 4){
                        spinnerEspecialidad.setSelection(10);
                    }
                    else if (minimo == 5){
                        spinnerEspecialidad.setSelection(3);
                    }

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
                    adapter = new ConsultoresFragment.DataConfigAdapter(consultoresFiltradosEsp, getActivity());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new ConsultoresFragment.DataConfigAdapter(consultores, getActivity());
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
                    adapter = new ConsultoresFragment.DataConfigAdapter(consultoresFiltradosEsp, getActivity());
                    recyclerView.setAdapter(adapter);
                }
                else{
                    // *********** LLENAMOS EL RECYCLER VIEW *****************************
                    adapter = new ConsultoresFragment.DataConfigAdapter(consultores, getActivity());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public static int getMinValue(int[] numbers){
        int minValue = numbers[0];
        for(int i=1;i<numbers.length;i++){
            if(numbers[i] < minValue){
                minValue = i;
            }
        }
        return minValue;
    }

    public void enviarCorreo(final String destinatario, final String titulo, final String mensajeCorreo){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("Enviar correo de contacto");
        builder.setMessage("Para contactar a este(a) consultor(a) debemos enviarle un correo de confirmación, este proceso se hace en automático por medio de esta aplicación pero necesitamos que nos confirme el nombre del emprendedor que requiere sus servicios y su correo para establecer la comunicación");
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.dialog_send_email,
                null, false);

        editCorreo = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarCorreoCorreoResponsable);
        editResponsable = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarCorreoNombreResponsable);
        editTelefono = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarCorreoTelefono);

        builder.setTitle("Enviar Correo")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if(editCorreo.length() > 1 && editResponsable.length() > 1 && editTelefono.length() > 1){

                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            // gmail
                            Properties properties = new Properties();
                            properties.put("mail.smtp.host","smtp.googlemail.com");
                            properties.put("mail.smtp.socketFactory.port","465");
                            properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                            properties.put("mail.smtp.auth","true");
                            properties.put("mail.smtp.port","465");

                            // hotmail
                                /*
                                properties.put("mail.smtp.host","smtp.live.com");
                                properties.put("mail.smtp.socketFactory.port","587");
                                properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
                                properties.put("mail.smtp.auth","true");
                                properties.put("mail.smtp.starttls.enable","true");
                                */


                            try{
                                session = Session.getDefaultInstance(properties, new Authenticator() {
                                    @Override
                                    protected PasswordAuthentication getPasswordAuthentication() {
                                        return new PasswordAuthentication("atencion.metodo@gmail.com","MetodoAsesores1");
                                    }
                                });

                                if(session!= null){

                                    BodyPart texto = new MimeBodyPart();
                                    texto.setText(mensajeCorreo + " " + editResponsable.getText().toString() + ", cuyo correo de contacto es: " + editCorreo.getText().toString() + " o al teléfono " + editTelefono.getText().toString());

                                    // BodyPart adjunto = new MimeBodyPart();
                                    //adjunto.setDataHandler(new DataHandler(new FileDataSource(direccion)));
                                    //adjunto.setFileName(nombreCuestionario);

                                    MimeMultipart multiParte = new MimeMultipart();

                                    multiParte.addBodyPart(texto);
                                    //multiParte.addBodyPart(adjunto);

                                    javax.mail.Message message = new MimeMessage(session);
                                    message.setFrom(new InternetAddress("atencion.metodo@gmail.com"));
                                    message.setSubject(titulo);
                                    message.setRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
                                    message.setContent(multiParte);

                                    Transport.send(message);
                                    Toast.makeText(getActivity(), "Correo enviado", Toast.LENGTH_LONG).show();

                                }
                            }
                            catch (Exception e){

                            }
                        }

                        else{
                            Toast.makeText(getActivity(), "Debes llenar los datos requeridos", Toast.LENGTH_LONG).show();
                        }


                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(formElementsView);
        // Add action buttons
        builder.create();
        builder.show();
    }
}
