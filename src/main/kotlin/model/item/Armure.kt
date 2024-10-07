package model.item

import model.personnage.Personnage

/**
 * La classe Armure représente un type d'objet qui peut être équipé par un personnage pour augmenter sa défense.
 *
 * @param nom Le nom de l'armure.
 * @param description Une brève description de l'armure.
 * @param typeArmure Le type d'armure qui détermine ses caractéristiques (bonus de type, etc.).
 * @param qualite La qualité de l'armure qui influence les bonus de défense.
 */
class Armure(nom: String, description: String, val typeArmure: TypeArmure, val qualite: Qualite) :
    Item(nom, description) {
    /**
     * Équipe l'armure sur un personnage, permettant au personnage d'augmenter sa défense.
     *
     * @param cible Le personnage sur lequel l'armure est équipée.
     */
    override fun utiliser(cible: Personnage) {
        cible.equipe(this)
    }

    /**
     * Retourne une chaîne de caractères représentant l'armure, y compris son type, sa qualité et ses caractéristiques.
     *
     * @return Une chaîne de caractères représentant l'armure.
     */
    override fun toString(): String {
        return "${qualite.getColorCode()} ${qualite.nom} $nom ${qualite.bonusRarete} (type=${typeArmure.nom}) \u001B[0m"
    }

}