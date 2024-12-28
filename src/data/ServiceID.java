package data;

import java.math.BigDecimal;
import java.util.Objects;

public class ServiceID {

    private final String serviceID;
    private BigDecimal importe;

    public ServiceID(String serviceID) {
        if (serviceID == null || !isValidServiceID(serviceID)) {
            throw new IllegalArgumentException("ServiceID no puede ser nulo y debe ser alfanumérico con un máximo de 8 caracteres.");
        }
        this.serviceID = serviceID;
    }

    private boolean isValidServiceID(String serviceID) {
        return serviceID.matches("^[a-zA-Z0-9]{1,5}$");
    }

    public String getServiceID() {
        return serviceID;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public setImporte(BigDecimal importe){
        this.importe = importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }
    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceID that = (ServiceID) o;
        return Objects.equals(serviceID, that.serviceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceID);
    }

    // toString
    @Override
    public String toString() {
        return "ServiceID{" +
                "serviceID='" + serviceID + '\'' +
                '}';
    }
}
