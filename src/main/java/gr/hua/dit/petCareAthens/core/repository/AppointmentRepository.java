package gr.hua.dit.petCareAthens.core.repository;

import gr.hua.dit.petCareAthens.core.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
