package org.spacehq.mc.protocol.data.status;

public class VersionInfo {

    private String name;
    private int protocol;

    public VersionInfo(String name, int protocol) {
        this.name = name;
        this.protocol = protocol;
    }

    public String getVersionName() {
        return this.name;
    }

    public int getProtocolVersion() {
        return this.protocol;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        VersionInfo that = (VersionInfo) o;

        if(protocol != that.protocol) return false;
        if(!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + protocol;
        return result;
    }

}
