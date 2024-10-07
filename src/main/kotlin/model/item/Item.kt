package model.item

import model.personnage.Personnage

/**
 * La classe Item est une classe abstraite représentant un objet générique dans le jeu.
 *
 * @property nom Le nom de l'objet.
 * @property description Une brève description de l'objet.
 */
open abstract class Item(val nom: String, val description: String) {

    /**
     * Méthode abstraite permettant d'utiliser l'objet sur une cible (personnage).
     *
     * @param cible Le personnage sur lequel l'objet est utilisé.
     */
    abstract fun utiliser(cible: Personnage)

    /**
     * Retourne une représentation textuelle de l'objet.
     *
     * @return Une chaîne de caractères contenant le nom et la description de l'objet.
     */
    override fun toString(): String {
        return "${nom} (nom='$nom', description='$description')"
    }


}