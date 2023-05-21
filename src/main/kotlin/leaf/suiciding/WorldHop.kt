package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.World
import org.powbot.api.rt4.Worlds
import org.powbot.api.script.tree.Leaf

class WorldHop(script: Script) : Leaf<Script>(script, "World-hopping") {
    override fun execute() {
        var worlds = Worlds.stream().filtered { it.number != Constants.worldId && it.type == World.Type.MEMBERS
                && !Constants.worldSpecialtyFilter.contains(it.specialty) && it.population > 0 && it.population < 990 }
        for (n in 1..10) {
            if (worlds.isNotEmpty())
                break
            Condition.sleep(50)
            worlds = Worlds.stream().filtered { it.number != Constants.worldId && it.type == World.Type.MEMBERS
                    && !Constants.worldSpecialtyFilter.contains(it.specialty) && it.population > 0 && it.population < 990 }
        }

        if (worlds.isEmpty()) {
            LoggingService.info("Failed to find a list of worlds to hop to.")
            return
        }

        val newWorld = worlds.list().random()
        if (!newWorld.valid()) {
            LoggingService.info("Failed to find a valid world in our list.")
            return
        }

        Constants.worldId = newWorld.number
        if (!newWorld.hop()) {
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            if (!newWorld.hop()) {
                LoggingService.info("Failed to attempt to hop to our new world.")
                return
            }
        }

        if (!Condition.wait({ Worlds.current().number == Constants.worldId || Players.local().healthBarVisible() }, 50, 200)) {
            LoggingService.info("JayLOGS: Failed to find that we had hopped to our new world.")
            return
        }

        // Wait a little right after hopping.
        Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
    }
}