package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

/**
 * Cette classe représente un repository pour les objets Qualite, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class QualiteDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String,Qualite> {
        val result = mutableMapOf<String,Qualite>()

        val sql = "SELECT * FROM Qualite"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonus= resultatRequete.getInt("bonusRarete")
                val couleur= resultatRequete.getString("couleur")
                result.set(nom.lowercase(),Qualite(id,nom,bonus,couleur))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne une qualité par nom (retourne la première correspondance).
     *
     * @param nomRechecher Le nom à rechercher.
     * @return La première qualité correspondant au nom donné, ou null si aucune n'est trouvée.
     */
    fun findByNom(nomRechecher:String): MutableMap<String,Qualite> {
        val result = mutableMapOf<String,Qualite>()

        val sql = "SELECT * FROM Qualite WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonus= resultatRequete.getInt("bonusRarete")
                val couleur= resultatRequete.getString("couleur")
                result.set(nom.lowercase(),Qualite(id,nom,bonus,couleur))
            }
        }
        requetePreparer.close()
        return result
    }

    /**
     * Recherche et retourne une qualité par nom (retourne la première correspondance).
     *
     * @param Int L'id à rechercher.
     * @return La première qualité correspondant au nom donné, ou null si aucune n'est trouvée.
     */
    fun findById(id:Int): Qualite? {
        var result :Qualite?=null
        val sql = "SELECT * FROM Qualite WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonus= resultatRequete.getInt("bonusRarete")
                val couleur= resultatRequete.getString("couleur")
                result=Qualite(id,nom,bonus,couleur)
                requetePreparer.close()
                return result
            }
        }
        requetePreparer.close()
        return result
    }
    /**
     * Sauvegarde une qualité dans la base de données.
     *
     * @param uneQualite L'objet Qualite à sauvegarder.
     * @return L'objet Qualite sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneQualite: Qualite): Qualite? {

        val requetePreparer:PreparedStatement

        if (uneQualite.id == null) {
            val sql =
                "Insert Into Qualite (nom,bonusRarete,couleur) values (?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneQualite.nom)
            requetePreparer?.setInt(2, uneQualite.bonusRarete)
            requetePreparer?.setString(3, uneQualite.couleur)
        } else {
            var sql = ""
            sql =
                "Update  Qualite set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneQualite.nom)
            requetePreparer?.setInt(2, uneQualite.bonusRarete)
            requetePreparer?.setString(3, uneQualite.couleur)
            requetePreparer?.setInt(4, uneQualite.id!!)
        }


        // Exécutez la requête d'insertion
        val nbLigneMaj = requetePreparer?.executeUpdate()
        // La méthode executeUpdate() retourne le nombre de lignes modifié par un insert, update ou delete sinon elle retourne 0 ou -1

        // Si l'insertion a réussi
        if (nbLigneMaj != null && nbLigneMaj > 0) {
            // Récupérez les clés générées (comme l'ID auto-incrémenté)
            val generatedKeys = requetePreparer.generatedKeys
            if (generatedKeys.next()) {
                val id = generatedKeys.getInt(1) // Supposons que l'ID est la première col
                uneQualite.id = id // Mettez à jour l'ID de l'objet Qualite avec la valeur générée
                return uneQualite
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesQualites La liste des objets Qualite à sauvegarder.
     * @return Une liste des objets Qualite sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesQualites:Collection<Qualite>):MutableMap<String,Qualite>{
    var result= mutableMapOf<String,Qualite>()
        for (uneQualite in lesQualites){
            val qualiteSauvegarde=this.save(uneQualite)
            if (qualiteSauvegarde!=null){
                result.set(qualiteSauvegarde.nom.lowercase(),qualiteSauvegarde)
            }
        }
        return result
    }

    /**
     * Supprime une qualité de la base de données en fonction de son ID.
     *
     * @param id L'ID de la qualité à supprimer.
     * @return `true` si la qualité a été supprimée avec succès, sinon `false`.
     */
    fun deleteById(id: Int): Boolean {
        val sql = "DELETE FROM Qualite WHERE id = ?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setInt(1, id)
        try {
            val nbLigneMaj = requetePreparer?.executeUpdate()
            requetePreparer.close()
            if(nbLigneMaj!=null && nbLigneMaj>0){
                return true
            }else{
                return false
            }
        } catch (erreur: SQLException) {
            println("Une erreur est survenue lors de la suppression de la qualité : ${erreur.message}")
            return false
        }
    }
}