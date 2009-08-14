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
package org.apache.commons.net.ssh.digest;

/**
 * MD5 Digest.
 * 
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class MD5 extends BaseDigest
{
    
    /**
     * Named factory for MD5 digest
     */
    public static class Factory implements org.apache.commons.net.ssh.Factory.Named<Digest>
    {
        
        public Digest create()
        {
            return new MD5();
        }
        
        public String getName()
        {
            return "md5";
        }
    }
    
    /**
     * Create a new instance of a MD5 digest
     */
    public MD5()
    {
        super("MD5", 16);
    }
}
