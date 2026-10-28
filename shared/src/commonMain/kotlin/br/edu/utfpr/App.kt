package br.edu.utfpr

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import br.edu.utfpr.ui.cep.CepScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        CepScreen()
    }
}
