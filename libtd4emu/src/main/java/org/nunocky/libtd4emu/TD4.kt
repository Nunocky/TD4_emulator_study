package org.nunocky.libtd4emu

import androidx.annotation.VisibleForTesting
import kotlin.experimental.and

private const val debug = true

private fun logd(msg: String) {
    if (debug) {
        println(msg)
    }
}

class TD4 {
    // 4ビットアドレスバス
    var address: u8 = 0
        private set

    // 8ビットデータバス
    var data: u8 = 0

    var sw: u8 = 0 // 4ビット入力

    var led: u8 = 0 // 4ビット出力
        private set

    var regA: u8 = 0 // 4bitレジスタ
        private set

    var regB: u8 = 0 // 4bitレジスタ
        private set

    var cf: Boolean = false // 1bitレジスタ (キャリーフラグ)
        private set

    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("address:0x${address.toString(16).padStart(4, '0')} ")
        sb.append("data:0x${data.toUInt().toString(16).padStart(4, '0').takeLast(4)} ")
        sb.append("sw:${sw.toString(2).padStart(4, '0')} ")
        sb.append("led:${led.toString(2).padStart(4, '0')} ")
        sb.append("regA:${regA.toString(16).padStart(4, '0')} ")
        sb.append("regB:${regB.toString(16).padStart(4, '0')} ")
        sb.append("cf:${if (cf) 1 else 0}")
        sb.append("\n")
        return sb.toString()
    }

    fun reset() {
        address = 0
        data = 0
        led = 0
        regA = 0
        regB = 0
        cf = false
    }

    /**
     * クロック立ち上がり時の処理
     */
    fun step() {
        // ROMから命令を読み出す
        // 現在の ip(address)対するROMのデータは dataにセットされている
        val opcode: u8 = (data.toInt() shr 4).and(0x0f).toU8() // dataの上位4ビット
        val imm: u8 = data.and(0x0f) // dataの下位 4ビット

        // opcodeによって処理を分岐
        when (opcode) {
            OP_MOV_A_B -> {
                logd("MOV A, B")
                regA = regB
                address = address.inc()
            }

            OP_MOV_B_A -> {
                logd("MOV B, A")
                regB = regA
                address = address.inc()
            }

            OP_MOV_A_IMM -> {
                logd("MOV A, ${imm}")
                regA = imm
                address = address.inc()
            }

            OP_MOV_B_IMM -> {
                logd("MOV B, ${imm}")
                regB = imm
                address = address.inc()
            }

            OP_IN_A -> {
                logd("IN A")
                regA = sw
                address = address.inc()
            }

            OP_IN_B -> {
                logd("IN B")
                regB = sw
                address = address.inc()
            }

            OP_OUT_B -> {
                logd("OUT B")
                led = regB
                address = address.inc()
            }

            OP_OUT_IMM -> {
                logd("OUT ${imm}")
                led = imm
                address = address.inc()
            }

            OP_ADD_A_IMM -> {
                logd("ADD A, ${imm}")
                // Aは4ビットレジスタなので 15を超えたら cf = trueとする
                (regA.toInt() + imm.toInt()).let { newValue ->
                    if (15 < newValue) {
                        cf = true
                        regA = (newValue and 0x0f).toU8()
                    } else {
                        cf = false
                        regA = (newValue and 0x0f).toU8()
                    }
                }
                address = address.inc()
            }

            OP_ADD_B_IMM -> {
                logd("ADD B, ${imm}")
                (regB.toInt() + imm.toInt()).let { newValue ->
                    if (15 < newValue) {
                        cf = true
                        regB = (newValue and 0x0f).toU8()
                    } else {
                        cf = false
                        regB = (newValue and 0x0f).toU8()
                    }
                }
                address = address.inc()
            }

            OP_JMP_IMM -> {
                logd("JMP ${imm}")
                address = imm
            }

            OP_JNC_IMM -> {
                logd("JNC ${imm}")
                if (!cf) {
                    address = imm
                } else {
                    address = address.inc()
                }
            }

            else -> panic("no such instruction")
        }
    }
}