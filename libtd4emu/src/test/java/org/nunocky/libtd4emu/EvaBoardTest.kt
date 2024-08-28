package org.nunocky.libtd4emu

import org.junit.Test

class EvaBoardTest {
    @Test
    fun runTimer() {
        val program: ByteArray = byteArrayOf(
            0x60, // 0: IN B
            0x90.toU8(), // 1: OUT B
            0x3d, // 2: MOV A, 13
            0x01, // 3: ADD A, 1
            0xe3.toU8(), // 4: JNC 3
            0x51, // 5: ADD B, 1
            0xe1.toU8(), // 6: JNC 1
            0xB0.toU8(), // 7: OUT 0
            0xbf.toU8(), // 8: OUT 15
            0xf7.toU8(), // 9: JMP 7
        )

        val board = EvaBoard(program)
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