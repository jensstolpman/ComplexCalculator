package com.stolpe.commandprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

@WebServlet("/Configure")
public class CalculatorConfigurator extends HttpServlet{
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = CommandInterpreter.getInstance();

    public void init() {
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Config config = commandInterpreter.getConfig();
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), config);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Reader reader = request.getReader();
        Config config = objectMapper.readValue(reader, Config.class);
        commandInterpreter.setConfig(config);
    }

}
