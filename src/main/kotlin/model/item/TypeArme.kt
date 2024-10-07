package model.item

/**
 * La classe TypeArme représente un type d'arme utilisé dans le jeu.
 *
 * @property nom Le nom du type d'arme.
 * @property nombreDes Le nombre de dés utilisés pour les attaques avec cette arme.
 * @property valeurDeMax La valeur maximale du dé pour les attaques avec cette arme.
 * @property multiplicateurCritique Le multiplicateur de dégâts en cas de coup critique.
 * @property activationCritique Le seuil d'activation pour les coups critiques.
 */
class TypeArme(
    val nom: String,
    val nombreDes: Int,
    val valeurDeMax: Int,
    val multiplicateurCritique: Int,
    val activationCritique: Int
) {

}