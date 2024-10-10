package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import model.item.Potion
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

/**
 * Cette classe représente un repository pour les objets Potion, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class PotionDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, Potion> {
        val result = mutableMapOf<String, Potion>()

        val sql = "SELECT * FROM Potion"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description =resultatRequete.getString("description")
                val soin =resultatRequete.getInt("soin")
                var nouveauType= Potion(nom,description, soin)
                nouveauType.id=id
                result.set(nom.lowercase(), nouveauType)
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
    fun findByNom(nomRechecher:String): MutableMap<String, Potion> {
        val result = mutableMapOf<String, Potion>()

        val sql = "SELECT * FROM Potion WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description =resultatRequete.getString("description")
                val soin =resultatRequete.getInt("soin")
                var nouveauType= Potion(nom,description, soin)
                nouveauType.id=id
                result.set(nom.lowercase(), nouveauType)
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
    fun findById(id:Int): Potion? {
        var result :Potion?=null
        val sql = "SELECT * FROM Potion WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description =resultatRequete.getString("description")
                val soin =resultatRequete.getInt("soin")
                result= Potion(nom,description, soin)
                result.id=id
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
     * @param unePotion L'objet Potion à sauvegarder.
     * @return L'objet Potion sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(unePotion: Potion): Potion? {

        val requetePreparer:PreparedStatement

        if (unePotion.id == null) {
            val sql =
                "Insert Into Potion (nom,nombreDes,multiplicateurCritique,activationCritique,valeurDeMax) values (?,?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, unePotion.nom)
            requetePreparer?.setString(2, unePotion.description)
            requetePreparer?.setInt(3, unePotion.soin)
        } else {
            var sql = ""
            sql =
                "Update  Potion set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, unePotion.nom)
            requetePreparer?.setString(2, unePotion.description)
            requetePreparer?.setInt(3, unePotion.soin)
            requetePreparer?.setInt(4, unePotion.id!!)
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
                unePotion.id = id // Mettez à jour l'ID de l'objet Potion avec la valeur générée
                return unePotion
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesPotions La liste des objets Potion à sauvegarder.
     * @return Une liste des objets Potion sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesPotions:Collection<Potion>):MutableMap<String,Potion>{
        var result= mutableMapOf<String,Potion>()
        for (unePotion in lesPotions){
            val PotionSauvegarde=this.save(unePotion)
            if (PotionSauvegarde!=null){
                result.set(PotionSauvegarde.nom.lowercase(),PotionSauvegarde)
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
        val sql = "DELETE FROM Potion WHERE id = ?"
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
