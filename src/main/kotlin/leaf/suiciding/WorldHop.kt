package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.World
import org.powbot.api.rt4.Worlds
import org.powbot.api.script.tree.Leaf

class WorldHop(script: ChaosTemple) : Leaf<ChaosTemple>(script, "World-hopping") {
    override fun execute() {
        val worlds = Worlds.stream().filtered { it.number != Variables.worldId && it.type == World.Type.MEMBERS
                && !Constants.WORLD_SPECIALITY_FILTER.contains(it.specialty) && it.population > 0 && it.population < 990 }
        if (worlds.isEmpty()) {
            script.info("Failed to find a list of worlds to hop to.")
            return
        }

        val newWorld = worlds.list().random()
        if (!newWorld.valid()) {
            script.info("Failed to find a valid world in our list.")
            return
        }

        if (!newWorld.hop() && !Condition.wait({ Worlds.current().number == newWorld.number }, 50, 10)) {
            script.info("Failed to attempt to hop to our new world.")
            return
        }

        if (!Condition.wait({ Worlds.current().number == newWorld.number || Players.local().healthBarVisible() }, 50, 200)) {
            script.info("Failed to find that we had hopped to our new world.")
            return
        }

        // Wait a little right after hopping.
        Variables.worldId = newWorld.number
        Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
    }
}