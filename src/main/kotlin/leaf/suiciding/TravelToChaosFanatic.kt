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
        if (Constants.AREA_ALTAR.contains(Players.local())) {
            var altarDoor = Objects.stream().within(20).id(1524, 1525).first()
            if (!altarDoor.valid())
                return

            if (altarDoor.id() == 1524) {
                if (!altarDoor.inViewport()) {
                    Camera.turnTo(altarDoor.tile)
                    Condition.wait({ altarDoor.inViewport() }, 50, 50)
                }

                altarDoor.bounds(-64, -56, -160, -16, -32, 32)
                // Short sleep between attempts of opening the door.
                if (!altarDoor.interact("Open") || !Condition.wait({ !altarDoor.valid() }, 50, 50))
                    return
            }

            while (!ScriptManager.isStopping() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
                && (SUICIDE_PATH.traverse() || SUICIDE_PATH.next() != SUICIDE_PATH.end())) {
                if (Constants.AREA_ALTAR.contains(Players.local())) {
                    altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                    if (!altarDoor.valid())
                        return

                    if (altarDoor.id() == 1524) {
                        if (!altarDoor.inViewport()) {
                            Camera.turnTo(altarDoor.tile)
                            Condition.wait({ altarDoor.inViewport() }, 50, 50)
                        }

                        altarDoor.bounds(-64, -56, -160, -16, -32, 32)
                        // Short sleep between attempts of opening the door.
                        if (!altarDoor.interact("Open") || !Condition.wait({
                                !altarDoor.valid() }, 50, 50))
                            return
                    }
                }

                Condition.sleep(50)
            }

            Condition.wait({ !Players.local().inMotion() }, 50, 50)
            if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
                return

            if (Constants.AREA_ALTAR.contains(Players.local())) {
                script.severe("Failed to exit the altar.")
                return
            }
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