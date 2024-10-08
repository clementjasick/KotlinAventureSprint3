package generateur

import model.item.TypeArmure
import java.nio.file.Files
import java.nio.file.Paths

class GenerateurTypeArmures(val cheminFichier: String) {
    fun generer(): MutableMap<String, TypeArmure> {
        val mapObjets = mutableMapOf<String, TypeArmure>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = TypeArmure(nom = ligneObjet[0], bonusType = ligneObjet[1].toInt())
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}