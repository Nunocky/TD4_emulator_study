package org.nunocky.libtd4emu

fun panic(message: String) {
    throw RuntimeException(message)
}
