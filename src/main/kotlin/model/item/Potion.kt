package model.item

import model.personnage.Personnage

/**
 * La classe Potion représente un type spécifique d'objet dans le jeu qui peut être utilisé pour soigner un personnage.
 *
 * @param nom Le nom de la potion.
 * @param description Une brève description de la potion.
 * @param soin La quantité de points de vie que la potion peut restaurer.
 */
class Potion(
    nom: String,
    description: String,
    val soin: Int
) : Item(nom, description) {

    /**
     * Utilise la potion pour soigner un personnage en appelant la méthode boirePotion sur la cible.
     *
     * @param cible Le personnage sur lequel la potion est utilisée pour soigner.
     */
    override fun utiliser(cible: Personnage) {
        cible.boirePotion()
    }
}