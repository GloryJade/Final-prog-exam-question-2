package za.co.mattressgui;

public class ProductCalculations implements IProductCalculations {
    private double price;

    public ProductCalculations(double price) {
        this.price = price;
    }

    @Override
    public double GetVat() {
        return price * 0.15;
    }

    @Override
    public double GetCommision() {
        return price * 0.05;
    }

    @Override
    public double GetTotal() {
        return price + GetVat() + GetCommision();
    }
}
