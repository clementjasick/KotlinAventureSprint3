package generateur

import model.item.Arme
import model.item.Potion
import qualites
import qualitesFromCSV
import typeArmes
import java.nio.file.Files
import java.nio.file.Paths

class GenerateurPotions(val cheminFichier: String) {
    fun generer(): MutableMap<String, Potion> {
        val mapObjets = mutableMapOf<String, Potion>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = Potion( nom = ligneObjet[0],
                description = ligneObjet[1],
                soin = ligneObjet[2].toInt()
            )
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}