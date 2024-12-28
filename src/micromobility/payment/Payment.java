package micromobility.payment;

import data.UserAccount;
import micromobility.JourneyService;

import java.math.BigDecimal;

public class Payment {

    JourneyService journeyService;
    UserAccount user;
    private BigDecimal importe;

    public Payment(UserAccount user, JourneyService journeyService, BigDecimal importe){
        this.user = user;
        this.journeyService = journeyService;
        this.importe = importe;
    }

    public void setImporte(){
        this.importe = journeyService.getImporte();
    }
}
