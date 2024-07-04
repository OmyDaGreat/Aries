package util

import lombok.Cleanup
import java.sql.*

object Keys {
    private const val JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"
    private const val DB_URL = "jdbc:mysql://2V2PSw5K9bbz49c.root:KeUs2CyBbV9Wxom3@gateway01.us-east-1.prod.aws.tidbcloud.com:4000/ParseButPro?sslMode=VERIFY_IDENTITY"

    @Throws(SQLException::class)
    @JvmStatic
    fun get(key: String): String? {
        @Cleanup val connection: Connection?
        @Cleanup val statement: Statement?
        @Cleanup val resultSet: ResultSet?

        try {
            // Register JDBC driver
            Class.forName(JDBC_DRIVER)

            // Open a connection
            connection = DriverManager.getConnection(DB_URL)

            // Create a statement
            statement = connection.createStatement()

            // Build the query
            val sql = "SELECT apikey FROM apikeys WHERE service = '$key'"

            // Execute the query
            resultSet = statement.executeQuery(sql)

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