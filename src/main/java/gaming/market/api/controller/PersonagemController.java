package gaming.market.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gaming.market.api.model.Personagem;
import gaming.market.api.repository.PersonagemRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/personagem")
public class PersonagemController {
    
    @Autowired
    private PersonagemRepository personagemRepository;

    private Personagem getHero(Long id){
        return personagemRepository.
            findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    private ResponseEntity<Personagem> createHero(@Valid @RequestBody Personagem personagem) {
        return ResponseEntity.ok(personagemRepository.save(personagem));
    }
    
    @GetMapping
    private ResponseEntity<List<Personagem>> listAllHeroes(){
        return ResponseEntity.ok(personagemRepository.findAll());
    }

    @DeleteMapping("/{id}")
    private void deleteHero(@PathVariable Long id){
        personagemRepository.delete(getHero(id));
    }
    

}
