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

                var stairCase = Objects.stream().within(18).id(16671).nearest().first()
                for (n in 1..30) {
                    if (stairCase.valid())
                        break

                    Condition.sleep(100)
                    stairCase = Objects.stream().within(18).id(16671).nearest().first()
                }

                if (!stairCase.valid()) {
                    LoggingService.info("Failed to find the staircase in lumby castle.")
                    return
                }
                else if (!Condition.wait({ Players.local().distanceTo(stairCase).toInt() < 6
                        || !Players.local().inMotion() }, 50, 300)) {
                    LoggingService.info("Failed to walk to the lumby castle stairs.")
                    return
                }

                for (n in 1..10) {
                    if (!stairCase.inViewport())
                        Camera.turnTo(stairCase)
                    else if (Players.local().floor() == 1
                        || (stairCase.interact("Climb-up") && Condition.wait({ Players.local().floor() == 1 }, 50, 90))) {
                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                        break
                    }

                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                }

                if (Players.local().floor() != 1) {
                    LoggingService.info("Failed to walk up the stairs on the bottom floor.")
                    return
                }

                var secondStairCase = Objects.stream().within(6).id(16672).first()
                for (n in 1..30) {
                    if (secondStairCase.valid())
                        break

                    Condition.sleep(100)
                    secondStairCase = Objects.stream().within(6).id(16672).first()
                }

                if (!secondStairCase.valid()) {
                    LoggingService.info("Failed to find the staircase on the first floor of the lumby castle.")
                    return
                }
                else for (n in 1..10) {
                    if (Players.local().floor() == 2
                        || (secondStairCase.interact("Climb-up", true)
                                && Condition.wait({ Players.local().floor() == 2 }, 50, 90))) {
                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                        break
                    }

                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                }

                if (Players.local().floor() != 2) {
                    LoggingService.info("Failed to walk up the stairs on the first floor.")
                    return
                }
                else while (!ScriptManager.isStopping() && (lumbyBankTopFloorPath.traverse()
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