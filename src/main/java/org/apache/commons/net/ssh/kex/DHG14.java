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
package org.apache.commons.net.ssh.kex;

/**
 * Diffie-Hellman key exchange with SHA-1 and Oakley Group 14 [RFC3526] (2048-bit MODP Group).
 * <p>
 * DHG14 does not work with the default JCE implementation provided by Sun because it does not support 2048 bits
 * encryption. It requires BouncyCastle to be used.
 */
public class DHG14 extends AbstractDHG
{
    
    /**
     * Named factory for DHG14 key exchange
     */
    public static class Factory implements org.apache.commons.net.ssh.Factory.Named<KeyExchange>
    {
        
        public KeyExchange create()
        {
            return new DHG14();
        }
        
        public String getName()
        {
            return "diffie-hellman-group14-sha1";
        }
        
    }
    
    @Override
    protected void initDH(DH dh)
    {
        dh.setG(DHGroupData.getG());
        dh.setP(DHGroupData.getP14());
    }
    
}
