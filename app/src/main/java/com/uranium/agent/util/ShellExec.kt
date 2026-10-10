package com.uranium.agent.util

object ShellExec {
    fun run(cmd: String): String {
        val p = Runtime.getRuntime().exec(cmd)
        return p.inputStream.bufferedReader().readText()
    }
}
