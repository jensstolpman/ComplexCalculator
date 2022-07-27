package com.stolpe.commandprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatorConfiguratorTest {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CommandInterpreter commandInterpreter = CommandInterpreter.getInstance();

    private CalculatorConfigurator servlet;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Before
    public void setUp() {
        servlet = new CalculatorConfigurator();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }
    @Test
    public void doGet() throws IOException {
        final int decimals = 7;
        commandInterpreter.getConfig().setDecimals(decimals);
        servlet.doGet(request, response);
        Config config = objectMapper.readValue(response.getContentAsByteArray(), Config.class);
        assertEquals(config.getDecimals(), decimals);
    }

    @Test
    public void doPost() throws IOException {
        Config config = new Config();
        final int decimals = 5;
        config.setDecimals(decimals);
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, config);
        request.setContentType("application/json");
        byte[] value = writer.toString().getBytes(Charset.defaultCharset());
        request.setContent(value);
        servlet.doPost(request,response);
        assertEquals(commandInterpreter.getConfig().getDecimals(), decimals);
    }

}