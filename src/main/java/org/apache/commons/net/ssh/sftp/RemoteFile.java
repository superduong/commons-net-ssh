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
package org.apache.commons.net.ssh.sftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ssh.sftp.Response.StatusCode;

public class RemoteFile extends RemoteResource
{
    
    public RemoteFile(SFTP sftp, String path, String handle)
    {
        super(sftp, path, handle);
    }
    
    public InputStream getInputStream()
    {
        return new RemoteFileInputStream(this);
    }
    
    public OutputStream getOutputStream()
    {
        return new RemoteFileOutputStream(this);
    }
    
    public FileAttributes getFileAttributes() throws IOException
    {
        Request req = newRequest(PacketType.FSTAT);
        sftp.send(req);
        Response res = req.getFuture().get(sftp.timeout);
        res.ensurePacket(PacketType.ATTRS);
        return res.readFileAttributes();
    }
    
    public int read(long fileOffset, byte[] buf, int offset, int len) throws IOException
    {
        Request req = newRequest(PacketType.READ);
        req.putUINT64(fileOffset);
        req.putInt(len);
        sftp.send(req);
        Response res = req.getFuture().get(sftp.timeout);
        switch (res.getType())
        {
        case DATA:
            int recvLen = res.readInt();
            System.arraycopy(res.array(), res.rpos(), buf, offset, recvLen);
            return recvLen;
        case STATUS:
            res.ensureStatus(StatusCode.EOF);
            return -1;
        default:
            throw new SFTPException("Unexpected packet: " + res.getType());
        }
    }
    
    public void write(long fileOffset, byte[] data, int off, int len) throws IOException
    {
        Request req = newRequest(PacketType.WRITE);
        req.putUINT64(fileOffset);
        req.putInt(len - off);
        req.putRawBytes(data, off, len);
        // req.putString(data, off, len);
        sftp.send(req);
        req.getFuture().get(sftp.timeout).ensureStatusOK();
    }
    
    public void setAttributes(FileAttributes attrs) throws IOException
    {
        Request req = newRequest(PacketType.FSETSTAT);
        req.putFileAttributes(attrs);
        sftp.send(req);
        req.getFuture().get(sftp.timeout).ensureStatusOK();
    }
    
    public int getOutgoingPacketOverhead()
    {
        return 1 + // packet type
                4 + // request id
                4 + // handle length
                handle.length() + // handle
                8 + // file offset
                4 + // data length
                4; // packet length
    }
    
}
