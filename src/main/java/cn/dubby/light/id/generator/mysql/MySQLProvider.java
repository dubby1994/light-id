package cn.dubby.light.id.generator.mysql;

import cn.dubby.light.id.generator.IDProvider;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySQLProvider implements IDProvider {

    private static final Logger logger = LoggerFactory.getLogger(MySQLProvider.class);

    public MySQLProvider(int id, GenericObjectPool<Connection> connPool, String tableName) {
        this.id = id;
        this.connPool = connPool;
        this.sql = "INSERT INTO " + tableName + "(ID) VALUE(NULL);";
    }

    private int id;

    private GenericObjectPool<Connection> connPool;

    private String sql;

    @Override
    public long provide() {
        Connection conn = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            conn = connPool.borrowObject();
            statement = conn.createStatement();
            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long value = rs.getLong(1);
                rs.close();
                statement.close();
                return value << 10 | id;
            }
        } catch (Exception e) {
            logger.error("provide, id:{}, sql:{}", id, sql);
        } finally {
            if (conn != null) {
                connPool.returnObject(conn);
            }
        }
        return -1;
    }

}
