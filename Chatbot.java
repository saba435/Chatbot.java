
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ChatBot {

    private static final String WEATHER_API_KEY = "037f3417c5587e20f819c4b724d22d08";
    private static final String EXCHANGE_RATE_API_KEY = "cbfc42f5e84bf31620248c70";

    public static void main(String[] args) {
        ChatBot bot = new ChatBot();

        // Test the weather API
        String city = "Tbilisi";
        System.out.println("Current weather in " + city + ":");
        System.out.println(bot.getCurrentWeather(city));

        // Test the exchange rate API
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        System.out.println("\nExchange rate from " + baseCurrency + " to " + targetCurrency + ":");
        System.out.println(bot.getExchangeRate(baseCurrency, targetCurrency));
    }

    public String getCurrentWeather(String city) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + WEATHER_API_KEY + "&units=metric";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return parseWeatherResponse(response.toString());
            } else {
                return "GET request failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    private String parseWeatherResponse(String response) {
        // A simple parsing to extract relevant information, such as temperature and weather description.
        // For a more robust solution, consider using a JSON parsing library like org.json or Gson.
        String temp = response.split("\"temp\":")[1].split(",")[0];
        String description = response.split("\"description\":\"")[1].split("\"")[0];
        return "Temperature: " + temp + "°C, Weather: " + description;
    }

    public String getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            String urlString = "https://v6.exchangerate-api.com/v6/" + EXCHANGE_RATE_API_KEY + "/latest/" + baseCurrency;
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return parseExchangeRateResponse(response.toString(), targetCurrency);
            } else {
                return "GET request failed";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    private String parseExchangeRateResponse(String response, String targetCurrency) {
        // A simple parsing to extract the exchange rate.
        // For a more robust solution, consider using a JSON parsing library like org.json or Gson.
        String rate = response.split("\"" + targetCurrency + "\":")[1].split(",")[0];
        return "Exchange rate: 1 USD = " + rate + " " + targetCurrency;
    }
}
