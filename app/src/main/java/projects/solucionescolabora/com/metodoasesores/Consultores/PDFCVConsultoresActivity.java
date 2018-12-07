package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaAcademica;
import projects.solucionescolabora.com.metodoasesores.Modelos.ExperienciaProfesional;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Reconocimientos;
import projects.solucionescolabora.com.metodoasesores.Modelos.TemplatePDF;
import projects.solucionescolabora.com.metodoasesores.R;

public class PDFCVConsultoresActivity extends AppCompatActivity {

    private String nombreConsultor = "";
    private String edad = "";
    private String ubicacion = "";
    private String especialidad = "";
    private String rfc = "";
    private String mentor = "";
    private String urlConsultor = "";

    private List<ExperienciaAcademica> experienciaAcademicas = new ArrayList<>();
    private List<ExperienciaProfesional> experienciaProfesionals = new ArrayList<>();
    private List<Reconocimientos> reconocimientos = new ArrayList<>();

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private String UUIDUser;

    // GENERACION DE PDF
    private TemplatePDF templatePDF;
    private String formattedDate ="";
    private String[]headerExpAcaemica = {"Grado", "Escuela", "Materia de estudio", "Generación"};
    private String[]headerExpProfesional = {"Puesto", "Empresa", "Descripción", "Período"};
    private String[]headerReconocimientos = {"Otorgado por", "Nombre", "Año"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfcvconsultores);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDUser = user.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

        tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.CONSULTOR_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Consultor consultor = snapshot.getValue(Consultor.class);

                    if(consultor.getId().equals(UUIDUser)){
                        nombreConsultor = consultor.getNombres() + " " + consultor.getApellidos();
                        edad = "Edad:" + consultor.getEdad() + " años";
                        ubicacion = "Ubicación: " + consultor.getUbicacion();
                        rfc = "RFC: " + consultor.getRFC();
                        urlConsultor = consultor.getUrlFoto();
                        mentor = "Mentor desde: " + consultor.getAnioMentor();
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
                        especialidad = carreras;

                        if (especialidad.equals("TecnologiaInnovacion")){
                            especialidad = "Tecnología e innovación";
                        }
                        else if (especialidad.equals("DisenoDigital")){
                            especialidad = "Diseño digital";
                        }
                        else if (especialidad.equals("Administracion")){
                            especialidad = "Administración";
                        }
                        else if (especialidad.equals("ProteccionIntelectual")){
                            especialidad = "Protección intelectual";
                        }
                        else if (especialidad.equals("DesarrolloSocial")){
                            especialidad = "Desarrollo social";
                        }
                        else if (especialidad.equals("DisenoIndustrial")){
                            especialidad = "Diseño industrial";
                        }


                        experienciaAcademicas = consultor.getExperienciaAcademica();
                        experienciaProfesionals = consultor.getExperienciaProfesional();

                        if(consultor.getReconocimientos() != null){
                            reconocimientos = consultor.getReconocimientos();
                        }
                    }
                }

                if(urlConsultor.length() > 2){
                    // ********* IMPORTANTE CAMBIAR ************
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Consultores").child(UUIDUser + "-profile");
                    final long ONE_MEGABYTE = 1024 * 1024;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {


                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            ArrayList<String[]> rowsAcedemica = new ArrayList<>();
                            ArrayList<String[]> rowsProfesional = new ArrayList<>();
                            ArrayList<String[]> rowsReconocimientos = new ArrayList<>();

                            for(int i = 0; i < experienciaAcademicas.size(); i++){
                                rowsAcedemica.add(new String[] {experienciaAcademicas.get(i).getGrado(), experienciaAcademicas.get(i).getEscuela(),  experienciaAcademicas.get(i).getMateria(), experienciaAcademicas.get(i).getGeneracion()});
                            }

                            for(int i = 0; i < experienciaProfesionals.size(); i++){
                                rowsProfesional.add(new String[] {experienciaProfesionals.get(i).getPuesto(), experienciaProfesionals.get(i).getNombreEmpresa(), experienciaProfesionals.get(i).getDescripcion(), experienciaProfesionals.get(i).getPeriodo()});
                            }

                            if(reconocimientos.size() > 0){
                                for (int i = 0; i < reconocimientos.size(); i++){
                                    rowsReconocimientos.add(new String[] {reconocimientos.get(i).getOtorgadoPor(), reconocimientos.get(i).getDescripcion(), reconocimientos.get(i).getAnio()});
                                }
                            }


                            String datos = edad + "\n" + rfc + "\n" + ubicacion + "\n" + mentor;

                            templatePDF = new TemplatePDF(getApplicationContext());
                            templatePDF.openDocument();
                            templatePDF.addMetaData("CV Consultor", "Metodo Asesores", "Soluciones Colabora");
                            templatePDF.addImage(getApplicationContext());
                            templatePDF.createTableWithFoto(getApplicationContext(), bitmap, nombreConsultor, especialidad, datos);
                           // templatePDF.addParagraph(nombreConsultor);
                            templatePDF.addSections("Experiencia Académica");
                            templatePDF.createTableWithTheSameLength(headerExpAcaemica, rowsAcedemica);
                            templatePDF.addSections("Experiencia Profesional");
                            templatePDF.createTableWithTheSameLength(headerExpProfesional, rowsProfesional);

                            if(rowsReconocimientos.size() > 0){
                                templatePDF.addSections("Reconocimientos");
                                templatePDF.createTableReconocimientos(headerReconocimientos, rowsReconocimientos);
                            }
                            /*templatePDF.addParagraph("Contribuyente");
                            templatePDF.createTableWithTheSameLength(headerContribuyente, rows);
                            templatePDF.addParagraph("Fecha y hora del reporte");
                            templatePDF.addSections(formattedDate);
                            templatePDF.addParagraph("Estimado Cliente:");
                            templatePDF.addParagraph("Hemos realizado la búsqueda del contribuyente arriba mencionado en todas las listas que a la fecha han sido publicadas en la página del SAT y/o en el Diario Oficial de la Federación relacionadas con Empresas que Facturan Operaciones Simuladas (efos) en términos del artículo 69-B del Código Fiscal de la Federación (Listas presuntas, definitivas y desvirtuados); en la lista vigente de los contribuyentes “no localizados” a que se refiere el artículo 69 del mismo código; así como en la relación de contribuyentes notificados vías estrados vinculadas con el tema del mismo artículo 69-B antes mencionado, arrojándo el siguiente resultado:");

                            if(listaCondonados.getSUPUESTO().equals("CONDONADOS")){
                                templatePDF.addSections("Contribuyente en Lista de Condonados");
                            }

                            templatePDF.addParagraph("Detalle de la lista en la que aparece:");
                            templatePDF.createTableWithTheSameLength(headerContribuyente, rows2);
                            templatePDF.addParagraph("Advertencia. El presente reporte se elaboró a partir de los caracteres capturados en el recuadro de búsqueda del sistema, ecomendamos siempre utilizar el RFC como criterio de búsqueda preferencial, verificándo siempre que éste sea correcto. Despacho CGF® actualiza constantemente su información por lo que el resultado aquí mostrado es suceptible de ser modificado en cualquier momento posterior. Nuestra base de datos se alimenta estrictamente de la información pública que periódicamente va dando a conocer tanto el SAT como el Diario Oficial de la Federación, por lo que efosmx® no tiene control alguno sobre la seguridad, legalidad, veracidad o exactitud del contenido. La información sobre notificaciones vía estrados es a partir del día 27 de abril de 2016 a la fecha. El hecho de que el contribuyente buscado no haya sido publicado a esta fecha y hora en cualquiera de las listas mencionadas, no significa una garantía de que el mismo no será publicado en el futuro, por lo que este reporte no constituye de ninguna manera una autorización para realizar operaciones con el contribuyente antes mencionado. efos.mx® no sustituye de ninguna manera la obligación legal de revisar por cuenta propia las publicaciones que se realizan en la página del SAT así como las del Diario Oficial de la Federación a que se refiere el artículo 69-B del Código Fiscal de la Federación, por lo que Soluciones Colabora, SAPI de C.V. no serán responsable de daños, perjuicios, ni consecuencias legales derivadas de las decisiones, prácticas, acciones u omisiones en la actuación de cualquier persona como resultado de este reporte, quedando exonerada de cualquier tipo de responsabilidad por posibles daños y perjuicios, aun y cuando se demuestre que la persona actuó atendiendo a la información contenida en este reporte o en el sistema.");
        */
                            templatePDF.closeDocument();

                            templatePDF.viewPDF("CV");





                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    });
                }




            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
