package com.silenttiers.client.api;

public class TierData {
    public boolean found;
    public String ign;
    public String tier;
    public String previousTier;
    public String peakTier;
    public String tester;
    public String region;

    public TierData() {
        this.found = false;
        this.ign = "";
        this.tier = "Unranked";
        this.previousTier = "Unranked";
        this.peakTier = "Unranked";
        this.tester = "Unknown";
        this.region = "EU";
    }

    public String getDisplayTier() {
        return switch (tier) {
            case "LT5" -> "Low Tier 5";
            case "HT5" -> "High Tier 5";
            case "LT4" -> "Low Tier 4";
            case "HT4" -> "High Tier 4";
            case "LT3" -> "Low Tier 3";
            case "HT3" -> "High Tier 3";
            case "LT2" -> "Low Tier 2";
            case "HT2" -> "High Tier 2";
            case "LT1" -> "Low Tier 1";
            case "HT1" -> "High Tier 1";
            default -> "Unranked";
        };
    }

    public String getShortTier() {
        return tier != null ? tier : "?";
    }

    public int getTierColor() {
        return switch (tier) {
            case "HT1", "LT1" -> 0xFF5555;
            case "HT2", "LT2" -> 0xFF8C00;
            case "HT3", "LT3" -> 0xFFAA00;
            case "HT4", "LT4" -> 0x55FF55;
            case "HT5", "LT5" -> 0x55FFFF;
            default -> 0xAAAAAA;
        };
    }
}
