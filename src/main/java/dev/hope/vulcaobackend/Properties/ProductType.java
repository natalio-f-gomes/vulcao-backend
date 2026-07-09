package dev.hope.vulcaobackend.Properties;

import lombok.Getter;

@Getter
public enum ProductType {
    FRESCOS_E_HORTÍCOLAS,    // Frutas e legumes frescos (Produce)
    CARNES_E_AVES,
    PEIXARIA_E_MARISCO,
    LACTICÍNIOS,
    PRODUTOS_VEGETAIS,       // Alternativas plant-based
    PADARIA_E_PASTELARIA,
    CONGELADOS,
    CEREAIS_E_MASSAS,
    CONSERVAS_E_FRASCOS,
    ÓLEOS_E_VINAGRES,
    ERVAS_E_ESPECIARIAS,
    SNACKS_E_APERITIVOS,
    DOCES_E_CHOCOLATES,
    BEBIDAS,
    REFEIÇÕES_PRONTAS_E_CHARCUTARIA,

    // --- ESSENCIAIS DO DIA A DIA PARA A CASA ---
    LIMPEZA_DA_CASA,          // Detergente da loiça, detergente da roupa, etc.
    PRODUTOS_DE_PAPEL_E_PLÁSTICO, // Papel higiénico, rolos de cozinha, sacos do lixo
    MATERIAL_DE_ESCRITÓRIO,   // Canetas, fita cola, artigos básicos de papelaria

    // --- HIGIENE PESSOAL E SAÚDE ---
    HIGIENE_PESSOAL,         // Champô, pasta de dentes, desodorizante
    SAÚDE_E_BEM_ESTAR,       // Medicamentos de venda livre (MNSRM), pensos rápidos

    // --- BEBÉ E ANIMAIS DE ESTIMAÇÃO ---
    ARTIGOS_PARA_BEBÉ,       // Fraldas, toalhitas, comida de bebé
    ALIMENTAÇÃO_E_ACESSÓRIOS_PARA_ANIMAIS, // Comida para cão/gato e areia sanitária

    // --- CAIXA E SOUVENIRS / DIVERSOS ---
    REVISTAS,                // Revistas expostas junto à caixa de pagamento
    CARTÕES_PRESENTE,
    OUTROS
}