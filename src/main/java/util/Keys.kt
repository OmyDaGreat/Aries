package util

import lombok.Cleanup
import java.sql.*
import java.util.*

/**
 * Singleton object for managing API keys stored in a database.
 * It provides functionality to load database properties and retrieve API keys by service name.
 */
object Keys {
  private lateinit var props: Properties

  /**
   * Initializes the Keys object by loading database connection properties.
   */
  init {
    loadProperties()
  }

  /**
   * Loads database connection properties from the `db.properties` file.
   * Logs an error if the properties file cannot be found or loaded.
   */
  private fun loadProperties() {
    try {
      val resourceStream = javaClass.classLoader.getResourceAsStream("db.properties")
      if (resourceStream != null) {
        props = Properties().apply {
          load(resourceStream)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
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