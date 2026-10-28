package br.edu.utfpr.ui.cep

import br.edu.utfpr.domain.model.Endereco

data class CepUiState(
    val cepInput: String = "",
    val isLoading: Boolean = false,
    val endereco: Endereco? = null
) {
    val isCepValido: Boolean get() = cepInput.length == 8
    val campoHabilitado: Boolean get() = !isLoading
    val botaoHabilitado: Boolean get() = isCepValido && !isLoading
}
