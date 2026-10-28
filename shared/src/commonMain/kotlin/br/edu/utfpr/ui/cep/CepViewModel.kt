package br.edu.utfpr.ui.cep

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.utfpr.domain.repository.CepRepository
import br.edu.utfpr.data.repository.CepRepositoryImpl
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class CepViewModel(
    private val repository: CepRepository = CepRepositoryImpl()
) : ViewModel() {
    private val _uiState = MutableStateFlow(CepUiState())
    val uiState: StateFlow<CepUiState> = _uiState.asStateFlow()

    private val _erros = Channel<String>(Channel.BUFFERED)
    val erros = _erros.receiveAsFlow()

    fun onCepChange(valor: String) {
        val filtrado = valor.filter { it.isDigit() }.take(8)
        _uiState.value = _uiState.value.copy(cepInput = filtrado)
    }

    fun buscarCep() {
        val estadoAtual = _uiState.value
        if (!estadoAtual.botaoHabilitado) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, endereco = null)
            val resultado = repository.buscarCep(_uiState.value.cepInput)
            resultado
                .onSuccess { endereco ->
                    _uiState.value = _uiState.value.copy(isLoading = false, endereco = endereco)
                }
                .onFailure { exception ->
                    if (exception is CancellationException) throw exception
                    _uiState.value = _uiState.value.copy(isLoading = false, endereco = null)
                    _erros.trySend("Ocorreu um erro ao consultar o CEP. Aguarde um momento e tente novamente.")
                }
        }
    }
}
