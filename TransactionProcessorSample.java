import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


// This template shows input parameters format.
// It is otherwise not mandatory to use, you can write everything from scratch if you wish.
public class TransactionProcessorSample {

    public static void main(final String[] args) throws IOException {
        List<User> users = TransactionProcessorSample.readUsers(Paths.get(args[0]));
        List<Transaction> transactions = TransactionProcessorSample.readTransactions(Paths.get(args[1]));
        List<BinMapping> binMappings = TransactionProcessorSample.readBinMappings(Paths.get(args[2]));

        List<Event> events = TransactionProcessorSample.processTransactions(users, transactions, binMappings);

        TransactionProcessorSample.writeBalances(Paths.get(args[3]), users);
        TransactionProcessorSample.writeEvents(Paths.get(args[4]), events);
    }

    private static List<User> readUsers(final Path filePath) throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                User user = new User(
                    values[0], // userId
                    values[1], // username
                    new BigDecimal(values[2]), // balance
                    values[3], // country
                    Integer.parseInt(values[4]), // frozen
                    new BigDecimal(values[5]), // depositMin
                    new BigDecimal(values[6]), // depositMax
                    new BigDecimal(values[7]), // withdrawMin
                    new BigDecimal(values[8])  // withdrawMax
                );
                users.add(user);
            }
        }
        return users;
    }

    private static List<Transaction> readTransactions(final Path filePath) throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String transactionId = values[0];
                String userId = values[1];
                String type = values[2];
                BigDecimal amount = new BigDecimal(values[3]);
                String method = values[4];
                String accountNumber = values[5];
                
                Transaction transaction = new Transaction(transactionId, userId, type, amount, method, accountNumber);
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private static List<BinMapping> readBinMappings(final Path filePath) throws IOException {
        List<BinMapping> binMappings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            // Skip the header
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                BinMapping binMapping = new BinMapping(
                    values[0], // name
                    values[1], // range_from
                    values[2], // range_to
                    values[3], // type
                    values[4]  // country
                );
                binMappings.add(binMapping);
            }
        }
        return binMappings;
    }

    private static String convertCountryCodeAlpha2ToAlpha3(String alpha2Code) {
        Map<String, String> countryCodeMap = new HashMap<>();
        // Populate the map with some example country codes
        countryCodeMap.put("US", "USA");
        countryCodeMap.put("GB", "GBR");
        countryCodeMap.put("DE", "DEU");
        countryCodeMap.put("CA", "CAN");
        countryCodeMap.put("FR", "FRA");
        countryCodeMap.put("JP", "JPN");
    
        // Return the alpha-3 code, or null if not found
        return countryCodeMap.getOrDefault(alpha2Code, null);
    }

    private static List<Event> processTransactions(final List<User> users, final List<Transaction> transactions, final List<BinMapping> binMappings) {
        List<Event> events = new ArrayList<>();
        Set<String> uniqueTransactionIds = new HashSet<>();
        Map<String, String> accountUserMap = new HashMap<>(); // Maps account numbers to userIds for checking exclusive use.

        for (Transaction transaction : transactions) {
            Event event = new Event(transaction.getTransactionId(), Event.STATUS_DECLINED, "General decline"); // Default assumption

            // Check for unique transaction ID
            if (!uniqueTransactionIds.add(transaction.getTransactionId())) {
                event.setMessage("Duplicate transaction ID");
                events.add(event);
                continue;
            }

            // Verify user exists and is not frozen
            User user = users.stream().filter(u -> u.getUserId().equals(transaction.getUserId())).findFirst().orElse(null);
            if (user == null || user.getFrozen() == 1) {
                event.setMessage("User not found or account is frozen");
                events.add(event);
                continue;
            }

            // Validate payment method
            if (!transaction.getMethod().equals("CARD") && !transaction.getMethod().equals("TRANSFER")) {
                event.setMessage("Invalid payment method");
                events.add(event);
                continue;
            }

            // For CARD payments, validate card details 
            if (transaction.getMethod().equals("CARD")) {
                boolean cardValid = false; // Assume card is invalid initially
                String cardNumber = transaction.getAccountNumber();
                String cardBin = cardNumber.substring(0, 10); // Extract the BIN (first 10 digits)
            
                // Attempt to find a matching BinMapping
                for (BinMapping binMapping : binMappings) {
                    if (new BigDecimal(cardBin).compareTo(new BigDecimal(binMapping.getRangeFrom())) >= 0 &&
                        new BigDecimal(cardBin).compareTo(new BigDecimal(binMapping.getRangeTo())) <= 0 &&
                        binMapping.getType().equals("DC")) { // Ensure it's a debit card
            
                        // Convert user's two-letter country code to three-letter code (this requires a conversion method)
                        String userCountryCodeAlpha3 = convertCountryCodeAlpha2ToAlpha3(user.getCountry());
            
                        if (binMapping.getCountry().equals(userCountryCodeAlpha3)) {
                            cardValid = true; // Card is valid if all conditions are met
                            break;
                        }
                    }
                }
            
                if (!cardValid) {
                    event.setMessage("Invalid card details");
                    events.add(event);
                    continue;
                }
            }

            // For TRANSFER payments, validate IBAN 
            if (transaction.getMethod().equals("TRANSFER")) {
                String iban = transaction.getAccountNumber();
                boolean ibanValid = false; // Assume invalid until proven valid
            
                if (iban.length() >= 15 && iban.length() <= 34) { // Basic length check for IBAN
                    String ibanCountryCode = iban.substring(0, 2);
                    if (ibanCountryCode.equals(user.getCountry())) {
                        ibanValid = true;
                    }
                }
            
                if (!ibanValid) {
                    event.setMessage("Invalid IBAN details");
                    events.add(event);
                    continue;
                }
            }

            // Check if the account number has been used by another user
            if (accountUserMap.containsKey(transaction.getAccountNumber()) && !accountUserMap.get(transaction.getAccountNumber()).equals(transaction.getUserId())) {
                event.setMessage("Account number already used by another user");
                events.add(event);
                continue;
            } else {
                accountUserMap.put(transaction.getAccountNumber(), transaction.getUserId());
            }

            // Validate transaction amount and limits
            BigDecimal amount = transaction.getAmount();
            if (amount.compareTo(BigDecimal.ZERO) <= 0 || 
                (transaction.getType().equals("DEPOSIT") && (amount.compareTo(user.getDepositMin()) < 0 || amount.compareTo(user.getDepositMax()) > 0)) ||
                (transaction.getType().equals("WITHDRAW") && (amount.compareTo(user.getWithdrawMin()) < 0 || amount.compareTo(user.getWithdrawMax()) > 0))) {
                event.setMessage("Transaction amount out of bounds");
                events.add(event);
                continue;
            }

            // For withdrawals, ensure sufficient balance
            if (transaction.getType().equals("WITHDRAW") && user.getBalance().compareTo(amount) < 0) {
                event.setMessage("Insufficient balance");
                events.add(event);
                continue;
            }

            // All checks passed, process transaction
            if (transaction.getType().equals("DEPOSIT")) {
                user.setBalance(user.getBalance().add(amount));
            } else if (transaction.getType().equals("WITHDRAW")) {
                user.setBalance(user.getBalance().subtract(amount));
            }

            event.setStatus(Event.STATUS_APPROVED);
            event.setMessage("OK");
            events.add(event);
        }

        return events;
    }

    private static void writeBalances(final Path filePath, final List<User> users) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()))) {
            writer.write("user_id,balance\n"); // Header
            for (User user : users) {
                writer.write(user.getUserId() + "," + user.getBalance().toPlainString() + "\n");
            }
        }
    }

    private static void writeEvents(final Path filePath, final List<Event> events) throws IOException {
        try (final FileWriter writer = new FileWriter(filePath.toFile(), false)) {
            writer.append("transaction_id,status,message\n");
            for (final var event : events) {
                writer.append(event.getTransactionId())
      .append(",")
      .append(event.getStatus())
      .append(",")
      .append(event.getMessage())
      .append("\n");
            }
        }
    }
}

