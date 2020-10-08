package nix.finalproject.carrentalservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "engine_types")
public class EngineType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "engine_capacity", nullable = false)
    private String capacity;

    @OneToMany(mappedBy = "engineType")
    private List<Car> cars;

    public EngineType(Long id, String type, String capacity) {
        this.id = id;
        this.type = type;
        this.capacity = capacity;
    }

    public EngineType(String type, String capacity) {
        this.type = type;
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EngineType that = (EngineType) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(type, that.type) &&
                Objects.equals(capacity, that.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, capacity);
    }
}
