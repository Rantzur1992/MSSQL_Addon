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

@Action(name = "Test SQL Connection (NEW MSSQL Database)", description = "Test the connection to the MSSQL Database")
public class TestSQLConnectionAction extends TestSQLConnectionActionBase implements GenericAction {

    @Parameter(description = "Host or IP address")
    public String host = "";

    @Parameter(description = "Port (Default: 1433)")
    public String port = "";

    @Parameter(description = "User (Default: root)")
    public String username = "";

    @Parameter(description = "Password")
    public String password = "";

    @Parameter(description = "Database Name (Required)")
    public String databaseName = "";

    @Parameter(description = "\"true\" if the connection is fine, \"false\" if not", direction = ParameterDirection.OUTPUT)
    public String isConnected = "";

    @Parameter(description = "Should the data be encrypted? (Default: false)")
    public String encrypt = "";

    @Parameter(description = "Trust Server Certificate? (Default: false)")
    public String trustServerCertificate = "";

    @Parameter(description = "Login timeout (Default: 30 seconds)")
    public String loginTimeout = "";

    public TestSQLConnectionAction() {
        super("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433);
    }

    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        registerFields(host, port, username, password, databaseName, encrypt, trustServerCertificate, loginTimeout);
        ExecutionResult result = executeBase(helper);
        isConnected = super.m_isConnected;
        return result;
    }
}
