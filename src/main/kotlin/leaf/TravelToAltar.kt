package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.CHAOS_ALTAR_PATH
import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Input
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class TravelToAltar(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Traveling To Altar") {
    override fun execute() {
        if (Combat.wildernessLevel() == -1) {
            val burningAmulet = Inventory.stream().name(*Constants.BURNING_AMULETS).first()
            if (!burningAmulet.valid())
                return

            if (!burningAmulet.interact("Rub")) {
                script.info("Failed to rub the amulet.")
                return
            }

            var lavaMazeChatOption = Chat.stream().text("Lava Maze").first()
            for (i in 1..10) {
                if (lavaMazeChatOption.valid())
                    break

                Condition.sleep(Random.nextGaussian(270, 450, 300, 30.0))
                lavaMazeChatOption = Chat.stream().text("Lava Maze").first()

            }

            if (!lavaMazeChatOption.valid()) {
                script.info("Failed to find chat options after rubbing the amulet.")
                return
            }

            var wildernessChatOption = Chat.stream().text("Okay, teleport to level 41 Wilderness.").first()
            Input.send("3")
            for (i in 1..10) {
                if (wildernessChatOption.valid())
                    break

                Condition.sleep(Random.nextGaussian(270, 450, 300, 30.0))
                wildernessChatOption = Chat.stream().text("Okay, teleport to level 41 Wilderness.").first()
            }

            if (!wildernessChatOption.valid()) {
                script.info("Failed to find the next chat option.")
                return
            }
            else for (n in 1..5) {
                Input.send("1")
                if (Condition.wait({ Players.local().distanceTo(Constants.LAVA_MAZE_TILE).toInt() < 10 }, 50, 140))
                    break

                wildernessChatOption = Chat.stream().text("Okay, teleport to level 41 Wilderness.").first()
                if (!wildernessChatOption.valid())
                    break
            }
            if (Players.local().distanceTo(Constants.LAVA_MAZE_TILE).toInt() > 9) {
                script.info("Failed to find that teleported to the Lava Maze.")
                return
            }
        }

        // Protect item support
        if (Variables.protectItem && Skills.realLevel(Skill.Prayer) >= 25 && Skills.level(Skill.Prayer) > 5
            && !Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
            if (Prayer.prayer(Prayer.Effect.PROTECT_ITEM, true))
                Condition.wait({ Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM) || jChaosTemple.antiPkingCheck() },
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 13)

            if (Variables.escapePker)
                return
            else if (!Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM))
                script.info("Failed to turn on Protect Item.")
        }

        for (n in 1..3) {
            if (Game.tab(Game.Tab.LOGOUT))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            if (jChaosTemple.antiPkingCheck())
                return
        }

        if (!Condition.wait({ jChaosTemple.antiPkingCheck() || Game.tab() == Game.Tab.LOGOUT }, 50, 50))
            script.severe("We were unable to open up the logout tab after starting to travel towards the altar.")
        else if (Variables.escapePker)
            return
        else if (Players.local().distanceTo(CHAOS_ALTAR_PATH.end()).toInt() > 8) {
            while (!ScriptManager.isStopping() && !jChaosTemple.antiPkingCheck() && Skills.level(Skill.Hitpoints) != 0
                && !Constants.AREA_LUMBY.contains(Players.local())
                && (CHAOS_ALTAR_PATH.traverse() || CHAOS_ALTAR_PATH.next() != CHAOS_ALTAR_PATH.end()))
                Condition.sleep(50)

            Condition.wait({ Players.local().distanceTo(CHAOS_ALTAR_PATH.end()).toInt() < 9 || !Players.local().inMotion()
                    || jChaosTemple.antiPkingCheck() }, 50, 50)

            if (Variables.escapePker || Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
                return
            else if (Players.local().distanceTo(CHAOS_ALTAR_PATH.end()).toInt() > 8) {
                script.severe("We were unable to get to the entrance to the Chaos Temple.")
                return
            }
        }

        val altarDoor = Objects.stream().within(20).id(1524, 1525).first()
        if (!altarDoor.valid()) {
            script.info("Failed to find the doors at the temple.")
            return
        }
        else if (altarDoor.id() == 1524) {
            if (!altarDoor.inViewport()) {
                Camera.turnTo(altarDoor)
                if (Condition.wait({ altarDoor.inViewport() || jChaosTemple.antiPkingCheck() }, 50 ,50)) {
                    if (Variables.escapePker)
                        return
                }
            }

            if (!altarDoor.inViewport())
                script.info("Failed to find the door at the Chaos Altar to be in our viewport.")

            altarDoor.bounds(-64, -56, -160, -16, -32, 32)
            if (!altarDoor.interact("Open")) {
                script.info("Failed to try and open the door.")
                return
            }
            else if (!Condition.wait({ !altarDoor.valid() || jChaosTemple.antiPkingCheck()  }, 50, 50)) {
                script.info("Failed to find that the door was open.")
                return
            }
            else if (Variables.escapePker)
                return
        }

        if (!Variables.ALTAR_TILE_MATRIX.valid()) {
            Variables.ALTAR_TILE_MATRIX = Constants.ALTAR_TILE.matrix()
            return
        }
        
        if (!Variables.ALTAR_TILE_MATRIX.inViewport()) {
            Camera.turnTo(Variables.ALTAR_TILE_MATRIX)
            if (Condition.wait({ Variables.ALTAR_TILE_MATRIX.inViewport() || jChaosTemple.antiPkingCheck() }, 50 ,50)) {
                if (Variables.escapePker)
                    return
            }
        }

        if (Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
            return
        else if (!Movement.step(Constants.ALTAR_TILE)) {
            script.info("Failed to step towards the altar tile.")
            return
        }
        else if (!Condition.wait({ Constants.AREA_ALTAR.contains(Players.local())
                    || jChaosTemple.antiPkingCheck() }, 50, 50)) {
            script.info("Failed to find that we are inside the temple.")
            return
        }
        else if (Variables.escapePker)
            return

        Condition.wait({ !Players.local().inMotion() || jChaosTemple.antiPkingCheck() }, 50 ,80)
    }
}