package ch.spacebase.mcprotocol.event;

/**
 * Visitable interface to allow packets to accept visitors
 * 
 * @author dconnor
 */
public interface PacketVisitable {

    public void accept(PacketVisitor visitor);
}
