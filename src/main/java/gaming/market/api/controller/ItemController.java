package gaming.market.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gaming.market.api.model.Item;
import gaming.market.api.model.Personagem;
import gaming.market.api.model.enums.Raridade;
import gaming.market.api.model.enums.Tipo;
import gaming.market.api.repository.ItemRepository;
import gaming.market.api.repository.PersonagemRepository;
import gaming.market.api.specification.ItemSpecification;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/item")
public class ItemController {

    public record ItemFilter(String nome, Tipo tipo, Integer precoMin, Integer precoMax, Raridade raridade) {
    }

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PersonagemRepository personagemRepository;

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody @Valid Item item) {
        if (item.getDono() == null || item.getDono().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Personagem ID é obrigatório");
        }

        Personagem personagem = personagemRepository.findById(item.getDono().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personagem não encontrado"));

        item.setDono(personagem);
        personagem.getInventario().add(item);
        
        return ResponseEntity.ok(itemRepository.save(item));
    }

    @GetMapping
    public ResponseEntity<Page<Item>> searchItens(ItemFilter filters,
    @PageableDefault(size = 10, sort = "nome", direction = Direction.ASC) Pageable pageable) {

    var specification = ItemSpecification.withFilter(filters);

    Page<Item> itens = itemRepository.findAll(specification, pageable);

    return ResponseEntity.ok(itens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado com id: " + id));
        
        return ResponseEntity.ok(item);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody @Valid Item updatedItem) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado com id: " + id));
        
        if (updatedItem.getDono() != null && updatedItem.getDono().getId() != null && 
            (existingItem.getDono() == null || 
             !updatedItem.getDono().getId().equals(existingItem.getDono().getId()))) {
            
            if (existingItem.getDono() != null) {
                Personagem oldOwner = personagemRepository.findById(existingItem.getDono().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Antigo dono não encontrado"));
                oldOwner.getInventario().remove(existingItem);
                personagemRepository.save(oldOwner);
            }
            
            Personagem newOwner = personagemRepository.findById(updatedItem.getDono().getId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Novo dono não encontrado"));
            existingItem.setDono(newOwner);
            newOwner.getInventario().add(existingItem);
            personagemRepository.save(newOwner);
        }
        
        existingItem.setNome(updatedItem.getNome());
        existingItem.setTipo(updatedItem.getTipo());
        existingItem.setRaridade(updatedItem.getRaridade());
        existingItem.setPreco(updatedItem.getPreco());
        
        return ResponseEntity.ok(itemRepository.save(existingItem));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item não encontrado com id: " + id));
        
        if (item.getDono() != null) {
            Personagem owner = personagemRepository.findById(item.getDono().getId())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Dono do item não encontrado"));
            owner.getInventario().remove(item);
            personagemRepository.save(owner);
        }
        
        itemRepository.delete(item);
        return ResponseEntity.noContent().build();
    }
}