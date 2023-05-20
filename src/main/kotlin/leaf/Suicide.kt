package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.suicidePath
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.dax.api.DaxWalker
import org.powbot.mobile.script.ScriptManager

class Suicide(script: Script) : Leaf<Script>(script, "Suiciding") {
    override fun execute() {
        if (Constants.AREA_ALTAR.contains(Players.local())) {
            var altarDoor = Objects.stream().within(20).id(1524, 1525).first()
            for (n in 1..20) {
                if (altarDoor.valid())
                    break

                Condition.sleep(50)
                altarDoor = Objects.stream().within(20).id(1524, 1525).first()
            }

            if (!altarDoor.valid())
                return
            else if (altarDoor.id() == 1524) {
                for (n in 1..10) {
                    if (altarDoor.inViewport())
                        break

                    Camera.turnTo(altarDoor.tile)
                    Condition.wait({ altarDoor.inViewport() }, 50, 25)
                }

                if (!altarDoor.inViewport())
                    return

                altarDoor.bounds(-64, -56, -160, -16, -32, 32)
                for (n in 1..20) {
                    if (!altarDoor.valid() || (altarDoor.interact("Open")
                                && Condition.wait({ !altarDoor.valid() }, 50, 50)))
                        break

                    Condition.sleep(50)
                }

                if (altarDoor.valid())
                    return
            }

            while (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && (suicidePath.traverse() || suicidePath.next() != suicidePath.end())) {
                if (Constants.AREA_ALTAR.contains(Players.local())) {
                    altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                    for (i in 1..20) {
                        if (altarDoor.valid())
                            break

                        Condition.sleep(50)
                        altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                    }

                    if (!altarDoor.valid())
                        return
                    else if (altarDoor.id() == 1524) {
                        altarDoor.bounds(-64, -56, -160, -16, -32, 32)
                        for (i in 1..20) {
                            if (!altarDoor.valid())
                                break
                            else if (altarDoor.interact("Open")) {
                                if (Players.local().distanceTo(altarDoor) < 3) {
                                    if (Condition.wait({ !altarDoor.valid() }, 50, 50))
                                        break
                                } else if (Condition.wait({ !altarDoor.valid() }, 50, 120))
                                    break
                            }

                            Condition.sleep(50)
                        }

                        if (altarDoor.valid())
                            return
                    }
                }

                Condition.sleep(50)
            }

            Condition.wait({ !Players.local().inMotion() }, 50, 50)
            if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
                return
            else if (Constants.AREA_ALTAR.contains(Players.local())) {
                LoggingService.severe("Failed to exit the altar.")
                return
            }
            else if (Constants.suicideTileMatrix.valid()) {
                for (n in 1..10) {
                    if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local())
                        || Players.local().distanceTo(Constants.suicideTile) < 7)
                        break
                    else if (!Players.local().inMotion()) {
                        if (!Constants.suicideTileMatrix.inViewport() || Constants.suicideTileMatrix.interact("Walk here")
                            && Condition.wait({ Skills.level(Skill.Hitpoints) == 0
                                    || Constants.AREA_LUMBY.contains(Players.local()) || Players.local().inMotion() }, 50 , 50))
                            break
                    }

                    Condition.sleep(50)
                }
            }

            if (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.suicideTile) > 6) {
                LoggingService.severe("Failed to get to the suicide tile.")
                ScriptManager.stop()
                return
            }
        }
        else if (Players.local().distanceTo(Constants.suicideTile) > 6) {
            val generatePath = DaxWalker.getPath(Constants.suicideTile)
            if (generatePath.isNullOrEmpty())
                return

            val escapePath = TilePath(generatePath.toTypedArray())
            if (!escapePath.valid())
                return
            else while (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && (escapePath.traverse() || escapePath.next() != escapePath.end()))
                Condition.sleep(50)

            Condition.wait({ !Players.local().inMotion() }, 50, 50)
            if (Constants.suicideTileMatrix.valid()) {
                for (n in 1..10) {
                    if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local())
                        || Players.local().distanceTo(Constants.suicideTile) < 7)
                        break
                    else if (!Players.local().inMotion()) {
                        if (!Constants.suicideTileMatrix.inViewport() || Constants.suicideTileMatrix.interact("Walk here")
                            && Condition.wait({ Skills.level(Skill.Hitpoints) == 0
                                    || Constants.AREA_LUMBY.contains(Players.local()) || Players.local().inMotion() }, 50 , 50))
                            break
                    }

                    Condition.sleep(50)
                }
            }

            if (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.suicideTile) > 6) {
                LoggingService.severe("Failed to get to the suicide tile 2.")
                ScriptManager.stop()
                return
            }
        }

        // Some users might use accounts with high stats that are really tanky, and so we should wait at least a full minute.
        if (!Condition.wait({ Constants.AREA_LUMBY.contains(Players.local()) }, 50 ,1200)) {
            LoggingService.severe("Failed to suicide.")
            ScriptManager.stop()
        }
    }
}