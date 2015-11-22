package com.ridesforme.ridesforme;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ridesforme.ridesforme.util.Urls;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LoginActivity extends Activity {
    UserSessionManager session;
    EditText txtLogin;
    EditText txtPassword;
    Boolean exit = false;
    MaterialDialog mDialog;



    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
        } else {
            Toast.makeText(this, R.string.confirm_exit,
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mDialog != null){
            mDialog.dismiss();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogar = (Button) findViewById(R.id.btnConofirmarLogin);
        txtLogin = (EditText) findViewById(R.id.edtLogin);
        txtPassword = (EditText) findViewById(R.id.edtPassWord);
        TextView txtCadastrar = (TextView) findViewById(R.id.cadastrarLogin);
        txtCadastrar.setText(Html.fromHtml("<p><u>Cadastre-se agora!</u></p>"));
        session = new UserSessionManager(getApplication());

        /*Sem Webservice
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog = new MaterialDialog.Builder(LoginActivity.this)
                        .title(R.string.login_progress_dialog)
                        .content(R.string.wait)
                        .progress(true, 0)
                        .show();

                String username = txtLogin.getText().toString();
                String password = txtPassword.getText().toString();
                session.createUserLoginSession(username, password, "1");
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });*/





        //Com WebService
        btnLogar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validarDados()) {
                    String username = txtLogin.getText().toString();
                    String password = txtPassword.getText().toString();
                    String b;
                    String result="";
                    String idUser ="";
                    try {
                        b = new LoginControllerTask().execute(username, password).get();
                        try {
                            JSONObject jObject = new JSONObject(b);
                            result = jObject.getString("result");
                            JSONArray obj2  = jObject.getJSONArray("user");
                            JSONObject obj = obj2.getJSONObject(0);
                            idUser = obj.getString("UsuarioId");

                            Log.i("id",idUser);
                            Log.i("result",result);

                        } catch (JSONException e) {
                            Log.i("b",b);
                        }
                        if (result.equals("true")) {
                            new MaterialDialog.Builder(LoginActivity.this)
                                    .title(R.string.login_progress_dialog)
                                    .content(R.string.wait)
                                    .progress(true, 10)
                                    .show();

                            session.createUserLoginSession(username, password,idUser);
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplication(), "login ou senha inválidos!", Toast.LENGTH_SHORT).show();

                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, CadastroActivity.class);
                startActivity(it);
            }
        });

    }

    /**
     * Created by Felipe on 27/08/2015.
     */
    public static class LoginControllerTask extends AsyncTask<String, Integer, String> {
        Urls urls;

        @Override
        protected String doInBackground(String... params) {
            try {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new MultipartBuilder()
                        .type(MultipartBuilder.FORM) //this is what I say in my POSTman (Chrome plugin)
                        .addFormDataPart("login",params[0])
                        .addFormDataPart("senha",params[1])
                        .build();
                Request request = new Request.Builder()
                        //teste login servidor casa felipe
                        .url(urls.login)
                        .post(requestBody)
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String responseString = response.body().string();
                    response.body().close();

                    return responseString;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public Boolean verificarEspacoBranco(String s) {
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(s);
        boolean found = matcher.find();
        return found;
    }

    public Boolean validarDados() {
        boolean validado = true;

        if (verificarEspacoBranco(txtLogin.getText().toString())) {
            txtLogin.setError("Não pode conter espaços em branco.");
            txtLogin.requestFocus();
            validado = false;
        }
        if (verificarEspacoBranco(txtPassword.getText().toString())) {
            txtPassword.setError("Não pode conter espaços em branco.");
            txtPassword.requestFocus();
            validado = false;
        }

        if(txtLogin.getText().toString().equals("")){
            txtLogin.setError(getString(R.string.alert_campo_obrigatorio));
            txtLogin.requestFocus();
            validado = false;
        }
        if(txtPassword.getText().toString().equals("")){
            txtPassword.setError(getString(R.string.alert_campo_obrigatorio));
            txtPassword.requestFocus();
            validado = false;
        }
        return validado;
    }

}
