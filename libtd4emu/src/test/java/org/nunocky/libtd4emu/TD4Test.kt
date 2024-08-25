package org.nunocky.libtd4emu

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TD4Test {
    @Test
    fun testMovAn() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0x30 + n).toU8(), // MOV A, n
            )

            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.regA)
        }
    }

    @Test
    fun testMovBn() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0x70 + n).toU8(), // MOV B, n
            )

            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.regB)
        }
    }

    @Test
    fun testMovAB() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0x70 + n).toU8(), // MOV B, n
                0x10, // MOV A, B
            )

            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            repeat(2) {
                rom.address = cpu.address
                cpu.data = rom.data
                cpu.step()
            }

            assertEquals(n.toU8(), cpu.regA)
        }
    }

    @Test
    fun testMovBA() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0x30 + n).toU8(), // MOV A, n
                0x40, // MOV B, A
            )

            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            repeat(2) {
                rom.address = cpu.address
                cpu.data = rom.data
                cpu.step()
            }

            assertEquals(n.toU8(), cpu.regA)
        }
    }

    @Test
    fun testInA() {
        val program = byteArrayOf(
            0x20, // IN A
        )

        repeat(15) { n ->
            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            cpu.sw = n.toU8()
            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.regA)
        }
    }

    @Test
    fun testInB() {
        val program = byteArrayOf(
            0x60, // IN B
        )

        repeat(15) { n ->
            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            cpu.sw = n.toU8()
            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.regB)
        }
    }

    @Test
    fun testOutB() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0x70 + n).toU8(), // MOV B, n
                0x90.toU8(), // OUT B
            )
            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            repeat(2) {
                rom.address = cpu.address
                cpu.data = rom.data
                cpu.step()
            }

            assertEquals(n.toU8(), cpu.led)
        }
    }

    @Test
    fun testOutImm() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0xB0 + n).toU8(), // OUT n
            )
            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.led)
        }
    }

    @Test
    fun testAddAImm() {
        repeat(15) { a ->
            repeat(15) { n ->
                val program = byteArrayOf(
                    (0x30 + a).toU8(), // MOV A, a
                    (0x00 + n).toU8(), // ADD A, n
                )

                val rom = Rom(program)
                val cpu = TD4()
                cpu.reset()

                repeat(2) {
                    rom.address = cpu.address
                    cpu.data = rom.data
                    cpu.step()
                }

                val sum = a + n
                val cf = (sum > 15)
                assertEquals(sum.and(0x0f).toU8(), cpu.regA)
                assertEquals(cf, cpu.cf)
            }
        }
    }

    @Test
    fun testAddBImm() {
        repeat(15) { b ->
            repeat(15) { n ->
                val program = byteArrayOf(
                    (0x70 + b).toU8(), // MOV B, b
                    (0x50 + n).toU8(), // ADD B, n
                )

                val rom = Rom(program)
                val cpu = TD4()
                cpu.reset()

                repeat(2) {
                    rom.address = cpu.address
                    cpu.data = rom.data
                    cpu.step()
                }

                val sum = b + n
                val cf = (sum > 15)
                assertEquals(sum.and(0x0f).toU8(), cpu.regB)
                assertEquals(cf, cpu.cf)
            }
        }
    }

    @Test
    fun testJmpImm() {
        repeat(15) { n ->
            val program = byteArrayOf(
                (0xF0 + n).toU8(), // JMP n
            )

            val rom = Rom(program)
            val cpu = TD4()
            cpu.reset()

            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()

            assertEquals(n.toU8(), cpu.address)
        }
    }

    @Test
    fun testJncImm_CF0() {
        val program = byteArrayOf(
            (0xE0 + 0x0F).toU8(), // JNC n
        )

        val rom = Rom(program)
        val cpu = TD4()
        cpu.reset()

        rom.address = cpu.address
        cpu.data = rom.data
        cpu.step()

        assertEquals(0x0F.toU8(), cpu.address)
    }

    @Test
    fun testJncImm_CF1() {
        val program = byteArrayOf(
            0x3F, // MOV A, 15
            0x01, // ADD A, 1
            (0xE0 + 0x0F).toU8(),  // JNC 15
        )

        val rom = Rom(program)
        val cpu = TD4()
        cpu.reset()

        repeat(3) {
            rom.address = cpu.address
            cpu.data = rom.data
            cpu.step()
        }

        assertEquals(0x03.toU8(), cpu.address)
    }
}