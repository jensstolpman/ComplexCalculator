package com.stolpe.complexcalculator;

import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.stolpe.commandprocessor.CalculatorConfigurator;
import com.stolpe.commandprocessor.CalculatorProcessor;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import java.net.ServerSocket;

public class MainActivity extends AppCompatActivity {
    private final int port = getFreePort();
    public final String FILE_ANDROID_ASSET_INDEX_JSP = "file:///android_asset/index.jsp?port="+port;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView calcView = findViewById(R.id.Calculator);
        String name = this.getApplication().getApplicationContext().getApplicationInfo().nativeLibraryDir;
        configureCalcView(calcView);
        startJetty();
    }

    private void configureCalcView(WebView calcView){
        WebSettings webSettings = calcView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(false);
        webSettings.setAllowFileAccessFromFileURLs(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        calcView.loadUrl(FILE_ANDROID_ASSET_INDEX_JSP);
    }

    private void startJetty() {
        Server jetty = new Server(port);
        try {
            ServletHandler handler = new ServletHandler();
            handler.addServletWithMapping(CalculatorProcessor.class, "/Calculate");
            handler.addServletWithMapping(CalculatorConfigurator.class, "/Configure");
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