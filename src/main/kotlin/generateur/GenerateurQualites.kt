package generateur
import model.item.Qualite
import java.nio.file.Paths
import java.nio.file.Files

/**
 * La classe GenerateurQualites permet de générer des objets de type Qualite à partir d'un fichier CSV.
 *
 * @param cheminFichier Le chemin vers le fichier CSV contenant les données des qualités.
 */
class GenerateurQualites(val cheminFichier: String) {
    /**
     * Génère et retourne un mapping des qualités à partir des données contenues dans le fichier CSV.
     *
     * @return Un mapping des qualités où la clé est le nom de la qualité en minuscules et la valeur est un objet Qualite.
     */
    fun generer(): MutableMap<String, Qualite> {
        val mapObjets = mutableMapOf<String, Qualite>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = Qualite(nom = ligneObjet[0], bonusRarete = ligneObjet[1].toInt(), couleur = ligneObjet[2])
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}

