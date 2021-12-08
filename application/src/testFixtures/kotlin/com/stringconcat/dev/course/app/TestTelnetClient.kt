package com.stringconcat.dev.course.app

import io.kotest.matchers.shouldBe
import java.io.OutputStreamWriter
import java.io.Writer
import org.apache.commons.net.telnet.TelnetClient

open class TestTelnetClient(hostname: String, port: Int) : TelnetClient() {

    private val writer: Writer

    companion object {
        const val PROMPT = "\r\n>"
        const val OK_RESPONSE = "\r\nOK"
    }

    init {
        super.connect(hostname, port)
        writer = OutputStreamWriter(outputStream)
        readMessage() // приветствие
    }

    fun lastMessageShouldBeOk() {
        readMessage() shouldBe OK_RESPONSE
    }

    fun readMessage(): String {
        var buffer = ""
        var cValue = inputStream.read()
        while (!buffer.endsWith(PROMPT) && cValue != -1) {
            val c = Char(cValue)
            buffer += c
            cValue = inputStream.read()
        }
        return buffer.removeSuffix(PROMPT)
    }

    fun writeCommand(command: String) {
        writer.write("$command\n")
        writer.flush()
    }
}