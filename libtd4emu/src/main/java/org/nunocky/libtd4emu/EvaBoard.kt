package org.nunocky.libtd4emu

class EvaBoard(
    private val program: ByteArray
) {
    val cpu = TD4()
    val rom = Rom(program)
    var sw: u8 = 0 // 4ビット入力
    var led: u8 = 0 // 4ビット出力
        private set

    fun reset() {
        cpu.reset()
        led = 0
    }

    fun step() {
        rom.address = cpu.address

        cpu.sw = sw
        cpu.data = rom.data

        cpu.step()
        led = cpu.led
    }
}
