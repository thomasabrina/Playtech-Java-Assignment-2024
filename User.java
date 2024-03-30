import java.math.BigDecimal;
public class User {
    private String userId;
    private String username;
    private BigDecimal balance;
    private String country;
    private int frozen;
    private BigDecimal depositMin;
    private BigDecimal depositMax;
    private BigDecimal withdrawMin;
    private BigDecimal withdrawMax;

    public User(String userId, String username, BigDecimal balance, String country, int frozen, BigDecimal depositMin, BigDecimal depositMax, BigDecimal withdrawMin, BigDecimal withdrawMax) {
        this.userId = userId;
        this.username = username;
        this.balance = balance;
        this.country = country;
        this.frozen = frozen;
        this.depositMin = depositMin;
        this.depositMax = depositMax;
        this.withdrawMin = withdrawMin;
        this.withdrawMax = withdrawMax;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public BigDecimal getBalance() { return balance; }
    public String getCountry() { return country; }
    public int getFrozen() { return frozen; }
    public BigDecimal getDepositMin() { return depositMin; }
    public BigDecimal getDepositMax() { return depositMax; }
    public BigDecimal getWithdrawMin() { return withdrawMin; }
    public BigDecimal getWithdrawMax() { return withdrawMax; }

    // Setters
    public void setUserId(String userId) { this.userId = userId; }
    public void setUsername(String username) { this.username = username; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setCountry(String country) { this.country = country; }
    public void setFrozen(int frozen) { this.frozen = frozen; }
    public void setDepositMin(BigDecimal depositMin) { this.depositMin = depositMin; }
    public void setDepositMax(BigDecimal depositMax) { this.depositMax = depositMax; }
    public void setWithdrawMin(BigDecimal withdrawMin) { this.withdrawMin = withdrawMin; }
    public void setWithdrawMax(BigDecimal withdrawMax) { this.withdrawMax = withdrawMax; }
}

