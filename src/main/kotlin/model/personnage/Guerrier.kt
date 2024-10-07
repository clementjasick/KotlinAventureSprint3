package model.personnage

import model.item.Arme

class Guerrier(
    nom: String,
    pointDeVie: Int,
    pointDeVieMax: Int,
    attaque: Int,
    defense: Int,
    endurance: Int,
    vitesse: Int,
) : Personnage(nom, pointDeVie, pointDeVieMax, attaque, defense, endurance, vitesse) {
    var armeSecondaireEquipee: Arme? = null

    override fun attaquer(adversaire: Personnage) {
        var degats: Int = 1
        val bonusAtq = this.attaque / 2
        val cibleDef = adversaire.calculeDefense()
        if (armeEquipee != null) {
            super.attaquer(adversaire)

        }
        if (armeSecondaireEquipee != null) {
            val degatsArme2 =
                this.armeSecondaireEquipee!!.calculerDegats() - cibleDef + bonusAtq
            degats += maxOf(1, degatsArme2)
        } else {
            degats += maxOf(bonusAtq - cibleDef, 1)
        }

        // Appliquer les dégâts à l'adversaire
        adversaire.pointDeVie -= degats

        println("$nom attaque ${adversaire.nom} avec ${armeEquipee?.nom ?: "une attaque de base"} et ${armeSecondaireEquipee?.nom ?: "une attaque de base"}  inflige $degats points de dégâts.")
    }

    /**
     * Affiche les armes equipées du personnages
     */
    fun afficherArmes() {
        println("$nom a les armes suivantes:")
        if (armeEquipee != null) {
            println("1. ${armeEquipee!!.nom}")
        } else {
            println("1. Aucune arme équipée")
        }

        if (armeSecondaireEquipee != null) {
            println("2. ${armeSecondaireEquipee!!.nom}")
        } else {
            println("2. Aucune deuxième arme équipée")
        }
    }

    // Surcharge de la méthode equiperArme
    override fun equipe(arme: Arme) {
        this.afficherArmes()
        print("À quelle emplacement voulez-vous équiper l'arme (1 ou 2) : ")
        val choixEmplacement = readLine()?.toIntOrNull()

        when (choixEmplacement) {
            1 -> {
                armeEquipee = arme
                println("$nom équipe ${arme.nom} à l'emplacement 1.")
            }
            2 -> {
                armeSecondaireEquipee = arme
                println("$nom équipe ${arme.nom} à l'emplacement 2.")
            }
            else -> {
                println("Choix invalide. L'arme n'a pas été équipée.")
            }
        }
    }

    override fun toString(): String {
        return "Guerrier ${super.toString()} (armeSecondaireEquipee=$armeSecondaireEquipee)"
    }

}