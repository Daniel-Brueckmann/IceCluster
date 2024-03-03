package de.brueckmannsolutions.icecluster.updater.update

import java.io.File
import java.net.URLClassLoader
import java.util.jar.JarFile

class RestartService {

    /*
            TODO

             reloadClassloader() is starting on jar not new :(
     */

    fun reloadClassloader(path: String) {
        val classLoader = URLClassLoader(arrayOf(File(path).toURI().toURL()))
        val jarFile = JarFile(File(path))

        // Iterate through JAR entries
        jarFile.entries().asSequence()
            .filter { !it.isDirectory && it.name.endsWith(".class") }
            .forEach { entry ->
                val className = entry.name.removeSuffix(".class").replace("/", ".")
                val loadedClass = classLoader.loadClass(className)
                try {
                    val mainMethod = loadedClass.getDeclaredMethod("main", Array<String>::class.java)
                    val args = arrayOf("--updated")
                    mainMethod.invoke(null, args)
                    return
                } catch (_: NoSuchMethodException) {
                }
            }

        // Close resources
        jarFile.close()
        classLoader.close()
    }

}