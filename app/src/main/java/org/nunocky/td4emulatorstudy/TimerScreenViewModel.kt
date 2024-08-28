package org.nunocky.td4emulatorstudy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.nunocky.libtd4emu.EvaBoard
import org.nunocky.libtd4emu.OP_ADD_A_IMM
import org.nunocky.libtd4emu.OP_ADD_B_IMM
import org.nunocky.libtd4emu.OP_IN_A
import org.nunocky.libtd4emu.OP_IN_B
import org.nunocky.libtd4emu.OP_JMP_IMM
import org.nunocky.libtd4emu.OP_JNC_IMM
import org.nunocky.libtd4emu.OP_MOV_A_B
import org.nunocky.libtd4emu.OP_MOV_A_IMM
import org.nunocky.libtd4emu.OP_MOV_B_A
import org.nunocky.libtd4emu.OP_MOV_B_IMM
import org.nunocky.libtd4emu.OP_OUT_B
import org.nunocky.libtd4emu.OP_OUT_IMM
import org.nunocky.libtd4emu.toU8

class TimerScreenViewModel : ViewModel() {
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

    private val evaBoard = EvaBoard(program)

    private val _programCodes = MutableStateFlow(
        program.map { b ->
            val opCode = (b.toInt() shr 4) and 0x0f // bの上位4ビット
            val imm = b.toInt() and 0x0f // bの下位4ビット
            when (opCode.toU8()) {
                OP_MOV_A_B -> "MOV A, B"
                OP_MOV_B_A -> "MOV B, A"
                OP_MOV_A_IMM -> "MOV A, $imm"
                OP_MOV_B_IMM -> "MOV B, $imm"
                OP_IN_A -> "IN A"
                OP_IN_B -> "IN B"
                OP_OUT_B -> "OUT B"
                OP_OUT_IMM -> "OUT $imm"
                OP_ADD_A_IMM -> "ADD A, $imm"
                OP_ADD_B_IMM -> "ADD B, $imm"
                OP_JMP_IMM -> "JMP $imm"
                OP_JNC_IMM -> "JNC $imm"
                else -> "unknown"
            }
        })

    val programCodes = _programCodes.asStateFlow()

    private val _step = MutableStateFlow(0)
    val step = _step.asStateFlow()

    private val _address = MutableStateFlow(0)
    val address = _address.asStateFlow()

    private val _data = MutableStateFlow(0)
    val data = _data.asStateFlow()

    private val _sw = MutableStateFlow(0)
    val sw = _sw.asStateFlow()

    private val _led = MutableStateFlow(0)
    val led = _led.asStateFlow()

    private val _regA = MutableStateFlow(0)
    val regA = _regA.asStateFlow()

    private val _regB = MutableStateFlow(0)
    val regB = _regB.asStateFlow()

    private val _cf = MutableStateFlow(false)
    val cf = _cf.asStateFlow()

    fun reset() {
        evaBoard.reset()
        updateBoardState()
        _step.value = 0
    }

    fun step() {
        evaBoard.sw = sw.value.toU8()
        evaBoard.step()
        updateBoardState()
        _step.value++
    }

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    fun run() {
        if (_isRunning.value) {
            return
        }

        viewModelScope.launch {
            _isRunning.value = true
            while (_isRunning.value) {
                step()
                delay(100)
            }
        }
    }

    fun stop() {
        _isRunning.value = false
    }

    fun toggleSwitch0() {
        val newValue = _sw.value.xor(0x01)
        _sw.value = newValue
    }

    fun toggleSwitch1() {
        val newValue = _sw.value.xor(0x02)
        _sw.value = newValue
    }

    fun toggleSwitch2() {
        val newValue = _sw.value.xor(0x04)
        _sw.value = newValue
    }

    fun toggleSwitch3() {
        val newValue = _sw.value.xor(0x08)
        _sw.value = newValue
    }

    private fun updateBoardState() {
        _address.value = evaBoard.cpu.address.toInt()
        _data.value = evaBoard.cpu.data.toInt()
        _led.value = evaBoard.led.toInt()
        _regA.value = evaBoard.cpu.regA.toInt()
        _regB.value = evaBoard.cpu.regB.toInt()
        _cf.value = evaBoard.cpu.cf
    }
}