package model.personnage

import model.jeu.Sort

class Mage(
    nom: String,
    pointDeVie: Int,
    pointDeVieMax: Int,
    attaque: Int,
    defense: Int,
    endurance: Int,
    vitesse: Int,
    val grimoire: MutableList<Sort> = mutableListOf()
) : Personnage(nom, pointDeVie, pointDeVieMax, attaque, defense, endurance, vitesse) {

    // Méthode pour afficher les sorts du grimoire
    fun afficherGrimoire() {
        println("Grimoire de $nom:")
        if (grimoire.isEmpty()) {
            println("Le grimoire est vide.")
        } else {
            for ((index, sort) in grimoire.withIndex()) {
                println("$index. ${sort.nom}")
            }
        }
    }

    // Méthode pour choisir et lancer un sort du grimoire
    fun choisirEtLancerSort(cible: Personnage) {
        if (grimoire.isEmpty()) {
            println("Le grimoire est vide. Vous ne pouvez pas lancer de sort.")
            return
        }

        // Afficher les sorts du grimoire
        afficherGrimoire()

        // Demander au joueur de choisir un sort à lancer
        print("Choisissez un sort à lancer (entrez le numéro) : ")
        val choixSort = readLine()?.toIntOrNull()

        if (choixSort != null && choixSort >= 0 && choixSort < grimoire.size) {
            val sortChoisi = grimoire[choixSort]
            var choix: Int
            do {
                println("Choisir la cible :")
                println("0=> ${this.nom}")
                println("1=>${cible.nom}")
                choix = readln().toInt()
            } while (choix != 0 && choix != 1)
            println("\u001B[34m")
            if (choix == 0) {
                // Appel de la fonction effet du sort sur le joueur
                sortChoisi.effet(this, this)
                println("$nom lance le sort ${sortChoisi.nom} sur ${this.nom}.")
            } else {
                // Appel de la fonction effet du sort sur la cible
                sortChoisi.effet(this, cible)
                println("$nom lance le sort ${sortChoisi.nom} sur ${cible.nom}.")
            }

        } else {
            println("Choix invalide.")
        }
    }


}