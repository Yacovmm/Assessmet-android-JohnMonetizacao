package com.yacov.assessmentandroidjohn;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    Button botaoAd;

    private AppCompatEditText nome, senha, senhaConfirm, email, cpf;
    private TextInputLayout senhaconfirmLayout, passLayout;
    static ArrayList<String> Arquivos = new ArrayList<String>();

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nome = findViewById(R.id.nomeID);
        email =  findViewById(R.id.emailID);
        senha =  findViewById(R.id.senhaID);
        passLayout = findViewById(R.id.passLyoutID);
        senhaConfirm = findViewById(R.id.senhaConfirmID);
        senhaconfirmLayout = findViewById(R.id.senhaConfirmLayout);
        cpf = findViewById(R.id.cpfID);

        passLayout.setCounterEnabled(true);
        passLayout.setCounterMaxLength(8);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(cpf, simpleMaskFormatter);
        cpf.addTextChangedListener((TextWatcher) maskTextWatcher);

        botaoAd = (Button) findViewById(R.id.btnSaveID);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-2533529778744441/8574594488");

        mInterstitialAd.setAdListener(new AdListener(){
            @Override

            public void onAdClosed() {
                requestNewInterstitial();
                Mensagem("Sucesso!");
            }
            });
            requestNewInterstitial();

        checkForStoragePermission();
        Listar();
    }



    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }



        //Password and confirm password validation
//        senhaconfirmLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (senhaConfirm.getText().toString() != senha.getText().toString()){
//                    senhaconfirmLayout.setErrorEnabled(true);
//                    senhaconfirmLayout.setError("A senha deve ser igual!");
//                }else if (senha.getText().equals(senhaConfirm.getText())){
//                    senhaconfirmLayout.setErrorEnabled(false);
//                }
//            }
//        });
//
//        senhaConfirm.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (senhaConfirm.getText().toString() != senha.getText().toString()){
//                    senhaconfirmLayout.setErrorEnabled(true);
//                    senhaconfirmLayout.setError("A senha deve ser igual!");
//                }else if (senha.getText().equals(senhaConfirm.getText())){
//                    senhaconfirmLayout.setErrorEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


    private boolean validateFields(){

        boolean res = false;

        String nameString =  nome.getText().toString();
        String loginString = email.getText().toString();
        String senhaString = senha.getText().toString();
        String senhaConfirmString = senhaConfirm.getText().toString();
        String cpfString =  cpf.getText().toString();

        if (res = isEmptyField(nameString)){
            nome.requestFocus();
        }else if (res = !isValidEmail(loginString)){
            email.requestFocus();
        }else if ( res = isEmptyField(senhaString)){
            senha.requestFocus();
        }else if (res = isEmptyField(senhaConfirmString)){
            senhaConfirm.requestFocus();
        }else if (res = isEmptyField(cpfString)){
            cpf.requestFocus();
        }else if (res = !senhaString.equals(senhaConfirmString)){
            senhaConfirm.requestFocus();
        }

        if (res){
            Mensagem("There are invalid or blank fields");
        }
        return res;
    }

    private boolean isEmptyField(String value){

        boolean resul = (TextUtils.isEmpty(value) || value.trim().isEmpty());
        return resul;
    }

    private boolean isValidEmail(String email){
        boolean resul = (!isEmptyField(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
        return resul;
    }



    public void Listar()
    {
        File diretorio = new File(ObterDiretorio());
        File[] arquivos = diretorio.listFiles();
        if(arquivos != null)
        {
            int length = arquivos.length;
            for(int i = 0; i < length; ++i)
            {
                File f = arquivos[i];
                if (f.isFile())
                {
                    Arquivos.add(f.getName());
                }
            }

        }
    }

    public void btnsave(View view){

        if (!validateFields()){

            String lstrNomeArq;
            File arq;
            byte[] dados;
            try{
                lstrNomeArq = nome.getText().toString() + ".txt";

                arq = new File(Environment.getExternalStorageDirectory(), lstrNomeArq);
                FileOutputStream fos;

                StringBuilder builder = new StringBuilder();
                builder.append("Nome - " + nome.getText().toString() + "\n" );
                builder.append("Login - " + email.getText().toString() + "\n" );
                builder.append("cpf - " + cpf.getText().toString() + "\n" );
                builder.append("Senha - " + senha.getText().toString() + "\n" );



                fos = new FileOutputStream(arq);
                fos.write(builder.toString().getBytes());
                fos.flush();
                fos.close();
                Mensagem("Texto Salvo com sucesso!");
                Listar();
                mInterstitialAd.show();
            }
            catch (Exception e){
                Mensagem("Erro : " + e.getMessage());
            }

        }


    }

    private String ObterDiretorio(){

        File root = android.os.Environment.getExternalStorageDirectory();
        return root.toString();
    }


    public void btnLimpar(View view){
        nome.setText("");
        email.setText("");
        senha.setText("");
        senhaConfirm.setText("");
        cpf.setText("");

        Mensagem("Campos apagados!");
    }


    private void Mensagem(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void checkForStoragePermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Toast.makeText(this, "Iremos salvar o formulario na sua memória externa", Toast.LENGTH_LONG);

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    this.checkForStoragePermission();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}

