package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    private WebView wb;
    private ProgressBar progressBar;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wb=(WebView)findViewById(R.id.web_view1);
        progressBar = findViewById(R.id.h_load1);


        if (!Connection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder alertadd = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater factory = LayoutInflater.from(MainActivity.this);
            final View view = factory.inflate(R.layout.connect, null);
            alertadd.setView(view);
            alertadd.show();


        }else {

            wb.setWebViewClient(new HelloWebViewClient());
            WebSettings webSettings = wb.getSettings();
            wb.getSettings().setDomStorageEnabled(true);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setAllowFileAccess(true);
            wb.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setPluginState(WebSettings.PluginState.ON);
            webSettings.setMediaPlaybackRequiresUserGesture(false);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setSupportZoom(true);
            webSettings.setBuiltInZoomControls(true); // allow pinch to zooom
            webSettings.setDisplayZoomControls(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setAppCacheEnabled(true);
            webSettings.setAppCachePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/cache");
            webSettings.setDatabaseEnabled(true);
            webSettings.setDatabasePath(getApplicationContext().getFilesDir().getAbsolutePath() + "/databases");
            try {
                wb.getClass().getMethod("onPause").invoke(wb, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            try {
                wb.getClass().getMethod("onResume").invoke(wb, (Object[]) null);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            if (savedInstanceState == null) {
                wb.loadUrl("https://reynsiatech.github.io/");
            }
        }



    }

    @Override
    public void onPause() {
        super.onPause();
        wb.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        wb.onResume();
    }
    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // view.loadUrl(url);
            // return true;
            if (url.startsWith("http:") || url.startsWith("https:")) {
                return false;
            } else {
                if (url.startsWith("intent://")) {
                    try {
                        Context context = wb.getContext();
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        if (intent != null) {
                            PackageManager packageManager = context.getPackageManager();
                            ResolveInfo info = packageManager.resolveActivity(intent,
                                    PackageManager.MATCH_DEFAULT_ONLY);
                            // This IF statement can be omitted if you are not strict about
                            // opening the Google form url in WebView & can be opened in an
                            // External Browser
                            if ((intent != null) && ((intent.getScheme().equals("https"))
                                    || (intent.getScheme().equals("http")))) {
                                String fallbackUrl = intent.getStringExtra(
                                        "browser_fallback_url");
                                wb.loadUrl(fallbackUrl);
                                return true;
                            }
                            if (info != null) {
                                context.startActivity(intent);
                            } else {
                                // Call external broswer
                                String fallbackUrl = intent.getStringExtra(
                                        "browser_fallback_url");
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse(fallbackUrl));
                                context.startActivity(browserIntent);
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    wb.getContext().startActivity(intent);
                    return true;
                }
            }

        }
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.setTitle("Loading...");
            progressDialog.setMessage("Please wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);
            if (progressDialog != null){
                progressDialog.dismiss();
            }
        }
    }
    @Override
    public void onBackPressed() {
        if (wb.canGoBack())
            wb.goBack();
        else
            super.onBackPressed();

    }
    @Override
    protected void onSaveInstanceState(Bundle outState )
    {
        super.onSaveInstanceState(outState);
        wb.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wb.restoreState(savedInstanceState);
    }

    public boolean isNetworkAvailable() {
        // Get Connectivity Manager class object from Systems Service
        ConnectivityManager cm = (ConnectivityManager)  getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get Network Info from connectivity Manager
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
