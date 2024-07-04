package util

import lombok.Cleanup
import org.apache.logging.log4j.LogManager
import java.sql.*
import java.util.Properties

object Keys {
    private val log = LogManager.getLogger(Keys::class.java)
    private lateinit var props: Properties

    init {
        loadProperties()
    }

    private fun loadProperties() {
        try {
            val resourceStream = javaClass.classLoader.getResourceAsStream("db.properties")
            if (resourceStream != null) {
                props = Properties().apply {
                    load(resourceStream)
                }
                props.forEach { key, value -> log.debug("{}: {}", key, value) }
            } else {
                log.debug("db.properties file not found")
            }
        } catch (e: Exception) {
            log.debug("Error loading db.properties: ${e.message}")
        }
    }

    private const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"

    @Throws(SQLException::class)
    @JvmStatic
    fun get(key: String): String? {
        @Cleanup val connection: Connection?
        @Cleanup val preparedStatement: PreparedStatement?
        @Cleanup val resultSet: ResultSet?

        try {
            Class.forName(JDBC_DRIVER)

            connection = DriverManager.getConnection(props.getProperty("DB_URL1") + props.getProperty("PASSWORD") + props.getProperty("DB_URL2"))
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
