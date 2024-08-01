package util

import kotlinx.coroutines.runBlocking
import lombok.Cleanup
import util.ResourcePath.getLocalResourcePath
import util.extension.downloadFile
import java.io.FileInputStream
import java.sql.*
import java.util.*

/**
 * Singleton object for managing API keys stored in a database.
 * It provides functionality to load database properties and retrieve API keys by service name.
 */
object Keys {
  private var props: Properties

  /**
   * Initializes the Keys object by loading database connection properties.
   */
  init {
    runBlocking {
      val resourceStream = FileInputStream(downloadFile(util.extension.props, getLocalResourcePath("db.properties")))
      props = Properties().apply {
        load(resourceStream)
      }
    }
  }

  private const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"

  /**
   * Retrieves an API key for a given service from the database.
   *
   * @param key The name of the service for which the API key is requested.
   * @return The API key as a String if found, null otherwise.
   * @throws SQLException If there is an issue with the database connection or query execution.
   */
  @Throws(SQLException::class)
  @JvmStatic
  fun get(key: String): String? {
    @Cleanup val connection: Connection?
    @Cleanup val preparedStatement: PreparedStatement?
    @Cleanup val resultSet: ResultSet?

    try {
      Class.forName(JDBC_DRIVER)
      connection =
        DriverManager.getConnection(props.getProperty("DB_URL1") + props.getProperty("PASSWORD") + props.getProperty("DB_URL2"))
      val sql = "SELECT apikey FROM apikeys WHERE service = ?"
      preparedStatement = connection.prepareStatement(sql)
      preparedStatement.setString(1, key)
      resultSet = preparedStatement.executeQuery()
      return if (resultSet.next()) {
        resultSet.getString("apikey")
      } else {
        null
      }
    } catch (e: ClassNotFoundException) {
      throw SQLException("MySQL JDBC Driver not found!", e)
    }
  }
}