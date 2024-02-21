package nets.tools.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import lombok.Getter;
import org.bson.Document;

public class MongodbConnector {

    @Getter
    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;

    private MongoDatabase walletDatabase;

    private String walletId;

    public MongodbConnector(String URI, String table, String walletId){
        this.mongoClient = new MongoClient(new MongoClientURI(URI));
        this.mongoDatabase = mongoClient.getDatabase(table);
        this.walletDatabase = mongoClient.getDatabase("Wallet");
        this.walletId = walletId;
    }

    /*
        Wallet
     */

    public MongoCollection<Document> getMongoHistoryCollection() {
        return walletDatabase.getCollection("History");
    }


    public double getBalance(String playerName){
        Document document = walletDatabase.getCollection("Players").find(Filters.eq("playerName", playerName)).limit(1).first();
        return document == null ? 0 : document.getDouble("balance");
    }

    public double addBalance(String playerName, double amount){
        return walletDatabase.getCollection("Players")
                .findOneAndUpdate(Filters.eq("playerName", playerName), Updates.inc("balance", amount), new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER))
                .getDouble("balance");
    }

    public double removeBalance(String playerName, double amount){
        return walletDatabase.getCollection("Players")
                .findOneAndUpdate(Filters.eq("playerName", playerName), Updates.inc("balance", amount > 0 ? amount * -1 : amount), new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.AFTER))
                .getDouble("balance");
    }

    public void setBalance(String playerName, double amount){
        walletDatabase.getCollection("Players")
                .findOneAndUpdate(Filters.eq("playerName", playerName), Updates.set("balance", amount), new FindOneAndUpdateOptions().upsert(true));
    }

    public boolean transferBalance(String from, String to, double amount){
        if(getBalance(from) >= amount){
            removeBalance(from, amount);
            addBalance(to, amount);
            addToHistory(to, amount, from, HistoryType.TRANSFER);
            return true;
        } else return false;
    }

    public void addToHistory(String playerName, double money, String by, HistoryType historyType){
        Document document = new Document();
        document.append("server", walletId);
        if(historyType.equals(HistoryType.RESET)){
            document.append("playerName", playerName);
            document.append("action", historyType.getPrefix());
        } else {
            if(!playerName.equals(by)) document.append("by", by);
            document.append("playerName", playerName);
            document.append("action", historyType.getPrefix());
            document.append("value", money);
        }
        getMongoHistoryCollection().insertOne(document);
    }

    public void addToHistory(String nickName, double money, HistoryType historyType){
        addToHistory(nickName, money, nickName, historyType);
    }

    public void disconnect(){
        mongoClient.close();
    }


    public enum HistoryType {

        ADD("ADD"),
        REMOVE("REMOVE"),
        SET("SET"),
        TRANSFER("TRANSFER"),
        RESET("%RESET%");

        private final String prefix;

        HistoryType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }
}
