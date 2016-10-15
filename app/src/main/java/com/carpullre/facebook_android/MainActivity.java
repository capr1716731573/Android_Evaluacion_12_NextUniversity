package com.carpullre.facebook_android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    private CallbackManager facebookCallBackManager;
    private LoginButton botonLoginFacebook;

    private AdView banner_AdView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /***** CODIGO PARA BOTON LOGIN DE FACEBOOK*//////////
        //inicializamos el sdk de facebook en la app
        FacebookSdk.sdkInitialize(getApplicationContext());
        facebookCallBackManager = CallbackManager.Factory.create();

        //declaro el codigo hash para la app
        getFacebookKeyHash("RJJPzFqVBHcIGQRNfmWY9EE5XaQ=");

        setContentView(R.layout.activity_main);

        botonLoginFacebook = (LoginButton) findViewById(R.id.btn_Login_Facebook);
        //Tratamiento del Resultado al quere registrar en la app con el usuario de facebook
        botonLoginFacebook.registerCallback(facebookCallBackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "App Facebook - Inicio de Sesion con Exito!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "App Facebook - Inicio de Sesion CANCELADA!!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, "App Facebook - Inicio de Sesion NO Exitosa!!!", Toast.LENGTH_SHORT).show();
            }
        });

        /***** CODIGO PARA PUBLICIDAD O BANNER *//////////
        // Initialize the Mobile Ads SDK.
        //MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        MobileAds.initialize(this, "ca-app-pub-9261460700217703~7180334079");
        //<string name="id_banner">ca-app-pub-9261460700217703/8657067276</string>
        banner_AdView = (AdView) findViewById(R.id.banner_adview);
        //construimos la peticion de la publicidad
        AdRequest peticion_AdRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        //cargo la respuesta dentro del AdView
        banner_AdView.loadAd(peticion_AdRequest);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //importo y sobreescribo los metodos del ciclo de vida de la app para tratar el AdView
    //en estos valido que si la app entra a cualquiera de estos estados y existe una publicodad dentro del AdView
    //lo destruya

    @Override
    protected void onDestroy() {
        if (banner_AdView != null) {
            banner_AdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (banner_AdView != null) {
            banner_AdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onPostResume() {
        if (banner_AdView != null) {
            banner_AdView.resume();
        }
        super.onPostResume();
    }

    //metodo que valida que el codigo hash sea correcto
    private void getFacebookKeyHash(String codigoHash) {
        try {

            PackageInfo info = getPackageManager().getPackageInfo(codigoHash, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("App Facebook KeyHash: ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                System.out.print("App Facebook KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }


        } catch (NoSuchAlgorithmException e) {

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        facebookCallBackManager.onActivityResult(requestCode, resultCode, data);
    }


}
