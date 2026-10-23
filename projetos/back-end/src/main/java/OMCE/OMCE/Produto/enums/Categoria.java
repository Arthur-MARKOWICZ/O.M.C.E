package OMCE.OMCE.Produto.enums;

import OMCE.OMCE.Execao.CategoriaInvalida;

public enum Categoria {
    ESP32,
    ARDUINO,
    REGISTORES,
    SENSORES,
    BATERIA,
    CABOS,
    MOTORES,
    CONECTORES,
    OUTRO;

    public static Categoria deTexto(String texto) {
        if (texto == null || texto.isBlank()) {
            return null;
        }
        try {
            return valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CategoriaInvalida("Categoria invalida: " + texto);
        }
    }
}