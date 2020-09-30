package nix.finalproject.carrentalservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;

    @Column(name = "start_order", nullable = false)
    private LocalDate startOrder;

    @Column(name = "end_order", nullable = false)
    private LocalDate endOrder;

    public Order(Client client, Car car, LocalDate startOrder, LocalDate endOrder) {
        this.client = client;
        this.car = car;
        this.startOrder = startOrder;
        this.endOrder = endOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                Objects.equals(client, order.client) &&
                Objects.equals(car, order.car) &&
                Objects.equals(startOrder, order.startOrder) &&
                Objects.equals(endOrder, order.endOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, car, startOrder, endOrder);
    }
}
