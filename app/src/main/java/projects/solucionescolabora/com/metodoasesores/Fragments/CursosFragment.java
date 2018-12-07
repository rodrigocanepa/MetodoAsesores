package projects.solucionescolabora.com.metodoasesores.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import projects.solucionescolabora.com.metodoasesores.R;
import projects.solucionescolabora.com.metodoasesores.VideoActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CursosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CursosFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView imgAtencion;
    private ImageView imgCapacidad;
    private ImageView imgInventarios;
    private ImageView imgOperacionesContables;
    private ImageView imgFondos;


    public CursosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cursos, container, false);

        imgAtencion = (ImageView)view.findViewById(R.id.imgStartupCursosAtencionClientes);
        imgCapacidad = (ImageView)view.findViewById(R.id.imgStartupCursosCapacidadEndeudamiento);
        imgInventarios = (ImageView)view.findViewById(R.id.imgStartupCursosInventarios);
        imgOperacionesContables = (ImageView)view.findViewById(R.id.imgStartupCursosOperacionesContables);
        imgFondos = (ImageView)view.findViewById(R.id.imgStartupCursosFondosFinanciamiento);

        imgAtencion.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgCapacidad.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgInventarios.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgOperacionesContables.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);
        imgFondos.setColorFilter(Color.argb(110,10,10,10), PorterDuff.Mode.DARKEN);

        imgAtencion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(VideoActivity.VIDEO_ID, "12vLcD8Sgt4");
                startActivity(i);
            }
        });

        imgCapacidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(VideoActivity.VIDEO_ID, "ShVCmUVKqNY");
                startActivity(i);
            }
        });

        imgInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(VideoActivity.VIDEO_ID, "EhXeG51J3KU");
                startActivity(i);
            }
        });

        imgOperacionesContables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(VideoActivity.VIDEO_ID, "xrxTJB7XWSo");
                startActivity(i);
            }
        });

        imgFondos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), VideoActivity.class);
                i.putExtra(VideoActivity.VIDEO_ID, "YdWcOIBdkM0");
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
}
