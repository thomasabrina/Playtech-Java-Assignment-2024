import java.math.BigDecimal;
public class Transaction {
    private String transactionId;
    private String userId;
    private String type; // DEPOSIT or WITHDRAW
    private BigDecimal amount;
    private String method; // CARD or TRANSFER
    private String accountNumber;

    public Transaction(String transactionId, String userId, String type, BigDecimal amount, String method, String accountNumber) {
        this.transactionId = transactionId;
        this.userId = userId;
        this.type = type;
        this.amount = amount;
        this.method = method;
        this.accountNumber = accountNumber;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getUserId() { return userId; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getMethod() { return method; }
    public String getAccountNumber() { return accountNumber; }

    // Setters
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setType(String type) { this.type = type; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setMethod(String method) { this.method = method; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
}
