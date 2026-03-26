package com.silenttiers.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.silenttiers.client.api.TierAPI;
import com.silenttiers.client.api.TierData;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public class SilentTiersCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("silenttiers")
            .then(literal("lookup")
                .then(argument("player", com.mojang.brigadier.arguments.StringArgumentType.word())
                    .executes(ctx -> {
                        String ign = com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "player");
                        TierAPI.fetchTierAsync(ign);
                        TierData data = TierAPI.getTier(ign);
                        if (data != null && data.found) {
                            ctx.getSource().sendFeedback(Text.literal("§6" + data.ign + "§r: §e" + data.tier));
                        } else {
                            ctx.getSource().sendFeedback(Text.literal("§cFetching... try again in a moment."));
                        }
                        return 1;
                    }))));

        dispatcher.register(literal("st")
            .then(literal("lookup")
                .then(argument("player", com.mojang.brigadier.arguments.StringArgumentType.word())
                    .executes(ctx -> {
                        String ign = com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "player");
                        TierAPI.fetchTierAsync(ign);
                        TierData data = TierAPI.getTier(ign);
                        if (data != null && data.found) {
                            ctx.getSource().sendFeedback(Text.literal("§6" + data.ign + "§r: §e" + data.tier));
                        } else {
                            ctx.getSource().sendFeedback(Text.literal("§cFetching... try again in a moment."));
                        }
                        return 1;
                    }))));
    }
}
