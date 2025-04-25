package gaming.market.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

enum Classe {
    GUERREIRO,
    MAGO,
    ARQUEIRO
}

@Entity
@Data
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "A classe não pode ser vazia")
    private Classe classe;

    @Min(value = 1, message = "O nível mínimo é 1")
    @Max(value = 99, message = "O nível máximo é 99")
    @NotBlank(message = "O nível não pode ser vazio")
    private int nivel;

    private int moedas = 0;

}
