package generateur

import model.item.Qualite
import model.item.TypeArme
import java.nio.file.Files
import java.nio.file.Paths

class GenerateurTypeArmes(val cheminFichier: String) {
    fun generer(): MutableMap<String, TypeArme> {
        val mapObjets = mutableMapOf<String, TypeArme>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = TypeArme(   nom = ligneObjet[0],
                nombreDes = ligneObjet[1].toInt(),
                valeurDeMax = ligneObjet[2].toInt(),
                multiplicateurCritique = ligneObjet[3].toInt(),
                activationCritique = ligneObjet[4].toInt())
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}