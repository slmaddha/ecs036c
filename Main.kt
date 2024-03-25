package edu.davis.cs.ecs36c.homework0

import java.io.File
import java.io.FileNotFoundException


/**
 * This function should take a filename (either the default file or the
 * one specified on the command line.)  It should create a new MutableSet,
 * open the file, and load each line into the set.
 *
 * @param filename may not exist, and in that case the function should
 * throw a FileNotFound exception.
 */

fun loadFile(filename: String): Set<String> {
    val set = mutableSetOf<String>()
    try {
        File(filename).forEachLine { line -> //Reads this file line by line using the specified charset and calls action for each line. Default charset is UTF-8.
            var word = ""
            for (char in line) {
                if (char.isLetter()) { //check to see if char is a letter
                    word = word + char // word += char
                } else {
                    if (word.isNotEmpty()) {
                        set.add(word) // check to see if the word is empty, if it is not, add it to set
                        word = ""
                    }
                }
            }

            if (word.isNotEmpty()) {
                set.add(word) //if there is still word left, add it to the mutable set
            }
        }
    } catch (e: FileNotFoundException) { // catch FileNotFoundException
        throw FileNotFoundException("File not found: $filename") //throw exception (exit code 55 is in main)
    }

    return set //return the mutable set for rest of functions use
}


/**
 * This function should check if a word is valid by checking the word,
 * the word in all lower case, and the word with all but the first character
 * converted in lower case with the first character unchanged.
 */
fun checkWord(word: String, dict: Set<String>) : Boolean {
    if (word.contains(Regex("\\d"))) { // use regex to see if there is a decimal in the string
        print("$word [sic]") // print [sic] after the string
        return false // return false for when it is in processInput
    }

    if (word in dict) { // check to see if the word is in dict
        return true // return true if it is in dict (bool)
    }

    val wordLower = word.lowercase() // convert the word to lowercase
    if (wordLower in dict) { //check to see if the lowercase word is in the dict
        return true // return true if it is in dict (bool)
    }

    val firstChar = word.first().toString() // get the first letter of the word
    val rest = word.drop(1).lowercase() // get the rest of the word in lowercase
    val returnWord = firstChar + rest // add the two together

    return returnWord in dict // return this in dict
}


/**
 * This function should take a set (returned from loadFile) and then
 * processes standard input one line at a time using readLine() until standard
 * input is closed
 *
 * Note: readLine() returns a String?: that is, a string or null, with null
 * when standard input is closed.  Under Unix or Windows in IntelliJ you can
 * close standard input in the console with Control-D, while on the mac it is
 * Command-D
 *
 * Once you have the line you should split it with a regular expression
 * into words and nonwords,
 */

fun processInput(dict: Set<String>) {

    val re = "\\p{Alpha}+".toRegex() // any “word” (defined as a sequence of alphabet characters)
    val punct = "."

    while (true) {
        val input = readLine()  // Return the line read or null if the input stream is redirected to a file and the end of file has been reached.
        if (input == null) {
            break
        }

        val words = re.findAll(input).map { it.value }.toList() //Returns a sequence of all occurrences of a regular expression within the input string, beginning at the specified startIndex.
        val split = re.split(input) //Splits the input CharSequence to a list of strings around matches of this regular expression.

        for (i in words.indices) {
            var wordInd = words[i]

        // get rid of any periods from the string when checking if it is in the dict
            if (wordInd.endsWith(".")) {
                wordInd = wordInd.substring(0, wordInd.length - 1) // remove punctuation from string
            }

            val checkinDict = checkWord(wordInd, dict) //call checkinDict, use true/false for next set of if statements
            if (checkinDict) {
                print(split[i])
                print(wordInd)
            } else {
                print(split[i])
                print("$wordInd [sic]") //add sic to words that are not found in dict

            }

        }

        println(split.last()) // The input item (be it a word or non-word) is passed to standard output unchanged using print, with println used as the last entry in any given line.
    }

}
/**
 * Your main function should accept an argument on the command line or
 * use the default filename if no argument is specified.  If the dictionary
 * fails to load with a FileNotFoundException it should use
 * kotlin.system.exitProcess with status code of 55
 */
fun main(args: Array<String>) {
    val filename: String // initialize the filename --> see if the user entered a file
    val defaultFile = "/usr/share/dict/words"
    if (args.isNotEmpty()) { // if the arguments are not empty, use the first arg entered by user for the rest of functions
        filename = args[0]
    } else {
        filename = defaultFile //use the defaultFile ("/usr/share/dict/words")
    }

    val dict: Set<String> // initialize dict before using
    try {
        dict = loadFile(filename) // try to loadFile using args entered, otherwise go to catch block
        processInput(dict) // call processInput using dict param
    } catch (e: FileNotFoundException) {
        kotlin.system.exitProcess(55)  // return status with 55 (as in directions)
    }

}


/*
Sources used:
Kotlin Documentation: https://kotlinlang.org/docs/home.html
Kotlin Exit Process: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.system/exit-process.html
What is Sic: https://en.wikipedia.org/wiki/Sic
Sets in Kotlin: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/
Kotlin forEachLine: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/java.io.-file/for-each-line.html
Kotlin readLine: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/read-line.html
findAll in Kotlin: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/find-all.html
Split in Kotlin: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.text/-regex/split.html
Kotlin - File Not Found Exception: File does exist though: https://stackoverflow.com/questions/59410866/kotlin-file-not-found-exception-file-does-exist-though
Reading console input in Kotlin: https://stackoverflow.com/questions/41283393/reading-console-input-in-kotlin
Indices in Kotlin: https://stackoverflow.com/questions/44303914/what-is-indices-meaning-in-kotlin
Indices: https://www.google.com/search?q=indices+in+kotlin&oq=indices+in+kotlin&gs_lcrp=EgZjaHJvbWUqBwgAEAAYgAQyBwgAEAAYgAQyCAgBEAAYFhgeMgYIAhBFGDzSAQgyMTIwajBqNKgCALACAA&sourceid=chrome&ie=UTF-8
 */