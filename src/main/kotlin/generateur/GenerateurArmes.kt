package generateur
import model.item.Arme
import model.item.Qualite
import model.item.TypeArme
import qualites
import typeArmes
import java.nio.file.Paths
import java.nio.file.Files

class GenerateurArmes(val cheminFichier: String) {

    fun generer(): MutableMap<String, Arme> {
        val mapObjets = mutableMapOf<String, Arme>()

        // Lecture du fichier CSV, le contenu du fichier est stocké dans une liste mutable :
        val cheminCSV = Paths.get(this.cheminFichier)
        val listeObjCSV = Files.readAllLines(cheminCSV)

        // Instance des objets :
        for (i in 1..listeObjCSV.lastIndex) {
            val ligneObjet = listeObjCSV[i].split(";")
            val cle = ligneObjet[0].lowercase()
            val objet = Arme(nom = ligneObjet[0], description = ligneObjet[1], type = typeArmes[ligneObjet[2]]!!, qualite = qualites[ligneObjet[3]]!! )
            mapObjets[cle] = objet
        }
        return mapObjets
    }
}



