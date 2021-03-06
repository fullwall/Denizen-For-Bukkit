package net.aufdemrand.denizen.utilities.entity;

import net.aufdemrand.denizen.objects.dEntity;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.EntityTracker;
import net.minecraft.server.v1_10_R1.EntityTrackerEntry;
import net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_10_R1.WorldServer;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


public class HideEntity {

    public static Map<UUID, Set<UUID>> hiddenEntities = new HashMap<UUID, Set<UUID>>();

    public static void hideEntity(Player player, Entity entity, boolean keepInTabList) {
        CraftPlayer craftPlayer = (CraftPlayer)player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        UUID playerUUID = player.getUniqueId();
        if (entityPlayer.playerConnection != null && !craftPlayer.equals(entity)) {
            if (!hiddenEntities.containsKey(playerUUID)) {
                hiddenEntities.put(playerUUID, new HashSet<UUID>());
            }
            Set hidden = hiddenEntities.get(playerUUID);
            UUID entityUUID = entity.getUniqueId();
            if (!hidden.contains(entityUUID)) {
                hidden.add(entityUUID);
                EntityTracker tracker = ((WorldServer)craftPlayer.getHandle().world).tracker;
                net.minecraft.server.v1_10_R1.Entity other = ((CraftEntity)entity).getHandle();
                EntityTrackerEntry entry = tracker.trackedEntities.get(other.getId());
                if (entry != null) {
                    entry.clear(entityPlayer);
                }
                if (dEntity.isPlayer(entity) && !keepInTabList) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, (EntityPlayer) other));
                }
            }
        }
    }

    public static void showEntity(Player player, Entity entity) {
        CraftPlayer craftPlayer = (CraftPlayer)player;
        EntityPlayer entityPlayer = craftPlayer.getHandle();
        UUID playerUUID = player.getUniqueId();
        if (entityPlayer.playerConnection != null && !craftPlayer.equals(entity) && hiddenEntities.containsKey(playerUUID)) {
            Set hidden = hiddenEntities.get(playerUUID);
            UUID entityUUID = entity.getUniqueId();
            if (hidden.contains(entityUUID)) {
                hidden.remove(entityUUID);
                EntityTracker tracker = ((WorldServer)craftPlayer.getHandle().world).tracker;
                net.minecraft.server.v1_10_R1.Entity other = ((CraftEntity)entity).getHandle();
                if (dEntity.isPlayer(entity)) {
                    entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, (EntityPlayer) other));
                }
                EntityTrackerEntry entry = tracker.trackedEntities.get(other.getId());
                if(entry != null && !entry.trackedPlayers.contains(entityPlayer)) {
                    entry.updatePlayer(entityPlayer);
                }
            }
        }
    }
}
