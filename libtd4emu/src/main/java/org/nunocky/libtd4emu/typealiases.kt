package org.nunocky.libtd4emu

typealias u8 = Byte
typealias u16 = UShort
typealias u32 = UInt
typealias u64 = ULong
typealias s8 = Byte
typealias s16 = Short
typealias s32 = Int
typealias s64 = Long

fun Int.toU8(): u8 = this.toByte()