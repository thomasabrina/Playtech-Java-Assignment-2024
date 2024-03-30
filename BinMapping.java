public class BinMapping {
    private String name;
    private String rangeFrom;
    private String rangeTo;
    private String type; // DC (Debit Card) or CC (Credit Card)
    private String country; // ISO 3166-1 alpha-3 country code

    // Constructor
    public BinMapping(String name, String rangeFrom, String rangeTo, String type, String country) {
        this.name = name;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.type = type;
        this.country = country;
    }

    // Getters
    public String getName() { return name; }
    public String getRangeFrom() { return rangeFrom; }
    public String getRangeTo() { return rangeTo; }
    public String getType() { return type; }
    public String getCountry() { return country; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setRangeFrom(String rangeFrom) { this.rangeFrom = rangeFrom; }
    public void setRangeTo(String rangeTo) { this.rangeTo = rangeTo; }
    public void setType(String type) { this.type = type; }
    public void setCountry(String country) { this.country = country; }
}
