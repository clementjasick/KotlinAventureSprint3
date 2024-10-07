package model.jeu

import model.personnage.Personnage

/**
 * La classe Sort représente un sort utilisé dans le jeu.
 *
 * @property nom Le nom du sort.
 * @property effet La fonction d'effet du sort qui prend deux personnages en tant que paramètres (le premier le mage(caster), le deuxième la cible).
 */
class Sort(val nom: String, val effet: (Personnage, Personnage) -> Unit)