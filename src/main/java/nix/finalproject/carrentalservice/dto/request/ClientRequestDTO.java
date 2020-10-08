package nix.finalproject.carrentalservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRequestDTO {

    @Size(min = 2, max = 60, message = "Name must be longer then 2 letters, but shorter then 60")
    @NotNull(message = "You must enter a name!")
    private String firstName;

    @Size(min = 2, max = 60, message = "Surname must be longer then 2 letters, but shorter then 60")
    @NotNull(message = "You must enter a surname!")
    private String lastName;

    @Pattern(regexp = "^[+][0-9]{3}[0-9]{2}[0-9]{7}$", message = "You must enter the phone number according to the template - +380990964311")
    @NotNull(message = "You must enter a phone number!")
    private String phoneNumber;

    @Email
    @NotNull(message = "You must enter an email!")
    private String email;

    @Size(min = 6, message = "Password must be longer then 6 characters")
    @NotNull(message = "You must enter a password!")
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRequestDTO that = (ClientRequestDTO) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber, email, password);
    }

    @Override
    public String toString() {
        return "ClientRequestDTO{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
