package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.SUICIDE_PATH
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.dax.api.DaxWalker
import org.powbot.mobile.script.ScriptManager

class TravelToChaosFanatic(script: Script) : Leaf<Script>(script, "Traveling To Suicide Area") {
    override fun execute() {
        if (Constants.AREA_ALTAR.contains(Players.local())) {
            var altarDoor = Objects.stream().within(20).id(1524, 1525).first()
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

            while (!ScriptManager.isStopping() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
                && (SUICIDE_PATH.traverse() || SUICIDE_PATH.next() != SUICIDE_PATH.end())) {
                if (Constants.AREA_ALTAR.contains(Players.local())) {
                    altarDoor = Objects.stream().within(20).id(1524, 1525).first()
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
            else if (Constants.SUICIDE_TILE_MATRIX.valid()) {
                for (n in 1..10) {
                    if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local())
                        || Players.local().distanceTo(Constants.SUICIDE_TILE) < 6)
                        break
                    else if (!Players.local().inMotion()) {
                        if (!Constants.SUICIDE_TILE_MATRIX.inViewport() || Constants.SUICIDE_TILE_MATRIX.interact("Walk here")
                            && Condition.wait({ Skills.level(Skill.Hitpoints) == 0
                                    || Constants.AREA_LUMBY.contains(Players.local()) || Players.local().inMotion() }, 50 , 50))
                            break
                    }

                    Condition.sleep(50)
                }
            }

            if (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.SUICIDE_TILE) > 5)
                LoggingService.severe("Failed to get to the suicide tile hardcoded path.")
        }
        else if (Players.local().distanceTo(Constants.SUICIDE_TILE) > 5) {
            val generatePath = DaxWalker.getPath(Constants.SUICIDE_TILE)
            if (generatePath.isNullOrEmpty())
                return

            val escapePath = TilePath(generatePath.toTypedArray())
            if (!escapePath.valid())
                return
            else while (!ScriptManager.isStopping() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
                && (escapePath.traverse() || escapePath.next() != escapePath.end()))
                Condition.sleep(50)

            Condition.wait({ !Players.local().inMotion() }, 50, 50)
            if (Constants.SUICIDE_TILE_MATRIX.valid()) {
                for (n in 1..10) {
                    if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local())
                        || Players.local().distanceTo(Constants.SUICIDE_TILE) < 6)
                        break
                    else if (!Players.local().inMotion()) {
                        if (!Constants.SUICIDE_TILE_MATRIX.inViewport() || Constants.SUICIDE_TILE_MATRIX.interact("Walk here")
                            && Condition.wait({ Skills.level(Skill.Hitpoints) == 0
                                    || Constants.AREA_LUMBY.contains(Players.local()) || Players.local().inMotion() }, 50 , 50)) {
                            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                            break
                        }
                    }

                    Condition.sleep(50)
                }
            }

            if (Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.SUICIDE_TILE) > 5)
                LoggingService.severe("Failed to get to the suicide tile with generated path..")
        }
    }
}