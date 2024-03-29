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
package examples.ssh;

import java.net.InetSocketAddress;

import org.apache.commons.net.ssh.SSHClient;

/**
 * This example demonstrates local port forwarding, i.e. when we listen on a particular address and port; and forward
 * all incoming connections to SSH server which further forwards them to a specified address and port.
 */
public class LocalPF
{
    
    // static
    // {
    // BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%d [%-15.15t] %-5p %-30.30c{1} - %m%n")));
    // }
    
    public static void main(String... args) throws Exception
    {
        SSHClient ssh = new SSHClient();
        
        ssh.loadKnownHosts();
        
        ssh.connect("localhost");
        try
        {
            
            ssh.authPublickey(System.getProperty("user.name"));
            
            /*
             * _We_ listen on localhost:8080 and forward all connections on to server, which then forwards it to
             * google.com:80
             */

            ssh.newLocalPortForwarder(new InetSocketAddress("localhost", 8080), "google.com", 80).listen();
            
        } finally
        {
            ssh.disconnect();
        }
    }
    
}
