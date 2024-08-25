package org.nunocky.libtd4emu

import org.junit.Test

class EvaBoardTest {
    @Test
    fun runTimer() {
        val board = EvaBoard()
        board.reset()
        board.sw = 0x07

        print(board.cpu)
        repeat(300) { step ->
            println("-- step ${step} ---------------------------------------------")
            board.step()
            print(board.cpu)
            println("led : ${board.led.toUInt().toString(2).padStart(4, '0')}")
        }
    }
}