package projects.solucionescolabora.com.metodoasesores.Cuestinario;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import projects.solucionescolabora.com.metodoasesores.Graficas.RadarMarkerView;
import projects.solucionescolabora.com.metodoasesores.Modelos.PreguntasDiagnostico;
import projects.solucionescolabora.com.metodoasesores.Modelos.TemplatePDF;
import projects.solucionescolabora.com.metodoasesores.R;

public class OtrasAreasActivity extends AppCompatActivity {

    private TextView txtArea;
    private TextView txtPregunta;
    private Button btnSi;
    private Button btnNo;
    private Button btnNoSe;
    private Button btnSiguiente;

    private int contador = 0;
    private int contadorResponsabilidad = 0;
    private int contadorGobierno = 0;
    private int contadorComercio = 0;
    private List<PreguntasDiagnostico> preguntas = new ArrayList<>();
    private boolean respondida = false;
    private SharedPreferences misDatos;

    // GENERACION DE PDF
    private TemplatePDF templatePDF;
    private String formattedDate ="";
    private String[]headerPreguntas = {"Pregunta", "Respuesta"};

    private RadarChart mChart;
    protected Typeface mMontserrat;
    private String UUIDUser = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otras_areas);

        txtArea = (TextView)findViewById(R.id.txtAreaOtrasAreas);
        txtPregunta = (TextView)findViewById(R.id.txtOtrasAreasPregunta);
        btnSi = (Button) findViewById(R.id.btnOtrasAreasSi);
        btnNo = (Button) findViewById(R.id.btnOtrasAreasNo);
        btnNoSe = (Button) findViewById(R.id.btnOtrasAreasNoSe);
        btnSiguiente = (Button) findViewById(R.id.btnOtrasAreasSiguientePregunta);
        mChart = (RadarChart)findViewById(R.id.chartOtrasAreasDiagnostico);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDUser = user.getUid();

        // ********************************************** GRÁFICA ****************************************************
        mMontserrat = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf");
        mChart.getDescription().setEnabled(false);

        mChart.setWebLineWidth(1f);
        mChart.setWebColor(Color.BLACK);
        mChart.setWebLineWidthInner(1f);
        mChart.setWebColorInner(Color.BLACK);
        mChart.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        MarkerView mv = new RadarMarkerView(getApplicationContext(), R.layout.radar_markerview);
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
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mChart.getYAxis();
        yAxis.setTypeface(mMontserrat);
        yAxis.setLabelCount(6, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(9f);
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
        mChart.setVisibility(View.INVISIBLE);
        // ***********************************************************************************************************
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "1. Se asume el cumplimiento de las normas ambientales como compromiso en todas partes de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "2. La empresa cumple con todas sus obligaciones legales y fiscales.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "3. La empresa no ha sido acreedor a sanciones por parte de autoridades gubernamentales.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "4. Se tienen programas internos de prevención de riesgos a la salud y la seguridad de los trabajadores.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "5. Se tiene transparencia en la información al publico consumidor delos posibles riesgos de los productos o servicios que ofrece la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "6. Se tienen políticas comerciales que garantizan la honradez en todas sus transacciones.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "7. Se tienen procedimientos de control y sanación ante posibles practicas corruptas.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "8. Se brinda gratuitamente a los trabajadores equipo de seguridad necesario para el desempeño de sus labores.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "9. La empresa participa constantemente en programas sociales altruistas.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Responsabilidad Social Empresarial", "10. Los pagos a sus proveedores se llevan a cabo en tiempo y forma.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "11. Se cuenta con órganos de gobierno (como un Consejo de Administración y Comités) que aseguren la atención de los temas medulares del negocio.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "12. En los órganos de gobierno con que se cuenta, participan consejeros independientes cual experiencia es útil a los propósitos del negocio.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "13. Los participantes en los órganos de gobierno tienen claramente definidas las reglas de operación de manera que conozcan sus roles y responsabilidades.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "14. En los órganos de gobierno se tienen planes anuales y calendario de sesiones conocidos para todos los miembros.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "15. Se lleva un registro de todos los recuerdos de los órganos de gobierno y se da seguimiento a los mismos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "16. Se cuenta con un plan de sucesión formal para la dirección general y los puestos clave de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "17. Se tiene asignado un órgano de gobierno responsable de definir las remuneraciones a consejeros, director general y directivos claves así como sus compensaciones, en función de las condiciones reales de la empresa y el mercado.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "18. Se cuenta con un código de ética así como de un proceso formal para recibir y atender las sugerencias quejas y denuncias de accionistas, consejeros, empleados y terceros.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "19. Se cuenta con un proceso formal para evaluar objetivamente el desempeño anual de consejeros y directivos clave con e fin de definir su continuidad en el negocio.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Gobierno Corporativo", "20. En el proceso de toma de decisiones se tiene una comunicación adecuada y ágil con los accionistas y directivos clave.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "21. Se cuenta con productos y/o servicios listos para ser exportados a otros países del mundo.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "22. Se tienen identificados mercados en los que exista interés (demanda) por los productos y/o servicios de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "23. Se cuenta con interés formal de parte de entidades (empresa o particulares) externos respecto a los productos y/o servicios de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "24. Se conocen los documentos y tramites a realizar relacionados con el proceso de exportación al país o países de interés.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "25. Se conocen las regulaciones arancelarias y no arancelarias relacionadas con el proceso de exportación al país o países de interés.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "26. Se han analizado los requerimientos de empaque relacionados con la exportación del producto.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "27. Se tiene conocimiento y se aprovechan los apoyos gubernamentales relacionados con la exportación.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "28. En la empresa, se cuenta con personal capacitado y con experiencia en comercio internacional.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "29. En la empresa, se cuenta con personal que habla el idioma ingles o el idioma el país o países a exportar.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Comercio Exterior", "30. Se conoce en forma detallada los términos de los contratos internacionales y del tema de soluciones de controversias.", false, ""));

        setPreguntas();

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSi.setBackgroundColor(Color.GREEN);
                btnNo.setBackgroundColor(Color.GRAY);
                btnNoSe.setBackgroundColor(Color.GRAY);
                respondida = true;
                if(contador<30){
                    preguntas.get(contador).setRespuesta("Si");
                }
            }
        });

        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSi.setBackgroundColor(Color.GRAY);
                btnNo.setBackgroundColor(Color.GREEN);
                btnNoSe.setBackgroundColor(Color.GRAY);
                respondida = true;
                if(contador<30){
                    preguntas.get(contador).setRespuesta("No");
                }
            }
        });

        btnNoSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSi.setBackgroundColor(Color.GRAY);
                btnNo.setBackgroundColor(Color.GRAY);
                btnNoSe.setBackgroundColor(Color.GREEN);
                respondida = true;
                if(contador<30){
                    preguntas.get(contador).setRespuesta("No sé");
                }
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contador == 30){

                }

                if(respondida == true){

                    if(contador >= 0 && contador < 10){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorResponsabilidad++;
                        }
                    }
                    else if (contador >= 10 && contador < 20){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorGobierno++;
                        }
                    }
                    else if (contador >= 20 && contador < 30){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorComercio++;
                            if(contador == 29){
                                // *********** Guardamos el dato de la ubicación actual del usuario *************

                                misDatos = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = misDatos.edit();

                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());
                                editor.putString(UUIDUser + "diagnosticoOtrosDatos", date);
                                editor.commit();

                                mChart.setVisibility(View.VISIBLE);
                                setData(contadorResponsabilidad, contadorGobierno, contadorComercio);

                                ArrayList<String[]> respuestas = new ArrayList<>();
                                ArrayList<String[]> respuestasVentas = new ArrayList<>();
                                ArrayList<String[]> respuestasContabilidad = new ArrayList<>();

                                templatePDF = new TemplatePDF(getApplicationContext());
                                templatePDF.openDocument();
                                templatePDF.addMetaData("Diagnostico Otras Areas", "Metodo Asesores", "Soluciones Colabora");
                                templatePDF.addImage(getApplicationContext());
                                templatePDF.addTitles("DIAGNÓSTICO: OTRAS ÁREAS", "Debido a la estadística de crecimiento de las empresas, nos preocupamos por conocer las áreas de oportunidades donde podemos aportarles un beneficio para sus negocios garantizando la mejora continua. El siguiente diagnostico está diseñado para conocer un poco más sobre su empresa y necesidades.",date);
                                templatePDF.addParagraph("Las siguientes preguntas son para determinar un diagnóstico más personalizado sobre los requerimientos que considera que pueda tener en su empresa. Esto será analizado por consultores expertos. Toda la información proporcionada será únicamente para fines de análisis empresarial y no será publicado o reproducido por algún otro medio. A partir de este momento nos comprometemos a su confidencialidad.");

                                for(int i = 0; i < 10; i ++){
                                    respuestas.add(new String[]{preguntas.get(i).getPregunta(), preguntas.get(i).getRespuesta()});
                                }
                                for(int i = 10; i < 20; i ++){
                                    respuestasVentas.add(new String[]{preguntas.get(i).getPregunta(), preguntas.get(i).getRespuesta()});
                                }
                                for(int i = 20; i < 30; i ++){
                                    respuestasContabilidad.add(new String[]{preguntas.get(i).getPregunta(), preguntas.get(i).getRespuesta()});
                                }
                                templatePDF.addSections("Responsabilidad Social Empresarial");
                                templatePDF.createTableWith2cell(headerPreguntas, respuestas);

                                templatePDF.addSections("Gobierno Corporativo");
                                templatePDF.createTableWith2cell(headerPreguntas, respuestasVentas);

                                templatePDF.addSections("Comercio Exterior");
                                templatePDF.createTableWith2cell(headerPreguntas, respuestasContabilidad);

                                templatePDF.addSections("Resultados Generales");

                                int suma = contadorComercio + contadorGobierno + contadorResponsabilidad;
                                if(suma > 0 && suma <= 11 ){
                                    templatePDF.addParagraph("Tu emprendimiento está en una fase de desarrollo y planeación, no es una empresa formal, es necesario trabajar en aspectos básicos del plan de negocios, buscar asesoría profesional, repasar la terminología de administración, aspectos legales, contables, financieros, ventas y mercadotecnia.");
                                }
                                else if (suma > 11 && suma <= 15 ){
                                    templatePDF.addParagraph("Tienes un conocimiento básico de lo que es una empresa y las partes que la componen, sin embargo tu estructura no está bien definida, probablemente aun no tengas definido cual es tu visión o misión como empresa, y no hayas creado estrategias que te lleven a cumplir las metas propuestas, es necesario asesorarte en cada una de las áreas.");
                                }
                                else if (suma > 15 && suma <= 23 ){
                                    templatePDF.addParagraph("Tu emprendimiento está tomando forma como una empresa formal, tienes un conocimiento amplio pero no suficiente de lo que es una empresa, y de todo lo que involucra, tienes la teoría necesaria para continuar con tu proyecto, pero no la experiencia práctica para aterrizarlo con éxito, tus debilidades son en temas puntuales, y probablemente ya seas experto en más de una área, por lo cual es importante reforzar las áreas débiles.");
                                }
                                else if (suma > 23 && suma <= 30 ){
                                    templatePDF.addParagraph("Felicitaciones, tienes los conocimientos necesarios para llevar al éxito tu emprendimiento, tienes un dominio amplio en cada una de las áreas, sin embargo tienes algunos detalles de desinformación que si los dejas pasar, pueden perjudicar la operación de tu empresa, se te recomienda atender esos detalles puntuales con consultores experimentados.");
                                }
                                Bitmap bitmap;
                                mChart.setDrawingCacheEnabled(true);
                                bitmap = Bitmap.createBitmap(mChart.getDrawingCache());
                                mChart.setDrawingCacheEnabled(false);

                                templatePDF.createGraficaFoto(getApplicationContext(), bitmap);

                                Toast.makeText(getApplicationContext(), "Diagnóstico completado con éxito", Toast.LENGTH_LONG).show();
                                templatePDF.closeDocument();

                                templatePDF.viewPDF("Diagnostico3");
                                finish();
                            }
                        }
                    }

                    if(contador == 59){

                    }
                    else{
                        contador++;
                        setPreguntas();
                        btnSi.setBackgroundColor(Color.WHITE);
                        btnNo.setBackgroundColor(Color.WHITE);
                        btnNoSe.setBackgroundColor(Color.WHITE);
                        respondida = false;
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Debes responder la pregunta antes de continuar", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private void setPreguntas(){
        if(contador<30){
            txtArea.setText(preguntas.get(contador).getTitulo());
            txtPregunta.setText(preguntas.get(contador).getPregunta());

            if(contador == 29){
                btnSiguiente.setText("Finalizar");
            }
        }

    }

    public void setData(int puntosPlaneacion, int puntosVentas, int puntosContabilidad) {


        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        entries1.add(new RadarEntry(puntosPlaneacion));
        entries1.add(new RadarEntry(puntosVentas));
        entries1.add(new RadarEntry(puntosContabilidad));

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
}
