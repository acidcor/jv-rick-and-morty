package mate.academy.rickandmorty.repository;

import java.util.Collection;
import java.util.List;
import mate.academy.rickandmorty.model.Personage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonageRepository extends JpaRepository<Personage, Long> {
    Page<Personage> findByNameContains(String name, Pageable pageable);

    List<Personage> findAllByExternalIdIn(Collection<Long> externalId);
}
