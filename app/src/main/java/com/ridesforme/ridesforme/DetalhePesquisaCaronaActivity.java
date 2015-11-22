package com.ridesforme.ridesforme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ridesforme.ridesforme.basicas.Carona;
import com.ridesforme.ridesforme.basicas.Viagem;
import com.ridesforme.ridesforme.util.Urls;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.util.concurrent.ExecutionException;

public class DetalhePesquisaCaronaActivity extends AppCompatActivity {

    TextView mTxtEnderecoOrigem;
    TextView mTxtCidadeOrigem;
    TextView mTxtBairroOrigem;
    TextView mTxtNumeroOrigem;
    TextView mTxtEnderecoDestino;
    TextView mTxtCidadeDestino;
    TextView mTxtBairroDestino;
    TextView mTxtNumeroDestino;
    TextView mTxtVagas;
    TextView mTxtValor;
    Button btnSolicitarCarona;
    Carona carona;
    Integer idCarona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_pesquisa_carona);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTxtEnderecoOrigem = (TextView)findViewById(R.id.txtEnderecoOrigem);
        mTxtCidadeOrigem = (TextView)findViewById(R.id.txtCidadeOrigem);
        mTxtBairroOrigem = (TextView)findViewById(R.id.txtBairroOrigem);
        mTxtNumeroOrigem = (TextView)findViewById(R.id.txtNumeroOrigem);
        mTxtEnderecoDestino = (TextView)findViewById(R.id.txtEnderecoDestino);
        mTxtCidadeDestino = (TextView)findViewById(R.id.txtCidadeDestino);
        mTxtBairroDestino = (TextView)findViewById(R.id.txtBairroDestino);
        mTxtNumeroDestino = (TextView)findViewById(R.id.txtNumeroDestino);
        mTxtVagas = (TextView) findViewById(R.id.txtVagas);
        mTxtValor = (TextView) findViewById(R.id.txtValor);
        btnSolicitarCarona = (Button)findViewById(R.id.btnSolicitarCarona);

        Intent it = getIntent();
        carona = (Carona)it.getSerializableExtra("carona_selecionada");

        if(carona != null){
            mTxtEnderecoOrigem.setText(carona.getRuaOrigem());
            mTxtCidadeOrigem.setText(carona.getCidadeOrigem());
            mTxtBairroOrigem.setText(carona.getBairroOrigem());
            mTxtEnderecoDestino.setText(carona.getRuaDestino());
            mTxtCidadeDestino.setText(carona.getCidadeDestino());
            mTxtBairroDestino.setText(carona.getBairroDestino());
            mTxtVagas.setText(carona.getVagas());
            mTxtValor.setText(carona.getValor());
            if (Integer.parseInt(carona.getVagas())==0) {
                btnSolicitarCarona.setEnabled(false);
            }
            idCarona = carona.getCaronaId();
        }

        btnSolicitarCarona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();

                SharedPreferences sf = getSharedPreferences("AndroidExamplePref", MODE_PRIVATE);
                String chaveSalva = sf.getString("iduser","");
                Log.i("idUsuario",chaveSalva);
                Log.i("idCarona", idCarona.toString());
                Viagem vi = new Viagem();

                vi.setCaronaID(idCarona);
                vi.setUsuarioID(Integer.parseInt(chaveSalva));
                vi.setClassificacao(0);
                Boolean b;
                String json = gson.toJson(vi);
                Integer caronaVagas = Integer.parseInt(carona.getVagas())-1;
                try {
                    b = new CadastroViagemTask().execute(json,idCarona.toString(),caronaVagas.toString()).get();
                    Log.i("json",json);
                    Toast.makeText(getApplicationContext(),"Sua solicitação de carona foi enviada com sucesso.",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    public static class CadastroViagemTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("viagem",params[0])
                        .addFormDataPart("idcarona",params[1])
                        .addFormDataPart("vagas",params[2])
                        .build();
                Request request = new Request.Builder()
                        //teste login servidor casa felipe
                        .url(Urls.cadastrarViagem)
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    response.body().close();
                    Log.v("a", responseString);
                    boolean aaa = Boolean.parseBoolean(responseString);
                    return  aaa;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}
