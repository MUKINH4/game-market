package gaming.market.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import gaming.market.api.model.enums.Raridade;
import gaming.market.api.model.enums.Tipo;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;


@Entity
@Data
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull(message = "O tipo não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Tipo tipo;

    @NotNull(message = "A raridade não pode ser vazio")
    @Enumerated(EnumType.STRING)
    private Raridade raridade;

    @Positive
    private int preco;

    @ManyToOne
    @JoinColumn(name = "personagem_id", referencedColumnName = "id")
    @JsonBackReference
    private Personagem dono;

    


}
