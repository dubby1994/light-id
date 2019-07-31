package cn.dubby.light.id.generator.mysql;

import cn.dubby.light.id.config.GenProviderConfig;
import cn.dubby.light.id.generator.IDProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLProvider extends IDProvider {

    private static final Logger logger = LoggerFactory.getLogger(MySQLProvider.class);

    public MySQLProvider(GenProviderConfig config) throws SQLException {
        this.config = config;
        this.id = config.getId();
        this.sql = "INSERT INTO " + config.getNamespace() + "(ID) VALUE(NULL);";
        connection = DriverManager.getConnection(config.getUrl(), config.getOptions().get("username"), config.getOptions().get("password"));
    }

    private GenProviderConfig config;

    private Connection connection;

    private String sql;

    @Override
    public long increaseByDataSource() {
        Statement statement = null;
        ResultSet rs = null;
        try {
            statement = connection.createStatement();
            statement.execute(sql, Statement.RETURN_GENERATED_KEYS);
            rs = statement.getGeneratedKeys();
            if (rs.next()) {
                long value = rs.getLong(1);
                rs.close();
                statement.close();
                return value;
            }
        } catch (Exception e) {
            logger.error("provide, id:{}, sql:{}", id, sql);
            if (connection == null) {
                try {
                    createConn();
                } catch (SQLException e1) {
                    logger.error("provide, id:{}, sql:{}", id, sql);
                    return -1;
                }
            }
        }
        return -1;
    }

    private void createConn() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(config.getUrl(), config.getOptions().get("username"), config.getOptions().get("password"));
        }
    }

}
