package org.nunocky.libtd4emu

class Rom(private val _data: ByteArray) {
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