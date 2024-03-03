package de.brueckmannsolutions.icecluster.updater.update

import java.io.File
import java.net.URI
import kotlin.io.path.absolutePathString
import kotlin.io.path.toPath

class UpdateManager {

    private val latestVersionURL = "https://raw.githubusercontent.com/Daniel-Brueckmann/IceCluster/master/VERSION"
    private val latestVersionBinary =
        "https://github.com/Daniel-Brueckmann/IceCluster/releases/download/kotlin/IceCluster.jar"

    fun getCurrentVersion(): String =
        this.javaClass.`package`.implementationVersion

    fun getLatestVersion(): String =
        URI(latestVersionURL).toURL().openConnection().getInputStream().bufferedReader().use { it.readText() }
            .replace("\n", "")

    fun updateAvailable(): Boolean {
        val formattedCurrentVersion = getCurrentVersion()
            .replace("-SNAPSHOT", "")
            .replace(".", "")
            .replace("\n", "")
            .replace("v", "")

        val formattedLastVersion = getLatestVersion()
            .replace("-SNAPSHOT", "")
            .replace(".", "")
            .replace("\n", "")
            .replace("v", "")

        return formattedCurrentVersion.toInt() < formattedLastVersion.toInt()
    }

    fun updateCluster() {
        val tempFolder = File("temp")
        tempFolder.mkdirs()

        println("Download new version --> IceCluster-" + this.getLatestVersion() + ".jar")
        val newVersion = this.downloadFile(this.latestVersionBinary, "temp/IceCluster-" + this.getLatestVersion() + ".jar")

        println("Installing new version...")
        val newPath = this.getCurrentExecutionPath() + "/IceCluster-" + this.getLatestVersion() + ".jar"
        val currentFile = File(this.getCurrentExecutionPath() + "/IceCluster-" + this.getCurrentVersion() + ".jar")
        currentFile.writeBytes(newVersion.readBytes())
        currentFile.renameTo(File(newPath))

        println("Update was successfully, restarting in 3 seconds....")
        Thread.sleep(3000)
        RestartService().reloadClassloader(newPath)
    }

    private fun downloadFile(url: String, path: String): File {
        val file = File(path)
        file.createNewFile()

        URI(url).toURL().openConnection().getInputStream().use { file.writeBytes(it.readAllBytes()) }

        return file
    }

    private fun getCurrentExecutionPath(): String =
        UpdateManager::class.java.protectionDomain.codeSource.location.toURI().toPath().absolutePathString()
            .substringBeforeLast('/').substringBeforeLast('/').substringBeforeLast('/')
}