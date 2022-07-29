package com.stolpe.complexcalculator;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stolpe.commandprocessor.CalculatorConfigurator;
import com.stolpe.commandprocessor.CalculatorProcessor;
import com.stolpe.commandprocessor.CommandInterpreter;
import com.stolpe.calculatorcore.CalculatorCore;
import com.stolpe.commandprocessor.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String STACK = "stack";
    public static final String CONFIG = "config";
    public static final String CALCULATE = "/Calculate";
    public static final String CONFIGURE = "/Configure";
    private final int port = getFreePort();
    public final String FILE_ANDROID_ASSET_INDEX_JSP = "file:///android_asset/index.jsp?port="+port;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = CommandInterpreter.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView calcView = findViewById(R.id.Calculator);
        //String name = this.getApplication().getApplicationContext().getApplicationInfo().nativeLibraryDir;
        configureCalcView(calcView);
        startJetty();
    }

    @SuppressLint("SetJavaScriptEnabled")
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
            handler.addServletWithMapping(CalculatorProcessor.class, CALCULATE);
            handler.addServletWithMapping(CalculatorConfigurator.class, CONFIGURE);
            jetty.setHandler(handler);
            jetty.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        Config config = commandInterpreter.getConfig();
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> stack = new ArrayList<>(commandInterpreter.getCalculator().getStack());
        outState.putStringArrayList(STACK, stack);
        outState.putString(CONFIG, writer.toString());
    }
    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String configString = (String) savedInstanceState.get(CONFIG);
        if (configString!=null){
            try {
                Config config = objectMapper.readValue(configString, Config.class);
                commandInterpreter.setConfig(config);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        ArrayList<String> stack = savedInstanceState.getStringArrayList(STACK);
        if (stack!=null)
            commandInterpreter.getCalculator().setStack(stack);
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