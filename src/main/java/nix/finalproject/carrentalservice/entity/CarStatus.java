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
@Table(name = "car_status")
public class CarStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "carStatus")
    private List<Car> cars;

    public CarStatus(Long id, String status) {
        this.id = id;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarStatus carStatus = (CarStatus) o;
        return Objects.equals(id, carStatus.id) &&
                Objects.equals(status, carStatus.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}
