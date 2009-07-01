/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.net.ssh.userauth;

import org.apache.commons.net.ssh.Service;
import org.apache.commons.net.ssh.Constants.Message;
import org.apache.commons.net.ssh.transport.Session;
import org.apache.commons.net.ssh.util.Buffer;

public class AuthNone extends AbstractAuthMethod
{
    
    public static final String NAME = "none";
    
    public AuthNone(Session session, Service nextService, String username)
    {
        super(session, nextService, username);
    }
    
    @Override
    protected Buffer buildRequest()
    {
        return buildRequestCommon(new Buffer(Message.USERAUTH_REQUEST));
    }
    
    public String getName()
    {
        return NAME;
    }
    
}