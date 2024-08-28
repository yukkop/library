package org.example;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.block.Block;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Point;
import net.minestom.server.instance.generator.GenerationUnit;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        // Initialization
        MinecraftServer minecraftServer = MinecraftServer.init();

        // Create the instance
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instanceContainer = instanceManager.createInstanceContainer();

        // Set the ChunkGenerator
        instanceContainer.setGenerator(unit -> {
          unit.modifier().fillHeight(0, 1, Block.CHISELED_BOOKSHELF);
          unit.modifier().fillHeight(9, 10, Block.CHISELED_BOOKSHELF);
        });
	instanceContainer.setGenerator(unit -> {
            Random random = new Random();
            Point start = unit.absoluteStart();
        
            // Create a snow carpet for the snowmen
            unit.modifier().fillHeight(-64, -60, Block.SNOW);
        
            // Exit out if unit is not the bottom unit, and exit 5 in 6 times otherwise
            if (start.y() > -64 || random.nextInt(6) != 0) {
                return;
            }
        
            // Let's fork this section to add our tall snowman.
            // We add two extra sections worth of space to this fork to fit the snowman.
            GenerationUnit fork = unit.fork(start, start.add(16, 32, 16));
        
            // Now we add the snowman to the fork
            fork.modifier().fill(start, start.add(5, 19, 5), Block.POWDER_SNOW);
            fork.modifier().setBlock(start.add(2, 19, 2), Block.JACK_O_LANTERN);
        });

        instanceContainer.setBlock(0, 2, 0, Block.TNT);

        // Add an event callback to specify the spawning instance (and the spawn position)
        GlobalEventHandler globalEventHandler = MinecraftServer.getGlobalEventHandler();
        globalEventHandler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            final Player player = event.getPlayer();
            event.setSpawningInstance(instanceContainer);
            player.setRespawnPoint(new Pos(0, 3, 0));
        });

        // Start the server on port 25565
        minecraftServer.start("0.0.0.0", 25565);
    }
}
