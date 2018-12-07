package projects.solucionescolabora.com.metodoasesores.Fragments;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Graficas.RadarMarkerView;
import projects.solucionescolabora.com.metodoasesores.Instituciones.VerCurriculumsConsultoresActivity;
import projects.solucionescolabora.com.metodoasesores.MainActivity;
import projects.solucionescolabora.com.metodoasesores.Modelos.Anuncios;
import projects.solucionescolabora.com.metodoasesores.Modelos.Consultor;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.NotificationHelper;
import projects.solucionescolabora.com.metodoasesores.R;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RadarChart mChart;
    protected Typeface mMontserrat;
    private TextView tv;
    //private ImageView imgFondo;
    private CircleImageView imgAvatar;
    private TextView txtNombreStartup;
    private TextView txtNombreEmprededor;

    private SharedPreferences sharedPreferences;
    private NotificationHelper notificationHelper;

    private int contadorPlaneacion = 0;
    private int contadorVentas = 0;
    private int contadorContabilidad = 0;
    private int contadorLegal = 0;
    private int contadorInnovacion = 0;
    private int contadorProcuracion = 0;

    private String UUIDUser = "";
    private FloatingActionButton fab;
    private boolean estaRespondido = false;

    private String url = "";
    private String startupNombre = "";
    private String nombre = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();

    private String idActual = "";
   // private String nombreActual = "";
   // private String especialidadActual = "";
   // private String urlFotoActual = "";
   // private boolean saveFoto = false;
  //  private String correoActual = "";

    private CarouselView customCarouselView;
    private List<Anuncios> anuncios = new ArrayList<>();

    private LinearLayout linearLayout;

    private ImageView imgCarousel;
    private TextView txtTituloCarousel;
    private TextView txtNombreCarousel;
    private TextView txtDescripcionCarousel;
    private Button btnCarousel;

    List<Bitmap> bitmaps = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mMontserrat = Typeface.createFromAsset(getActivity().getAssets(), "Montserrat-Regular.ttf");
        imgAvatar = (CircleImageView)view.findViewById(R.id.imgAvatarHome);
        txtNombreStartup = (TextView)view.findViewById(R.id.txtAvatarStartupNameHome);
        txtNombreEmprededor = (TextView)view.findViewById(R.id.txtAvatarUsuarioNameHome);
        //imgFondo = (ImageView)view.findViewById(R.id.imgHomeFondo);
        fab = (FloatingActionButton)view.findViewById(R.id.fabHomeStartupDiag);
        linearLayout = (LinearLayout)view.findViewById(R.id.linearDiagnostico);
        customCarouselView = (CarouselView) view.findViewById(R.id.carouselView);


        //imgFondo.setColorFilter(Color.argb(180,10,10,10), PorterDuff.Mode.DARKEN);

        sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");
        /*nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");
        urlFotoActual = sharedPreferences.getString("urlFotoActual","");
        saveFoto = sharedPreferences.getBoolean("saveFoto",false);*/

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment initialFragment = new DiagnosticoFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
            }
        });
/*

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

                                StorageReference refSubirImagen = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(idActual + "-profile");

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

                                        Toast.makeText(getActivity(), "Foto subida con exito", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        })
                        .setOnPickCancel(new IPickCancel() {
                            @Override
                            public void onCancelClick() {
                                //TODO: do what you have to if user clicked cancel
                            }
                        }).show(getActivity().getSupportFragmentManager());
            }
        });

*/



        sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
        if(sharedPreferences.getString(idActual + "diagnosticoEstatusActual","").length() > 2){
            contadorContabilidad = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualContabilidad", "0"));
            contadorInnovacion = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualInnovacion", "0"));
            contadorLegal = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualLegal", "0"));
            contadorPlaneacion = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualPlaneacion", "0"));
            contadorProcuracion = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualProcuracion", "0"));
            contadorVentas = Integer.valueOf(sharedPreferences.getString(idActual + "diagnosticoEstatusActualVentas", "0"));
            estaRespondido = true;
        }
        else{
            estaRespondido = false;
        }
        //tv = view.findViewById(R.id.textView);
        //tv.setTypeface(mMontserrat);
        //tv.setTextColor(Color.WHITE);
        //tv.setBackgroundColor(Color.rgb(60, 65, 82));

        mChart = view.findViewById(R.id.chart1);
        //mChart.setBackgroundColor(Color.rgb(60, 65, 82));

        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.LTGRAY);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.LTGRAY);
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(getActivity(), R.layout.radar_markerview);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart


        //mChart.animateXY(2000, 2000);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTypeface(mMontserrat);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"Planeación", "Ventas", "Contabilidad", "Legal", "Innovación", "Procuración"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.WHITE);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(mMontserrat);
        yAxis.setLabelCount(6, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(true);
        l.setTypeface(mMontserrat);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);

        mChart.getLegend().setEnabled(false);

        setData(contadorPlaneacion, contadorVentas, contadorContabilidad, contadorLegal, contadorInnovacion, contadorProcuracion);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (estaRespondido){
                    String mensaje = "";
                    int suma = contadorContabilidad + contadorProcuracion + contadorInnovacion + contadorLegal + contadorVentas + contadorPlaneacion;
                    if(suma > 0 && suma <= 21 ){
                        mensaje = "Tu emprendimiento está en una fase de desarrollo y planeación, no es una empresa formal, es necesario trabajar en aspectos básicos del plan de negocios, buscar asesoría profesional, repasar la terminología de administración, aspectos legales, contables, financieros, ventas y mercadotecnia.";
                    }
                    else if (suma > 21 && suma <= 30 ){
                        mensaje = "Tienes un conocimiento básico de lo que es una empresa y las partes que la componen, sin embargo tu estructura no está bien definida, probablemente aun no tengas definido cual es tu visión o misión como empresa, y no hayas creado estrategias que te lleven a cumplir las metas propuestas, es necesario asesorarte en cada una de las áreas.";
                    }
                    else if (suma > 30 && suma <= 45 ){
                        mensaje = "Tu emprendimiento está tomando forma como una empresa formal, tienes un conocimiento amplio pero no suficiente de lo que es una empresa, y de todo lo que involucra, tienes la teoría necesaria para continuar con tu proyecto, pero no la experiencia práctica para aterrizarlo con éxito, tus debilidades son en temas puntuales, y probablemente ya seas experto en más de una área, por lo cual es importante reforzar las áreas débiles.";
                    }
                    else if (suma > 45 && suma <= 60 ){
                        mensaje = "Felicitaciones, tienes los conocimientos necesarios para llevar al éxito tu emprendimiento, tienes un dominio amplio en cada una de las áreas, sin embargo tienes algunos detalles de desinformación que si los dejas pasar, pueden perjudicar la operación de tu empresa, se te recomienda atender esos detalles puntuales con consultores experimentados.";
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Información");
                    builder.setMessage(mensaje)
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            })
                            .setNeutralButton("Actividades sugeridas", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(getActivity(), VerCurriculumsConsultoresActivity.class);
                                    i.putExtra(VerCurriculumsConsultoresActivity.INTENT_ID_CONSULTOR, idActual);
                                    i.putExtra(VerCurriculumsConsultoresActivity.TYPE, "diagnostico");
                                    startActivity(i);
                                }
                            })
                            .setNegativeButton("Ver consultores adecuados", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                    Fragment initialFragment = new ConsultoresFragment();
                                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
                                }
                            });
                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Información");
                    builder.setMessage("En esta sección podrás ver la gráfica con los resultados correspondientes a la evaluación que resulta de los diagnósticos que respondas, pero para poder visualizar dicha gráfica primero debes responder los cuestionarios que se encuentras en el menú lateral de la aplicación en la sección 'Diagnóstico'.")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // FIRE ZE MISSILES!
                                }
                            });                    // Create the AlertDialog object and return it
                    builder.create();
                    builder.show();
                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

        tutorialRef.child(FirebaseReferences.ANUNCIOS_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Anuncios anuncio = snapshot.getValue(Anuncios.class);

                    anuncios.add(anuncio);
                }

                for(int i = 0; i < anuncios.size(); i++){
                    StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Instituciones").child(anuncios.get(i).getUrlInstitucion());
                    final long ONE_MEGABYTE = 1024 * 1024;
                    final int finalI = i;
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            bitmaps.add(bitmap);

                            if(finalI == anuncios.size() - 1){
                                if(anuncios.size() > 0){
                                    // set ViewListener for custom view
                                    if(anuncios.size() == bitmaps.size()){
                                        customCarouselView.setViewListener(viewListener);
                                        customCarouselView.setPageCount(anuncios.size());
                                    }


                                }
                            }
                        }
                    });


                }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

    public void setData(int puntosPlaneacion, int puntosVentas, int puntosContabilidad, int puntosLegal, int puntosInnovacion, int puntosProcuracion) {


        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        entries1.add(new RadarEntry(puntosPlaneacion * 10));
        entries1.add(new RadarEntry(puntosVentas * 10));
        entries1.add(new RadarEntry(puntosContabilidad * 10));
        entries1.add(new RadarEntry(puntosLegal * 10));
        entries1.add(new RadarEntry(puntosInnovacion * 10));
        entries1.add(new RadarEntry(puntosProcuracion * 10));


        RadarDataSet set1 = new RadarDataSet(entries1, "");
        //set1.setColor(Color.rgb(103, 110, 129));
        //set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setColor(Color.rgb(121, 162, 175));
        set1.setFillColor(Color.rgb(121, 162, 175));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(false);

        /*RadarDataSet set2 = new RadarDataSet(entries2, "Diagnóstico Actual");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);*/

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        //sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTypeface(mMontserrat);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();
    }

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(final int position) {
            View customView = getLayoutInflater().inflate(R.layout.carousel_view, null);
            //set view attributes here

            imgCarousel = (ImageView)customView.findViewById(R.id.imgCarouselStartup);
            txtTituloCarousel = (TextView)customView.findViewById(R.id.txtCarouselTituloInstitucion);
            txtDescripcionCarousel = (TextView)customView.findViewById(R.id.txtCarouselDescripcionInstitucion);
            txtNombreCarousel = (TextView)customView.findViewById(R.id.txtCarouselInstitucionNombre);
            btnCarousel = (Button) customView.findViewById(R.id.btnCarousel);

            txtTituloCarousel.setText(anuncios.get(position).getTitulo());
            txtNombreCarousel.setText(anuncios.get(position).getNombreInstitucion());
            txtDescripcionCarousel.setText(anuncios.get(position).getDescripcion());

            imgCarousel.setImageBitmap(bitmaps.get(position));

            btnCarousel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uriUrl = Uri.parse(anuncios.get(position).getLink());
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);
                }
            });

            return customView;
        }
    };
}
