package model.item

import model.jeu.TirageDes
import model.personnage.Personnage

/**
 * La classe Arme représente un type d'objet qui peut être équipé par un personnage pour infliger des dégâts.
 *
 * @param nom Le nom de l'arme.
 * @param description Une brève description de l'arme.
 * @param type Le type de l'arme qui détermine ses caractéristiques (nombre de dés, valeur de dé maximum, etc.).
 * @param qualite La qualité de l'arme qui influence les dégâts infligés.
 */
class Arme(
    nom: String,
    description: String,
    val type: TypeArme,
    val qualite: Qualite
) : Item(nom, description) {
    /**
     * Calcule les dégâts infligés par l'arme en fonction de son type et de sa qualité.
     * Les dégâts peuvent inclure des coups critiques et des bonus de qualité.
     *
     * @return Les dégâts infligés par l'arme.
     */
    fun calculerDegats(): Int {
        // Exemple : 1d6 +2 ( cad un nombre entre 1 et 6 plus le modificateur 2)
        //      On tire 1 dè de 6 (min: 1 max:6 )
        //      Si on tire 6 alors on multiplie par le multiplicateur de critique du type de l'arme ( par exemple 3)
        //      Sinon on garde le nombre du tirage tel quelle
        //      On ajoute le bonus de qualite au dégat ici 2

        // Instanciation d'un tirage de dés
        val deDegat = TirageDes(type.nombreDes, type.valeurDeMax)
        // on lance les dés
        var resultatLancer = deDegat.lance()
        val deCritique = TirageDes(1, 20).lance()
        if (deCritique >= type.activationCritique) {
            // Coup critique (si le nombre tiré correspond au maximum)
            println("Coup critique !")
            resultatLancer = resultatLancer * type.multiplicateurCritique
        }
        resultatLancer += this.qualite.bonusRarete
        return resultatLancer
    }

    /**
     * Équipe l'arme sur un personnage, permettant au personnage de l'utiliser pour attaquer.
     *
     * @param cible Le personnage sur lequel l'arme est équipée.
     */
    override fun utiliser(cible: Personnage) {
        cible.equipe(this)
    }

    /**
     * Retourne une chaîne de caractères représentant l'arme, y compris son type, sa qualité et ses caractéristiques.
     *
     * @return Une chaîne de caractères représentant l'arme.
     */
    override fun toString(): String {
        return "${qualite.getColorCode()} ${type.nom} ${qualite.nom}  Dégâts :${type.nombreDes}d${type.valeurDeMax} +${qualite.bonusRarete}  \u001B[0m"

    }
}