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

import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Provides methods to convert between different data structures
 *
 * @author TestProject LTD.
 * @version 1.0
 */
public class Converter {

    /**
     * Convert the first row in ResultSet object to JSONObject
     *
     * @param resultSet The ResultSet object to convert
     * @return JSONObject of the first row in the ResultSet object
     * @throws FailureException - In case an error has occurred in the process of converting.
     */

    // TODO: Add better exception instead of FailureException.. something that related to convert exception
    public static JSONObject convertResultSetToJson(ResultSet resultSet) throws JSONException, SQLException {

        // Create json object
        JSONObject jsonObject = new JSONObject();

        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get the number of columns in the row
        int columnCount = metaData.getColumnCount();

        // Add each column with its value as key with value to the json object
        for (int i = 0; i < columnCount; i++) {
            //if (resultSet.getString()) continue;

            String key = metaData.getColumnLabel(i + 1);

            Object value;
            try {
                value = resultSet.getString(i + 1);
            } catch (SQLException e) {
                value = "ERROR_READING_VALUE";
                // TODO: Check this
                /* This bug sometimes occurred in my tests.. it is rare and occurred on empty values
                    This is how I recover from the bug
                 */
            }

            jsonObject.put(key, value);

        }
        return jsonObject;
    }
}
