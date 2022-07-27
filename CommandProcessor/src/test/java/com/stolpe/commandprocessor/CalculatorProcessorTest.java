package com.stolpe.commandprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import org.junit.jupiter.api.Order;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorProcessorTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = CommandInterpreter.getInstance();

    private CalculatorProcessor servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        servlet = new CalculatorProcessor();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }


    @Test
    @Order(2)
    public void doGet() throws IOException {
        CalculatorProcessor.Mantissa mantissa;
        servlet.doGet(request, response);
        mantissa = objectMapper.readValue(response.getContentAsByteArray(), CalculatorProcessor.Mantissa.class);
        assertNotNull(mantissa);
        assertEquals(mantissa.getMantissa(), "-2.072+5.24i");
        assertEquals(mantissa.getStack().size(), 7);
    }

    @Test
    @Order(1)
    public void doPost() throws IOException {
        String[] values = {"0", "-4i", " - 5 - 4 i ", "-5", "5+4i", "4i", "5-4i", "5" ,"-4.345-5.335i"};
        for (String s : values) {
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            Command command = new Command();
            command.setValue(s);
            command.setCommand("enter");
            command.setNewEnter(0);
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, command);
            request.setContentType("application/json");
            byte[] value = writer.toString().getBytes(Charset.defaultCharset());
            request.setContent(value);
            servlet.doPost(request,response);
        }
        Command command = objectMapper.readValue(response.getContentAsByteArray(), Command.class);
        List stack = command.getStack();
        assertEquals(command.getStack(), commandInterpreter.getCalculator().getStack());
        assertEquals(command.getStack().size(), values.length+2);
        String[] commands ={"add", "subtract", "divide", "multiply"};
        String[] results = {"0.655-5.335i","4.345+1.335i","0.2585+0.8412i","-2.072+5.24i"};
        for (int i=0; i<commands.length; i++){
            request = new MockHttpServletRequest();
            response = new MockHttpServletResponse();
            String function = commands[i];
            command = new Command();
            command.setValue("");
            command.setCommand(function);
            StringWriter writer = new StringWriter();
            objectMapper.writeValue(writer, command);
            request.setContentType("application/json");
            byte[] value = writer.toString().getBytes(Charset.defaultCharset());
            request.setContent(value);
            servlet.doPost(request,response);
            command = objectMapper.readValue(response.getContentAsByteArray(), Command.class);
            assertEquals(command.getValue(), results[i]);
        }
    }
}