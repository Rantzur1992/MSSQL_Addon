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

import com.google.common.base.Strings;

/**
 * Provides methods validate common fields of the actions
 *
 * @author TestProject LTD.
 * @version 1.0
 */
public class ValidateFields {


    /**
     * Validate base fields of the actions in the addon
     *
     * @param dbHost        The database host/ip
     *
     * @param dbUser        The database username
     *
     *
     * @throws IllegalArgumentException - In case an error has occurred in the process of converting.
     *
     */
    public static void loginFields(String dbHost, String dbUser) throws IllegalArgumentException {
        if (dbHost.equals(""))
            throw new IllegalArgumentException("The host field can't be empty");

        if (dbUser.equals(""))
            throw new IllegalArgumentException("The username field can't be empty");
    }


    public static void connectionFields(String query, String DbName) throws IllegalArgumentException {
        if (query.isEmpty())
            throw new IllegalArgumentException("The query field can't be empty");
        if(Strings.isNullOrEmpty(DbName))
            throw new IllegalArgumentException("The Database Name field can't be empty");
    }
}
