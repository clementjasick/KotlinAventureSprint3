package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import model.item.Arme
import qualiteRepository
import typeArmeRepository
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement
import kotlin.collections.set

/**
 * Cette classe représente un repository pour les objets Arme, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class ArmeDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, Arme> {
        val result = mutableMapOf<String, Arme>()

        val sql = "SELECT * FROM Arme"
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
                val leType = typeArmeRepository.findById(type)
                var nouveauType= Arme(nom,description,leType!!,laQualite!!)
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
    fun findByNom(nomRechecher:String): MutableMap<String, Arme> {
        val result = mutableMapOf<String, Arme>()

        val sql = "SELECT * FROM Arme WHERE nom=?"
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
                val leType = typeArmeRepository.findById(type)
                var nouveauType= Arme(nom,description,leType!!,laQualite!!)
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
    fun findById(id:Int): Arme? {
        var result :Arme?=null
        val sql = "SELECT * FROM Arme WHERE id=?"
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
                val leType = typeArmeRepository.findById(type)
                result= Arme(nom,description,leType!!,laQualite!!)
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
     * @param uneArme L'objet Arme à sauvegarder.
     * @return L'objet Arme sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneArme: Arme): Arme? {

        val requetePreparer:PreparedStatement

        if (uneArme.id == null) {
            val sql =
                "Insert Into Arme (nom,description,fk_id_qualite,fk_id_type) values (?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneArme.nom)
            requetePreparer?.setString(2, uneArme.description)
            requetePreparer?.setInt(3, uneArme.qualite.id!!)
            requetePreparer?.setInt(4, uneArme.type.id!!)
        } else {
            var sql = ""
            sql =
                "Update  Arme set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneArme.nom)
            requetePreparer?.setString(2, uneArme.description)
            requetePreparer?.setInt(3, uneArme.qualite.id!!)
            requetePreparer?.setInt(4, uneArme.type.id!!)
            requetePreparer?.setInt(5, uneArme.id!!)
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
                uneArme.id = id // Mettez à jour l'ID de l'objet Arme avec la valeur générée
                return uneArme
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesArmes La liste des objets Arme à sauvegarder.
     * @return Une liste des objets Arme sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesArmes:Collection<Arme>):MutableMap<String,Arme>{
        var result= mutableMapOf<String,Arme>()
        for (uneArme in lesArmes){
            val ArmeSauvegarde=this.save(uneArme)
            if (ArmeSauvegarde!=null){
                result.set(ArmeSauvegarde.nom.lowercase(),ArmeSauvegarde)
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
        val sql = "DELETE FROM Arme WHERE id = ?"
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