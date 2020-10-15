package io.testproject.Base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.testproject.Common.Converter;
import io.testproject.Common.SQLSession;
import io.testproject.Common.ValidateFields;
import io.testproject.java.sdk.v2.addons.helpers.AddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.reporters.ActionReporter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The io.testproject.Base for the action "Send SQL Query" in all addons
 *
 * @author TestProject LTD.
 * @version 1.0
 */
public class SendSQLQueryActionBase {

    // Parameters of the action
    public String m_host = "";

    public String m_port = "";

    public String m_username = "";

    public String m_password = "";

    public String m_query = "";

    public String m_queryResponse = "";

    public String m_queryResponseAsJson = "";

    public String m_dbName = "";

    public String m_dbEncrypt = "";

    public String m_dbTrustCertificate = "";

    public String m_dbTimeOut = "";

    // SQL settings
    private int sqlDefaultPort;

    private String sqlType;

    private String jdbcDriver;

    /**
     * Initialize SQL settings (what is the type of the SQL server, the default port of it)
     *
     * @param sqlType        - The type of the SQL
     * @param sqlDefaultPort - The default port of the SQL server
     * @param jdbcDriver     - The jdbc driver to use for the connection
     */
    public SendSQLQueryActionBase(String sqlType, String jdbcDriver, int sqlDefaultPort) {
        this.sqlType = sqlType;
        this.sqlDefaultPort = sqlDefaultPort;
        this.jdbcDriver = jdbcDriver;
    }

    /**
     * Register the action fields of the addon so the base class will know their values
     *
     * @param host        - The host of the SQL server
     * @param port        - The port of the host
     * @param username    - The username for connecting to the host
     * @param password    - The password for connecting to the host
     * @param query       - The query to send
     */
    protected void registerFields(String host, String port, String username, String password,
                                  String dbName, String dbEncrypt, String dbTrustCertificate,
                                  String dbTimeOut, String query) {
        this.m_host = host;
        this.m_port = port;
        this.m_username = username;
        this.m_password = password;
        this.m_dbName = dbName;
        this.m_dbEncrypt = dbEncrypt;
        this.m_dbTrustCertificate = dbTrustCertificate;
        this.m_dbTimeOut = dbTimeOut;
        this.m_query = query;
    }

    /**
     * The base of the action
     *
     * @param helper - WebAddonHelper object
     * @return ExecutionResult
     */
    protected ExecutionResult executeBase(AddonHelper helper) throws SQLException {
        ActionReporter report = helper.getReporter();

        // Set the default values
        if (m_username.isEmpty()) m_username = "root";
        if (m_port.isEmpty()) m_port = String.valueOf(sqlDefaultPort);
        if (m_dbEncrypt.isEmpty()) m_dbEncrypt = "false";
        if (m_dbTrustCertificate.isEmpty()) m_dbTrustCertificate = "false";
        if (m_dbTimeOut.isEmpty()) m_dbTimeOut = "30";

        // Validate fields
        try {
            ValidateFields.loginFields(m_host, m_username);
            ValidateFields.connectionFields(m_query, m_dbName);
        } catch (IllegalArgumentException e) {
            report.result(e.getMessage());
            return ExecutionResult.FAILED;
        }

        // Connect to the server
        SQLSession sqlSession;

        try {
            sqlSession = new SQLSession(sqlType, jdbcDriver, m_username, m_password, m_host, Integer.parseInt(m_port),
                    m_dbName,  Boolean.parseBoolean(m_dbEncrypt),
                    Boolean.parseBoolean(m_dbTrustCertificate),
                    Integer.parseInt(m_dbTimeOut));
        } catch (ClassNotFoundException e) {
            report.result("Unable to load the jdbc driver \"" + jdbcDriver + "\". Exception: " + e.getMessage());
            return ExecutionResult.FAILED;
        } catch (SQLException e) {
            report.result(e.getMessage());
            return ExecutionResult.FAILED;
        }

        // Send SQL query to the server
        ResultSet resultSet;
        try {
            resultSet = sqlSession.sendQuery(m_query);
        } catch (Exception e) {
            report.result(e.getMessage());
            sqlSession.free();
            return ExecutionResult.FAILED;
        }

        // In case we got data from the query, we extract the selectedRow into json
        if (resultSet != null) {
            List<Map<String, Object>> rows = new ArrayList<>();
            ResultSetMetaData rsmd = resultSet.getMetaData();
            int columnCount = rsmd.getColumnCount();

            while (resultSet.next()) {
                // Convert the response to json string and save it on the output variable ( NOT AS JSON )
                try {
                    m_queryResponse = m_queryResponse + Converter.convertResultSetToJson(resultSet).toString();
                } catch (Exception e) {
                    report.result("An error has occurred while reading the response from the server: " + e);
                    return ExecutionResult.FAILED;
                }

                // Represent a row in DB. Key: Column name, Value: Column value
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    // Note that the index is 1-based
                    String colName = rsmd.getColumnName(i);
                    Object colVal = resultSet.getObject(i);
                    row.put(colName, colVal);
                }
                rows.add(row);
            }
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                m_queryResponseAsJson = objectMapper.writeValueAsString(rows.toString());
            } catch (JsonProcessingException e) {
                report.result("Failed to parse results: " + e);
                return ExecutionResult.FAILED;
            }
        }

        // Free object resources
        sqlSession.free();

        // Report the final result
        report.result("Successfully sent the query.\n The query Response is: \n" + m_queryResponse + "\n Query response as JSON is: " + m_queryResponseAsJson);
        return ExecutionResult.PASSED;
    }
}
