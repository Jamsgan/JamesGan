package datapager.tools.databaseconnector;

import java.sql.Driver;
import java.sql.SQLException;

import javax.sql.DataSource;

import datapager.datapager.Options;
import datapager.datapager.UserCredentials;

public interface ConnectionOptions extends Options, DataSource {

	String getConnectionUrl();

	Driver getJdbcDriver() throws SQLException;

	UserCredentials getUserCredentials();

}
