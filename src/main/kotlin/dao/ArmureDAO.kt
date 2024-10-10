package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import model.item.Armure
import qualiteRepository
import typeArmureRepository
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import kotlin.collections.set

/**
 * Cette classe représente un repository pour les objets Armure, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class ArmureDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, Armure> {
        val result = mutableMapOf<String, Armure>()

        val sql = "SELECT * FROM Armure"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val qualite = resultatRequete.getInt("fk_id_qualite")
                val type = resultatRequete.getInt("fk_id_type")
                val laQualite = qualiteRepository.findById(qualite)
                val leType = typeArmureRepository.findById(type)
                var nouveauType= Armure(nom,description,leType!!,laQualite!!)
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
    fun findByNom(nomRechecher:String): MutableMap<String, Armure> {
        val result = mutableMapOf<String, Armure>()

        val sql = "SELECT * FROM Armure WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val qualite = resultatRequete.getInt("fk_id_qualite")
                val type = resultatRequete.getInt("fk_id_type")
                val laQualite = qualiteRepository.findById(qualite)
                val leType = typeArmureRepository.findById(type)
                var nouveauType= Armure(nom,description,leType!!,laQualite!!)
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
    fun findById(id:Int): Armure? {
        var result :Armure?=null
        val sql = "SELECT * FROM Armure WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val description = resultatRequete.getString("description")
                val qualite = resultatRequete.getInt("fk_id_qualite")
                val type = resultatRequete.getInt("fk_id_type")
                val laQualite = qualiteRepository.findById(qualite)
                val leType = typeArmureRepository.findById(type)
                result= Armure(nom,description,leType!!,laQualite!!)
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
     * @param uneArmure L'objet Armure à sauvegarder.
     * @return L'objet Armure sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneArmure: Armure): Armure? {

        val requetePreparer:PreparedStatement

        if (uneArmure.id == null) {
            val sql =
                "Insert Into Armure (nom,description,fk_id_qualite,fk_id_type) values (?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneArmure.nom)
            requetePreparer?.setString(2, uneArmure.description)
            requetePreparer?.setInt(3, uneArmure.qualite.id!!)
            requetePreparer?.setInt(4, uneArmure.typeArmure.id!!)
        } else {
            var sql = ""
            sql =
                "Update  Armure set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneArmure.nom)
            requetePreparer?.setString(2, uneArmure.description)
            requetePreparer?.setInt(3, uneArmure.qualite.id!!)
            requetePreparer?.setInt(4, uneArmure.typeArmure.id!!)
            requetePreparer?.setInt(5, uneArmure.id!!)
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
                uneArmure.id = id // Mettez à jour l'ID de l'objet Armure avec la valeur générée
                return uneArmure
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesArmures La liste des objets Armure à sauvegarder.
     * @return Une liste des objets Armure sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesArmures:Collection<Armure>):MutableMap<String,Armure>{
        var result= mutableMapOf<String,Armure>()
        for (uneArmure in lesArmures){
            val ArmureSauvegarde=this.save(uneArmure)
            if (ArmureSauvegarde!=null){
                result.set(ArmureSauvegarde.nom.lowercase(),ArmureSauvegarde)
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
        val sql = "DELETE FROM Armure WHERE id = ?"
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