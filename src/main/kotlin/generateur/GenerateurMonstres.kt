package generateur

import armes
import armures
import bombes
import model.item.Arme
import model.item.Item
import model.personnage.Monstre
import potions
import qualites
import qualitesFromCSV
import typeArmes
import java.nio.file.Files
import java.nio.file.Paths

class GenerateurMonstres(val cheminFichier: String) {
    fun generer(): MutableMap<String, Monstre> {
        val mapObjets = mutableMapOf<String, Monstre>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val stringInventaire = ligneObjet[9].split(",")
            val inventaire = mutableListOf<Item>()
            for (stringItem in stringInventaire) {
                if (stringItem in armes) inventaire.add(armes[stringItem]!!)
                if (stringItem in armures) inventaire.add(armures[stringItem]!!)
                if (stringItem in potions) inventaire.add(potions[stringItem]!!)
                if (stringItem in bombes) inventaire.add(bombes[stringItem]!!)
            }
            val objet = Monstre(nom = ligneObjet[0],
                pdv = ligneObjet[1].toInt(),
                pdvMax = ligneObjet[2].toInt(),
                attaque = ligneObjet[3].toInt(),
                defense = ligneObjet[4].toInt(),
                endurance = ligneObjet[5].toInt(),
                vitesse = ligneObjet[6].toInt(),
                armeEquipee = if (ligneObjet[7] in armes) armes[ligneObjet[7]]!! else null,
                armureEquipee = if (ligneObjet[8] in armures) armures[ligneObjet[8]]!! else null,
                inventaire = inventaire)
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}