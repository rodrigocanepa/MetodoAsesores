package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.R;

public class ConsultorVerPDFActivity extends AppCompatActivity {

    private PDFView pdfView;
    private File file;
    private String direccion;
    private Button btnPDF;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultor_ver_pdf);

        pdfView = (PDFView)findViewById(R.id.pdfViewConsuloresPDF);
        btnPDF = (Button)findViewById(R.id.btnSubirPDFCV);

        progressDialog = new ProgressDialog(ConsultorVerPDFActivity.this);
        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");


        final Bundle bundle = getIntent().getExtras();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        final String UUIDUser = user.getUid();

        if(bundle != null){
            file = new File((bundle.getString("path","")));
            direccion = bundle.getString("path","");
        }

        btnPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(file.exists()){

                        Uri fileUri = Uri.fromFile(file);

                        if(bundle.getString("tipo", "").equals("CV")){
                            progressDialog.show();
                            String toTabla = "gs://metodo-asesores.appspot.com/Usuarios/Consultores/";
                            StorageReference refSubirImagen = storage.getReferenceFromUrl(toTabla).child(UUIDUser + "-CV.pdf");

                            UploadTask uploadTask = refSubirImagen.putFile(fileUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Intent i = new Intent(ConsultorVerPDFActivity.this, ConsultoresMainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("typeCurrentUser", "Consultor");
                                    editor.putString("registroCompleto", "Completo");
                                    editor.putString("idActual", "");
                                    editor.commit();
                                    // ******************************************************************************
                                    startActivity(i);

                                }
                            });
                        }

                        else if(bundle.getString("tipo", "").equals("Diagnostico1")){
                            progressDialog.show();
                            String toTabla = "gs://metodo-asesores.appspot.com/Usuarios/Startup/";
                            StorageReference refSubirImagen = storage.getReferenceFromUrl(toTabla).child(UUIDUser + "-Diagnostico1.pdf");

                            UploadTask uploadTask = refSubirImagen.putFile(fileUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Intent i = new Intent(ConsultorVerPDFActivity.this, ConsultoresMainActivity.class);
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Diagnóstico subido con éxito", Toast.LENGTH_LONG ).show();
                                    finish();
                                    //startActivity(i);

                                }
                            });
                        }

                        else if(bundle.getString("tipo", "").equals("Diagnostico2")){
                            progressDialog.show();
                            String toTabla = "gs://metodo-asesores.appspot.com/Usuarios/Startup/";
                            StorageReference refSubirImagen = storage.getReferenceFromUrl(toTabla).child(UUIDUser + "-Diagnostico2.pdf");

                            UploadTask uploadTask = refSubirImagen.putFile(fileUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Intent i = new Intent(ConsultorVerPDFActivity.this, ConsultoresMainActivity.class);
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Diagnóstico subido con éxito", Toast.LENGTH_LONG ).show();
                                    finish();
                                    //startActivity(i);

                                }
                            });
                        }
                        else if(bundle.getString("tipo", "").equals("Diagnostico3")){
                            progressDialog.show();
                            String toTabla = "gs://metodo-asesores.appspot.com/Usuarios/Startup/";
                            StorageReference refSubirImagen = storage.getReferenceFromUrl(toTabla).child(UUIDUser + "-Diagnostico3.pdf");

                            UploadTask uploadTask = refSubirImagen.putFile(fileUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Intent i = new Intent(ConsultorVerPDFActivity.this, ConsultoresMainActivity.class);
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Diagnóstico subido con éxito", Toast.LENGTH_LONG ).show();
                                    finish();
                                    //startActivity(i);

                                }
                            });
                        }
                        //************************************************
                        else if(bundle.getString("tipo", "").startsWith("Curso%")){
                            progressDialog.show();
                            String[] split = bundle.getString("tipo", "").split("%");
                            String id = split[1];
                            String toTabla = "gs://metodo-asesores.appspot.com/Usuarios/Consultores/";
                            StorageReference refSubirImagen = storage.getReferenceFromUrl(toTabla).child(UUIDUser + "-Curso-" + id + ".pdf");

                            UploadTask uploadTask = refSubirImagen.putFile(fileUri);
                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                }
                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    //Intent i = new Intent(ConsultorVerPDFActivity.this, ConsultoresMainActivity.class);
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(getApplicationContext(), "Diagnóstico subido con éxito", Toast.LENGTH_LONG ).show();
                                    Intent i = new Intent(ConsultorVerPDFActivity.this, VerMisCursosActivity.class);
                                    startActivity(i);
                                    finish();
                                    //startActivity(i);

                                }
                            });
                        }
                }
            }
        });

        pdfView.fromFile(file)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .enableAntialiasing(true)
                .load();
/*
        Intent i = new Intent(RegistroReconocimientosConsulActivity.this, PDFCVConsultoresActivity.class);
                            /*i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("typeCurrentUser", "Consultor");
                            editor.putString("registroCompleto", "Completo");
                            editor.commit();*/
        // ******************************************************************************
    }
}
