package br.edu.utfpr.domain.model

data class Endereco(
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val localidade: String,
    val uf: String
)
