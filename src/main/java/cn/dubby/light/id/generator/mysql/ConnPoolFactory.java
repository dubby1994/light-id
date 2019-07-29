package cn.dubby.light.id.generator.mysql;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnPoolFactory extends BasePooledObjectFactory<Connection> {

    private String uri;

    private String username;

    private String password;

    public ConnPoolFactory(String uri, String username, String password, int timeout) {
        this.uri = uri;
        this.username = username;
        this.password = password;
    }

    @Override
    public Connection create() throws Exception {
        return DriverManager.getConnection(uri, username, password);
    }

    @Override
    public PooledObject<Connection> wrap(Connection obj) {
        return new DefaultPooledObject<>(obj);
    }

    @Override
    public void destroyObject(PooledObject<Connection> p) throws Exception {
        p.getObject().close();
    }
}
