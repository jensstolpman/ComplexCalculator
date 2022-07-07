package com.stolpe.commandprocessor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        CommandInterpreterTest.class,
        CommandTest.class
})

public class TestSuite {
    public TestSuite(){
        super();
    }
}