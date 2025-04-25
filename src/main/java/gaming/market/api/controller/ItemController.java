package gaming.market.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gaming.market.api.model.Item;
import gaming.market.api.repository.ItemRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/item")
public class ItemController {
    
    @Autowired
    private ItemRepository itemRepository;

    @PostMapping
    private Item createItem(@RequestBody @Valid Item item) {
        return itemRepository.save(item);
    }
}
