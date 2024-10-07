package model.personnage

class Voleur(
    nom: String,
    pointDeVie: Int,
    pointDeVieMax: Int,
    attaque: Int,
    defense: Int,
    endurance: Int,
    vitesse: Int
) : Personnage(nom, pointDeVie, pointDeVieMax, attaque, defense, endurance, vitesse) {
    /**
     * Prend un objet dans l'inventaire de la cible
     */
    fun volerItem(cible: Personnage) {
        if (cible.inventaire.isNotEmpty()) {
            val objetVole = cible.inventaire.random() // Prend un objet au hasard de l'inventaire de l'ennemi
            if (objetVole == cible.armeEquipee) cible.armeEquipee =
                null //Si on vole l'item correspond a l'arme de la cible alors on lui retire son arme
            else if (objetVole == cible.armureEquipee) cible.armureEquipee = null
            inventaire.add(objetVole)
            cible.inventaire.remove(objetVole)
            println("$nom vole ${objetVole.nom} de ${cible.nom}.")
        } else {
            println("$cible.nom n'a pas d'objets à voler.")
        }
    }
}