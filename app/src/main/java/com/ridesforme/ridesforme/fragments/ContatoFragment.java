package com.ridesforme.ridesforme.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ridesforme.ridesforme.R;
import com.ridesforme.ridesforme.UserSessionManager;
import com.ridesforme.ridesforme.basicas.Contato;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContatoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContatoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContatoFragment extends Fragment{


    TextView mTxtNomeContatoResultado;
    EditText mTxtEmailContato;
    EditText mTxtMensagemContato;
    RatingBar mRatingNotaApp;
    RadioButton mRadioTipoSugestao;
    RadioButton mRadioTipoProblema;
    Button mBotaoEnviarEmailContato;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContatoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContatoFragment newInstance(String param1, String param2) {
        ContatoFragment fragment = new ContatoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ContatoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_contato,container,false);


        UserSessionManager sessao = new UserSessionManager(getActivity());
        HashMap<String, String> mapUser = sessao.getUserDetails();


        //Obtendo a instância dos componentes da Activity.
        mTxtNomeContatoResultado = (TextView)v.findViewById(R.id.txtViewResultadoNomeContato);
        mTxtMensagemContato = (EditText)v.findViewById(R.id.edtMensagemContato);
        mRatingNotaApp = (RatingBar)v.findViewById(R.id.ratingBar);
        mBotaoEnviarEmailContato = (Button)v.findViewById(R.id.botao_enviar);
        mRadioTipoSugestao = (RadioButton)v.findViewById(R.id.rb_sugestao);
        mRadioTipoProblema = (RadioButton)v.findViewById(R.id.rb_problema);

        mBotaoEnviarEmailContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Teste", "Clicou");
                Contato contato = new Contato();
                contato.nomeContato = mTxtNomeContatoResultado.getText().toString();
                contato.mensagemContato = mTxtMensagemContato.getText().toString();
                contato.classificacaoApp = mRatingNotaApp.getRating();
                if(mRadioTipoProblema.isChecked()){
                    contato.tipoContato = mRadioTipoProblema.getText().toString();
                }else{
                    if(mRadioTipoSugestao.isChecked()){
                        contato.tipoContato = mRadioTipoSugestao.getText().toString();
                    }
                }

                if(isDadosValidos()){
                    Intent emailIntent = new Intent(Intent.ACTION_VIEW);
                    emailIntent.setData(Uri.parse("contato@ridesforme.com"));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contato@ridesforme.com" });
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contato");
                    emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    emailIntent.setType("text/html");
                    String mensagemBody = "<b><i>Dados do Contato:</i></b><br>"
                            + "Usuário: " + "<b>" + contato.nomeContato + "</b><br>" +
                            "Classificação:" + "<b>" + String.valueOf(contato.classificacaoApp) + "</b><br>" +
                            "Mensagem:<br>" + "<p>"+ contato.mensagemContato + "</p><br>" +
                            "<span style='margin:auto'><b>Mensagem enviada automaticamente pelo App RidesForMe.</b></span>";

                    emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(mensagemBody));
                    startActivity(Intent.createChooser(emailIntent, "Email"));
                    Toast.makeText(getActivity(), getString(R.string.email_success), Toast.LENGTH_LONG).show();
                }
            }
        });

        mTxtNomeContatoResultado.setText(mapUser.get(UserSessionManager.KEY_NAME));
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private boolean isDadosValidos(){
        boolean validado = true;

        if(!mRadioTipoSugestao.isChecked() && !mRadioTipoProblema.isChecked()){
            Toast.makeText(getActivity(), getString(R.string.alert_radioButtonEmpty), Toast.LENGTH_SHORT).show();
            validado = false;
        }

        if(mTxtMensagemContato.getText().toString().equals("")){
            mTxtMensagemContato.setError(getString(R.string.alert_campo_obrigatorio));
            mTxtMensagemContato.requestFocus();
            validado = false;
        }

        return validado;
    }




}
