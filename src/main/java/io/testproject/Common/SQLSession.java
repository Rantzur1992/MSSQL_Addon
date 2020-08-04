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
package io.testproject.Common;
import java.sql.*;

/**
 * // TODO: Help me write the description
 *
 * @author TestProject LTD.
 * @version 1.0
 */
public class SQLSession {

    private Connection connection = null;
    private Statement statement = null;



    /**
     * The constructor of the SQLSession
     * Once initialized, a connection with the MySQL server is created.
     *
     * @param dbUser      The database username
     * @param dbPassword  The database password
     * @param dbHost      The database host
     * @param dbPort      The database port
     *
     * @throws ClassNotFoundException - The JDBC driver not found
     * @throws  SQLException - An an sql error has been occurred
     *
     */
    public SQLSession(String sqlType, String jdbcDriver, String dbUser, String dbPassword, String dbHost, int dbPort, String dbName) throws ClassNotFoundException, SQLException {
        Class.forName(jdbcDriver);
        // Set URL
        String url = String.format("jdbc:db2://%s:%s/%s", dbHost, dbPort, dbName);
        // Add SID or Service name if applicable.
        // Older format for SID.
        //if (!SID.equals("")) {
            //url = url + ":" + SID;
        //}
        // Newer format for Service.
        //if (!Service.equals("")) {
            //url = url + "/" + Service;
        //}

        connection = DriverManager.getConnection(url, dbUser, dbPassword);
        statement = connection.createStatement();
    }

    /**
     * Send query to the MySQL server and reserve response
     *
     * @param query The query to send the the server
     * @return ResultSet object that contain the requested information by the query
     * @throws SQLException - An an sql error has been occurred
     */
    public ResultSet sendQuery(String query) throws SQLException {

        ResultSet resultSet = null;

        if (statement.execute(query))
            resultSet = statement.getResultSet();

        return resultSet;
    }

    /**
     * Close the connection with the server.
     * Call this when you finish working with the server and the object
     */
    public void free() {
        try {
            if (statement != null)
                statement.close();

            if (connection != null)
                connection.close();

        } catch (SQLException e) {
            //
        }
    }
}
