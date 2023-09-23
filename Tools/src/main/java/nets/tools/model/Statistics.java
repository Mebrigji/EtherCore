package nets.tools.model;

public interface Statistics {

    double getTraveledDistance();
    void setTraveledDistance(double distance);

    long getTotalTimeSpend();
    void setTotalTimeSpend(long timeSpend);

    long getBlocksMined();
    void setBlocksMined(long blocksMined);

    long getBlocksPlaced();
    void setBlocksPlaced(long blocksPlaced);

    double getDamageGiven();
    void setDamageGiven(double damageGiven);

    double getDamageTaken();
    void setDamageTaken(double damageTaken);

    long getKilledEntities();
    void setKilledEntities(long entityCount);

    long getKilledPlayers();
    void setKilledPlayers(long playerCount);

    long getLogoutEscapes();
    void setLogoutEscapes(long escapes);

    double getRegeneratedHealth();
    void setRegeneratedHealth(double regeneratedHealth);

    long getLickedEnchantedGoldenApples();
    void setLickedEnchantedGoldenApples(long lickedEnchantedGoldenApples);


}
