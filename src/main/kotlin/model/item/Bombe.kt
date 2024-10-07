package model.item

import model.jeu.TirageDes
import model.personnage.Personnage

/**
 * La classe Bombe représente un type d'objet explosif dans le jeu, qui peut être utilisé pour infliger des dégâts à un personnage.
 *
 * @param nom Le nom de la bombe.
 * @param description Une brève description de la bombe.
 * @param nombreDeDes Le nombre de dés à lancer pour déterminer les dégâts.
 * @param maxDe La valeur maximale d'un dé lancé pour calculer les dégâts.
 */
class Bombe(nom: String, description: String, val nombreDeDes: Int, val maxDe: Int) : Item(nom, description) {
    /**
     * Utilise la bombe pour infliger des dégâts à un personnage en lançant un certain nombre de dés.
     * Les dégâts infligés sont calculés en soustrayant la défense de la cible.
     *
     * @param cible Le personnage sur lequel la bombe est utilisée pour infliger des dégâts.
     */
    override fun utiliser(cible: Personnage) {
        val deDegat = TirageDes(nombreDeDes, maxDe)
        var degat = deDegat.lance()
        degat = maxOf(1, degat - cible.calculeDefense())
        cible.pointDeVie -= degat
        println("La/Le ${this.nom} inflige $degat points de dégâts")
    }
}