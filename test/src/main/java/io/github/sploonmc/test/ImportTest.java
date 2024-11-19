package io.github.sploonmc.test;

public class ImportTest {
    // Please don't run this as it will throw NoClassDefFoundErrors.
    // This solely exists to make sure that everything imports properly.
    // If anything does not import, something went wrong.
    public static void main(String[] args) {
        System.out.println(net.minecraft.server.MinecraftServer.class);
        System.out.println(net.md_5.bungee.chat.ComponentSerializer.class);
        System.out.println(org.bukkit.Bukkit.class);
        System.out.println(org.bukkit.craftbukkit.v1_21_R2.CraftServer.class);
        System.out.println(org.spigotmc.Metrics.class);
    }
}
