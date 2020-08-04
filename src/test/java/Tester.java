import io.testproject.Action.TestSQLConnectionAction;
import io.testproject.Action.SendSQLQueryAction;
import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;


import java.io.IOException;

public class Tester {

    private static Runner runner;
    private final static String devToken = "kJCeheoppFyo8uaZ17k0JQyBck1qLIf5ZrynbI6t7Fk1";

    public static void init() throws InstantiationException {
        runner = Runner.createWeb(devToken, AutomatedBrowserType.Chrome);
    }

    public static void tearDown() throws IOException {
        runner.close();
    }

    public static void main(String[] args) throws Exception {
        init();

        // Create actions.
        TestSQLConnectionAction test = new TestSQLConnectionAction();
        //SendSQLQueryAction send = new SendSQLQueryAction();

        // CONNECTION TEST. -- WORKS.
        // Set Params for test.
        test.host = "localhost";
        test.password = "1234";
        test.username = "db2inst1";
        test.port = "50000";
        test.databaseName = "books";
        //test.SID = "xe";
        //test.Service = "xe";
        runner.run(test);


        //SendSQLQueryAction send = new SendSQLQueryAction();

        //send.host = "localhost";
        //send.password = "1234";
        //send.username = "db2inst1";
        //send.port = "50000";
        //send.databaseName = "books";
        //send.query = "SELECT * FROM authors";
        //test.SID = "xe";
        //test.Service = "xe";
        //runner.run(send);


        tearDown();
    }
}

