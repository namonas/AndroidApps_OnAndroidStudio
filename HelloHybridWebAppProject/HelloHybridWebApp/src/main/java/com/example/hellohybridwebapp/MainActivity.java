package com.example.hellohybridwebapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
    final Activity activity = this;

    WebView webview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        String targetUrl = "http://www.google.co.jp";

        webview = new WebView(this);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setSupportZoom(false);
        webview.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String desc, String url) {
                Toast.makeText(activity, "Error:"+desc, Toast.LENGTH_LONG).show();
            }
        });

        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                activity.setProgress(progress * 100);
            }
        });
        webview.loadUrl(targetUrl);

        layout.addView(webview);
        setContentView(layout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void finish() {
        FragmentManager manager = getFragmentManager();
        MainFragmentDialog dialog = new MainFragmentDialog();
        dialog.show(manager, "dialog");
    }

    public void endApp() {
        super.finish();
    }

    public class MainFragmentDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confrim");
            builder.setMessage("Finish?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    endApp();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // do nothing
                }
            });
            return builder.create();
        }
    }
}
