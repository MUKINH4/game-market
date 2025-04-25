package gaming.market.api.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import gaming.market.api.model.Personagem;
import gaming.market.api.model.enums.Classe;
import gaming.market.api.repository.PersonagemRepository;
import gaming.market.api.specification.PersonagemSpecification;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/personagem")
public class PersonagemController {
    public record PersonagemFilter(String nome, Classe classe) {
    }

    @Autowired
    private PersonagemRepository personagemRepository;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    private ResponseEntity<Personagem> createHero(@Valid @RequestBody Personagem personagem) {
        personagemRepository.save(personagem);
        return ResponseEntity.ok(personagem);
    }
    
    @GetMapping
    public ResponseEntity<Page<Personagem>> searchPersonagens(PersonagemFilter filters,
    @PageableDefault(size = 10, sort = "nome", direction = Direction.ASC) Pageable pageable) {

    var specification = PersonagemSpecification.withFilter(filters);

    Page<Personagem> personagens = personagemRepository.findAll(specification, pageable);

    return ResponseEntity.ok(personagens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> listHeroesById(@PathVariable Long id) {
        Optional<Personagem> personagem = personagemRepository.findById(id);

        if (personagem.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "personagem não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        return ResponseEntity.ok(personagem.get());
    }
    

    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("/{id}")
    private ResponseEntity<Map<String, String>> deleteHero(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();

        if (!personagemRepository.existsById(id)) {
            response.put("message", "personagem não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        personagemRepository.deleteById(id);
        response.put("message", "deletado com sucesso");
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Object> editHero(@PathVariable Long id, @Valid @RequestBody Personagem updatedPersonagem) {
        Optional<Personagem> lastPersonagem = personagemRepository.findById(id);
        if (lastPersonagem.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "personagem não encontrado");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        Personagem personagem = lastPersonagem.get();
        personagem.setNome(updatedPersonagem.getNome());
        personagem.setNivel(updatedPersonagem.getNivel());
        personagem.setClasse(updatedPersonagem.getClasse());
        personagem.setMoedas(updatedPersonagem.getMoedas());

        personagemRepository.save(personagem);
        return ResponseEntity.ok(personagem);
    }

}
