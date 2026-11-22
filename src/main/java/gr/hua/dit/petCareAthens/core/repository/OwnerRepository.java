package gr.hua.dit.petCareAthens.core.repository;

import gr.hua.dit.petCareAthens.core.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerRepository extends JpaRepository<Owner, Long> {

}
