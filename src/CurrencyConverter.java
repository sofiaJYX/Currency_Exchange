/**
 * The CurrencyConverter class is a program that retrieves exchange rates from the Bank of Canada website and allows the user to convert between currencies and Canadian dollar.
 * The program uses Jsoup library to extract the HTML content of the website which contains the rates' info and store it in the ExchangeRateList class.
 *
 * @developer Sofia Jia
 * @contact yx881396@dal.ca
 */

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class CurrencyConverter {

    public static void main(String[] args) {
        //the website to get the rate info from
        final String url = "https://www.bankofcanada.ca/rates/exchange/daily-exchange-rates/";

        try {
            final Document document = Jsoup.connect(url).get();

            //select the table from website
            Element table = document.getElementsByTag("tbody").first();
            //generate a list based on rates
            ExchangeRateList exchangeRateList = new ExchangeRateList(readTable(table));

            Scanner in = new Scanner(System.in);
            printCommandMenu();

            //prompts user to enter command
            String input = in.nextLine();

            while (!input.equals("exit")) {
                if (input.equals("help")) {
                    helpCommand(exchangeRateList);
                }
                if (input.equals("convert")) {
                    convertCommand(exchangeRateList);
                }
                input = in.nextLine();
            }
        } catch (Exception e) {
            //handle any errors
            e.printStackTrace();
        }
    }


    /**
     * readTable reads the info in a table by rows to cells and return a map containing the rates
     * @param table a table element
     * @return Map element with currency(string) as key, rate(double) as value
     */
    static Map<String,Double> readTable(Element table) {
        Map<String, Double> rateList = new HashMap<>();
        Elements currencyElement, rates; Element rateElement = null;

        if (table == null) {
            System.out.println("Unable to read table as it is null.");
        } else {
            //select each row in the table
            for (Element row : table.select("tr")) {
                //select currency
                currencyElement = row.select("th");
                //select all rates
                rates = row.select("td");

                if (rates.size() > 0) {
                    //get the newest rate
                    rateElement = rates.get(rates.size()-1);
                }

                String currency = currencyElement.text();
                double rate = Double.parseDouble(rateElement.text());

                rateList.put(currency,rate);
            }
        }

        return rateList;
    }

    /**
     * converter calculate the currency to CAD based on given currency rate
     * @param rate double value representing currency rate
     * @param amount double value representing currency amount
     * @return double value of currency in CAD
     */
    static double converter(double rate, double amount) {
        return rate*amount;
    }

    /**
     * printCommandMenu prints available commands
     */
    static void printCommandMenu() {
        System.out.print("Welcome to currency converter!\n" +
                " - Type 'help' to see available currencies\n" +
                " - Type 'exit' to end the program\n" +
                " - Type 'convert' to covert currency\n" +
                "Tips: You can type in the digit code from 'help' command as your currency when converting.\n");
    }

    /**
     * helpCommand prints available currencies to convert from
     * @param exchangeRateList an ExchangeRateList object
     */
    static void helpCommand(ExchangeRateList exchangeRateList) {
        System.out.println("Sure! Here's our available currencies.");

        //iterate through each currency and print
        ArrayList currencies = exchangeRateList.getCurrencies();
        for (int i=0; i<currencies.size(); i++) {
            System.out.println(i + ") " + currencies.get(i));
        }
    }

    /**
     * convertCommand prompts the user to enter currency and an amount,
     * and outputs the converted value in Canadian dollars using converter method.
     * @param exchangeRateList an ExchangeRateList object
     */
    static void convertCommand(ExchangeRateList exchangeRateList) {
        Scanner in = new Scanner(System.in);

        //get currency
        String currency = "";
        System.out.println("Please type in the currency to convert to: (country code available in 'help' command)");

        //if user input the currency digit code
        if (in.hasNextInt()) {
            int index = Integer.parseInt(in.nextLine());

            //check if index is in valid range
            if (index < 23 && index > -1) {
                currency = exchangeRateList.getCurrencies().get(index);
            } else {
                System.out.println("Cannot find this currency:( Enter 'help' to see available currencies.");
                return;
            }
        }
        //if user input the currency
        else if (in.hasNext()) {
            String input = in.nextLine();

            //check if currency is in the exchange rate list
            if (exchangeRateList.getCurrencies().contains(input)) {
                currency = input;
            }
            else {
                System.out.println("Cannot find this currency:( Enter 'help' to see available currencies.");
                return;
            }
        }
        else {
            System.out.println("Cannot verify your input:( Type another command!");
            return;
        }

        //get amount
        int count = 0;
        while (count < 3) {
            System.out.println("Please enter the amount:");

            try {
                double amount = in.nextDouble();

                //output result
                System.out.println(amount + " " + currency + " in CAD is $" + converter(exchangeRateList.getRateList().get(currency), amount));
                break;
            } catch (InputMismatchException e) {
                count++;

                if (count < 3) {
                    System.out.println("Please enter a valid number. You have " + (3 - count) + " attempts left.");
                } else {
                    System.out.println("Cannot verify your input:( Type another command!");
                }

                in.next();
            }
        }
    }
}
