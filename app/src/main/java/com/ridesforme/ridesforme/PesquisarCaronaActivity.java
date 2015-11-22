package com.ridesforme.ridesforme;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.ridesforme.ridesforme.adapter.CaronaAdapter;
import com.ridesforme.ridesforme.basicas.Carona;
import com.ridesforme.ridesforme.repositorios.RepositorioCarona;
import com.ridesforme.ridesforme.util.DataUtil;
import com.ridesforme.ridesforme.util.Urls;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PesquisarCaronaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mListViewCarona;
    private Button mBotaoFiltrarCarona;
    private static final int REQUEST_CODE = 1;
    private RepositorioCarona caronaDAO = RepositorioCarona.getSingleton();
    private List<Carona> mCaronas;
    private List<Carona> mCaronasFiltro;

    private ListarCaronasTask mTask;
    private ListarCaronasFiltroBairroTudoTask mTaskFiltro;
    private ListarCaronasFiltroBairroPagaTask mTaskFiltroBairroPaga;
    private ListarCaronasFiltroBairroGratisTask mTaskFiltroBairroGratis;
    private ListarCaronasFiltroBairroDataGratisTask mTaskFiltroBairroDataGratisTask;
    private ListarCaronasFiltroBairroDataPagoTask mTaskFiltroBairroDataPagoTask;
    private ListarCaronasFiltroBairroDataTudoTask mTaskFiltroBairroDataPagoTudo;

    private String bairroOridem;
    private String bairroDestino;
    private String dataOrigem;
    private String horaOrigem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_carona);

       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListViewCarona = (ListView) findViewById(R.id.listCarona);
        mBotaoFiltrarCarona = (Button) findViewById(R.id.btnFiltrarCarona);
        mBotaoFiltrarCarona.setOnClickListener(this);
        mCaronas = new ArrayList<>();
        mCaronasFiltro = new ArrayList<>();

        mTask = new ListarCaronasTask();
        mTaskFiltro = new ListarCaronasFiltroBairroTudoTask();
        mTaskFiltroBairroPaga = new ListarCaronasFiltroBairroPagaTask();
        mTaskFiltroBairroGratis = new ListarCaronasFiltroBairroGratisTask();
        mTaskFiltroBairroDataGratisTask = new ListarCaronasFiltroBairroDataGratisTask();
        mTaskFiltroBairroDataPagoTask = new ListarCaronasFiltroBairroDataPagoTask();
        mTaskFiltroBairroDataPagoTudo = new ListarCaronasFiltroBairroDataTudoTask();


        String filtro = getIntent().getStringExtra("filtro");
        if (filtro != null) {
            Intent it = getIntent();
            Carona carona = (Carona) it.getSerializableExtra("carona_filtrada");

            bairroOridem = carona.getBairroOrigem();
            bairroDestino = carona.getBairroDestino();
            //horaOrigem = carona.getHoraOrigem();

            if (getIntent() != null && filtro.equals("bairroTudo")) {
                mTaskFiltro.execute(bairroOridem, bairroDestino);
            }
            else if (getIntent() != null && filtro.equals("bairroPaga")) {
                mTaskFiltroBairroPaga.execute(bairroOridem, bairroDestino);
            }
            else if (getIntent() != null && filtro.equals("bairroGratis")) {
                mTaskFiltroBairroGratis.execute(bairroOridem, bairroDestino);
            }
            else if (getIntent() != null && filtro.equals("bairroDataGratis")) {
                dataOrigem = DataUtil.dateToString(carona.getDataHoraSaidaIda());
                mTaskFiltroBairroDataGratisTask.execute(bairroOridem, bairroDestino, dataOrigem);
            }
            else if (getIntent() != null && filtro.equals("bairroDataPaga")) {
                dataOrigem = DataUtil.dateToString(carona.getDataHoraSaidaIda());
                mTaskFiltroBairroDataPagoTask.execute(bairroOridem, bairroDestino, dataOrigem);
            }
            else if (getIntent() != null && filtro.equals("bairroDataTudo")) {
                dataOrigem = DataUtil.dateToString(carona.getDataHoraSaidaIda());
                mTaskFiltroBairroDataPagoTudo.execute(bairroOridem, bairroDestino, dataOrigem);
            }



        } else {
            mTask.execute();
        }


        mListViewCarona.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, FilterCaronaActivity.class);
        startActivity(intent);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Carona carona = (Carona) parent.getItemAtPosition(position);
        Intent intent = new Intent(this, DetalhePesquisaCaronaActivity.class);
        intent.putExtra("carona_selecionada", carona);
        startActivity(intent);
    }

    public class ListarCaronasTask extends AsyncTask<Void, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(Void... params) {
            String url;
            OkHttpClient client = new OkHttpClient();
            Request request;

            try {
                url = Urls.AllCarona;
                request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                    Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronas.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronas;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronas));
            Log.i("done", "done");

        }
    }

    public class ListarCaronasFiltroBairroTudoTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .build();
                url = Urls.getCaronaFiltroBairroAll;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                    Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }

    public class ListarCaronasFiltroBairroPagaTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .build();
                url = Urls.getCaronaFiltroBairroPaga;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                  Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }

    public class ListarCaronasFiltroBairroGratisTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .build();
                url = Urls.getCaronaFiltroBairroGratis;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                  Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }

    public class ListarCaronasFiltroBairroDataGratisTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .addFormDataPart("DataOrigem", params[2])
                        .build();
                url = Urls.getCaronaFiltroBairroDataGratis;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                  Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }

    public class ListarCaronasFiltroBairroDataPagoTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .addFormDataPart("DataOrigem", params[2])
                        .build();
                url = Urls.getCaronaFiltroBairroDataPago;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                    Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }

    public class ListarCaronasFiltroBairroDataTudoTask extends AsyncTask<String, Void, List<Carona>> {
        @Override
        protected List<Carona> doInBackground(String... params) {
            String url;


            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM)
                        .addFormDataPart("BairroOrigem", params[0])
                        .addFormDataPart("BairroDestino", params[1])
                        .addFormDataPart("DataOrigem", params[2])
                        .build();
                url = Urls.getCaronaFiltroBairroDataTudo;
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                String jsonStr = response.body().string();
                Log.i("pesquisa", jsonStr);

                JSONArray jsonarray = new JSONArray(jsonStr);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject caronaJSON = jsonarray.getJSONObject(i);

                    Integer caronaId = caronaJSON.getInt("CaronaID");
                    Integer usuarioId = caronaJSON.getInt("UsuarioID");
                    String estadoOrigem = caronaJSON.getString("EstadoOrigem");
                    String cidadeOrigem = caronaJSON.getString("CidadeOrigem");
                    String bairroOrigem = caronaJSON.getString("BairroOrigem");
                    String ruaOrigem = caronaJSON.getString("RuaOrigem");
                    String estadoDestino = caronaJSON.getString("EstadoDestino");
                    String cidadeDestino = caronaJSON.getString("CidadeDestino");
                    String bairroDestino = caronaJSON.getString("BairroDestino");
                    String ruaDestino = caronaJSON.getString("RuaDestino");
                    Integer valor = caronaJSON.getInt("Valor");
                    String descricaoCarona = caronaJSON.getString("DescricaoCarona");
                    Integer tipoVeiculo = caronaJSON.getInt("TipoVeiculo");
                    String descricaoVeiculo = caronaJSON.getString("DescricaoVeiculo");
                    Integer vagas = caronaJSON.getInt("Vagas");
                    Integer tipoTrajeto = caronaJSON.getInt("TipoTrajeto");
                    Integer tipoOferta = caronaJSON.getInt("TipoOferta");
//                  Integer dataHoraSaidaIda = caronaJSON.getInt("DataHoraSaidaIda");
//                  Integer dataHoraSaidaVolta = caronaJSON.getInt("DataHoraSaidaVolta");
                    String diaDaSemana = caronaJSON.getString("DiaDaSemana");
//                  Integer status = caronaJSON.getInt("Status");
//                  Integer classificacao = caronaJSON.getInt("Classificacao");

                    Carona carona = new Carona();

                    carona.setCaronaId(caronaId);
                    carona.setUsuarioID(usuarioId);
                    carona.setEstadoOrigem(estadoOrigem);
                    carona.setCidadeOrigem(cidadeOrigem);
                    carona.setBairroOrigem(bairroOrigem);
                    carona.setEstadoDestino(estadoDestino);
                    carona.setCidadeDestino(cidadeDestino);
                    carona.setBairroDestino(bairroDestino);
                    carona.setValor(valor.toString());
                    carona.setDescricaoCarona(descricaoCarona);
                    carona.setTipoVeiculo(tipoVeiculo.toString());
                    carona.setDescricaoVeiculo(descricaoVeiculo);
                    carona.setVagas(vagas.toString());
                    carona.setTipoTrajeto(tipoTrajeto.toString());
                    carona.setTipoOferta(tipoOferta.toString());
//                  carona.setDataHoraSaidaIda(new Date(dataHoraSaidaIda));
//                  carona.setDataHoraSaidaVolta(new Date(dataHoraSaidaVolta));
                    carona.setDiaDaSemana(diaDaSemana);
//                  carona.setStatus(status.toString());
//                  carona.setClassificacao(classificacao);
                    carona.setRuaOrigem(ruaOrigem);
                    carona.setRuaDestino(ruaDestino);

                    mCaronasFiltro.add(carona);
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mCaronasFiltro;
        }

        @Override
        protected void onPostExecute(List<Carona> caronas) {
            super.onPostExecute(caronas);
            mListViewCarona.setAdapter(new CaronaAdapter(PesquisarCaronaActivity.this, mCaronasFiltro));
            Log.i("doneFiltro", "doneFiltro");

        }
    }
}

