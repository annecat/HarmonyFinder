package fr.milleron.harmonyfinder.core

import android.util.Log

class SoundTouch {
    private val soundTouchHandle : Long = 0

    companion object {
        init {
            System.loadLibrary("soundtouch")
        }
    }

    fun onCreate() {

        val inputFile = "/path/to/input.wav"
        val outputFile = "/path/to/output.wav"

        // Create an instance of SoundTouch and get the handle
        val soundTouchHandle = newInstance()

        // Call the native method to process the file
        val result = processFile(soundTouchHandle, inputFile, outputFile)

        if (result != 0) {
            Log.e("SoundTouch", "Error processing file: ${getErrorString()}")
        }

        // Clean up the native SoundTouch instance
        deleteInstance(soundTouchHandle)
    }

    // Declare the external/native methods
    external private fun newInstance(): Long
    external private fun deleteInstance(handle: Long)
    external private fun processFile(handle: Long, inputFile: String, outputFile: String): Int
    external private fun getErrorString(): String
}