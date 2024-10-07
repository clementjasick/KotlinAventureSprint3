package dao

import jdbc.BDD
import model.item.Qualite
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.sql.SQLException

class QualiteDAOTest {

    private lateinit var bdd: BDD
    private lateinit var qualiteDAO: QualiteDAO

    @BeforeEach
    fun setUp() {
        // Initialisation de la classe BDD avec les informations de la base de données MySQL de test
        bdd = BDD(url = "jdbc:mysql://localhost:3306/test_db_kotlinAventure?createDatabaseIfNotExist=true&useSSL=false")
        qualiteDAO = QualiteDAO(bdd)

        // Créer la table de test
        createTestTable()
    }

    @AfterEach
    fun tearDown() {
        // Détruire la table de test
        dropTestTable()

        bdd.connectionBDD?.close()
    }

    private fun createTestTable() {
        try {
            val statement = bdd.connectionBDD?.createStatement()
            val createTableSQL = """
                CREATE TABLE Qualite (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(255),
                    bonusRarete INT,
                    couleur VARCHAR(255)
                )
            """.trimIndent()
            statement?.executeUpdate(createTableSQL)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    private fun dropTestTable() {
        try {
            val statement = bdd.connectionBDD?.createStatement()
            val dropTableSQL = "DROP TABLE IF EXISTS Qualite"
            statement?.executeUpdate(dropTableSQL)
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    @Test
    fun testSaveAndFindById() {
        val qualite = Qualite(null, "NouvelleQualite", 50, "ANSI_YELLOW")
        //Sauvegarde de la qualite dans la bdd
        val savedQualite = qualiteDAO.save(qualite)
        //Véfirications que la savedQualite n'est pas null et a une id (après la sauvegarde)
        assertNotNull(savedQualite)
        assertNotNull(savedQualite?.id)
        //Recuperation de la qualite par son id
        val retrievedQualite = qualiteDAO.findById(savedQualite?.id!!)
        //Vérifications
        assertNotNull(retrievedQualite)
        assertEquals(savedQualite.id, retrievedQualite?.id)
        assertEquals(savedQualite.nom, retrievedQualite?.nom)
        assertEquals(savedQualite.bonusRarete, retrievedQualite?.bonusRarete)
        assertEquals(savedQualite.couleur, retrievedQualite?.couleur)
    }

    @Test
    fun testSaveAll() {
        //Meme principe que le test d'avant
        val qualites = listOf(
            Qualite(null, "Qualite1", 10, "ANSI_RED"),
            Qualite(null, "Qualite2", 20, "ANSI_GREEN")
        )

        val savedQualites = qualiteDAO.saveAll(qualites)
        assertNotNull(savedQualites)

        val retrievedQualite1 = qualiteDAO.findByNom("Qualite1").values.first()
        val retrievedQualite2 = qualiteDAO.findByNom("Qualite2").values.first()

        assertNotNull(retrievedQualite1)
        assertNotNull(retrievedQualite2)

        assertEquals("Qualite1", retrievedQualite1.nom)
        assertEquals("Qualite2", retrievedQualite2.nom)
    }

    @Test
    fun testDeleteById() {
        val qualite = Qualite(null, "QualiteASupprimer", 30, "ANSI_BLUE")
        //Enregistrement de la qualite
        val savedQualite = qualiteDAO.save(qualite)
        assertNotNull(savedQualite)

        val idToDelete = savedQualite?.id
        // Suppresion de la qualite par son id
        val isDeleted = qualiteDAO.deleteById(idToDelete!!)
        // Vérificaton que deletedById retroune vrai
        assertTrue(isDeleted)

        val retrievedQualite = qualiteDAO.findById(idToDelete)
        // vérification que la méthode findByID ne trouve pas la qualite supprimé ( retourne null)
        assertNull(retrievedQualite)
    }
}