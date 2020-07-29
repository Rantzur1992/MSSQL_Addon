/*
 * Copyright 2018 TestProject LTD. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.testproject.Base;


import io.testproject.java.annotations.v2.Action;
import io.testproject.java.sdk.v2.addons.helpers.AddonHelper;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.reporters.ActionReporter;
import io.testproject.Common.SQLSession;
import io.testproject.Common.ValidateFields;

@Action(name = "Test SQL Connection")
public class TestSQLConnectionActionBase {

    // Parameters of the action
    public String m_host = "";

    public String m_port = "";

    public String m_username = "";

    public String m_password = "";

    public String m_isConnected = "";

    public String m_DbName;

    // SQL settings
    private int sqlDefaultPort;

    private String sqlType;

    private String jdbcDriver;

    /**
     * Initialize SQL settings (what is the type of the SQL server, the default port of it)
     */
    public TestSQLConnectionActionBase(String sqlType, String jdbcDriver, int sqlDefaultPort) {
        this.sqlType = sqlType;
        this.sqlDefaultPort = sqlDefaultPort;
        this.jdbcDriver = jdbcDriver;
    }

    /**
     * Register the action fields of the addon so the base class will know their values
     *
     * @param host     - The host of the SQL server
     * @param port     - The port of the host
     * @param username - The username for connecting to the host
     * @param password - The password for connecting to the host
     */
    protected void registerFields(String host, String port, String username, String password, String dbName) {
        this.m_host = host;
        this.m_port = port;
        this.m_username = username;
        this.m_password = password;
        this.m_DbName = dbName;
    }

    /**
     * The base of the action
     *
     * @param helper - WebAddonHelper object
     * @return ExecutionResult
     */
    protected ExecutionResult executeBase(AddonHelper helper) {

        ActionReporter report = helper.getReporter();

        m_isConnected = "false";

        // Set the default values
        if (m_port.equals("")) m_port = String.valueOf(sqlDefaultPort);
        if (m_username.equals("")) m_username = "root";

        // Validate fields
        try {
            ValidateFields.loginFields(m_host, m_username);
        } catch (IllegalArgumentException e) {
            report.result(e.getMessage());
            return ExecutionResult.FAILED;
        }

        // Connect to the server

        SQLSession sqlSession;
        try {
            sqlSession = new SQLSession(sqlType, jdbcDriver, m_username, m_password, m_host, Integer.parseInt(m_port), m_DbName);
        } catch (Exception e) {
            report.result(e.getMessage());
            return ExecutionResult.FAILED;
        }

        m_isConnected = "true";

        report.result("Connection to " + m_host + " has been established");

        sqlSession.free();

        return ExecutionResult.PASSED;
    }
}
