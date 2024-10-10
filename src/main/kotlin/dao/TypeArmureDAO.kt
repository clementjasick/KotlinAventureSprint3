package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import model.item.TypeArmure
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import kotlin.collections.set

/**
 * Cette classe représente un repository pour les objets TypeArmure, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class TypeArmureDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String, TypeArmure>()

        val sql = "SELECT * FROM TypeArmure"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusType")
                var nouveauType= TypeArmure(nom,bonusType)
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
    fun findByNom(nomRechecher:String): MutableMap<String, TypeArmure> {
        val result = mutableMapOf<String, TypeArmure>()

        val sql = "SELECT * FROM TypeArmure WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusType")
                var nouveauType= TypeArmure(nom,bonusType)
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
    fun findById(id:Int): TypeArmure? {
        var result :TypeArmure?=null
        val sql = "SELECT * FROM TypeArmure WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val bonusType = resultatRequete.getInt("bonusType")
                result = TypeArmure(nom,bonusType)
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
     * @param uneTypeArmure L'objet TypeArmure à sauvegarder.
     * @return L'objet TypeArmure sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneTypeArmure: TypeArmure): TypeArmure? {

        val requetePreparer:PreparedStatement

        if (uneTypeArmure.id == null) {
            val sql =
                "Insert Into TypeArmure (nom,bonusType) values (?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneTypeArmure.nom)
            requetePreparer?.setInt(2, uneTypeArmure.bonusType)
        } else {
            var sql = ""
            sql =
                "Update  TypeArmure set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneTypeArmure.nom)
            requetePreparer?.setInt(2, uneTypeArmure.bonusType)
            requetePreparer?.setInt(3, uneTypeArmure.id!!)
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
                uneTypeArmure.id = id // Mettez à jour l'ID de l'objet TypeArmure avec la valeur générée
                return uneTypeArmure
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesTypeArmures La liste des objets TypeArmure à sauvegarder.
     * @return Une liste des objets TypeArmure sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesTypeArmures:Collection<TypeArmure>):MutableMap<String,TypeArmure>{
        var result= mutableMapOf<String,TypeArmure>()
        for (uneTypeArmure in lesTypeArmures){
            val TypeArmureSauvegarde=this.save(uneTypeArmure)
            if (TypeArmureSauvegarde!=null){
                result.set(TypeArmureSauvegarde.nom.lowercase(),TypeArmureSauvegarde)
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
        val sql = "DELETE FROM TypeArmure WHERE id = ?"
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