package com.stolpe.complexcalculator;

import android.net.Uri;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.stolpe.commandprocessor.CalculatorProcessor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_ANDROID_ASSET_INDEX_JSP = "file:///android_asset/index.jsp";
    private int port = getFreePort();

    public static final String XCASPAD = "xcaspad";

    static {
        try {
            System.loadLibrary(XCASPAD);
        } catch (SecurityException e){
            e.printStackTrace();
        }
        catch (UnsatisfiedLinkError e){
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView calcView = findViewById(R.id.Calculator);
        WebSettings webSettings = calcView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        calcView.loadUrl(FILE_ANDROID_ASSET_INDEX_JSP+"?port="+port);

        startJetty();
    }

    private void startJetty() {
        String appBase = ".";
        Server jetty = new Server(port);
        try {
            ServletHandler handler = new ServletHandler();
            handler.addServletWithMapping(CalculatorProcessor.class, "/Calculate");
            FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
            holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
            holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
            holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

            handler.addFilter(holder);

            jetty.setHandler(handler);
            jetty.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getFreePort(){
        int result = -1;
        try {
            ServerSocket socket = new ServerSocket(0);
            result = socket.getLocalPort();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}