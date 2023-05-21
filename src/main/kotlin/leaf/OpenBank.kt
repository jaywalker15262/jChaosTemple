package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.LUMBY_BOTTOM_FLOOR_PATH
import com.jay.chaostemple.Constants.LUMBY_TOP_FLOOR_PATH
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class OpenBank(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Opening Bank") {
    override fun execute() {
        if (Bank.inViewport()) {
            if (Bank.open())
                Condition.wait({ Bank.opened() }, Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 50)
        }
        else {
            if (Constants.AREA_LUMBY.contains(Players.local())) {
                while (!ScriptManager.isStopping() && (LUMBY_BOTTOM_FLOOR_PATH.traverse()
                            || LUMBY_BOTTOM_FLOOR_PATH.next() != LUMBY_BOTTOM_FLOOR_PATH.end()))
                    Condition.sleep(50)

                if (Game.floor() == 0) {
                    val stairCase = Objects.stream().within(18).id(16671).nearest().first()
                    if (!stairCase.valid()) {
                        script.info("Failed to find the staircase in lumby castle.")
                        return
                    } else if (!Condition.wait({
                            stairCase.distanceTo(Players.local()).toInt() < 5
                                    || !Players.local().inMotion()
                        }, 50, 300)) {
                        script.info("Failed to walk to the lumby castle stairs.")
                        return
                    }

                    stairCase.bounds(-26, 26, -76, 24, -26, 26)
                    for (n in 1..10) {
                        if (!stairCase.inViewport()) {
                            script.info("Stairs are not in view.")
                            Camera.turnTo(stairCase)
                            Condition.wait({ stairCase.inViewport() }, 50, 50)
                        }
                        if (Players.local().floor() == 1
                            || (stairCase.interact("Climb-up") && Condition.wait({ Game.floor() == 1 }, 50, 90))) {
                            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                            break
                        }

                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    }

                    if (Game.floor() != 1) {
                        script.info("Failed to walk up the stairs on the bottom floor.")
                        return
                    }
                }
                if (Game.floor() == 1) {
                    val secondStairCase = Objects.stream().within(6).id(16672).first()
                    if (!secondStairCase.valid()) {
                        script.info("Failed to find the staircase on the first floor of the lumby castle.")
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
                        script.info("Failed to walk up the stairs on the first floor.")
                        return
                    }
                }

                while (!ScriptManager.isStopping() && (LUMBY_TOP_FLOOR_PATH.traverse()
                            || LUMBY_TOP_FLOOR_PATH.next() != LUMBY_TOP_FLOOR_PATH.end()))
                    Condition.sleep(50)

                Condition.wait({ !Players.local().inMotion() }, 50, 100)

            }
            else Movement.moveToBank()

            Camera.turnTo(Bank.nearest().tile())
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }
    }
}