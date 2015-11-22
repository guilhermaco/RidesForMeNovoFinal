package com.ridesforme.ridesforme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaronaPasso1Activity extends AppCompatActivity {
    private TextView txtEnderecoOrigem;
    private TextView txtNumeroOrigem;
    private TextView txtCidadeOrigem;
    private TextView txtBairroOrigem;

    private TextView txtEnderecoDestino;
    private TextView txtNumeroDestino;
    private TextView txtCidadeDestino;
    private TextView txtBairroDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carona_passo1);

        txtEnderecoDestino = (TextView) findViewById(R.id.txtRuaDestino);
        txtNumeroDestino = (TextView) findViewById(R.id.txtNumeroDestino);
        txtCidadeDestino = (TextView) findViewById(R.id.txtCidadeDestino);
        txtBairroDestino = (TextView) findViewById(R.id.txtBairroDestino);


        Intent intent = getIntent();
        Bundle params = intent.getExtras();
        txtEnderecoOrigem = (TextView) findViewById(R.id.txtEnderecoOrigem);
        txtNumeroOrigem = (TextView) findViewById(R.id.txtNumeroOrigem);
        txtCidadeOrigem = (TextView) findViewById(R.id.txtCidadeOrigem);
        txtBairroOrigem = (TextView) findViewById(R.id.txtBairroOrigem);


        if (params!=null) {
            txtEnderecoOrigem.setText(params.getString("endereco"));
            txtNumeroOrigem.setText(params.getString("numero"));
            txtCidadeOrigem.setText(params.getString("cidade"));
            txtBairroOrigem.setText(params.getString("bairro"));
        }

        Button proxPasso = (Button) findViewById(R.id.button4);
        proxPasso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString("RuaOrigem",txtEnderecoOrigem.getText().toString());
                b.putString("numeroOrigem",txtNumeroOrigem.getText().toString());
                b.putString("BairroOrigem",txtBairroOrigem.getText().toString());
                b.putString("CidadeOrigem",txtCidadeOrigem.getText().toString());
                b.putString("RuaDestino",txtEnderecoDestino.getText().toString());
                b.putString("BairroDestino",txtBairroDestino.getText().toString());
                b.putString("numeroDestino",txtNumeroDestino.getText().toString());
                b.putString("CidadeDestino",txtCidadeDestino.getText().toString());
                Intent intent = new Intent(CaronaPasso1Activity.this,CaronaPasso2Activity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.localizacao, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
