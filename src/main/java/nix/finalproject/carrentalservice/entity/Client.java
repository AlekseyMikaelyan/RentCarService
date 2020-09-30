package nix.finalproject.carrentalservice.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Size(min = 2, max = 60, message = "Name must be longer then 2 letters, but shorter then 60")
    @NotNull(message = "You must enter a name!")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(min = 2, max = 60, message = "Surname must be longer then 2 letters, but shorter then 60")
    @NotNull(message = "You must enter a surname!")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Pattern(regexp = "^[+][0-9]{3}[0-9]{2}[0-9]{7}$", message = "You must enter the phone number according to the template - +380990964311")
    @NotNull(message = "You must enter a phone number!")
    @Column(name = "phone_number")
    private String phoneNumber;

    @Email
    @NotNull(message = "You must enter an email!")
    @Column(name = "email")
    private String email;

    @Size(min = 6, message = "Password must be longer then 6 characters")
    @NotNull(message = "You must enter a password!")
    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    public Client(Long id,
                  @Size(min = 2, max = 60, message = "Name must be longer then 2 letters, but shorter then 60")
                  @NotNull(message = "You must enter a name!")
                          String firstName,
                  @Size(min = 2, max = 60, message = "Surname must be longer then 2 letters, but shorter then 60")
                  @NotNull(message = "You must enter a surname!")
                          String lastName,
                  @Pattern(regexp = "^[+][0-9]{3}[0-9]{2}[0-9]{7}$", message = "You must enter the phone number according to the template - +380990964311")
                  @NotNull(message = "You must enter a phone number!")
                          String phoneNumber,
                  @Email @NotNull(message = "You must enter an email!")
                          String email,
                  @Size(min = 6, message = "Password must be longer then 6 characters")
                  @NotNull(message = "You must enter a password!")
                          String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id) &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(phoneNumber, client.phoneNumber) &&
                Objects.equals(email, client.email) &&
                Objects.equals(password, client.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, phoneNumber, email, password);
    }
}
