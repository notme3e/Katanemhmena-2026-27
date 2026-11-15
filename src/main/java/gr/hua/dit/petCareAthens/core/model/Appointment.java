package gr.hua.dit.petCareAthens.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;


@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    long id;

    LocalDateTime date;
    AppointmentStatus status; //TODO enum with status code (ex. 1:completed, 2:cancelled)

    public Appointment(){

    }
    public Appointment(long id, LocalDateTime date, AppointmentStatus status){
        this.id = id;
        this.date = date;
        this.status = status;
    }
}
