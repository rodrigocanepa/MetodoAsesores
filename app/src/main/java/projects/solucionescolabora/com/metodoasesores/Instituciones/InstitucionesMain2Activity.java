package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
import com.facebook.login.LoginManager;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import projects.solucionescolabora.com.metodoasesores.LogOrRegActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Anuncios;
import projects.solucionescolabora.com.metodoasesores.Modelos.ConsultorConv;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.R;

public class InstitucionesMain2Activity extends AppCompatActivity {

    private CircleImageView imgAvatar;
    private ImageView imgFondo;
    private TextView txtNombre;
    private TextView txtRFC;
    private ImageView imgCrear;
    private ImageView imgConsultores;
    private ImageView imgHistorial;
    private ImageView imgCerrar;

    private String url = "";
    private String nombre = "";
    private String rfc = "";
    private String UUIDUser = "";
    private String plan = "";
    private String anuncios = "";

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences sharedPreferences;

    private String idActual = "";
    private String nombreActual = "";
    private String especialidadActual = "";
    private String urlFotoActual = "";
    private boolean saveFoto = false;
    private FloatingActionButton floatingActionButton;
    private String planActual = "";
    private String anunciosRestantes = "";

    private TextInputEditText editTituloAnuncio;
    private TextInputEditText editMensajeAnuncio;
    private TextInputEditText editLinkAnuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instituciones_main2);

        imgAvatar = (CircleImageView)findViewById(R.id.imgInstitucionAvatarHome);
        imgFondo = (ImageView)findViewById(R.id.imgInstitucionHomeFondo);
        txtNombre = (TextView)findViewById(R.id.txtInstitucionNameHome);
        txtRFC = (TextView)findViewById(R.id.txtInstitucionRFCHome);
        imgCrear = (ImageView)findViewById(R.id.imgInstitucionHomeCrearConv);
        imgConsultores = (ImageView)findViewById(R.id.imgInstitucionHomeVerConsul);
        imgHistorial = (ImageView)findViewById(R.id.imgInstitucionVerHist);
        imgCerrar = (ImageView)findViewById(R.id.imgInstitucionHomeCerrarSes);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fabInstitucionNuevoAnuncio);

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");
        nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");
        urlFotoActual = sharedPreferences.getString("urlFotoActual","");
        planActual = sharedPreferences.getString("planActual","");
        anunciosRestantes = sharedPreferences.getString("anunciosRestantes","");
        saveFoto = sharedPreferences.getBoolean("saveFoto",false);

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                        .setCameraButtonText("Cámara")
                        .setGalleryButtonText("Galería"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                imgFondo.setImageBitmap(r.getBitmap());
                                imgAvatar.setImageBitmap(r.getBitmap());

                                imgAvatar.setDrawingCacheEnabled(true);
                                imgAvatar.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                                //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                                imgAvatar.buildDrawingCache();
                                final Bitmap bitmap = r.getBitmap();

                                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                                byte[] data = outputStream.toByteArray();

                                StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones").child(idActual + "-profile");

                                UploadTask uploadTask = refSubirImagen.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        // ***************************** GUARDAMOS LA IMAGEN ***********************
                                        File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                                        if(!folder.exists())
                                            folder.mkdirs();
                                        File file = new File(folder, "fotoMetodoApp.png");

                                        if (file.exists ()) file.delete ();
                                        try {
                                            FileOutputStream out = new FileOutputStream(file);
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                            out.flush();
                                            out.close();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        Toast.makeText(getApplicationContext(), "Foto subida con exito", Toast.LENGTH_SHORT).show();

                                    }
                                });
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

        if(idActual.length() > 2){
            txtNombre.setText(nombreActual);
            txtRFC.setText(especialidadActual);

            if (saveFoto){
                File folder = new  File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");
                if(!folder.exists())
                    folder.mkdirs();
                File file = new File(folder, "fotoMetodoApp.png");

                if(file.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    imgAvatar.setImageBitmap(myBitmap);
                    imgFondo.setImageBitmap(myBitmap);
                    imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);
                    saveFoto = false;
                }
            }
            else{
                // ********* IMPORTANTE CAMBIAR ************
                StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones").child(idActual + "-profile");
                final long ONE_MEGABYTE = 1024 * 1024;
                storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgAvatar.setImageBitmap(bitmap);
                        imgFondo.setImageBitmap(bitmap);
                        imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                        // ***************************** GUARDAMOS LA IMAGEN ***********************
                        File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                        if(!folder.exists())
                            folder.mkdirs();
                        File file = new File(folder, "fotoMetodoApp.png");

                        if (file.exists ()) file.delete ();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("saveFoto", true);
                            editor.commit();
                            // ******************************************************************************

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }
        }
        else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
            UUIDUser = user.getUid();

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Institucion institucion = snapshot.getValue(Institucion.class);

                        if(institucion.getId().equals(UUIDUser)){
                            url = institucion.getUrlLogo();
                            nombre = institucion.getNombre();
                            rfc = institucion.getRfc();
                            plan = institucion.getPlan();
                            anuncios = institucion.getAnuncios();
                        }
                    }

                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                    sharedPreferences = getSharedPreferences("misDatos", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("idActual", UUIDUser);
                    editor.putString("nombreActual", nombre);
                    editor.putString("especialidadActual", rfc);
                    editor.putString("urlFotoActual", url);
                    editor.putString("planActual", plan);
                    editor.putString("anunciosRestantes", anuncios);
                    idActual = UUIDUser;
                    nombreActual = nombre;
                    especialidadActual = rfc;
                    urlFotoActual = url;
                    planActual = plan;
                    anunciosRestantes = anuncios;
                    editor.commit();
                    // ******************************************************************************

                    if(url.length() > 2){
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones").child(UUIDUser + "-profile");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgAvatar.setImageBitmap(bitmap);
                                imgFondo.setImageBitmap(bitmap);
                                imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                                // ***************************** GUARDAMOS LA IMAGEN ***********************
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                                if(!folder.exists())
                                    folder.mkdirs();
                                File file = new File(folder, "fotoMetodoApp.png");

                                if (file.exists ()) file.delete ();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("saveFoto", true);
                                    editor.commit();
                                    // ******************************************************************************

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    }

                    if(nombre.length() > 1){
                        txtNombre.setText(nombre);
                    }
                    if(rfc.length() > 1){
                        txtRFC.setText(rfc);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        imgCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InstitucionesMain2Activity.this);
                builder.setTitle("Cerrar sesión");
                builder.setMessage("¿Estas seguro de querer cerrar sesión?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("typeCurrentUser", "");
                                editor.putString("registroCompleto", "");
                                editor.putString("idActual", "");
                                editor.putString("nombreActual", "");
                                editor.putString("especialidadActual", "");
                                editor.putString("urlFotoActual", "");
                                editor.commit();
                                // ******************************************************************************
                                FirebaseAuth.getInstance().signOut();
                                LoginManager.getInstance().logOut();
                                Intent i = new Intent(InstitucionesMain2Activity.this, LogOrRegActivity.class);
                                startActivity(i);
                                finish();
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

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        imgCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(planActual.equals("prueba")){
                    Toast.makeText(getApplicationContext(), "Tu institución aún no ha sido aprobada", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent i = new Intent(InstitucionesMain2Activity.this, CrearConvocatoriaMainActivity.class);
                    i.putExtra(CrearConvocatoriaMainActivity.INTENT_NOMBRE_INSTITUCION, nombreActual);
                    i.putExtra(CrearConvocatoriaMainActivity.INTENT_URL_INSTITUCION, urlFotoActual);
                    i.putExtra(CrearConvocatoriaMainActivity.INTENT_ID_INSTITUCION, idActual);
                    startActivity(i);
                }
            }
        });

        imgConsultores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(InstitucionesMain2Activity.this, InstitucionesVerConsultoresActivity.class);
                startActivity(i);
            }
        });

        imgHistorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(InstitucionesMain2Activity.this, InstitucionesVerHistorialActivity.class);
                startActivity(i);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(planActual.equals("prueba")){
                    Toast.makeText(getApplicationContext(), "Tu institución aún no ha sido aprobada", Toast.LENGTH_LONG).show();
                }
                else{
                    if(anunciosRestantes.equals("0")){
                        Toast.makeText(getApplicationContext(), "Ya no tienes anuncios en tu plan", Toast.LENGTH_LONG).show();
                    }
                    else{
                        final AlertDialog.Builder builder = new AlertDialog.Builder(InstitucionesMain2Activity.this);
                        // Get the layout inflater
                        builder.setTitle("Nuevo anuncio");
                        builder.setMessage("Por favor llene los datos del anuncio requeridos");
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View formElementsView = inflater.inflate(R.layout.dialog_send_anuncio,
                                null, false);

                        editLinkAnuncio = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarAnuncioLink);
                        editMensajeAnuncio = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarAnuncioMensaje);
                        editTituloAnuncio = (TextInputEditText) formElementsView.findViewById(R.id.txtEnviarAnuncioTitulo);

                        builder.setTitle("Enviar Correo")
                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        int longTit = editTituloAnuncio.getText().toString().length();
                                        int longMensaje = editMensajeAnuncio.getText().toString().length();
                                        int longLink = editLinkAnuncio.getText().toString().length();

                                        if (longTit > 2 && longTit < 100){

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Título inválido", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                        if (longMensaje > 2 && longMensaje < 600){

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(), "Mensaje inválido", Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                                        Anuncios anuncio = new Anuncios(idActual + editTituloAnuncio.getText().toString(), editTituloAnuncio.getText().toString(), editMensajeAnuncio.getText().toString(),urlFotoActual,editLinkAnuncio.getText().toString(), nombreActual);
                                        tutorialRef.child(FirebaseReferences.ANUNCIOS_REFERENCE).push().setValue(anuncio).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                               int anunciosRest = Integer.valueOf(anunciosRestantes);
                                               int anunciosAct = anunciosRest - 1;
                                                Toast.makeText(getApplicationContext(), "Anuncio publicado con éxito", Toast.LENGTH_SHORT).show();
                                                tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).child(idActual).child("anuncios").setValue(Integer.toString(anunciosAct));
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putString("anunciosRestantes", Integer.toString(anunciosAct));
                                                anunciosRestantes = Integer.toString(anunciosAct);
                                                editor.commit();

                                                enviarNotificaciones("startup", "¡Tienes un nuevo anuncio de " + nombreActual + "!");
                                            }
                                        });
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
