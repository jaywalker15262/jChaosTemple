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

            while (!ScriptManager.isStopping() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
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
                        || Players.local().distanceTo(Constants.suicideTile) < 6)
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
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.suicideTile) > 5) {
                LoggingService.severe("Failed to get to the suicide tile.")
                ScriptManager.stop()
                return
            }
        }
        else if (Players.local().distanceTo(Constants.suicideTile) > 5) {
            val generatePath = DaxWalker.getPath(Constants.suicideTile)
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
            if (Constants.suicideTileMatrix.valid()) {
                for (n in 1..10) {
                    if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local())
                        || Players.local().distanceTo(Constants.suicideTile) < 6)
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
                && !Players.local().inCombat() && Players.local().distanceTo(Constants.suicideTile) > 5) {
                LoggingService.severe("Failed to get to the suicide tile 2.")
                ScriptManager.stop()
                return
            }
        }

        /* Chaos Fanatic might not always be alive and is sometimes killed or in combat with other players/bots.
        So as counter measure to make sure we die, make sure we wait for him to spawn and to attack him instead of
        waiting for us to timeout and the script ending*/
        var chaosFanatic = Npcs.stream().within(13).name("Chaos fanatic").filtered {
            it.distanceTo(Players.local()).toInt() <= 11 && !it.healthBarVisible() && it.inViewport() }.first()
        // Some users might use accounts with high stats that are really tanky, and so we should wait at least a full minute.
        for (n in 1..1200) {
            if (Constants.AREA_LUMBY.contains(Players.local()))
                return
            // Let's afk until Chaos Fanatic is there, so that we do not time out.
            while (Game.loggedIn() && !Constants.AREA_LUMBY.contains(Players.local())
                && Skills.level(Skill.Hitpoints) > 0 && !Players.local().healthBarVisible() &&
                (!chaosFanatic.valid() || Players.local().interacting() != chaosFanatic)) {
                for (i in 1..20) {
                    if (chaosFanatic.valid() && !chaosFanatic.healthBarVisible())
                        break

                    // The stream below is expensive but very robust, so lets sleep for at least 100ms each iteration.
                    Condition.sleep(100)
                    chaosFanatic = Npcs.stream().within(13).name("Chaos Fanatic").filtered {
                        it.distanceTo(Players.local()).toInt() <= 11 && !it.healthBarVisible() && it.inViewport() }.first()
                }

                if (chaosFanatic.valid()) {
                    chaosFanatic.bounds(-16, 16, -16, -16, -16, 16)
                    if (chaosFanatic.interact("Attack"))
                        Condition.wait({ chaosFanatic.healthBarVisible() }, 50, 90)
                }

                Condition.sleep(50)
            }
            if (!Game.loggedIn())
                return

            Condition.sleep(50)
        }

        // Some users might use accounts with high stats that are really tanky, and so we should wait at least a full minute.
        if (!Constants.AREA_LUMBY.contains(Players.local())) {
            LoggingService.severe("Failed to suicide.")
            ScriptManager.stop()
        }
    }
}