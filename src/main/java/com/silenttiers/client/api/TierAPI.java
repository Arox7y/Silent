package com.silenttiers.client.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CompletableFuture;

public class TierAPI {

    private static final String SILENT_TIERS_URL = "http://51.68.34.78:20219/api/player/";
    private static final String MCTIERS_URL = "https://mctiers.com/api/player/";

    private static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();

    private static final Map<String, CachedTier> cache = new ConcurrentHashMap<>();
    private static final long CACHE_DURATION = 5 * 60 * 1000;

    private record CachedTier(TierData data, long fetchedAt) {}

    public static TierData getTier(String ign) {
        String key = ign.toLowerCase();
        CachedTier cached = cache.get(key);
        if (cached != null && System.currentTimeMillis() - cached.fetchedAt < CACHE_DURATION) {
            return cached.data;
        }
        return null;
    }

    public static void fetchTierAsync(String ign) {
        String key = ign.toLowerCase();
        CachedTier cached = cache.get(key);
        if (cached != null && System.currentTimeMillis() - cached.fetchedAt < CACHE_DURATION) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            TierData data = fetchFromSilentTiers(ign);
            if (data == null || !data.found) {
                TierData mcData = fetchFromMCTiers(ign);
                if (mcData != null && mcData.found) data = mcData;
            }
            if (data == null) { data = new TierData(); data.ign = ign; }
            cache.put(key, new CachedTier(data, System.currentTimeMillis()));
        });
    }

    private static TierData fetchFromSilentTiers(String ign) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SILENT_TIERS_URL + ign))
                    .timeout(Duration.ofSeconds(3)).GET().build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            TierData data = new TierData();
            data.found = json.has("found") && json.get("found").getAsBoolean();
            if (data.found) {
                data.ign = json.has("ign") ? json.get("ign").getAsString() : ign;
                data.tier = json.has("tier") ? json.get("tier").getAsString() : "Unranked";
                data.previousTier = json.has("previousTier") ? json.get("previousTier").getAsString() : "Unranked";
                data.peakTier = json.has("peakTier") ? json.get("peakTier").getAsString() : data.tier;
                data.tester = json.has("tester") ? json.get("tester").getAsString() : "Unknown";
                data.region = json.has("region") ? json.get("region").getAsString() : "EU";
            }
            return data;
        } catch (Exception e) { return null; }
    }

    private static TierData fetchFromMCTiers(String ign) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(MCTIERS_URL + ign))
                    .timeout(Duration.ofSeconds(3)).GET().build();
            HttpResponse<String> response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) return null;
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            TierData data = new TierData();
            if (json.has("rankings")) {
                JsonObject rankings = json.getAsJsonObject("rankings");
                if (rankings.has("crystal")) {
                    JsonObject crystal = rankings.getAsJsonObject("crystal");
                    data.found = true; data.ign = ign;
                    if (crystal.has("tier")) {
                        int t = crystal.get("tier").getAsInt();
                        boolean h = crystal.has("high") && crystal.get("high").getAsBoolean();
                        data.tier = (h ? "HT" : "LT") + t;
                    }
                    data.region = crystal.has("region") ? crystal.get("region").getAsString().toUpperCase() : "EU";
                }
            }
            return data;
        } catch (Exception e) { return null; }
    }

    public static void clearCache() { cache.clear(); }
    public static void clearCache(String ign) { cache.remove(ign.toLowerCase()); }
}
