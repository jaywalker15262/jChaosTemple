package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf

class Suicide(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Suiciding") {
    override fun execute() {
        val chaosFanatic = Npcs.stream().within(13).name("Chaos fanatic")
            .filtered { it.inViewport() }.first()
        if (chaosFanatic.valid() && !chaosFanatic.healthBarVisible()) {
            chaosFanatic.bounds(-16, 16, -16, -16, -16, 16)
            if (chaosFanatic.interact("Attack") || chaosFanatic.click())
                Condition.wait({ (Players.local().healthBarVisible() && chaosFanatic.healthBarVisible())
                        || Constants.AREA_LUMBY.contains(Players.local()) }, 50, 90)
        }
    }
}