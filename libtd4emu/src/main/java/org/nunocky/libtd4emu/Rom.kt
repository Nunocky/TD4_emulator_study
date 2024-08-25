package org.nunocky.libtd4emu

class Rom constructor(
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
    private val _data: ByteArray = byteArrayOf(
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
) {
    var data: u8 = 0
        private set

    var address: u8 = 0
        set(value) {
            field = value
            data = if (value.toInt() < _data.size) {
                _data[value.toInt()]
            } else {
                0
            }
        }
}