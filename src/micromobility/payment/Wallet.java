package micromobility.payment;

import exceptions.NotEnoughWalletException;

import java.math.BigDecimal;

public class Wallet {

    private WalletPayment walletPayment; // Asociación con WalletPayment
    private BigDecimal balance; // Saldo de la billetera

    // Constructor para inicializar Wallet con un WalletPayment y saldo en 0
    public Wallet(WalletPayment walletPayment) {
        this.walletPayment = walletPayment;
        this.balance = BigDecimal.ZERO; // Inicia con saldo 0
    }

    public void deduct(BigDecimal imp) throws NotEnoughWalletException {
        if (balance.compareTo(imp) < 0) { // balance < amount
            throw new NotEnoughWalletException("No hay saldo suficiente para deducir el importe.");
        }
        balance = balance.subtract(imp);
        System.out.println("Importedel trayecto: " + imp);
        System.out.println("Saldo despues de cobro: " + balance);
    }

    public BigDecimal getBalance() {
        return balance;
    }

}
