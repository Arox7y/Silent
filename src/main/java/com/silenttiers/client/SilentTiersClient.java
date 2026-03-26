package com.silenttiers.client;

import com.silenttiers.client.command.SilentTiersCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

public class SilentTiersClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            SilentTiersCommand.register(dispatcher);
        });
    }
}
