package com.stolpe.commandprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

public class CalculatorProcessor extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = new CommandInterpreter();

    public void init() {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
         Reader reader = request.getReader();
        Command command = objectMapper.readValue(reader, Command.class);
        Command result = commandInterpreter.processCommand(command);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), result);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException  {
        res.setContentType("text/html");
        PrintWriter printWriter = res.getWriter();
        printWriter.print("Hello World");
    }


}
