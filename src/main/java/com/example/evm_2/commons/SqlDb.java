package com.example.evm_2.commons;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class SqlDb {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception E){
            E.printStackTrace();
        }

        config.setJdbcUrl( Credentials.SQL_URL );
        config.setUsername( Credentials.SQL_USER_NAME );
        config.setPassword( Credentials.SQL_PWD );
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource( config );
    }

    private SqlDb() {

    }

    public static Connection getConnection() throws Exception {

        return ds.getConnection();
    }
}
