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
import io.testproject.Base.SendSQLQueryActionBase;

import java.sql.SQLException;

@Action(name ="Send SQL Query (MSSQL Database)", description = "Send an SQL query to an MSSQL Database")
public class SendSQLQueryAction extends SendSQLQueryActionBase implements GenericAction {

    // Changed params to public.
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

    @Parameter(description = "SQL Query")
    public String query = "";

    @Parameter(description = "Should the data be encrypted? (Default: false)")
    public String encrypt = "";

    @Parameter(description = "Trust Server Certificate? (Default: false)")
    public String trustServerCertificate = "";

    @Parameter(description = "Login timeout (Default: 30 seconds)")
    public String loginTimeout = "";

    @Parameter(description = "SQL output - JSON strings of the result", direction = ParameterDirection.OUTPUT)
    public String queryResponse = "";

    public SendSQLQueryAction() {
        super("sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver", 1433);
    }

    @Override
    public ExecutionResult execute(AddonHelper helper) throws FailureException {
        // Fix the query string.
        // Replace UI space with regular space.
        this.query = query.replace(' ', ' ');
        // Trim last semi-colon.
        if (query.substring(query.length() - 1).equals(";")) {
            query = query.substring(0, query.length() - 1);
        }
        // Register the action's field with the base class.
        registerFields(host, port, username, password, databaseName, encrypt, trustServerCertificate, loginTimeout, query);
        // Run the base class to send the query and collect the response.
        ExecutionResult result = null;
        try {
            result = executeBase(helper);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        queryResponse = super.m_queryResponse;
        // Return the result of execution.
        return result;
    }

}
