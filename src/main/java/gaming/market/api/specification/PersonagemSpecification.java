package gaming.market.api.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import gaming.market.api.controller.PersonagemController.PersonagemFilter;
import gaming.market.api.model.Personagem;
import jakarta.persistence.criteria.Predicate;

public class PersonagemSpecification {
    public static Specification<Personagem> withFilter(PersonagemFilter filter){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter.nome() != null && !filter.nome().isBlank()){
                predicates.add(
                    cb.like(
                        cb.lower(root.get("nome")), "%" + filter.nome().toLowerCase() + "%"));
            }
            if (filter.classe() != null) {
                predicates.add(cb.equal(root.get("classe"), filter.classe()));
            }
            var arrayPredicates = predicates.toArray(Predicate[]::new);
            return cb.and(arrayPredicates);
        };

    }
}
