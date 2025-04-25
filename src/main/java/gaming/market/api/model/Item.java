package gaming.market.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

enum Tipo {
    ARMA,
    ARMADURA,
    POCAO,
    ACESSORIO
}

enum Raridade {
    COMUM,
    RARO,
    EPICO,
    LENDARIO
}

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank(message = "O tipo não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotBlank(message = "A raridade não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Raridade raridade;

    @NotBlank(message = "O preço não pode ser vazio")
    private int preco;

    @ManyToOne
    @JoinColumn(name = "personagem_id")
    private Personagem dono;

}
