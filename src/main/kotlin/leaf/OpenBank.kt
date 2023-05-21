package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.lumbyBankBottomFloorPath
import com.jay.chaostemple.Constants.lumbyBankTopFloorPath
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class OpenBank(script: Script) : Leaf<Script>(script, "Opening Bank") {
    override fun execute() {
        if (Bank.inViewport()) {
            for (n in 1..10) {
                if (Bank.opened())
                    break
                else if (Bank.open()) {
                    if (Condition.wait({ Bank.opened() }, 50, 50)) {
                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                        break
                    }
                }

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }
        }
        else {
            if (Constants.AREA_LUMBY.contains(Players.local())) {
                while (!ScriptManager.isStopping() && (lumbyBankBottomFloorPath.traverse()
                            || lumbyBankBottomFloorPath.next() != lumbyBankBottomFloorPath.end()))
                    Condition.sleep(50)

                if (Game.floor() == 0) {
                    var stairCase = Objects.stream().within(18).id(16671).nearest().first()
                    if (!stairCase.valid()) {
                        LoggingService.info("Failed to find the staircase in lumby castle.")
                        return
                    } else if (!Condition.wait({
                            stairCase.distanceTo(Players.local()).toInt() < 5
                                    || !Players.local().inMotion()
                        }, 50, 300)) {
                        LoggingService.info("Failed to walk to the lumby castle stairs.")
                        return
                    }

                    stairCase.bounds(-26, 26, -76, 24, -26, 26)
                    for (n in 1..10) {
                        if (!stairCase.inViewport()) {
                            LoggingService.info("Stairs are not in view.")
                            Camera.turnTo(stairCase)
                            Condition.wait({ stairCase.inViewport() }, 50, 50)
                        }
                        if (Players.local().floor() == 1
                            || (stairCase.interact("Climb-up") && Condition.wait({ Game.floor() == 1 }, 50, 90))
                        ) {
                            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                            break
                        }

                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    }

                    if (Game.floor() != 1) {
                        LoggingService.info("Failed to walk up the stairs on the bottom floor.")
                        return
                    }
                }
                if (Game.floor() == 1) {
                    var secondStairCase = Objects.stream().within(6).id(16672).first()
                    if (!secondStairCase.valid()) {
                        LoggingService.info("Failed to find the staircase on the first floor of the lumby castle.")
                        return
                    }

                    secondStairCase.bounds(-26, 26, -76, 24, -26, 26)
                    for (n in 1..10) {
                        if (Players.local().floor() == 2
                            || (secondStairCase.interact("Climb-up", true)
                                    && Condition.wait({ Game.floor() == 2 }, 50, 90))) {
                            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                            break
                        }

                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    }

                    if (Game.floor() != 2) {
                        LoggingService.info("Failed to walk up the stairs on the first floor.")
                        return
                    }
                }

                while (!ScriptManager.isStopping() && (lumbyBankTopFloorPath.traverse()
                            || lumbyBankTopFloorPath.next() != lumbyBankTopFloorPath.end()))
                    Condition.sleep(50)

                Condition.wait({ !Players.local().inMotion() }, 50, 100)

            }
            else Movement.moveToBank()

            Camera.turnTo(Bank.nearest().tile())
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }
    }
}