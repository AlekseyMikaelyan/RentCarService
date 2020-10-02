package nix.finalproject.carrentalservice.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class ClientRequestDTO {

    private Long id;

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

    public ClientRequestDTO(String firstName, String lastName, String phoneNumber, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }
}
