package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.SUICIDE_PATH
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.dax.api.DaxWalker
import org.powbot.mobile.script.ScriptManager

class TravelToChaosFanatic(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Traveling To Suicide Area") {
    override fun execute() {
        // Hardcoded path
        if (Players.local().distanceTo(SUICIDE_PATH.next()).toInt() > 8) {
            if (Constants.AREA_ALTAR.contains(Players.local())) {
                val altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                if (!altarDoor.valid())
                    return

                if (altarDoor.id() == 1524) {
                    if (!altarDoor.inViewport()) {
                        Camera.turnTo(altarDoor.tile)
                        Condition.wait({ altarDoor.inViewport() }, 50, 50)
                    }

                    altarDoor.bounds(-54, -46, -160, -16, -32, 32)
                    // Short sleep between attempts of opening the door.
                    if (!altarDoor.interact("Open") || !Condition.wait({ !altarDoor.valid() }, 50, 50))
                        return
                }
            }

            SUICIDE_PATH.traverse()
            if (Condition.wait({ Players.local().inMotion() || Players.local().distanceTo(SUICIDE_PATH.end()) > 5 }, 50, 50))
                return

            if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
                return
        }
        // DaxWalker-generated path
        else if (Players.local().distanceTo(Constants.SUICIDE_TILE).toInt() > 5) {
            val generatePath = DaxWalker.getPath(Constants.SUICIDE_TILE)
            if (generatePath.isNullOrEmpty())
                return

            val escapePath = TilePath(generatePath.toTypedArray())
            if (!escapePath.valid())
                return

            while (!ScriptManager.isStopping() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
                && (escapePath.traverse() || escapePath.next() != escapePath.end()))
                Condition.sleep(50)
        }


        if (!Variables.suicideTileMatrix.valid())
            Variables.suicideTileMatrix = Constants.ALTAR_TILE.matrix()

        // Screen-walking in case web-walking fails.
        if (!Players.local().inMotion() && Players.local().distanceTo(Constants.SUICIDE_TILE).toInt() > 5
            && Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
            && (!Players.local().inCombat() || Players.stream().interactingWithMe().isNotEmpty())
            && Variables.suicideTileMatrix.valid() && Variables.suicideTileMatrix.inViewport()
            && Variables.suicideTileMatrix.interact("Walk here")) {
                Condition.wait({ Players.local().inMotion() || Skills.level(Skill.Hitpoints) == 0
                        || Constants.AREA_LUMBY.contains(Players.local()) }, 50 , 50)
        }

        if (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
            && !Players.local().inCombat() && Players.local().distanceTo(Constants.SUICIDE_TILE).toInt() > 5)
            script.severe("Failed to get to the suicide tile.")
    }
}