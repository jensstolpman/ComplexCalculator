package com.stolpe.commandprocessor;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class CommandInterpreterTest {
    private CommandInterpreter commandInterpreter = null;

    public CommandInterpreterTest(){
        super();
    }
    @Before
    public void setUp() {
        this.commandInterpreter = new CommandInterpreter();
        Assertions.assertNotNull(this.commandInterpreter.getCalculator());
    }

    @After
    public void tearDown() {
    }

    @Test
    public void processCommandEnter(){
        Command command = new Command();
        command.setValue("4-5i");
        command.setCommand("enter");
        Command result = commandInterpreter.processCommand(command);
        Assertions.assertEquals(result.getValue(), command.getValue());
    }


}
