package com.stolpe.commandprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

@WebServlet("/Calculate")
public class CalculatorProcessor extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = CommandInterpreter.getInstance();

    public static class Mantissa {

        private String mantissa;
        private List<String> stack;
        public List<String> getStack() {
            return stack;
        }

        public void setStack(List<String> stack) {
            this.stack = stack;
        }

        public String getMantissa() {
            return mantissa;
        }

        public void setMantissa(String mantissa) {
            this.mantissa = mantissa;
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Mantissa mantissa = new Mantissa();
        mantissa.setMantissa(commandInterpreter.getMantissa());
        mantissa.setStack(commandInterpreter.getCalculator().getStack());
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), mantissa);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Reader reader = request.getReader();
        Command command = objectMapper.readValue(reader, Command.class);
        Command result = commandInterpreter.processCommand(command);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getWriter(), result);
    }

}
