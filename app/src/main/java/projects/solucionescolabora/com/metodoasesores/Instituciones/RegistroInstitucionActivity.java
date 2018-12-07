package projects.solucionescolabora.com.metodoasesores.Instituciones;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultorActivity;
import projects.solucionescolabora.com.metodoasesores.Consultores.RegistroConsultoresExpAcaActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Institucion;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.R;

public class RegistroInstitucionActivity extends AppCompatActivity {

    private ImageView imgAvatar;
    private TextInputEditText editNombre;
    private TextInputEditText editDescripcion;
    private TextInputEditText editUbicacion;
    private TextInputEditText editRFC;
    private Button btnFinalizar;

    private boolean fotoSelected = false;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_institucion);
        setTitle("Registro Instutición");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        imgAvatar = (ImageView)findViewById(R.id.imgRegistroInstitucionAvatar);
        editNombre = (TextInputEditText)findViewById(R.id.txtRegistroInstitucionNombre);
        editDescripcion = (TextInputEditText)findViewById(R.id.txtRegistroInstitucionDescripcion);
        editRFC = (TextInputEditText)findViewById(R.id.txtRegistroInstitucionRFC);
        editUbicacion = (TextInputEditText)findViewById(R.id.txtRegistroInstitucionUbicación);
        btnFinalizar = (Button)findViewById(R.id.btnRegistroInstitucionFinalizar);

        progressDialog = new ProgressDialog(RegistroInstitucionActivity.this);

        progressDialog.setTitle("Subiendo Información");
        progressDialog.setMessage("Espere un momento mientras el sistema sube su información a la base de datos");

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup().setTitle("Escoge una opción")
                        .setCameraButtonText("Cámara")
                        .setGalleryButtonText("Galería"))
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                //TODO: do what you have to...
                                imgAvatar.setImageBitmap(r.getBitmap());
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

        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean error = false;
                final String nombre = editNombre.getText().toString();
                final String descripcion = editDescripcion.getText().toString();
                final String rfc = editRFC.getText().toString();
                final String ubicacion = editUbicacion.getText().toString();

                if(nombre.length() < 2){
                    error = true;
                }
                else if (descripcion.length() < 2){
                    error = true;
                }
                else if (rfc.length() < 12 || rfc.length() > 13){
                    error = true;
                    editRFC.setError("Debes introducir un RFC válido");
                }
                else if (ubicacion.length() < 15){
                    error = true;
                    editUbicacion.setError("Debes escribir tu ubicación completa");
                }

                if (fotoSelected == false){
                    Toast.makeText(getApplicationContext(), "Debe seleccionar su logotipo", Toast.LENGTH_LONG).show();
                    return;
                }

                if (error == true){
                    Toast.makeText(getApplicationContext(),"Debe completar correctamente los campos requeridos", Toast.LENGTH_LONG).show();
                }
                else{
                    progressDialog.show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
                    final String UUIDUser = user.getUid();

                    imgAvatar.setDrawingCacheEnabled(true);
                    imgAvatar.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    //imgToUpload.layout(0, 0, imgToUpload.getMeasuredWidth(), imgToUpload.getMeasuredHeight());
                    imgAvatar.buildDrawingCache();
                    Bitmap bitmap = Bitmap.createBitmap(imgAvatar.getDrawingCache());

                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    byte[] data = outputStream.toByteArray();

                    StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones").child(UUIDUser + "-profile");

                    UploadTask uploadTask = refSubirImagen.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Logotipo subido con exito", Toast.LENGTH_SHORT).show();

                            Institucion institucion = new Institucion(UUIDUser, nombre,descripcion,UUIDUser + "-profile", ubicacion,rfc, "prueba","","","");

                            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.INSTITUCION_REFERENCE).child(UUIDUser).setValue(institucion).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Intent i = new Intent(RegistroInstitucionActivity.this, InstitucionesMain2Activity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("typeCurrentUser", "Institucion");
                                    editor.putString("registroCompleto", "Completo");
                                    editor.commit();
                                    // ******************************************************************************
                                    startActivity(i);
                                    finish();
                                }
                            });
                        }
                    });
                }

            }
        });


    }
}
