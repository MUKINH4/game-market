package gaming.market.api.specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import gaming.market.api.controller.ItemController.ItemFilter;
import gaming.market.api.model.Item;

public class ItemSpecification {
    public static Specification<Item> withFilter(ItemFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()){
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }

            if (filter.tipo() != null){
                predicates.add(cb.equal(root.get("tipo"), filter.tipo()));
            }
            if (filter.raridade() != null){
                predicates.add(cb.equal(root.get("raridade"), filter.raridade()));
            }

            if (filter.precoMin() != null && filter.precoMax() != null) {
                predicates.add(cb.between(root.get("preco"), filter.precoMin(), filter.precoMax()));
            } else if (filter.precoMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("preco"), filter.precoMin()));
            } else if (filter.precoMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("preco"), filter.precoMax()));
            }

            var arrayPredicates = predicates.toArray(Predicate[]::new);
            return cb.and(arrayPredicates);
        };

    }
}
