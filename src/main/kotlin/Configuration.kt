package model.jeu

const val ANSI_RESET = "\u001B[0m"

// Pour plus d'explication, voir l'article Wikipédia :
// https://en.wikipedia.org/wiki/ANSI_escape_code


// Définition des couleurs pour l'affichage dans le terminal :
const val ANSI_BLACK = "\u001B[30m"
const val ANSI_RED = "\u001B[31m"
const val ANSI_GREEN = "\u001B[32m"
const val ANSI_YELLOW = "\u001B[33m"
const val ANSI_BLUE = "\u001B[34m"
const val ANSI_PURPLE = "\u001B[35m"
const val ANSI_CYAN = "\u001B[36m"
const val ANSI_WHITE = "\u001B[37m"


// Définition des styles :
const val ANSI_BOLD = "\u001B[1m"
const val ANSI_UNBOLD = "\u001B[2m"
const val ANSI_ITALIC = "\u001B[3m"
const val ANSI_UNDERLINE = "\u001B[4m"
const val ANSI_NOT_UNDERLINE = "\u001B[24m"
const val ANSI_SLOW_BLINK = "\u001B[5m"
const val ANSI_RAPID_BLINK = "\u001B[6m"
const val ANSI_NOT_BLINK = "\u001B[25m"