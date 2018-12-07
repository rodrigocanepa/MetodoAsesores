package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
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
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.Convocatoria;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesVerPostuladosActivity extends AppCompatActivity {

    FirebaseStorage storage = FirebaseStorage.getInstance();

    // *************************** RECYCLER VIEW ************************

    private class DataConfigHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNombre;
        private TextView txtCarrera;
        private Button btnVerCurriculum;
        private Button btnVerTemarios;
        private Button btnAceptar;

        private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();
        private Context ctx;

        public DataConfigHolder(View itemView, Context ctx, final List<ConsultorConv> postulados) {
            super(itemView);
            itemView.setOnClickListener(this);

            this.postulados = postulados;
            this.ctx = ctx;

            txtNombre = (TextView) itemView.findViewById(R.id.txtConsultoresNombrePOSTULADOS);
            txtCarrera = (TextView) itemView.findViewById(R.id.txtConsultoresEspecialidadPOSTULADOS);
            btnVerCurriculum = (Button)itemView.findViewById(R.id.item_instituciones_ver_curriculumPOSTULADOS);
            btnVerTemarios = (Button)itemView.findViewById(R.id.item_instituciones_ver_temariosPOSTULADOS);
            btnAceptar = (Button)itemView.findViewById(R.id.item_instituciones_aceptar_postulacionPOSTULADOS);

            btnVerCurriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerPostuladosActivity.this, VerCurriculumsConsultoresActivity.class);
                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, postulados.get(getAdapterPosition()).getId());
                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "curriculum");
                    startActivity(i);
                }
            });

            btnVerTemarios.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(InstitucionesVerPostuladosActivity.this, VerTemariosActivity.class);
                    i.putExtra(VerTemariosActivity.ID_CONSULTOR, postulados.get(getAdapterPosition()).getId());
                    startActivity(i);
                }
            });

            btnAceptar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String mensaje = "Felicidades la institución '" + institucion_convocatoria + "' te ha seleccionado para su convocatoria '" + nombre_convocatoria + "', tienes 2 días máximo para responder este mensaje y ponerte en contacto con el responsable";
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InstitucionesVerPostuladosActivity.this);
                    builder.setTitle("Confirmar Postulado");
                    builder.setMessage("¿Desea aceptar este postulado?")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    enviarCorreo(postulados.get(getAdapterPosition()).getCorreo(), "Mensaje de Método Asesores", mensaje, postulados.get(getAdapterPosition()).getId(), postulados.get(getAdapterPosition()));
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            });

        }

        public void bindConfig(final ConsultorConv postulado) {
            txtNombre.setText(postulado.getNombreCompleto());
            txtCarrera.setText(postulado.getEspecialidad());

        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();

        }

    }


    private class DataConfigAdapter extends RecyclerView.Adapter<InstitucionesVerPostuladosActivity.DataConfigHolder> {

        private List<ConsultorConv> postulados;
        Context ctx;

        public DataConfigAdapter(List<ConsultorConv> postulados, Context ctx ){
            this.postulados = postulados;
            this.ctx = ctx;
        }

        @Override
        public InstitucionesVerPostuladosActivity.DataConfigHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_postulados, parent, false);
            return new InstitucionesVerPostuladosActivity.DataConfigHolder(view, ctx, postulados);
        }

        @Override
        public void onBindViewHolder(final InstitucionesVerPostuladosActivity.DataConfigHolder holder, final int position) {
            holder.bindConfig(postulados.get(position));

        }

        @Override
        public int getItemCount() {
            return postulados.size();
        }

    }

    private InstitucionesVerPostuladosActivity.DataConfigAdapter adapter;
    private List<ConsultorConv> postulados = new ArrayList<ConsultorConv>();
    private RecyclerView recyclerView;
    private DatabaseReference caucelAppRef;
    private TextInputEditText editCorreo;
    private TextInputEditText editResponsable;
    private TextInputEditText editTelefono;
    private Session session;

    private ProgressDialog progressDialog;

    // ******************************************************************

    public static final String ID_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_ID_CONVOCATORIA";
    public static final String NOMBRE_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_NOMBRE_CONVOCATORIA";
    public static final String INSTITUCION_CONVOCATORIA = "projects.solucionescolabora.com.metodoasesores.Instituciones_INSTITUCION_CONVOCATORIA";

    private String id_convocatoria = "";
    private String nombre_convocatoria = "";
    private String institucion_convocatoria = "";

    private SwipeRefreshLayout pullToRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_ver_postulados);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerVerPostulados);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        Intent intent = getIntent();
        id_convocatoria = intent.getStringExtra(ID_CONVOCATORIA);
        nombre_convocatoria = intent.getStringExtra(NOMBRE_CONVOCATORIA);
        institucion_convocatoria = intent.getStringExtra(INSTITUCION_CONVOCATORIA);

        progressDialog = new ProgressDialog(InstitucionesVerPostuladosActivity.this);

        progressDialog.setTitle("Enviando correo");
        progressDialog.setMessage("Espere un momento mientras el sistema envia el correo");


        pullToRefresh = findViewById(R.id.refreshInstituciones);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(true);
                refreshData(); // your code
            }
        });

        // ************** OBTENEMOS LA INSTANCIA A LA BASE DE DATOS ******************************
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        caucelAppRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);
        // ******************************************************************************************

        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("postulados").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ConsultorConv postulado = snapshot.getValue(ConsultorConv.class);
                    postulados.add(postulado);
                }

                if (postulados.size() == 0){
                    Toast.makeText(getApplicationContext(), "No hay consultores postulados", Toast.LENGTH_LONG).show();
                }
                
                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new InstitucionesVerPostuladosActivity.DataConfigAdapter(postulados, getApplicationContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void enviarCorreo(final String destinatario, final String titulo, final String mensajeCorreo, final String idConsultorPostulado, final ConsultorConv confirmado){
        final AlertDialog.Builder builder = new AlertDialog.Builder(InstitucionesVerPostuladosActivity.this);
        // Get the layout inflater
        builder.setTitle("Enviar correo de confirmación");
        builder.setMessage("Al momento de confirmar la participación de este(a) consultor(a) debemos enviarle un correo de confirmación, este proceso se hace en automático por medio de esta aplicación pero necesitamos que nos confirme el nombre del responsable de esta convocatoria y su correo");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                                    final String UUIDUser = user.getUid();

                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("postulados").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            String llave = "";
                                            for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                final ConsultorConv postulado = snapshot.getValue(ConsultorConv.class);
                                                if(postulado.getId().equals(idConsultorPostulado)){
                                                    llave = snapshot.getKey();
                                                }
                                            }
                                            tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("postulados").child(llave).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    final List<ConsultorConv> confirmados_ = new ArrayList<>();

                                                    tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("consultorConvs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                final ConsultorConv consultor = snapshot.getValue(ConsultorConv.class);
                                                                confirmados_.add(consultor);
                                                            }

                                                            confirmados_.add(confirmado);
                                                            tutorialRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("consultorConvs").setValue(confirmados_).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    postulados.clear();
                                                                    // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
                                                                    caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("postulados").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                                            // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                                                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                                ConsultorConv postulado = snapshot.getValue(ConsultorConv.class);
                                                                                postulados.add(postulado);
                                                                            }

                                                                            if (postulados.size() == 0){
                                                                                Toast.makeText(getApplicationContext(), "No hay consultores postulados", Toast.LENGTH_LONG).show();
                                                                            }
                                                                            // *********** LLENAMOS EL RECYCLER VIEW *****************************
                                                                            adapter = new InstitucionesVerPostuladosActivity.DataConfigAdapter(postulados, getApplicationContext());
                                                                            recyclerView.setAdapter(adapter);
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });

                                                                    Toast.makeText(getApplicationContext(), "Correo enviado con éxito", Toast.LENGTH_LONG).show();
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
                            }
                            catch (Exception e){

                            }
                        }

                        else{
                            Toast.makeText(getApplicationContext(), "Debes llenar los datos requeridos", Toast.LENGTH_LONG).show();
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

    private void refreshData(){
        postulados.clear();
        // ************** LEEMOS LA TABLA NECESARIA UNA VEZ PARA SACAR LOS OBJETOS A MOSTRAR ******************************
        caucelAppRef.child(FirebaseReferences.CONVOCATORIAS_REFERENCE).child(id_convocatoria).child("postulados").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ConsultorConv postulado = snapshot.getValue(ConsultorConv.class);
                    postulados.add(postulado);
                }

                if (postulados.size() == 0){
                    Toast.makeText(getApplicationContext(), "No hay consultores postulados", Toast.LENGTH_LONG).show();
                }

                // *********** LLENAMOS EL RECYCLER VIEW *****************************
                adapter = new InstitucionesVerPostuladosActivity.DataConfigAdapter(postulados, getApplicationContext());
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
