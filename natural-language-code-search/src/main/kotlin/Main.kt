package org.example
import java.io.File

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: java -jar TextSearch.jar <folder_path>")
        return
    }

    val folderPath = args[0]
    val folder = File(folderPath)

    if (!folder.exists() || !folder.isDirectory) {
        println("Invalid folder path provided.")
        return
    }

    val fileIndex = buildIndex(folder)

    if (fileIndex.isEmpty()) {
        println("No text files found in the provided folder.")
        return
    }

    println("Indexing completed. Ready for queries.")

    while (true) {
        print("Enter query (q to quit): ")
        val query = readLine()?.trim()

        if (query.equals("q", ignoreCase = true)) {
            println("Exiting...")
            break
        }

        val results = searchFiles(query!!, fileIndex)
        if (results.isEmpty()) {
            println("No occurrences found for \"$query\".")
        } else {
            println("Occurrences found for \"$query\":")
            results.forEach { println(it) }
        }
    }
}

fun buildIndex(folder: File): Map<File, List<String>> {
    val index = mutableMapOf<File, List<String>>()

    folder.listFiles { file -> file.extension == "txt" }?.forEach { file ->
        index[file] = file.readLines()
    }

    return index
}

fun searchFiles(query: String, index: Map<File, List<String>>): List<String> {
    val results = mutableListOf<String>()

    index.forEach { (file, lines) ->
        lines.forEachIndexed { lineNumber, line ->
            if (line.contains(query, ignoreCase = true)) {
                results.add("${file.name}: Line ${lineNumber + 1}")
            }
        }
    }

    return results
}
