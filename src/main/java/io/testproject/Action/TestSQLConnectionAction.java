package io.testproject.Action;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.enums.ParameterDirection;
import io.testproject.java.sdk.v2.addons.GenericAction;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.AddonHelper;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import io.testproject.Base.TestSQLConnectionActionBase;

@Action(name = "Test SQL Connection (Db2 Database)", description = "Test the connection to the IBM Db2 database")
public class TestSQLConnectionAction extends TestSQLConnectionActionBase implements GenericAction {

    @Parameter(description = "Host or IP address")
    public String host = "";

    @Parameter(description = "Port (Default: 50000)")
    public String port = "";

    @Parameter(description = "User (Default: root)")
    public String username = "";

    @Parameter(description = "Password")
    public String password = "";

    @Parameter(description = "Database Name (Required)")
    public String databaseName = "";

    @Parameter(description = "\"true\" if the connection is fine, \"false\" if not", direction = ParameterDirection.OUTPUT)
    public String isConnected = "";

    public TestSQLConnectionAction() {
        super("sqlserver", "com.ibm.db2.jcc.DB2Driver", 50000);
    }

    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        registerFields(host, port, username, password, databaseName);
        ExecutionResult result = executeBase(helper);
        isConnected = super.m_isConnected;
        return result;
    }
}
