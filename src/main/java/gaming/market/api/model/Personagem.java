package gaming.market.api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import gaming.market.api.model.enums.Classe;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Entity
@Data
public class Personagem {

    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;

    @Enumerated(EnumType.STRING)
    private Classe classe;

    @Min(value = 1, message = "O nível mínimo é 1")
    @Max(value = 99, message = "O nível máximo é 99")
    private int nivel;

    private int moedas = 0;

    @OneToMany(mappedBy = "dono", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Item> inventario = new ArrayList<>();
}
