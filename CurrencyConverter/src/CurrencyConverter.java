import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyConverter {

    public static void main(String[] args) {
        System.out.println("1 Rupees");
        System.out.println("2 Dollars");
        System.out.println("3 Euros");
        System.out.println("4 Rands");

        // Take input
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the currency");
        int choice = sc.nextInt();
        System.out.println("Enter the amount");
        double amount = sc.nextDouble();

        // Convert the amount
        switch (choice) {
            case 1:
                displayExchangeRates("INR", amount);
                break;
            case 2:
                displayExchangeRates("USD", amount);
                break;
            case 3:
                displayExchangeRates("EUR", amount);
                break;
            case 4:
                displayExchangeRates("ZAR", amount);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

public static void displayExchangeRates(String fromCurrency, double amount) {
    try {
        String apiUrl = "https://v6.exchangerate-api.com/v6/90d06dc8c65faf5930e6dcd9/latest/USD";

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());

            if (jsonResponse.has("conversion_rates")) {
                JSONObject rates = jsonResponse.getJSONObject("conversion_rates");

                System.out.println("Exchange rates for " + amount + " " + fromCurrency + ":");

                double toDollarRate = rates.getDouble("USD");
                double toEuroRate = rates.getDouble("EUR");
                double toRandRate = rates.getDouble("ZAR");
                double toInrRate = rates.getDouble("INR");

                if (fromCurrency.equals("ZAR")) {
                    double amountInDollar = amount / toRandRate;
                    double amountInEuro = amountInDollar * toEuroRate;
                    double amountInInr = amountInDollar * toInrRate;

                    System.out.println(amount + " " + fromCurrency + " = " + amountInDollar + " USD");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInEuro + " EUR");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInInr + " INR");
                } else if (fromCurrency.equals("USD")) {
                    double amountInDollar = amount;
                    double amountInRand = amount * toRandRate;
                    double amountInEuro = amountInDollar * toEuroRate;
                    double amountInInr = amountInDollar * toInrRate;

                    System.out.println(amount + " " + fromCurrency + " = " + amountInRand + " ZAR");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInEuro + " EUR");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInInr + " INR");
                } else if (fromCurrency.equals("EUR")) {
                    double amountInDollar = amount * toDollarRate;
                    double amountInRand = amountInDollar * toRandRate;
                    double amountInInr = amountInDollar * toInrRate;

                    System.out.println(amount + " " + fromCurrency + " = " + amountInDollar + " USD");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInRand + " ZAR");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInInr + " INR");
                } else if (fromCurrency.equals("INR")) {
                    double amountInDollar = amount / toInrRate;
                    double amountInRand = amountInDollar * toRandRate;
                    double amountInEuro = amountInDollar * toEuroRate;

                    System.out.println(amount + " " + fromCurrency + " = " + amountInDollar + " USD");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInRand + " ZAR");
                    System.out.println(amount + " " + fromCurrency + " = " + amountInEuro + " EUR");
                } else {
                    System.err.println("Invalid choice of currency.");
                }
            } else {
                System.err.println("No 'conversion_rates' object in the API response.");
            }
        } else {
            System.err.println("Failed to retrieve data. HTTP Error: " + responseCode);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}


}
