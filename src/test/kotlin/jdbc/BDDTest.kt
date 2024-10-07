package jdbc

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class BDDTest {
    private lateinit var bdd: BDD
    @BeforeEach
    fun setUp() {
        bdd = BDD(url = "jdbc:mysql://localhost:3306/test_db_kotlinAventure?createDatabaseIfNotExist=true&useSSL=false")
    }


    @Test
    fun testGetConnection() {
        val connection = bdd.connectionBDD
        assertNotNull(connection)
        if (connection != null) {
            assertEquals(false, connection.isClosed)
        }
    }

    @Test
    fun testExecutePreparedStatement() {
        val connection = bdd.connectionBDD
        assertNotNull(connection)

        // Créez un PreparedStatement pour une requête de test
        val preparedStatement = connection?.prepareStatement("SELECT 1")

        val resultSet = bdd.executePreparedStatement(preparedStatement!!)
        assertNotNull(resultSet)

        // Assurez-vous que le ResultSet n'est pas vide
        assertEquals(true, resultSet?.next())
        assertEquals(1, resultSet?.getInt(1))
    }

    @Test
    fun testExecutePreparedStatementWithError() {
        val connection = bdd.connectionBDD
        assertNotNull(connection)

        // Créez un PreparedStatement pour une requête incorrecte (ce qui générera une erreur)
        val preparedStatement = connection?.prepareStatement("SELECT * FROM TableInexistante")

        val resultSet = bdd.executePreparedStatement(preparedStatement!!)
        assertNull(resultSet) // Assurez-vous que le ResultSet est null en cas d'erreur
    }
}