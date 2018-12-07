package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import projects.solucionescolabora.com.metodoasesores.Consultores.ConsultorVerPDFActivity;
import projects.solucionescolabora.com.metodoasesores.R;

public class VerCurriculumsConsultoresActivity extends AppCompatActivity {

    private PDFView pdf;
    private File file;
    Intent intent;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    public static final String INTENT_ID_CONSULTOR = "projects.solucionescolabora.com.metodoasesores.Instituciones.id_consultor";
    public static final String TYPE = "projects.solucionescolabora.com.metodoasesores.Instituciones.TYPE";
    public static final String TITULO = "projects.solucionescolabora.com.metodoasesores.Instituciones.TITULO";
    private ProgressDialog progressDialogDescargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_curriculums_consultores);

        pdf = (PDFView)findViewById(R.id.pdfInstitucionesVerCVconsultores);

        Intent i = getIntent();
        String id = i.getStringExtra(INTENT_ID_CONSULTOR);
        String type = i.getStringExtra(TYPE);
        String tit = i.getStringExtra(TITULO);
        String referecia = "gs://metodo-asesores.appspot.com/Usuarios/Consultores/";

        progressDialogDescargando = new ProgressDialog(VerCurriculumsConsultoresActivity.this);
        progressDialogDescargando.setTitle("Descargando información");
        progressDialogDescargando.setMessage("Espere un momento por favor mientras el sistema descarga el PDF");

        progressDialogDescargando.show();

       // StorageReference storageRef = storage.getReferenceFromUrl(referecia).child(id + "-CV");

        /*File folder = new File(Environment.getExternalStorageDirectory().toString(), "Archivos de curriculums");

        if(!folder.exists())
            folder.mkdirs();
        //pdfFile = new File(folder, "TemplatePDF.pdf");

        StorageReference islandRef = storageRef.child("condonados_3.jpg");

        File localFile = null;
        //localFile = File.createTempFile("condonados", "json");
        localFile = new File(Environment.getExternalStorageDirectory().toString(), "condonados.json");

        islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Toast.makeText(getApplicationContext(), "Descargado con exito", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/

        if(type.equals("curriculum")){
            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(id + "-CV.pdf");
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    pdf.fromBytes(bytes)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .enableAntialiasing(true)
                            .load();

                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            });
        }

        if(type.equals("temario")){
            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(id + "-Curso-" + tit + ".pdf");
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    pdf.fromBytes(bytes)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .enableAntialiasing(true)
                            .load();
                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            });
        }

        if(type.equals("diagnostico")){
            StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(id + "-Diagnostico2.pdf");
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    pdf.fromBytes(bytes)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .enableAntialiasing(true)
                            .load();
                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    if(progressDialogDescargando.isShowing()){
                        progressDialogDescargando.dismiss();
                    }
                }
            });
        }
    }
}
