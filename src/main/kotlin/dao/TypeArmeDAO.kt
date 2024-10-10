package dao


import coBDD
import jdbc.BDD
import model.item.Qualite
import model.item.TypeArme
import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Statement

/**
 * Cette classe représente un repository pour les objets TypeArme, permettant d'effectuer des opérations de
 * recherche et de sauvegarde dans la base de données.
 *
 * @param bdd L'objet de base de données à utiliser pour les opérations de base de données.
 */
class TypeArmeDAO(val bdd: BDD=coBDD ) {

    /**
     * Recherche et retourne toutes les qualités de la base de données.
     *
     * @return Une liste de toutes les qualités trouvées.
     */
    fun findAll(): MutableMap<String, TypeArme> {
        val result = mutableMapOf<String, TypeArme>()

        val sql = "SELECT * FROM TypeArme"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getInt("nombreDes")
                val valeurDeMax= resultatRequete.getInt("valeurDeMax")
                val multiplicateurCritique= resultatRequete.getInt("multiplicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                var nouveauType= TypeArme(nom,nombreDes,valeurDeMax,multiplicateurCritique,activationCritique)
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
    fun findByNom(nomRechecher:String): MutableMap<String, TypeArme> {
        val result = mutableMapOf<String, TypeArme>()

        val sql = "SELECT * FROM TypeArme WHERE nom=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, nomRechecher)
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getInt("nombreDes")
                val valeurDeMax= resultatRequete.getInt("valeurDeMax")
                val multiplicateurCritique= resultatRequete.getInt("multiplicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                var nouveauType= TypeArme(nom,nombreDes,valeurDeMax,multiplicateurCritique,activationCritique)
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
    fun findById(id:Int): TypeArme? {
        var result :TypeArme?=null
        val sql = "SELECT * FROM TypeArme WHERE id=?"
        val requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql)
        requetePreparer?.setString(1, id.toString())
        val resultatRequete = this.bdd.executePreparedStatement(requetePreparer)
        if (resultatRequete != null) {
            while (resultatRequete.next()) {
                val  id =resultatRequete.getInt("id")
                val nom=resultatRequete.getString("nom")
                val nombreDes= resultatRequete.getInt("nombreDes")
                val valeurDeMax= resultatRequete.getInt("valeurDeMax")
                val multiplicateurCritique= resultatRequete.getInt("multiplicateurCritique")
                val activationCritique= resultatRequete.getInt("activationCritique")
                result= TypeArme(nom,nombreDes,valeurDeMax,multiplicateurCritique,activationCritique)
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
     * @param uneTypeArme L'objet TypeArme à sauvegarder.
     * @return L'objet TypeArme sauvegardé, y compris son ID généré, ou null en cas d'échec.
     */
    fun save(uneTypeArme: TypeArme): TypeArme? {

        val requetePreparer:PreparedStatement

        if (uneTypeArme.id == null) {
            val sql =
                "Insert Into TypeArme (nom,nombreDes,multiplicateurCritique,activationCritique,valeurDeMax) values (?,?,?,?,?)"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
            requetePreparer?.setString(1, uneTypeArme.nom)
            requetePreparer?.setInt(2, uneTypeArme.nombreDes)
            requetePreparer?.setInt(3, uneTypeArme.multiplicateurCritique)
            requetePreparer?.setInt(4, uneTypeArme.activationCritique)
            requetePreparer?.setInt(5, uneTypeArme.valeurDeMax)
        } else {
            var sql = ""
            sql =
                "Update  TypeArme set nom=?,bonusRarete=?,couleur=? where id=?"
            requetePreparer = this.bdd.connectionBDD!!.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)

            requetePreparer?.setString(1, uneTypeArme.nom)
            requetePreparer?.setInt(2, uneTypeArme.nombreDes)
            requetePreparer?.setInt(3, uneTypeArme.multiplicateurCritique)
            requetePreparer?.setInt(4, uneTypeArme.activationCritique)
            requetePreparer?.setInt(5, uneTypeArme.valeurDeMax)
            requetePreparer?.setInt(4, uneTypeArme.id!!)
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
                uneTypeArme.id = id // Mettez à jour l'ID de l'objet TypeArme avec la valeur générée
                return uneTypeArme
            }
        }
        requetePreparer.close()

        return null
    }

    /**
     * Sauvegarde toutes les qualités dans la liste dans la base de données.
     *
     * @param lesTypeArmes La liste des objets TypeArme à sauvegarder.
     * @return Une liste des objets TypeArme sauvegardés, y compris leurs ID générés, ou null en cas d'échec.
     */
    fun saveAll(lesTypeArmes:Collection<TypeArme>):MutableMap<String,TypeArme>{
        var result= mutableMapOf<String,TypeArme>()
        for (uneTypeArme in lesTypeArmes){
            val TypeArmeSauvegarde=this.save(uneTypeArme)
            if (TypeArmeSauvegarde!=null){
                result.set(TypeArmeSauvegarde.nom.lowercase(),TypeArmeSauvegarde)
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
        val sql = "DELETE FROM TypeArme WHERE id = ?"
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