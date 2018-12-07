package projects.solucionescolabora.com.metodoasesores.Cuestinario;

import android.app.ProgressDialog;
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

public class EstatusActualActivity extends AppCompatActivity {

    private TextView txtArea;
    private TextView txtPregunta;
    private Button btnSi;
    private Button btnNo;
    private Button btnNoSe;
    private Button btnSiguiente;

    private int contador = 0;
    private int contadorPlaneacion = 0;
    private int contadorVentas = 0;
    private int contadorContabilidad = 0;
    private int contadorLegal = 0;
    private int contadorInnovacion = 0;
    private int contadorProcuracion = 0;
    private List<PreguntasDiagnostico> preguntas = new ArrayList<>();
    private List<String> actividades = new ArrayList<>();
    private boolean respondida = false;
    private SharedPreferences misDatos;

    // GENERACION DE PDF
    private TemplatePDF templatePDF;
    private String formattedDate ="";
    private String[]headerPreguntas = {"Pregunta", "Actividad"};

    private RadarChart mChart;
    protected Typeface mMontserrat;
    private String UUIDUser = "";

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estatus_actual);

        txtArea = (TextView)findViewById(R.id.txtAreaEstatusActual);
        txtPregunta = (TextView)findViewById(R.id.txtEstatusActualPregunta);
        btnSi = (Button) findViewById(R.id.btnEstatusActualSi);
        btnNo = (Button) findViewById(R.id.btnEstatusActualNo);
        btnNoSe = (Button) findViewById(R.id.btnEstatusActualNoSe);
        btnSiguiente = (Button) findViewById(R.id.btnEstatusActualSigPregunta);
        mChart = (RadarChart)findViewById(R.id.chartEstatusActualDiagnostico);

        progressDialog = new ProgressDialog(EstatusActualActivity.this);

        progressDialog.setTitle("Descargando información");
        progressDialog.setMessage("Espere un momento");


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
        yAxis.setAxisMaximum(9);
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
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "1. Se tienen metas y objetivos claros y específicos para los próximos 3 a 5 años.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "2. Se tienen identificados los factores del entorno cuyo comportamiento más podrían afectar a su empresa y sus productos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "3. Se monitorean las señales de cambio en el entorno puedan llegar a convertirse en oportunidades o amenazas.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "4. La estrategia está  orientada a los segmentos de mercado que representan las mejores oportunidades en forma sostenible a largo plazo, con demandas grandes y/o crecientes.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "5. Se conoce la participación en el mercado y la de los competidores.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "6. Se conoce la situación y tendencia del sector industrial o giro en la que se encuentra la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "7. Se conoce la forma en que los clientes usan o aplican los productos para atender a sus necesidades y que es lo que determina sus preferencias.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "8. Se tienen identificadas las características distintivas del producto o servicio que son ventajas competitivas, así como las desventajas y se sabe cómo superarlas.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "9. Existen barreras de entrada al mercado en que se compite difíciles de superar.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Planeación Estratégica", "10. Se tiene un fuerte poder de negociación ante los clientes y/o proveedores.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "11. Se tienen estrategias de comercialización y se evalúa su impacto en función de la satisfacción de los clientes.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "12. La empresa lleva a cabo acciones para identificar las necesidades actuales y futuras de los clientes.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "13. Se cuenta con una estrategia de distribución y se seleccionan los canales y modalidades de distribución en función de ella.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "14. Se determinan los precios de los productos según parámetros del mercado y sólo se utilizan los costos para juzgar sobre su rentabilidad.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "15. Las especificaciones, precios y condiciones de venta están claras y coinciden con el producto o servicio que se ofrece.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "16. Se tienen desarrolladas las competencias en la fuerza de ventas para mejorar los resultados.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "17. Se conocen los márgenes de utilidad de las diferentes líneas de productos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "18. Se tiene una instancia interna en el otorgamiento de condiciones de pago, que a la vez es responsable de la cobranza.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "19. Se ofrece garantías sobre sus productos y servicios a los clientes.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Ventas y Mercadotecnica", "20. Se asignan los recursos para apoyar la comercialización en función a la efectividad de los medios empleados.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "21. Se tienen separadas las finanzas de la empresa de las personales.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "22. Se tienen identificados todos los costos de los procesos productivos en base a las operaciones que se les dan origen.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "23. La cartera de cuentas por cobrar no tiene partidas antiguas o en disputa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "24. Los precios de los productos aseguran una alta rentabilidad del negocio.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "25. Se asegura la disponibilidad de efectivo con el uso de proyecciones de flujo de efectivo.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "26. Se hace la liquidación de los créditos con el flujo de efectivo de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "27. Se generan flujos de efectivo que cubren el capital en trabajo requerido para la operación.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "28. Se elaboran estados financieros que reflejan la realidad dela empresa y no solo para el pago de impuestos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "29. Todos los activos productivos de la empresa (inventarios, maquinaria, etc.) son necesarios para la operación.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Contabilidad y Finanzas", "30. Se tiene una estructura de costos estándar para cada línea de productos.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "31. Se cuenta con un procedimiento, y este es conocido, para el restablecimiento de las operaciones en caso de incendio, inundación u otro desastre.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "32. La empresa cuenta con el adecuado asesoramiento legal.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "33. La empresa cuenta, para todos sus empleados, con contratos laborales debidamente revisados por un abogado especializado.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "34. La empresa cuenta con contratos legales adecuados relacionados con los productos y/o servicios que ofrece al mercado.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "35. La empresa cuenta con los permisos de operación correspondientes al giro en el cual opera.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "36. Se cuenta con un organigrama de la empresa donde los empleados conocen su asignación y responsabilidades.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "37. Se tiene un sistema de información adecuado para llevar un buen control de las operaciones.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "38. Se cuenta con políticas formuladas y conocidas para todos los interesados para regir su actuación.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "39. En el interior de  la empresa hay medios para proponer y desarrollar nuevas ideas para mejorar la operación.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Legal, Administración y R.H.", "40. Se fomenta  una buena comunicación entre las áreas, de modo que los problemas se resuelven antes de que produzcan consecuencias costosas.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "41. Se revisan periódicamente los diseños de sus productos para mejorar su funcionalidad, características y costos de producción.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "42. Se tiene protegida la propiedad intelectual de los procesos y/o productos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "43. Se tienen los recursos tecnológicos, físicos, económicos, intelectuales necesarios para la innovación de los procesos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "44. Se tiene la capacidad de desarrollar sus propios productos, así como los procesos de producción correlativos.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "45. Se tienen definidas acciones de innovación y/o desarrollo tecnologico, como parte del plan estratégico para mejorar la competitividad de la empresa.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "46. Se tienen planes de contingencia para asegurar la operación continua de equipos e instalaciones.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "47. Los inventarios de materiales son suficientes para cubrir los planes de producción.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "48. Se cuenta con especificaciones escritas de sus productos y se actualizan regularmente.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "49. Se cuenta con especificaciones escritas relativas a sus procesos de  producción y se  actualizan regularmente.", false, ""));
        preguntas.add(new PreguntasDiagnostico("Innovación, Tecnología y Procesos", "50. Se cuenta con programas de calidad total en los que intervienen todas las áreas de la empresa.", false, ""));

        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "51. ¿Cuenta con su última declaración de impuestos?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "52. ¿Cuenta con su 32D de hacienda actualizado?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "53. ¿Cuenta con uso de suelo?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "54. ¿Cuenta con licencia de funcionamiento?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "55. ¿Estás al día en el pago del predial?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "56. ¿Tiene contrato con el proveedor de basura como empresa?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "57. ¿Cuenta con capital de inversión?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "58. ¿Cuenta con historial crediticio?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "59. ¿Se encuentra en buró de crédito?", false, ""));
        preguntas.add(new PreguntasDiagnostico("Procuración de recursos", "60. ¿Ha participado en alguna convocatoria?", false, ""));

        // ********************************************************** DECLARAMOS LAS ACTIVIDADES A REALIZAR **************************************************************
        actividades.add("Establecer con tu equipo de trabajo objetivos y metas claras y realizables a corto, mediano y largo plazo.");
        actividades.add("Conocer los factores sociales, políticos, económicos y de la competencia que podrían resultar en una amenaza para tu empresa.");
        actividades.add("Estar al pendiente de los sucesos importantes que puedan resultar en oportunidades de crecimiento o alguna amenaza.");
        actividades.add("Identificar el nicho de mercado de la empresa, conocer las características del cliente y así poder crear las estrategias de marketing necesarias.");
        actividades.add("Realizar una investigación de mercado.");
        actividades.add("Identificar las tendencias actuales en tema de negocios, innovación y procesos del sector, que puedan representar una ventaja competitiva.");
        actividades.add("Conocer la forma en que los clientes interactúan con los productos de la empresa.");
        actividades.add("Conocer a detalle las ventajas y desventajas de su producto o servicio.");
        actividades.add("Buscar maneras de entrar al mercado, por medio de alianzas estratégicas, socios, o de lo contrario, buscar otro mercado meta.");
        actividades.add("Asistir a mentorías de negociación, rodearse de gente experta, tomar capacitaciones en coaching y certificaciones o diplomados de negocios.");

        actividades.add("Discutir con tu equipo de trabajo sobre las estrategias de comercialización de su producto o servicio, y monitorear la satisfacción del cliente respecto al producto.");
        actividades.add("Realizar encuestas a clientes potenciales, obtener retroalimentación de clientes, investigar al mercado y la competencia.");
        actividades.add("Seleccionar los canales de distribución adecuados para tu empresa, crear estrategias y modalidades de distribución.");
        actividades.add("Para determinar el precio de los productos, se requiere conocimiento en temas de finanzas y contabilidad.");
        actividades.add("Establecer condiciones de venta, precios definidos y aceptados por la empresa y el cliente.");
        actividades.add("Capacitación y asesoramiento en áreas de venta.");
        actividades.add("Establecer los margenes de utilidad (ganancia) que espera la empresa por cada producto y de acuerdo al mercado y la competencia.");
        actividades.add("Definir responsables y procesos de cobranza claros dentro de la empresa.");
        actividades.add("Desarrollar un plan de garantías sobre los productos y servicios que beneficien a los clientes");
        actividades.add("Invertir en comercialización de los productos y servicios de manera coherente y real.");

        actividades.add("Separar y tener cuentas diferentes entre las finanzas personales con las de la empresa.");
        actividades.add("Conocer el costo de cada proceso involucrado dentro de la producción del producto o servicio.");
        actividades.add("Es uno de los activos más importantes, ya que es el activo más líquido después del efectivo en una entidad de carácter económico. Las cuentas por cobrar representan venta o prestación de algún bien o servicio que se recuperará en dinero en sumas parciales.");
        actividades.add("Asesoramiento en temas de punto de equilibrio, margenes de utilidad, impuestos, y proyecciones financieras.");
        actividades.add("Realizar proyecciones financieras para más de 1 año, establecer estrategias de venta y control de recursos para asegurar la disponibilidad de efectivo en la empresa.");
        actividades.add("Liquidar cualquier tipo de crédito que haya pedido la empresa, y evitar mayores intereses, multas o sanciones.");
        actividades.add("Crear planes de acción que permitan la generación de flujo de efectivo favorable para cubrir el capital de trabajo requerido en las operaciones.");
        actividades.add("Conocer los conceptos generales de contabilidad y finanzas para saber interpretar el estado en que se encuentra la empresa financieramente y tomar acción respecto a dichos resultados.");
        actividades.add("De no ser necesario alguno de los activos productivos, venderlos, o rentarlos, para obtener más flujo de efectivo dentro de la empresa.");
        actividades.add("Conocer los gastos operativos que involucra cada producto o servicio.");

        actividades.add("Conocer y asesorarse de manuales de seguridad, para la proteccion de los empleados ante desastres naturales o incendios.");
        actividades.add("Consultar a expertos en materia legal de las áreas de la empresa que lo requieran.");
        actividades.add("Consultar con expertos la redacción detallada de los contratos legales de la empresa.");
        actividades.add("Buscar asesoramiento en áreas de garantías, términos y condiciones, patentes, protección de marcas, entre otros temas que protegen a los productos de la empresa, sus clientes y la empresa misma.");
        actividades.add("Consultar con un experto respecto a los trámites con el organismo de gobierno encargado de emitir los permisos de operaciones según el área de la empresa.");
        actividades.add("Definir los roles de cada empleado y directivo de la empresa y así crear el organigrama general de la empresa.");
        actividades.add("Adquirir  herramientas informáticas para el control, monitoreo de las operaciones, cadenas de suministro, inspección de calidad, control de nóminas ,etc.");
        actividades.add("Establecer reglamentos internos dentro de la empresa.");
        actividades.add("Establecer reuniones semanales o mensuales para discutir nuevas ideas dentro del personal.");
        actividades.add("Establecer estrategias de comunicación entre los empleados y directivos para atender cualquier problemática interna que pudiera perjudicar la empresa.");

        actividades.add("Hacer revisiones periódicas de los productos, para detectar fallas o área de oportunidad para mejorar el producto o servicio.");
        actividades.add("Asesoramiento con expertos en tema de propiedad intelectual, patentes, diseños industriales, marcas, etc.");
        actividades.add("Recibir capacitación sobre como administrar los recursos que propician la innovación en la empresa.");
        actividades.add("Asesoramiento con expertos en temas de tecnología e innovación, para aprender las metodologías como 'desing thinking' o 'lean startup' para generar protoripos, modelos de negocios disruptivos,etc. ");
        actividades.add("Empezar con un área de investigación y desarrollo dentro de la empresa.");
        actividades.add("Elaborar manuales de operación, almacenar datos técnicos de los equipos.");
        actividades.add("Capacitación en temas de invetarios, cadenas de suministro, materias primas, proveedores.");
        actividades.add("Desarrollar fichas técnicas de cada producto o servicio.");
        actividades.add("Elaborar Manuales de operación de cada producto, actualizar cada cambio que se llegara a tener.");
        actividades.add("Asesoramiento y capacitación en temas de calidad y supervisión, ISOS, Normativas, etc.");

        actividades.add("Asistir con un contador que auxilie en el ejercicio fiscal.");
        actividades.add("Actualizar lo más pronto posible el 32D.");
        actividades.add("Consultar con el organismo de gobierno responsable los requisitos para dar de alta el permiso.");
        actividades.add("Consultar con el organismo de gobierno responsable los requisitos para dar de alta el permiso.");
        actividades.add("Consultar con el organismo de gobierno responsable los requisitos para dar de alta el permiso.");
        actividades.add("Adquirir un contrato con el proveedor de basura.");
        actividades.add("Buscar asesoriamento en temas de inversión y búsqueda de capital con instituciones privadas y públicas.");
        actividades.add("Empezar a generar historial crediticio, adquiriendo una tarjeta departamental, o de crédito bajo.");
        actividades.add("Buscar asesoría profesional para salir de buro de crédito.");
        actividades.add("Entrar a los portales web y visitar las oficinas de las instituciones de apoyo a las empresas, tanto públicas como privadas.");

        setPreguntas();

        btnSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSi.setBackgroundColor(Color.GREEN);
                btnNo.setBackgroundColor(Color.GRAY);
                btnNoSe.setBackgroundColor(Color.GRAY);
                respondida = true;
                if(contador<60){
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
                if(contador<60){
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
                if(contador<60){
                    preguntas.get(contador).setRespuesta("No sé");
                }
            }
        });

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(contador == 60){

                }

                if(respondida == true){

                    if(contador >= 0 && contador < 10){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorPlaneacion++;
                        }
                    }
                    else if (contador >= 10 && contador < 20){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorVentas++;
                        }
                    }
                    else if (contador >= 20 && contador < 30){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorContabilidad++;
                        }
                    }
                    else if (contador >= 30 && contador < 40){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorLegal++;
                        }
                    }
                    else if (contador >= 40 && contador < 50){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorInnovacion++;
                        }
                    }
                    else if (contador >= 50 && contador < 60){
                        if(preguntas.get(contador).getRespuesta().equals("Si")){
                            contadorProcuracion++;
                            if(contador == 59){
                                // *********** Guardamos el dato de la ubicación actual del usuario *************
                                progressDialog.show();
                                misDatos = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = misDatos.edit();

                                DateFormat df = new SimpleDateFormat("dd/MM/yyyy, HH:mm");
                                String date = df.format(Calendar.getInstance().getTime());

                                mChart.setVisibility(View.VISIBLE);
                                setData(contadorPlaneacion, contadorVentas, contadorContabilidad, contadorLegal, contadorInnovacion, contadorProcuracion);

                                ArrayList<String[]> respuestas = new ArrayList<>();
                                ArrayList<String[]> respuestasVentas = new ArrayList<>();
                                ArrayList<String[]> respuestasContabilidad = new ArrayList<>();
                                ArrayList<String[]> respuestasLegal = new ArrayList<>();
                                ArrayList<String[]> respuestasInnovacion = new ArrayList<>();
                                ArrayList<String[]> respuestasProcuracion = new ArrayList<>();

                                templatePDF = new TemplatePDF(getApplicationContext());
                                templatePDF.openDocument();
                                templatePDF.addMetaData("Diagnostico Estatus Actual", "Metodo Asesores", "Soluciones Colabora");
                                templatePDF.addImage(getApplicationContext());
                                templatePDF.addTitles("DIAGNÓSTICO: ESTATUS ACTUAL", "Debido a la estadística de crecimiento de las empresas, nos preocupamos por conocer las áreas de oportunidades donde podemos aportarles un beneficio para sus negocios garantizando la mejora continua. El siguiente diagnostico está diseñado para conocer un poco más sobre su empresa y necesidades.",date);
                                templatePDF.addParagraph("Las siguientes preguntas son para determinar un diagnóstico más personalizado sobre los requerimientos que considera que pueda tener en su empresa y las actividades que debe realizar para mejorar la solidez de su startup y encontrar áreas de oportunidad. Esto será analizado por consultores expertos también si ellos lo requieren. Toda la información proporcionada será únicamente para fines de análisis empresarial y no será publicado o reproducido por algún otro medio. A partir de este momento nos comprometemos a su confidencialidad.");

                                for(int i = 0; i < 10; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestas.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }
                                for(int i = 10; i < 20; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestasVentas.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }
                                for(int i = 20; i < 30; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestasContabilidad.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }
                                for(int i = 30; i < 40; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestasLegal.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }
                                for(int i = 40; i < 50; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestasInnovacion.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }
                                for(int i = 50; i < 60; i ++){
                                    if(!preguntas.get(i).getRespuesta().equals("Si")){
                                        respuestasProcuracion.add(new String[]{preguntas.get(i).getPregunta(), actividades.get(i)});
                                    }
                                }

                                templatePDF.addSections("Planeación Estratégica");
                                templatePDF.addParagraph("La planeación estratégica es trazar el camino que se quiere seguir para lograr los objetivos de la empresa, se trata de conocer el entorno, la competencia, el mercado, los factores que pueden afectar tanto positiva como negativamente a la empresa.\n" +
                                        "Sin la planeación estratégica tu emprendimiento  marcharía sin rumbo al mercado, con el peligro de perderse en el camino.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestas);

                                templatePDF.addSections("Ventas y Mercadotecnia");
                                templatePDF.addParagraph("Una empresa no puede sovrevivir siin ventas ni mercadotecnia, si no hay ingresos por ventas de productos o servicios, no habra el recurso para pagar los salarios, costos y utilidades, es por eso que es necesario planear estrategias de comercialización, conocer el mercado, la competencia, el precio, margen de utilidad y el punto de equilibrio de cada producto o servicio que ofrece la empresa.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestasVentas);

                                templatePDF.addSections("Contabilidad y Finanzas");
                                templatePDF.addParagraph("Los registros contables en una empresa son esenciales para su éxito, ya que, además de ayudar en la toma de decisiones estratégicas, es la manera de evaluar constantemente el estado de sus finanzas y garantizar su rentabilidad.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestasContabilidad);

                                templatePDF.addSections("Legal, Administración y R. H.");
                                templatePDF.addParagraph("Dependiendo del lugar donde opere una empresa, hay normas y leyes que se tienen que cumplir y respetar, si no se tiene un buen asesoramiento legal, la empresa corre el riesgo de ser sancionada o clausurada temporal o definitivamente.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestasLegal);

                                templatePDF.addSections("Innovación, Tecnología y Procesos");
                                templatePDF.addParagraph("Cada empresa debe ser conciente de sus procesos diarios que harán que logren alcanzar sus metas propuestas; esto involucra una administración de recursos humanos, tecnológicos y financieros que trabajando unido como un engranaje, logran que la empresa sea productiva y competitiva y darán un valor agregado entre sus competidores.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestasInnovacion);

                                templatePDF.addSections("Procuración de Recursos");
                                templatePDF.addParagraph("La procuración de recursos es un proceso para conseguir fondos, mediante la solicitud de donaciones de particulares, empresas, fundaciones benéficas, o agencias gubernamentales, para poder conseguir fondos requieres estar al día en ciertos trámites.");
                                templatePDF.createTableWith2SameLengthcell(headerPreguntas, respuestasProcuracion);

                                templatePDF.addSections("Resultados Generales");

                                int suma = contadorContabilidad + contadorProcuracion + contadorInnovacion + contadorLegal + contadorVentas + contadorPlaneacion;
                                if(suma > 0 && suma <= 21 ){
                                    templatePDF.addParagraph("Tu emprendimiento está en una fase de desarrollo y planeación, no es una empresa formal, es necesario trabajar en aspectos básicos del plan de negocios, buscar asesoría profesional, repasar la terminología de administración, aspectos legales, contables, financieros, ventas y mercadotecnia.");
                                }
                                else if (suma > 21 && suma <= 30 ){
                                    templatePDF.addParagraph("Tienes un conocimiento básico de lo que es una empresa y las partes que la componen, sin embargo tu estructura no está bien definida, probablemente aun no tengas definido cual es tu visión o misión como empresa, y no hayas creado estrategias que te lleven a cumplir las metas propuestas, es necesario asesorarte en cada una de las áreas.");
                                }
                                else if (suma > 30 && suma <= 45 ){
                                    templatePDF.addParagraph("Tu emprendimiento está tomando forma como una empresa formal, tienes un conocimiento amplio pero no suficiente de lo que es una empresa, y de todo lo que involucra, tienes la teoría necesaria para continuar con tu proyecto, pero no la experiencia práctica para aterrizarlo con éxito, tus debilidades son en temas puntuales, y probablemente ya seas experto en más de una área, por lo cual es importante reforzar las áreas débiles.");
                                }
                                else if (suma > 45 && suma <= 60 ){
                                    templatePDF.addParagraph("Felicitaciones, tienes los conocimientos necesarios para llevar al éxito tu emprendimiento, tienes un dominio amplio en cada una de las áreas, sin embargo tienes algunos detalles de desinformación que si los dejas pasar, pueden perjudicar la operación de tu empresa, se te recomienda atender esos detalles puntuales con consultores experimentados.");
                                }
                                Bitmap bitmap;
                                mChart.setDrawingCacheEnabled(true);
                                bitmap = Bitmap.createBitmap(mChart.getDrawingCache());
                                mChart.setDrawingCacheEnabled(false);

                                templatePDF.createGraficaFoto(getApplicationContext(), bitmap);

                                Toast.makeText(getApplicationContext(), "Diagnóstico completado con éxito", Toast.LENGTH_LONG).show();
                                templatePDF.closeDocument();

                                editor.putString(UUIDUser + "diagnosticoEstatusActual", date);
                                editor.putString(UUIDUser + "diagnosticoEstatusActualContabilidad", Integer.toString(contadorContabilidad));
                                editor.putString(UUIDUser + "diagnosticoEstatusActualProcuracion", Integer.toString(contadorProcuracion));
                                editor.putString(UUIDUser + "diagnosticoEstatusActualInnovacion", Integer.toString(contadorInnovacion));
                                editor.putString(UUIDUser + "diagnosticoEstatusActualLegal", Integer.toString(contadorLegal));
                                editor.putString(UUIDUser + "diagnosticoEstatusActualVentas", Integer.toString(contadorVentas));
                                editor.putString(UUIDUser + "diagnosticoEstatusActualPlaneacion", Integer.toString(contadorPlaneacion));

                                editor.commit();

                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }

                                templatePDF.viewPDF("Diagnostico2");

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
        if(contador<60){
            txtArea.setText(preguntas.get(contador).getTitulo());
            txtPregunta.setText(preguntas.get(contador).getPregunta());

            if(contador == 59){
                btnSiguiente.setText("Finalizar");
            }
        }

    }

    public void setData(int puntosPlaneacion, int puntosVentas, int puntosContabilidad, int puntosLegal, int puntosInnovacion, int puntosProcuracion) {


        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        entries1.add(new RadarEntry(puntosPlaneacion));
        entries1.add(new RadarEntry(puntosVentas));
        entries1.add(new RadarEntry(puntosContabilidad));
        entries1.add(new RadarEntry(puntosLegal));
        entries1.add(new RadarEntry(puntosInnovacion));
        entries1.add(new RadarEntry(puntosProcuracion));


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
