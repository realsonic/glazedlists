/**
 * Glazed Lists
 * http://glazedlists.dev.java.net/
 *
 * COPYRIGHT 2003 O'DELL ENGINEERING LTD.
 */
package ca.odell.glazedlists.impl.rbp;

// NIO is used for BRP
import java.util.*;
import java.nio.*;
import java.io.*;
import ca.odell.glazedlists.impl.io.Bufferlo;
// logging
import java.util.logging.*;

/**
 * Maintains the state for a particular resource on a particular connection.
 *
 * @author <a href="mailto:jesse@odel.on.ca">Jesse Wilson</a>
 */
class ResourceConnection {

    /** logging */
    private static Logger logger = Logger.getLogger(ResourceConnection.class.toString());
    
    /** the connection */
    private PeerConnection connection;
    
    /** the resource */
    private PeerResource resource;
    
    /** the resource's current update */
    private int updateId = -1;
    
    /**
     * Create a new {@link ResourceConnection} to manage the state of the specified
     * connection and resource.
     */
    public ResourceConnection(PeerConnection connection, PeerResource resource) {
        this.connection = connection;
        this.resource = resource;
    }
    
    /**
     * The current update of this resource connection.
     */
    public void setUpdateId(int updateId) {
        this.updateId = updateId;
    }
    public int getUpdateId() {
        return updateId;
    }
    
    /**
     * Gets the connection that is interested in this resource.
     */
    public PeerConnection getConnection() {
        return connection;
    }
    
    /**
     * Gets the resource that is attached to this connection.
     */
    public PeerResource getResource() {
        return resource;
    }
}