package org.apache.commons.net.ssh;

import org.apache.commons.net.ssh.transport.Transport;
import org.apache.commons.net.ssh.transport.TransportException;
import org.apache.commons.net.ssh.util.Constants.DisconnectReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for {@link Service} that implements common or default functionality.
 * 
 * @author shikhar
 */
public abstract class AbstractService implements Service
{
    
    protected final Logger log = LoggerFactory.getLogger(getClass());
    
    protected final String name;
    protected final Transport trans;
    protected int timeout;
    
    public AbstractService(String name, Transport trans)
    {
        this.name = name;
        this.trans = trans;
        timeout = trans.getTimeout();
    }
    
    public String getName()
    {
        return name;
    }
    
    public int getTimeout()
    {
        return this.timeout;
    }
    
    public Transport getTransport()
    {
        return trans;
    }
    
    public void notifyUnimplemented(long seqNum) throws SSHException
    {
        throw new SSHException(DisconnectReason.PROTOCOL_ERROR, "Unexpected: SSH_MSG_UNIMPLEMENTED");
    }
    
    public void request() throws TransportException
    {
        Service active = trans.getService();
        if (!equals(active))
            if (active != null && getName().equals(active.getName()))
                trans.setService(this);
            else
                trans.reqService(this);
    }
    
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
    
}
