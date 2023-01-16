/**
 * The ExchangeRateList class contains exchange rates for different currencies.
 * It contains a rateList map, where the keys are the currency codes, and the values are the rates.
 * It also contains a currencies array list, which is populated from the keys of the rateList map.
 *
 * @developer Sofia Jia
 */
import java.util.ArrayList;
import java.util.Map;

public class ExchangeRateList {
    private Map<String, Double> rateList;
    private ArrayList<String> currencies = new ArrayList<>();

    //constructor
    public ExchangeRateList(Map<String,Double> rateList) {
        this.rateList = rateList;

        //add each currency to array list
        for (Map.Entry<String, Double> entry: rateList.entrySet()) {
            currencies.add(entry.getKey());
        }
    }

    //getters
    public Map<String, Double> getRateList() {return rateList;}
    public ArrayList<String> getCurrencies() {return currencies;}
}
