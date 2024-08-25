package org.nunocky.libtd4emu

class EvaBoard {
    val cpu = TD4()
    val rom = Rom()
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
