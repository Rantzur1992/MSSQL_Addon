import io.testproject.Action.TestSQLConnectionAction;
import io.testproject.Action.SendSQLQueryAction;
import io.testproject.java.enums.AutomatedBrowserType;
import io.testproject.java.sdk.v2.Runner;


import java.io.IOException;

public class Tester {

    private static Runner runner;
    private final static String devToken = "kJCeheoppFyo8uaZ17k0JQyBck1qLIf5ZrynbI6t7Fk1";

    public static void init() throws InstantiationException {
        runner = Runner.create(devToken);
    }

    public static void tearDown() throws IOException {
        runner.close();
    }

    public static void main(String[] args) throws Exception {
        init();

        // Create actions.
/*        TestSQLConnectionAction test = new TestSQLConnectionAction();
        SendSQLQueryAction send = new SendSQLQueryAction();
        test.databaseName = "test";
        test.host = "localhost";
        test.username = "sa";
        test.password = "Soleterr123!";
        test.encrypt = "false";
        test.trustServerCertificate = "false";
        test.loginTimeout = "30";
        runner.run(test);*/


        SendSQLQueryAction send = new SendSQLQueryAction();
        send.host = "localhost";
        send.password = "Soleterr123!";
        send.username = "sa";
        send.databaseName = "test";
        send.query = "SELECT TOP (1000) [brand_id]\n" +
                "      ,[brand_name]\n" +
                "  FROM [test].[production].[brands]";
        runner.run(send);

        tearDown();
    }
}

