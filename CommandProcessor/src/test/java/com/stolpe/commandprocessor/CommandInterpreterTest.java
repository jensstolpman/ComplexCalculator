package com.stolpe.commandprocessor;


import org.junit.*;
import org.junit.jupiter.api.Assertions;

public class CommandInterpreterTest {
    private CommandInterpreter commandInterpreter = null;

    public CommandInterpreterTest(){
        super();
    }
    @Before
    public void setUp() {
        this.commandInterpreter = CommandInterpreter.getInstance();
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
        command.setNewEnter(0);
        Command result = commandInterpreter.processCommand(command);
        Assertions.assertEquals(result.getValue(), command.getValue());
    }


}
