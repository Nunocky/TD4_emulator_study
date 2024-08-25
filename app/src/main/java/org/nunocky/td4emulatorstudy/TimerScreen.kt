package org.nunocky.td4emulatorstudy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import org.nunocky.td4emulatorstudy.ui.theme.TD4EmulatorStudyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerScreenViewModel = viewModel()
) {
    val step by viewModel.step.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val address by viewModel.address.collectAsState()
    val data by viewModel.data.collectAsState()
    val sw by viewModel.sw.collectAsState()
    val led by viewModel.led.collectAsState()
    val regA by viewModel.regA.collectAsState()
    val regB by viewModel.regB.collectAsState()
    val cf by viewModel.cf.collectAsState()
    val programCodes by viewModel.programCodes.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        TopAppBar(
            title = { Text("TD4 Ramen Timer") },
            colors = TopAppBarDefaults.topAppBarColors(),
        )
        Text("Board", style = MaterialTheme.typography.headlineLarge)
        Row() {
            Text("LED")
            Checkbox(checked = led.and(0x08) != 0, onCheckedChange = { })
            Checkbox(checked = led.and(0x04) != 0, onCheckedChange = { })
            Checkbox(checked = led.and(0x02) != 0, onCheckedChange = { })
            Checkbox(checked = led.and(0x01) != 0, onCheckedChange = { })
        }

        Row() {
            Text("SW")
            Checkbox(checked = sw.and(0x08) != 0, onCheckedChange = {
                viewModel.toggleSwitch3()
            })
            Checkbox(checked = sw.and(0x04) != 0, onCheckedChange = {
                viewModel.toggleSwitch2()
            })
            Checkbox(checked = sw.and(0x02) != 0, onCheckedChange = {
                viewModel.toggleSwitch1()
            })
            Checkbox(checked = sw.and(0x01) != 0, onCheckedChange = {
                viewModel.toggleSwitch0()
            })
        }

        // ステップ実行ボタン
        Row() {
            if (isRunning) {
                Button(onClick = {
                    viewModel.stop()
                }) {
                    Text("Stop")
                }
            } else {
                Button(onClick = {
                    viewModel.reset()
                }) {
                    Text("Reset")
                }
                Button(onClick = {
                    // ステップ実行
                    viewModel.step()
                }) {
                    Text("Step")
                }
                Button(onClick = {
                    viewModel.run()
                }) {
                    Text("Run")
                }
            }
        }

        Text("CPU", style = MaterialTheme.typography.headlineLarge)
        CPUSection(address, data, sw, led, regA, regB, cf)

        Text("ROM", style = MaterialTheme.typography.headlineLarge)
        RomSection(programCodes, address)
    }
}

@Composable
fun CPUSection(address: Int, data: Int, sw: Int, led: Int, regA: Int, regB: Int, cf: Boolean) {
    Text(
        "address: 0x${address.toString(16).padStart(2, '0')}, "
                + " data: 0x${data.toUInt().toString(16).padStart(2, '0').takeLast(2)} "
    )
    Text(
        "sw: 0b${sw.toString(2).padStart(4, '0')}, "
                + " led: 0b${led.toString(2).padStart(4, '0')} "
    )
    Text(
        "regA: 0x${regA.toString(16).padStart(2, '0')},"
                + " regB: 0x${regB.toString(16).padStart(2, '0')}, "
                + " cf: ${if (cf) 1 else 0}"
    )
}

@Composable
fun RomSection(programCodes: List<String>, address: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        programCodes.forEachIndexed { i, code ->
            if (address == i) {
                RunningCode(index = i, code = code)
            } else {
                OtherCode(index = i, code = code)
            }
        }
    }
}

@Composable
fun RunningCode(index: Int, code: String) {
    Text(
        "0x${index}: ${code}",
        modifier = Modifier
            .background(Color.Yellow)
            .fillMaxWidth()
    )
}

@Composable
fun OtherCode(index: Int, code: String) {

    Text(
        "0x${index}: ${code}",
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun GreetingPreview() {
    TD4EmulatorStudyTheme {
        TimerScreen()
    }
}