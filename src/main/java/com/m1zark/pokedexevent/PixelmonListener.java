package com.m1zark.pokedexevent;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BreedEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PixelmonListener {
    @SubscribeEvent
    public void onPokeDexUpdate(PokedexEvent event) {
        boolean legend = false;

        if(Config.legendaries) {
            if(EnumSpecies.legendaries.contains(event.pokemon.getSpecies().name()) || EnumSpecies.ultrabeasts.contains(event.pokemon.getSpecies().name())) {
                legend = true;
            }
        }

        if(!legend && !Config.whitelist.contains(event.pokemon.getSpecies().name())) {
            if (event.cause.equals(PokedexEvent.CAPTURE) && !Config.catching) event.setCanceled(true);
            if (event.cause.equals(PokedexEvent.EGG) && !Config.hatching) {
                if(event.pokemon.getPersistentData().getCompoundTag("ForgeData").hasKey("Breeder")) {
                    if (!event.pokemon.getPersistentData().getCompoundTag("ForgeData").getString("Breeder").equals(event.uuid.toString())) {
                        if(Config.ot) event.setCanceled(true);
                    }
                } else {
                    event.setCanceled(true);
                }
            }
            if (event.cause.equals(PokedexEvent.EVOLUTION) && !Config.evolution) event.setCanceled(true);
            if (event.cause.equals(PokedexEvent.TRADE_NPC) && !Config.npcTrade) event.setCanceled(true);
            if (event.cause.equals(PokedexEvent.TRADE_PLAYER) && !Config.playerTrade) event.setCanceled(true);
            if (event.cause.equals(PokedexEvent.STORAGE_MOVEMENT) && !Config.storage) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onEggCollect(BreedEvent.CollectEgg event) {
        if(Config.ot) {
            NBTTagCompound nbt = new NBTTagCompound();
            event.getEgg().writeToNBT(nbt);
            nbt.getCompoundTag("ForgeData").setString("Breeder", event.owner.toString());
            event.setEgg(Pixelmon.pokemonFactory.create(nbt));
        }
    }
}
