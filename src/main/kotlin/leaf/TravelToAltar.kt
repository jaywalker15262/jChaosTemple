package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.chaosAltarPath
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Input
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf

class TravelToAltar(script: Script) : Leaf<Script>(script, "Traveling To Altar") {
    override fun execute() {
        for (n in 1..10) {
            if (!Bank.opened())
                break
            else if (Bank.close()) {
                if (Condition.wait({ !Bank.opened() }, 50, 50)) {
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    break
                }
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        var wildyLevel = 1
        val yCoord = Players.local().y()
        if (yCoord > 3527)
            wildyLevel = ((yCoord - 3528) / 8) + 2

        if (wildyLevel == 1) {
            // Protect item support
            if (Constants.protectItem && Skills.realLevel(Skill.Prayer) >= 25 && Skills.level(Skill.Prayer) > 5
                && !Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
                for (n in 1..10) {
                    if (Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)
                        || (Prayer.prayer(Prayer.Effect.PROTECT_ITEM, true)
                        && Condition.wait({ Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM) }, 50, 50)))
                        break

                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                }

                if (!Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM))
                    LoggingService.info("Failed to turn on Protect Item.")
            }

            var burningAmulet = Inventory.stream().name(*Constants.BURNING_AMULETS).first()
            for (n in 1..10) {
                if (burningAmulet.valid())
                    break

                Condition.sleep(50)
                burningAmulet = Inventory.stream().name(*Constants.BURNING_AMULETS).first()
            }

            if (!burningAmulet.valid())
                return
            else if (burningAmulet.name() == "Burning amulet(1)")
                Constants.emptySlotCountCheck = 28
            else Constants.emptySlotCountCheck = 27

            for (n in 1..10) {
                if (Chat.chatting() || (burningAmulet.interact("Rub") && Condition.wait({ Chat.chatting() }, 50, 50)))
                    break

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            if (!Chat.chatting()) {
                LoggingService.info("Failed to find chat options after attempting to rub the amulet.")
                return
            }

            /* Will need to have a working reimplementation of this in the future.
            if (!Input.send("3")) {
                if (!Condition.wait({ !Chat.chatting() }, 50 ,80)
                    || !Input.send("3") || !Condition.wait({ !Chat.chatting() }, 50 ,80))
                    return
            }
            else if (!Condition.wait({ !Chat.chatting() }, 50 ,80))
                return*/
            if (!Input.send("3"))
                return

            Condition.sleep(Random.nextGaussian(370, 550, 400, 30.0))
            if (!Input.send("1"))
                return
            else if (!Condition.wait({ Players.local().distanceTo(Constants.lavaMazeTile).toInt() < 10 }, 50, 140))
                return
        }

        // Protect item support
        if (Constants.protectItem && Skills.realLevel(Skill.Prayer) >= 25 && Skills.level(Skill.Prayer) > 5
            && !Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
            for (n in 1..10) {
                if (Script.antiPkingCheck())
                    return
                else if (Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
                    break
                }
                else if (Prayer.prayer(Prayer.Effect.PROTECT_ITEM, true)
                            && Condition.wait({ Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)
                            || Script.antiPkingCheck() }, 50, 50)) {
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    break
                }

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }

            if (!Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM))
                LoggingService.info("Failed to turn on Protect Item.")
        }

        for (n in 1..3) {
            if (Script.antiPkingCheck())
                return
            else if (Game.tab(Game.Tab.LOGOUT))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (!Condition.wait({ Game.tab() == Game.Tab.LOGOUT || Script.antiPkingCheck() }, 50, 50))
            LoggingService.severe("We were unable to open up the logout tab after starting to travel towards the altar.")
        else if (Constants.escapePker)
            return
        else while (!Script.antiPkingCheck() && Skills.level(Skill.Hitpoints) != 0 && !Constants.AREA_LUMBY.contains(Players.local())
            && (chaosAltarPath.traverse() || chaosAltarPath.next() != chaosAltarPath.end()))
            Condition.sleep(50)

        if (Players.local().inMotion()) {
            Condition.wait({ Players.local().distanceTo(chaosAltarPath.end()) < 8
                    || Script.antiPkingCheck() }, 50, 25)
        }

        if (Constants.escapePker || Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
            return
        else if (Players.local().distanceTo(chaosAltarPath.end()) > 8) {
            LoggingService.severe("We were unable to get to the entrance to the Chaos Temple.")
            return
        }

        var altarDoor = Objects.stream().within(20).id(1524, 1525).first()
        for (n in 1..20) {
            if (Script.antiPkingCheck())
                return
            else if (altarDoor.valid())
                break

            Condition.sleep(50)
            altarDoor = Objects.stream().within(20).id(1524, 1525).first()
        }

        if (!altarDoor.valid()) {
            LoggingService.info("Failed to find the doors at the Chaos Altar.")
            return
        }
        else if (altarDoor.id() == 1524) {
            for (n in 1..10) {
                if (Script.antiPkingCheck())
                    return
                else if (altarDoor.inViewport())
                    break

                Camera.turnTo(altarDoor.tile)
                if (Condition.wait({ altarDoor.inViewport() || Script.antiPkingCheck() }, 50 ,25)) {
                    if (Constants.escapePker)
                        return
                }
            }

            if (!altarDoor.inViewport()) {
                LoggingService.info("Failed to find the door at the Chaos Altar to be in our viewport.")
                return
            }

            altarDoor.bounds(-64, -56, -160, -16, -32, 32)
            for (n in 1..20) {
                if (Script.antiPkingCheck())
                    return
                else if (!altarDoor.valid() || (altarDoor.interact("Open")
                            && Condition.wait({ !altarDoor.valid() || Script.antiPkingCheck()  }, 50, 50))) {
                    if (Constants.escapePker)
                        return

                    break
                }

                Condition.sleep(50)
            }

            if (altarDoor.valid())
                return
        }

        val altarTileMatrix = Constants.altarTile.matrix()
        if (!altarTileMatrix.valid())
            return
        else for (n in 1..10) {
            if (Script.antiPkingCheck())
                return
            else if (altarTileMatrix.inViewport())
                break

            Camera.turnTo(altarTileMatrix)
            if (Condition.wait({ altarTileMatrix.inViewport() || Script.antiPkingCheck() }, 50 ,25)) {
                if (Constants.escapePker)
                    return
            }
        }

        if (!altarTileMatrix.inViewport())
            return
        else for (n in 1..20) {
            if (Script.antiPkingCheck() || Skills.level(Skill.Hitpoints) == 0 || Constants.AREA_LUMBY.contains(Players.local()))
                return
            if (!Constants.AREA_ALTAR.contains(Players.local())) {
                altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                for (i in 1..20) {
                    if (Script.antiPkingCheck())
                        return
                    else if (altarDoor.valid())
                        break

                    Condition.sleep(50)
                    altarDoor = Objects.stream().within(20).id(1524, 1525).first()
                }

                if (!altarDoor.valid())
                    return
                else if (altarDoor.id() == 1524) {
                    altarDoor.bounds(-64, -56, -160, -16, -32, 32)
                    for (i in 1..20) {
                        if (Script.antiPkingCheck())
                            return
                        else if (!altarDoor.valid() || (altarDoor.interact("Open")
                                    && Condition.wait({ !altarDoor.valid() || Script.antiPkingCheck() }, 50, 50))) {
                            if (Constants.escapePker)
                                return

                            break
                        }

                        Condition.sleep(50)
                    }

                    if (altarDoor.valid())
                        continue
                }
            }

            if (Movement.step(Constants.altarTile) && Condition.wait({ Constants.AREA_ALTAR.contains(Players.local())
                        || Script.antiPkingCheck() }, 50, 50)) {
                if (Constants.escapePker)
                    return

                break
            }

            Condition.sleep(50)
        }

        for (n in 1..3) {
            if (Script.antiPkingCheck())
                return
            else if (Game.tab(Game.Tab.LOGOUT))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (!Condition.wait({ Game.tab() == Game.Tab.LOGOUT || Script.antiPkingCheck() }, 50, 50))
            LoggingService.severe("We were unable to open up the logout tab after starting to offer bones at he altar.")
        else if (Constants.escapePker)
            return

        if (!Constants.AREA_ALTAR.contains(Players.local()))
            return

        Condition.wait({ !Players.local().inMotion() || Script.antiPkingCheck() }, 50 ,80)
    }
}