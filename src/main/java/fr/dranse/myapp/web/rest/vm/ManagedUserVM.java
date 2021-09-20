package fr.dranse.myapp.web.rest.vm;

import fr.dranse.myapp.service.dto.AdminUserDTO;
import javax.validation.constraints.Size;

/**
 * View Model extending the AdminUserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends AdminUserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    private String telephone;

    private String adrRue;

    private Integer adrCodePostal;

    private String adrPays;

    private String adrVille;

    private String numCB;

    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //telephone
    public String getTelephone() {
        return telephone;
    }

    public String getAdrRue() {
        return adrRue;
    }

    public String getAdrCodePostal() {
        return adrCodePostal.toString();
    }

    public String getAdrPays() {
        return adrPays;
    }

    public String getAdrVille() {
        return adrVille;
    }

    public String getNumCB() {
        return numCB;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
