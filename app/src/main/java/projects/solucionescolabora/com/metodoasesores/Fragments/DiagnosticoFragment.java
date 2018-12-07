package projects.solucionescolabora.com.metodoasesores.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import projects.solucionescolabora.com.metodoasesores.Cuestinario.EstatusActualActivity;
import projects.solucionescolabora.com.metodoasesores.Cuestinario.NecesidadDatosEmpresaActivity;
import projects.solucionescolabora.com.metodoasesores.Cuestinario.NecesidadRepresentanteActivity;
import projects.solucionescolabora.com.metodoasesores.Cuestinario.OtrasAreasActivity;
import projects.solucionescolabora.com.metodoasesores.R;


public class DiagnosticoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView imgDatosGenerales;
    private ImageView imgEstatusActual;
    private ImageView imgOtrosDatos;
    private TextView txtDatosGenerales;
    private TextView txtEstatusActual;
    private TextView txtOtrosDatos;
    private Button btnFinalizar;

    private SharedPreferences sharedPreferences;
    private String UUIDUser = "";

    public DiagnosticoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diagnostico, container, false);

        imgDatosGenerales = (ImageView)view.findViewById(R.id.imgStartupDiagnosticoNecesidad);
        imgEstatusActual = (ImageView)view.findViewById(R.id.imgStartupDiagnosticoEstatus);
        imgOtrosDatos = (ImageView)view.findViewById(R.id.imgStartupDiagnosticoOtras);
        txtDatosGenerales = (TextView) view.findViewById(R.id.txtStartupDiagnosticoMainDatosGenerales);
        txtEstatusActual = (TextView)view.findViewById(R.id.txtStartupDiagnosticoMainEstatus);
        txtOtrosDatos = (TextView)view.findViewById(R.id.txtStartupDiagnosticoMainOtrasAreas);
       // btnFinalizar = (Button)view.findViewById(R.id.btnStartupMainDiagnosticoFinalizar);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
        UUIDUser = user.getUid();

        imgDatosGenerales.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgEstatusActual.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgOtrosDatos.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);

        sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
        final String diagnosticoEstatusActual = sharedPreferences.getString(UUIDUser + "diagnosticoEstatusActual","");
        final String diagnosticoDatosGenerales = sharedPreferences.getString(UUIDUser + "diagnosticoDatosGenerales","");
        final String diagnosticoOtrosDatos = sharedPreferences.getString(UUIDUser + "diagnosticoOtrosDatos","");

        if(diagnosticoDatosGenerales.length() > 0){
            txtDatosGenerales.setText("Estatus: Respondido (" + diagnosticoDatosGenerales + ")");
        }
        else{
            txtDatosGenerales.setText("Estatus: No respondido");
        }
        if(diagnosticoEstatusActual.length() > 0){
            txtEstatusActual.setText("Estatus: Respondido (" + diagnosticoEstatusActual + ")");
        }
        else{
            txtEstatusActual.setText("Estatus: No respondido");
        }
        if(diagnosticoOtrosDatos.length() > 0){
            txtOtrosDatos.setText("Estatus: Respondido (" + diagnosticoOtrosDatos + ")");
        }
        else{
            txtOtrosDatos.setText("Estatus: No respondido");
        }


        imgDatosGenerales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), NecesidadDatosEmpresaActivity.class);

            }
        });

/*        btnFinalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnosticoDatosGenerales.length() > 0 && diagnosticoEstatusActual.length() > 0 && diagnosticoOtrosDatos.length() > 0){

                }
                else{
                    Toast.makeText(getActivity(), "Para generar el diagnostico debe responder todos los cuestionarios", Toast.LENGTH_LONG).show();
                }
            }
        });*/

        imgDatosGenerales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NecesidadRepresentanteActivity.class);
                startActivity(i);
            }
        });

        imgEstatusActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EstatusActualActivity.class);
                startActivity(i);
            }
        });

        imgOtrosDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), OtrasAreasActivity.class);
                startActivity(i);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getActivity().getSharedPreferences("misDatos", 0);
        final String diagnosticoEstatusActual = sharedPreferences.getString(UUIDUser + "diagnosticoEstatusActual","");
        final String diagnosticoDatosGenerales = sharedPreferences.getString(UUIDUser + "diagnosticoDatosGenerales","");
        final String diagnosticoOtrosDatos = sharedPreferences.getString(UUIDUser + "diagnosticoOtrosDatos","");

        if(diagnosticoDatosGenerales.length() > 0){
            txtDatosGenerales.setText("Estatus: Respondido (" + diagnosticoDatosGenerales + ")");
        }
        else{
            txtDatosGenerales.setText("Estatus: No respondido");
        }
        if(diagnosticoEstatusActual.length() > 0){
            txtEstatusActual.setText("Estatus: Respondido (" + diagnosticoEstatusActual + ")");
        }
        else{
            txtEstatusActual.setText("Estatus: No respondido");
        }
        if(diagnosticoOtrosDatos.length() > 0){
            txtOtrosDatos.setText("Estatus: Respondido (" + diagnosticoOtrosDatos + ")");
        }
        else{
            txtOtrosDatos.setText("Estatus: No respondido");
        }
    }
}
