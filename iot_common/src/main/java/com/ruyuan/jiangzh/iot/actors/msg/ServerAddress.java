package com.ruyuan.jiangzh.iot.actors.msg;

import java.io.Serializable;

public class ServerAddress implements Serializable, Comparable<ServerAddress> {

    private final String host;
    private final int port;

    public ServerAddress(String host,int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public int compareTo(ServerAddress o) {
        int result = this.host.compareTo(o.host);
        if(result == 0){
            result = this.port - o.port;
        }
        return result;
    }

    @Override
    public String toString() {
        return "ServerAddress{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
