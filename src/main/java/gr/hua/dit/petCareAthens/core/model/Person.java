package gr.hua.dit.petCareAthens.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "owners")
public class Person {

    @Id
    long id;

    String name;
    String email;
    String phone;

    public Person(){

    }
    public Person(long id, String name, String email, String phone){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
