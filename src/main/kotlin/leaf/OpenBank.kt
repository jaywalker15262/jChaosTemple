package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.LUMBY_BOTTOM_FLOOR_PATH
import com.jay.chaostemple.Constants.LUMBY_TOP_FLOOR_PATH
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class OpenBank(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Opening Bank") {
    override fun execute() {
        if (Bank.inViewport()) {
            if (Bank.open())
                Condition.wait({ Bank.opened() }, Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 13)
        }
        else {
            if (Constants.AREA_LUMBY.contains(Players.local())) {
                if (Game.floor() == 0) {
                    while (!ScriptManager.isStopping() && (LUMBY_BOTTOM_FLOOR_PATH.traverse()
                                || LUMBY_BOTTOM_FLOOR_PATH.next() != LUMBY_BOTTOM_FLOOR_PATH.end()))
                        Condition.sleep(50)

                    val stairCase = Objects.stream().within(18).id(16671).nearest().first()
                    if (!stairCase.valid()) {
                        script.info("Failed to find the staircase in lumby castle.")
                        return
                    }

                    if (!Condition.wait({ stairCase.distanceTo(Players.local()).toInt() < 5
                            || !Players.local().inMotion() }, 50, 300)) {
                        script.info("Failed to walk to the lumby castle stairs.")
                        return
                    }

                    stairCase.bounds(-26, 26, -76, 24, -26, 26)
                    if (!stairCase.inViewport()) {
                        script.info("Stairs are not in view.")
                        Camera.turnTo(stairCase)
                        Condition.wait({ stairCase.inViewport() }, 50, 50)
                    }

                    if (!stairCase.interact("Climb-up") || !Condition.wait({ Game.floor() == 1 }, 50, 90)) {
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
                    if (!secondStairCase.interact("Climb-up") || !Condition.wait({ Game.floor() == 2 }, 50, 90)) {
                        script.info("Failed to walk up the stairs on the bottom floor.")
                        return
                    }

                    if (Game.floor() != 2) {
                        script.info("Failed to walk up the stairs on the first floor.")
                        return
                    }
                }

                while (!ScriptManager.isStopping() && (LUMBY_TOP_FLOOR_PATH.traverse()
                            || LUMBY_TOP_FLOOR_PATH.next() != LUMBY_TOP_FLOOR_PATH.end()))
                    Condition.sleep(50)

                Condition.wait({ Players.local().distanceTo(LUMBY_TOP_FLOOR_PATH.end()).toInt() < 4 ||
                        !Players.local().inMotion() }, 50, 100)
            }
            else Movement.moveToBank()

            if (!Bank.inViewport()) {
                Camera.turnTo(Bank.nearest().tile())
                Condition.wait({ Bank.inViewport() }, 50, 50)
            }
        }
    }
}