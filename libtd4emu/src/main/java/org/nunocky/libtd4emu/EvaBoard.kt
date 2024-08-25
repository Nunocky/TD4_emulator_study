package org.nunocky.libtd4emu

class EvaBoard {

    // テキストに記載されたタイマーのプログラム
    // 0: IN B
    // 1: OUT B
    // 2: MOV A, 13
    // 3: ADD A, 1
    // 4: JNC 3
    // 5: ADD B, 1
    // 6: JNC 1
    // 7: OUT 0
    // 8: OUT 15
    // 9: JMP 7
    private val program: ByteArray = byteArrayOf(
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

    val cpu = TD4()
    val rom = Rom(program)
    var sw: u8 = 0 // 4ビット入力
    var led: u8 = 0 // 4ビット出力
        private set

    fun reset() {
        cpu.reset()
    }

    fun step() {
        rom.address = cpu.address

        cpu.sw = sw
        cpu.data = rom.data

        cpu.step()

        led = cpu.led
    }
}
