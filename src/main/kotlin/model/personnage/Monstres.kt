package model.personnage

import model.item.Arme
import model.item.Armure
import model.item.Item

class Monstre(nom: String,
              pdv: Int,
              pdvMax: Int,
              attaque: Int,
              defense: Int,
              endurance: Int,
              vitesse: Int,
              armeEquipee: Arme? = null,
              armureEquipee: Armure? = null,
              inventaire: MutableList<Item>

): Personnage(nom = nom,
    pointDeVie = pdv,
    pointDeVieMax = pdvMax,
    attaque = attaque,
    defense = defense,
    endurance = endurance,
    vitesse = vitesse,
    armeEquipee = armeEquipee,
    armureEquipee = armureEquipee,
    inventaire = inventaire
) {
}