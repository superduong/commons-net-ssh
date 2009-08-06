package org.apache.commons.net.ssh.scp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.net.ssh.SSHClient;
import org.apache.commons.net.ssh.connection.ConnectionException;
import org.apache.commons.net.ssh.transport.TransportException;

public class SCPUploadClient extends SCPClient
{
    
    protected final ModeGetter modeGetter;
    
    public SCPUploadClient(SSHClient host)
    {
        this(host, null);
    }
    
    public SCPUploadClient(SSHClient host, ModeGetter modeGetter)
    {
        super(host);
        this.modeGetter = modeGetter == null ? new DefaultModeGetter() : modeGetter;
    }
    
    @Override
    public synchronized int copy(String sourcePath, String targetPath) throws IOException
    {
        init(targetPath);
        copy(new File(sourcePath), true);
        return exit();
    }
    
    protected void copy(File f, boolean check) throws IOException
    {
        if (check)
            checkResponseOK();
        
        if (modeGetter.shouldPreserveTimes())
            sendMessage("T" + modeGetter.getLastAccessTime(f) + " 0 " + modeGetter.getLastAccessTime(f) + " 0");
        
        if (f.isDirectory()) {
            log.info("Entering directory `{}`", f.getName());
            sendMessage("D0" + modeGetter.getPermissions(f) + " 0 " + f.getName());
            for (File child : f.listFiles())
                copy(child, false);
            sendMessage("E");
            log.info("Exiting directory `{}`", f.getName());
        }

        else if (f.isFile()) {
            sendMessage("C0" + modeGetter.getPermissions(f) + " " + f.length() + " " + f.getName());
            log.info("Sending `{}`", f.getName());
            doCopy(new FileInputStream(f), scp.getOutputStream(), scp.getRemoteMaxPacketSize(), f.length());
            checkResponseOK();
        }

        else
            throw new IOException("File type not supported for SCP: " + f);
    }
    
    protected void init(String target) throws ConnectionException, TransportException
    {
        List<String> args = new LinkedList<String>();
        args.add(Arg.SINK.toString());
        args.add(Arg.RECURSIVE.toString());
        args.add(target == null || target.equals("") ? "." : target);
        execSCPWith(args);
    }
    
}