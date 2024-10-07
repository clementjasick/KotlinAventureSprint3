package model.jeu


import exceptions.ActionImpossibleException
import exceptions.EquipementNotInInventoryException
import exceptions.NoPotionInInventoryException
import exceptions.WrongCharacterClassException
import model.personnage.Mage
import model.personnage.Personnage
import model.personnage.Voleur

/**
 * La classe Combat gère les combats entre le joueur et un monstre.
 *
 * @property joueur Le personnage du joueur.
 * @property monstre Le personnage du monstre.
 * @property nombreTours Le nombre de tours de combat.
 */
class Combat(
    val joueur: Personnage,
    val monstre: Personnage
) {
    var nombreTours: Int = 1

    /**
     * Simule un tour de combat du joueur en affichant les options disponibles.
     *
     * @throws ActionImpossibleException si une action invalide est choisie.
     */
    fun tourDeJoueur() {
        println("\u001B[34m")
        println("Tour de ${joueur.nom} (points de vie: ${joueur.pointDeVie})")
        println("1. Attaquer")
        println("2. Boire une potion")
        println("3. Passer")
        println("4. Afficher inventaire")
        println("5. Lancer un sort (Mage uniquement)") // Option pour le Mage
        println("6. Voler un objet (Voleur uniquement)") // Option pour le Voleur
        print("Choisissez une action (1, 2, 3, 4, 5, 6): ")
        val choix = readLine()?.toIntOrNull() ?: 0
        when (choix) {
            1 -> {
                joueur.attaquer(monstre)
            }
            2 -> {
                if (joueur.aUnePotion() == false) {
                    throw NoPotionInInventoryException("Action invalide : Pas de potion dans l'inventaire")
                } else {
                    joueur.boirePotion()
                }
            }
            3 -> {

                println("${joueur.nom} choisit de passer.")
            }
            4 -> {
                println("\u001b[0m")
                // Afficher l'inventaire
                joueur.afficherInventaire()
                // Demander au joueur de choisir un objet à utiliser
                print("Choisissez un objet à utiliser (entrez le numéro) : ")
                val choixObjet = readln().toInt()
                if (choixObjet >= 0 && choixObjet < joueur.inventaire.size) {
                    val objetChoisi = joueur.inventaire[choixObjet]
                    println("\u001B[34m")
                    objetChoisi.utiliser(joueur)
                } else {
                    throw EquipementNotInInventoryException("Action Invalide: Pas d'objet correspondent dans l'inventaire")
                }
            }
            5 -> {
                if (joueur is Mage) {
                    // Le joueur est un Mage, permettez-lui de choisir et de lancer un sort depuis son grimoire
                    joueur.choisirEtLancerSort(monstre)
                } else {

                    throw WrongCharacterClassException("Action invalide :${joueur.nom} n'est pas un Mage et ne peut pas lancer de sort.")
                }
            }
            6 -> {
                if (joueur is Voleur) {
                    // Le joueur est un Voleur, permettez-lui de voler un objet à la cible (monstre) actuelle
                    joueur.volerItem(monstre)
                } else {
                    throw WrongCharacterClassException("Action invalide : ${joueur.nom} n'est pas un Voleur et ne peut pas voler d'objet.")
                }
            }
            else -> {
                throw ActionImpossibleException("Action invalide. Choisissez 1 pour attaquer, 2 pour boire une potion, 3 pour passer, 4 pour afficher l'inventaire, 5 pour lancer un sort (Mage uniquement) ou 6 pour voler un objet (Voleur uniquement).")

            }
        }
        println("\u001b[0m")
    }

    /**
     * Simule un tour de combat du monstre en choisissant une action aléatoire.
     */
    fun tourDeMonstre() {
        println("---Tour de ${monstre.nom} (points de vie: ${monstre.pointDeVie}---)\u001B[31m")
        val nbAlea = (0..100).random()
        // Le monstre a une faible chance (par exemple, 10%) de boire une potion s'il est blessé
        if (monstre.pointDeVie < monstre.pointDeVieMax / 2 && nbAlea < 10 && monstre.aUnePotion()) {
            monstre.boirePotion()
        } else {
            if (nbAlea < 60) {
                monstre.attaquer(joueur)
            } else {
                println("${monstre.nom} choisit de passer.")
            }
        }
        println("\u001b[0m")
    }

    /**
     * Exécute un combat complet entre le joueur et le monstre, en alternant les tours.
     * Si le joueur est vaincu, le combat recommence.
     */
    fun executerCombat() {
        println("Début du combat : ${joueur.nom} vs ${monstre.nom}")
        var tourJoueur = true

        while (joueur.pointDeVie > 0 && monstre.pointDeVie > 0) {
            println("Tours de jeu : ${nombreTours}")
            var actionValide = true
            if (tourJoueur) {

                do {
                    try {
                        tourDeJoueur()
                        actionValide = true
                    } catch (e: ActionImpossibleException) {
                        println(e.message)
                        actionValide = false
                    }
                } while (!actionValide)
            } else {
                tourDeMonstre()
            }
            nombreTours++
            tourJoueur = !tourJoueur // Alternance des tours entre le joueur et le monstre
            println("${joueur.nom}: ${joueur.pointDeVie} points de vie | ${monstre.nom}: ${monstre.pointDeVie} points de vie")
            println("")
        }

        if (joueur.pointDeVie <= 0) {
            println("Game over ! ${joueur.nom} a été vaincu !")
            println("Le combat recommence")

            this.joueur.pointDeVie = this.joueur.pointDeVieMax
            this.monstre.pointDeVie = this.monstre.pointDeVieMax
            this.executerCombat()
        } else {
            println("BRAVO ! ${monstre.nom} a été vaincu !")
            this.joueur.loot(monstre)
        }
    }
}