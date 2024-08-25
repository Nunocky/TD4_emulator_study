package org.nunocky.libtd4emu

val OP_MOV_A_B: u8 = 0b0001.toU8()
val OP_MOV_B_A: u8 = 0b0100.toU8()
val OP_MOV_A_IMM: u8 = 0b0011.toU8()
val OP_MOV_B_IMM: u8 = 0b0111.toU8()
val OP_IN_A: u8 = 0b0010.toU8()
val OP_IN_B: u8 = 0b0110.toU8()
val OP_OUT_B: u8 = 0b1001.toU8()
val OP_OUT_IMM: u8 = 0b1011.toU8()
val OP_ADD_A_IMM: u8 = 0b0000.toU8()
val OP_ADD_B_IMM: u8 = 0b0101.toU8()
val OP_JMP_IMM: u8 = 0b1111.toU8()
val OP_JNC_IMM: u8 = 0b1110.toU8()
