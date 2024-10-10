import jdbc.BDD
import model.jeu.Jeu
import model.jeu.Sort
import model.jeu.TirageDes
import model.item.*
import model.personnage.Personnage
import dao.QualiteDAO
import dao.TypeArmeDAO
import dao.TypeArmureDAO
import generateur.*

//DEMO MISSION 1
val generateurQualites = GenerateurQualites("assets/qualites.csv")
val qualitesFromCSV = generateurQualites.generer()

val typeArmes = GenerateurTypeArmes("assets/typeArmes.csv").generer()
val armes = GenerateurArmes("assets/armes.csv").generer()

val typeArmures = GenerateurTypeArmures("assets/typeArmures.csv").generer()
val armures = GenerateurArmures("assets/armures.csv").generer()

val potions = GenerateurPotions("assets/potions.csv").generer()
val bombes = GenerateurBombes("assets/bombes.csv").generer()

val monstres = GenerateurMonstres("assets/monstres.csv").generer()


//DEMO MISSION 2 :
// TODO Retirer les commentaires des lignes 22 et 25
// TODO : A la ligne 13 renomé la variable qualites en qualitesFromCSV
//instanciation de la co à la BDD
val coBDD = BDD()
//instanciation d'un objet QualiteDAO
val qualiteRepository = QualiteDAO(coBDD)
val typeArmeRepository = TypeArmeDAO(coBDD)
val typeArmureRepository = TypeArmureDAO(coBDD)
//
//Sauvegarde des Qualites dans la BDD
val qualites = qualiteRepository.saveAll(qualitesFromCSV.values)


// instanciation des Sorts (pour le(s) mage(s))
val sortDeFeu = Sort("Boule de Feu") { caster, cible ->
    run {
        // Tire un nombre de dés qui depend de l'attaque du caster (mage)
        val tirageDes = TirageDes(caster.attaque / 3, 6)
        var degat = tirageDes.lance()
        degat = maxOf(1, degat - cible.calculeDefense())
        cible.pointDeVie -= degat
        println("Le sort de boule de feu inflige $degat à ${cible.nom}")
    }
}
val sortDeGuerison = Sort("Sort de Guérison") { caster, cible ->
    run {
        val tirageDes = TirageDes(1, 10)
        // on ajoute un bonus au soin qui corespond au bonus d'attaque/2
        val soin = tirageDes.lance() + caster.attaque / 2
        cible.pointDeVie += soin
        if (cible.pointDeVie > cible.pointDeVieMax) cible.pointDeVie = cible.pointDeVieMax
        println("Le sort de guérison soigne ${soin} PV à ${cible.nom}")
    }
}
val sortProjectileMagique = Sort("Sort de projectile magique") { caster, cible ->
    run {
        val tirageDes = TirageDes(1, 6)
        //On lance plusieurs fois projectile magique cela depend du score d'attaque
        repeat(caster.attaque / 2) {
            var degat = tirageDes.lance()
            degat = maxOf(1, degat - cible.calculeDefense())
            cible.pointDeVie -= degat
            println("Le projectile magique inflige $degat à ${cible.nom}")
        }
    }
}


val projectionAcide = Sort("Sort de projection acide", { caster, cible ->
    run {
        val tirageDes = TirageDes(1, 10)
        var degat = tirageDes.lance()
        degat = maxOf(1, degat - cible.calculeDefense())
        cible.pointDeVie -= degat
        println("Le jet d'acide inflige $degat à ${cible.nom}")

    }
})

//instanciation des types d'armes
//val typeEpeeLongue = TypeArme("Epee longue", 1, 8, 2, 20)
//val typeEpeeCourte = TypeArme("Epee courte", 1, 6, 2, 18)
//val typeDague = TypeArme("Epee courte", 1, 4, 3, 15)
//val typeLance = TypeArme("Lance", 1, 6, 3, 18)
//val typeMarteau = TypeArme("Marteau", 1, 8, 2, 20)

//instanciation des types d'armures
//val typeArmureCuir = typeArmures["armure en cuir"]  //TypeArmure("Armure en cuir", 1)

val sortInvocatinArme = Sort("Sort d'invocation d'arme magique") { caster, cible ->
    run {
        val tirageDes = TirageDes(1, 20)
        var resultat = tirageDes.lance()
        val qualite = when {
            resultat < 10 -> qualites["rare"]
            resultat < 15 -> qualites["epic"]
            resultat <= 20 -> qualites["legendaire"]
            else -> qualites["commun"]
        }
        val armeMagique = Arme("Epee magique", "Une arme magique", typeArmes["typeEpeeLongue"]!!, qualite!!)

        cible.inventaire.add(armeMagique)
        println("${armeMagique} est ajouté a l'inventaire de ${cible.nom}")
        cible.equipe(armeMagique)
    }
}

val sortInvocatinArmure = Sort("Sort d'invocation d'armure magique") { caster, cible ->
    run {
        val tirageDes = TirageDes(1, 20)
        var resultat = tirageDes.lance()
        val qualite = when {
            resultat < 10 -> qualites["rare"]
            resultat < 15 -> qualites["epic"]
            resultat <= 20 -> qualites["legendaire"]
            else -> qualites["commun"]
        }
        val armureMagique = Armure("Armure magique", "Une armure magique", typeArmures["armure en cuir"]!!, qualite!!)

        cible.inventaire.add(armureMagique)
        println("${armureMagique} est ajouté a l'inventaire de ${cible.nom}")
        cible.equipe(armureMagique)
    }
}


fun main() {
    //instanciation des armes des monstres
    //val epee = armes["épée courte"]
    //val epee = Arme("Épée Courte", "Une épée courte tranchante", typeEpeeCourte, qualites["commun"]!!)
    //val lance = armes["lance"]
    //val lance = Arme("Lance", "Une lance pointue", typeLance, qualites["rare"]!!)
    //val dague = armes["dague"]
    //val dague = Arme("Dague", "Une dague extrêmement pointue", typeDague, qualites["epic"]!!)
    //val marteau = armes["marteau"]
    //val marteau = Arme("Marteau", "un marteau legendaire pourfendeur de troll", typeMarteau, qualites["legendaire"]!!)

    // instanciation des potions et bombes des monstres
    //val potionDeSoin1 = Potion("Potion de Soin", "Restaure les points de vie", 20)
    //val potionDeSoin2 = Potion("Potion de Soin", "Restaure les points de vie", 20)
    //val potionDeSoin3 = Potion("Potion de Soin", "Restaure les points de vie", 20)
    //val potionDeSoin4 = Potion("Grande Potion de Soin", "Restaure les points de vie", 30)

    // Instanciation des Monstres
    /*
    val kobold =
        Personnage("Marvin le Kobold", 25, 25, 3, 4, 6, 12, armes["dague"], inventaire = mutableListOf(potionDeSoin2, armes["dague"]!!))
    val gobelin = Personnage(
        "Antoine le gobelin",
        pointDeVie = 20,
        pointDeVieMax = 20,
        attaque = 5,
        defense = 4,
        vitesse = 11,
        endurance = 6,
        armeEquipee = armes["épée courte"],
        inventaire = mutableListOf(potionDeSoin3, armes["épée courte"]!!)
    )
    val troll = Personnage(
        "Nassim le troll",
        pointDeVie = 100,
        100,
        6,
        3,
        12,
        3,
        armes["marteau"],
        inventaire = mutableListOf(potionDeSoin4, armes["marteau"]!!)
    )
*/

    //val jeu = Jeu(listOf(kobold, gobelin, troll))
    val jeu = Jeu(monstres = monstres.values.toList())

    jeu.lancerCombat()

}

//instanciation des qualités des objets
//val qualiteCommun = Qualite(nom="commun", bonusRarete = 0, couleur = "\u001B[32m")
//val qualiteRare = Qualite(nom="rare", bonusRarete = 1, couleur = "\u001B[34m")
//val qualiteEpic = Qualite(nom = "epic", bonusRarete =  2, couleur =  "\u001B[35m")
//val qualiteLegendaire = Qualite(nom = "legendaire", bonusRarete =  3, couleur =  "\u001B[33m")