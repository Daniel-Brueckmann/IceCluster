package de.brueckmannsolutions.icecluster.loader

import java.io.File
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.jar.JarFile

fun main(args: Array<String>) {
    val updaterPath = File("storage/modules/cloud-updater.jar").toPath()
    updaterPath.toFile().parentFile.mkdirs()

    // Copy JAR file
    ClassLoader.getSystemClassLoader().getResourceAsStream("cloud-updater.jar")?.let {
        Files.copy(
            it,
            updaterPath,
            StandardCopyOption.REPLACE_EXISTING
        )
    } ?: throw RuntimeException("Failed to copy JAR file")

    // Load JAR file
    val classLoader = URLClassLoader(arrayOf(updaterPath.toUri().toURL()))
    val jarFile = JarFile(updaterPath.toFile())

    // Iterate through JAR entries
    jarFile.entries().asSequence()
        .filter { !it.isDirectory && it.name.endsWith(".class") }
        .forEach { entry ->
            val className = entry.name.removeSuffix(".class").replace("/", ".")
            val loadedClass = classLoader.loadClass(className)
            try {
                val mainMethod = loadedClass.getDeclaredMethod("main", Array<String>::class.java)
                mainMethod.invoke(null, args)
                return
            } catch (_: NoSuchMethodException) {
            }
        }

    // Close resources
    jarFile.close()
    classLoader.close()
}