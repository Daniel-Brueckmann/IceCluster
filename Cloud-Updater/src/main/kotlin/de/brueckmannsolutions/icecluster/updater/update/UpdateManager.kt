package de.brueckmannsolutions.icecluster.updater.update

class UpdateManager {

    fun getCurrentVersion(): String =
        this.javaClass.`package`.implementationVersion

    fun getLatestVersion(): String = ""
}