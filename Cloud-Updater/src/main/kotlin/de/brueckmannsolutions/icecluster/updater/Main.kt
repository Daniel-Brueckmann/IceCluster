package de.brueckmannsolutions.icecluster.updater

import de.brueckmannsolutions.icecluster.updater.update.UpdateManager

fun main(args: Array<String>) {
    val updateManager = UpdateManager()

    var update = true
    var debug = false

    if(args.isNotEmpty()) {
        args.forEach {
            if (it == "--no-update") {
                update = false;
            } else if (it == "--debug") {
                debug = true;
            }
        }
    }

    val logo = "  _____          _____ _           _                               __ \n" +
            " |_   _|        / ____| |         | |                             /_ |\n" +
            "   | |  ___ ___| |    | |_   _ ___| |_ ___ _ __   ______  __   __  | |\n" +
            "   | | / __/ _ \\ |    | | | | / __| __/ _ \\ '__| |______| \\ \\ / /  | |\n" +
            "  _| || (_|  __/ |____| | |_| \\__ \\ ||  __/ |              \\ V /   | |\n" +
            " |_____\\___\\___|\\_____|_|\\__,_|___/\\__\\___|_|               \\_/    |_|\n" +
            "                                                                      \n" +
            "                                                                      "

    println(logo)
    if(!debug) {
        println("IceCluster by HttxTom v"+updateManager.getCurrentVersion())
    } else println("IceCluster by HttxTom v-DEBUG")

    if(update) {
        println("Check for Updates...")

    } else println("Skipping check for update!")
}