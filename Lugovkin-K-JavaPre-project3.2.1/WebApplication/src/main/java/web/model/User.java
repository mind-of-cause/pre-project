package web.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Data

@Entity
@Table(name = "usertable")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    private String name;
    @Column(name = "weight")
    private Integer weight;
    @Column(name = "has_Car")
    private Boolean hasCar;

    public User() {
    }

    public User(String name, Integer weight, Boolean hasCar) {
        this.name = name;
        this.weight = weight;
        this.hasCar = hasCar;
    }

    public Boolean isHasCar() {
        return hasCar;
    }

    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                ", hasCar=" + hasCar +
                '}';
    }

}
