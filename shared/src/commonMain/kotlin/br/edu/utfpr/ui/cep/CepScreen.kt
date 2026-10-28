package br.edu.utfpr.ui.cep

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.edu.utfpr.domain.model.Endereco

class CepVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text
        val formatted = StringBuilder()
        for (i in digits.indices) {
            if (i == 5) formatted.append('-')
            formatted.append(digits[i])
        }
        val out = formatted.toString()
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 5) return offset
                if (offset <= 8) return offset + 1
                return out.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 5) return offset
                if (offset <= 9) return offset - 1
                return digits.length
            }
        }
        return TransformedText(AnnotatedString(out), offsetMapping)
    }
}

@Composable
fun CepScreen(
    viewModel: CepViewModel = viewModel { CepViewModel() }
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.erros.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    CepScreenContent(
        state = state,
        onCepChange = viewModel::onCepChange,
        onBuscar = viewModel::buscarCep,
        snackbarHostState = snackbarHostState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CepScreenContent(
    state: CepUiState,
    onCepChange: (String) -> Unit,
    onBuscar: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.cepInput,
                onValueChange = onCepChange,
                label = { Text("Digite o CEP") },
                enabled = state.campoHabilitado,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                visualTransformation = CepVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            ElevatedButton(
                onClick = onBuscar,
                enabled = state.botaoHabilitado,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Buscar")
                }
            }

            Text(text = "CEP: ${state.endereco?.cep ?: ""}")
            Text(text = "Logradouro: ${state.endereco?.logradouro ?: ""}")
            Text(text = "Bairro: ${state.endereco?.bairro ?: ""}")
            Text(text = "Localidade: ${state.endereco?.localidade ?: ""}")
            Text(text = "UF: ${state.endereco?.uf ?: ""}")
        }
    }
}

@Preview
@Composable
fun CepScreenPreviewInicial() {
    CepScreenContent(
        state = CepUiState(),
        onCepChange = {},
        onBuscar = {}
    )
}

@Preview
@Composable
fun CepScreenPreviewConsultando() {
    CepScreenContent(
        state = CepUiState(cepInput = "85504430", isLoading = true),
        onCepChange = {},
        onBuscar = {}
    )
}

@Preview
@Composable
fun CepScreenPreviewSucesso() {
    CepScreenContent(
        state = CepUiState(
            cepInput = "85504430",
            endereco = Endereco(
                cep = "85504-430",
                logradouro = "Rua Itabira",
                bairro = "Bancários",
                localidade = "Pato Branco",
                uf = "PR"
            )
        ),
        onCepChange = {},
        onBuscar = {}
    )
}
