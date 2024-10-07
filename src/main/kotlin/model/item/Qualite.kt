package model.item

import model.jeu.*

/**
 * La classe Qualite représente les caractéristiques de qualité d'un objet.
 *
 * @property id L'identifiant de la qualité.
 * @property nom Le nom de la qualité.
 * @property bonusRarete Le bonus de rareté associé à la qualité.
 * @property couleur La couleur associée à la qualité (peut être sous forme de code de couleur ANSI).
 */
class Qualite(var id: Int? = null, val nom: String, val bonusRarete: Int, val couleur: String) {
    /**
     * Récupère le code de couleur associé à la qualité.
     *
     * @return Le code de couleur ANSI correspondant à la qualité, ou null si la couleur n'est pas reconnue.
     */
    fun getColorCode(): String? {
        return when (this.couleur) {
            "ANSI_BLACK" -> ANSI_BLACK
            "ANSI_RED" -> ANSI_RED
            "ANSI_GREEN" -> ANSI_GREEN
            "ANSI_YELLOW" -> ANSI_YELLOW
            "ANSI_BLUE" -> ANSI_BLUE
            "ANSI_PURPLE" -> ANSI_PURPLE
            "ANSI_CYAN" -> ANSI_CYAN
            "ANSI_WHITE" -> ANSI_WHITE
            else -> null
        }
    }
}