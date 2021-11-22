package com.stringconcat.dev.course.app.component

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.Writer
import org.apache.commons.net.telnet.TelnetClient

class TestTelnetClient : TelnetClient() {

    private val prompt = "\r\n>"

    lateinit var reader: BufferedReader
        private set
    lateinit var writer: Writer
        private set

    override fun connect(hostname: String?, port: Int) {
        super.connect(hostname, port)
        reader = BufferedReader(InputStreamReader(inputStream))
        writer = OutputStreamWriter(outputStream)
        readMessage() // приветствие
    }

    fun readMessage(): String {
        var buffer = ""
        var cValue = inputStream.read()
        while (!buffer.endsWith(prompt) && cValue != -1) {
            val c = Char(cValue)
            buffer += c
            cValue = inputStream.read()
        }
        return buffer.removeSuffix(prompt)
    }

    fun writeCommand(command: String) {
        writer.write("$command\n")
        writer.flush()
    }
}