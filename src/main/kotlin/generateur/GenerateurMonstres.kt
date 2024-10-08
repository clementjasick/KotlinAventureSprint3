package generateur

import model.item.Qualite
import model.personnage.Personnage
import java.nio.file.Files
import java.nio.file.Paths
import armes
import armures
import bombes
import model.item.Item
import potion

class GenerateurMonstres(val cheminFichier: String) {

    fun generer(): MutableMap<String, Personnage> {
        val mapObjets = mutableMapOf<String, Personnage>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val ligneInventaire = ligneObjet[9].split(",").toMutableList<String>()
            val Inventaire = mutableListOf<Item>()
            for (stringItem in ligneInventaire) {
                if (stringItem in potion) Inventaire.add(potion[stringItem]!!)
                if (stringItem in armes) Inventaire.add(armes[stringItem]!!)
                if (stringItem in armures) Inventaire.add((armures[stringItem]!!))
                if (stringItem in bombes) Inventaire.add((bombes[stringItem]!!))    
            }
            val cle = ligneObjet[0].lowercase()
            val objet = Personnage(nom = ligneObjet[0], pointDeVie = ligneObjet[1].toInt(),
                        pointDeVieMax = ligneObjet[2].toInt(),
                        attaque = ligneObjet[3].toInt(), defense = ligneObjet[4].toInt(),
                        endurance = ligneObjet[5].toInt(), vitesse = ligneObjet[6].toInt(),
                        armeEquipee = armes[ligneObjet[7]], armureEquipee = armures[ligneObjet[8]],
                        inventaire = Inventaire)
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}

