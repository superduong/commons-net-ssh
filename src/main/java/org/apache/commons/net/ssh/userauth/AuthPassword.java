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

import java.io.IOException;

import org.apache.commons.net.ssh.Service;
import org.apache.commons.net.ssh.Constants.Message;
import org.apache.commons.net.ssh.transport.Session;
import org.apache.commons.net.ssh.util.Buffer;
import org.apache.commons.net.ssh.util.LQString;

public class AuthPassword extends AbstractAuthMethod
{
    public interface ChangeRequestHandler extends UserAuthService.PasswordFinder
    {
        char[] getNewPassword();
        
        void notifyFailure();
        
        void notifySuccess();
        
        ChangeRequestHandler notifyUnacceptable();
        
        void setPrompt(LQString prompt);
    }
    
    public static final String NAME = "password";
    
    private final UserAuthService.PasswordFinder pwdf;
    private ChangeRequestHandler crh;
    private boolean changeRequested = false;
    
    // crh may be null
    public AuthPassword(Session session, Service nextService, String username,
            UserAuthService.PasswordFinder pwdf, ChangeRequestHandler crh)
    {
        super(session, nextService, username);
        assert pwdf != null;
        this.pwdf = pwdf;
        this.crh = crh;
    }
    
    @Override
    protected Buffer buildRequest()
    {
        Buffer buf = buildRequestCommon(new Buffer(Message.USERAUTH_REQUEST));
        buf.putBoolean(false);
        buf.putString(pwdf.getPassword());
        return buf;
    }
    
    public String getName()
    {
        return NAME;
    }
    
    @Override
    public Result handle(Message cmd, Buffer buf) throws IOException
    {
        switch (cmd)
        {
        case USERAUTH_SUCCESS:
            if (changeRequested)
                crh.notifySuccess();
            return Result.SUCCESS;
        case USERAUTH_FAILURE:
            setAllowedMethods(buf.getString());
            if (buf.getBoolean()) {
                if (changeRequested)
                    crh.notifySuccess();
                return Result.PARTIAL_SUCCESS;
            } else {
                if (changeRequested)
                    crh.notifyFailure();
                return Result.FAILURE;
            }
        case USERAUTH_60: // SSH_MSG_USERAUTH_PASSWD_CHANGEREQ
            if (changeRequested)
                crh = crh.notifyUnacceptable();
            if (crh != null) {
                crh.setPrompt(buf.getLanguageQualifiedField());
                sendChangeReq(buf.getString());
                changeRequested = true;
                return Result.CONTINUED;
            } else
                return Result.FAILURE;
        default:
            log.error("Unexpected packet");
            return Result.FAILURE;
        }
    }
    
    private void sendChangeReq(String prompt) throws IOException
    {
        Buffer buf = buildRequestCommon(new Buffer(Message.USERAUTH_60));
        buf.putBoolean(true);
        buf.putString(crh.getPassword());
        buf.putString(crh.getNewPassword());
        log.debug("Sending SSH_MSG_USERAUTH_PASSWD_CHANGEREQ");
        session.writePacket(buf);
    }
}