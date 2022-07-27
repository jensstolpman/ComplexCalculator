package com.stolpe.commandprocessor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CalculatorProcessorTest.class,
        CommandInterpreterTest.class,
        CommandTest.class,
        CalculatorConfiguratorTest.class,
})

public class TestSuite {
    public TestSuite(){
        super();
    }
}