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
package org.apache.commons.net.ssh.util;

import java.io.Closeable;
import java.io.IOException;

import org.apache.commons.net.ssh.transport.PacketWriter;

/**
 * TODO Add javadoc
 * 
 * @author <a href="mailto:dev@mina.apache.org">Apache MINA SSHD Project</a>
 */
public class IOUtils
{
    
    public static void closeQuietly(Closeable... closeables)
    {
        for (Closeable c : closeables)
            try {
                if (c != null)
                    c.close();
            } catch (IOException ignored) {
            }
    }
    
    public static long writeQuietly(PacketWriter pw, Buffer payload)
    {
        try {
            return pw.writePacket(payload);
        } catch (IOException ignored) {
            return -1;
        }
    }
    
}
