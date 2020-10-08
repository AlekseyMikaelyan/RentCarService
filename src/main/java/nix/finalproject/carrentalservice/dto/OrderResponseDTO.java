package nix.finalproject.carrentalservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nix.finalproject.carrentalservice.entity.Car;
import nix.finalproject.carrentalservice.entity.Order;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {

    private Long id;

    private String clientFirstName;

    private String clientLastName;

    private String clientPhoneNumber;

    private String carBrand;

    private String carModel;

    private LocalDate startOrder;

    private LocalDate endOrder;

    private String totalPrice;

    public static OrderResponseDTO fromOrder(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getClient().getFirstName(),
                order.getClient().getLastName(),
                order.getClient().getPhoneNumber(),
                order.getCar().getBrand().getBrandName(),
                order.getCar().getModel(),
                order.getStartOrder(),
                order.getEndOrder(),
                createATotalPrice(order.getCar(), findHowManyDaysClientOrderedACar(order.getStartOrder(), order.getEndOrder()))
        );
    }

    public static int findHowManyDaysClientOrderedACar(LocalDate startOrder, LocalDate endOrder) {
        int orderDays;

        Period period = Period.between(startOrder, endOrder);
        orderDays = period.getDays();

        return orderDays;
    }

    public static String createATotalPrice(Car car, int orderDays) {
        String priceForDay = car.getPrice();

        String [] array = priceForDay.split(" ");
        String countOfMoneyString = array[0];

        int countOfMoneyInt = Integer.parseInt(countOfMoneyString);

        int totalPriceInt = countOfMoneyInt * orderDays;

        String totalPrice = Integer.toString(totalPriceInt);

        return totalPrice + " " + array[1];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderResponseDTO that = (OrderResponseDTO) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(clientFirstName, that.clientFirstName) &&
                Objects.equals(clientLastName, that.clientLastName) &&
                Objects.equals(clientPhoneNumber, that.clientPhoneNumber) &&
                Objects.equals(carBrand, that.carBrand) &&
                Objects.equals(carModel, that.carModel) &&
                Objects.equals(startOrder, that.startOrder) &&
                Objects.equals(endOrder, that.endOrder) &&
                Objects.equals(totalPrice, that.totalPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientFirstName, clientLastName, clientPhoneNumber, carBrand, carModel, startOrder, endOrder, totalPrice);
    }

    @Override
    public String toString() {
        return "OrderResponseDTO{" +
                "id=" + id +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", carModel='" + carModel + '\'' +
                ", startOrder=" + startOrder +
                ", endOrder=" + endOrder +
                ", totalPrice='" + totalPrice + '\'' +
                '}';
    }
}
