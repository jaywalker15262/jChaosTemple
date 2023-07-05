package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Constants.LUMBY_BOTTOM_FLOOR_PATH
import com.jay.chaostemple.Constants.LUMBY_TOP_FLOOR_PATH
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf

class OpenBank(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Opening Bank") {
    override fun execute() {
        if (Bank.inViewport() && (Players.local().distanceTo(Constants.LUMBY_STAIRS_TILE).toInt() > 40
                    || Players.local().distanceTo(Constants.LUMBY_STAIRS_TILE).toInt() < 4
                    || !Players.local().inMotion())) {
            if (Bank.open())
                Condition.wait({ Bank.opened() },
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 13)
        }
        else {
            if (Constants.AREA_LUMBY.contains(Players.local())) {
                if (Game.floor() == 0) {
                    LUMBY_BOTTOM_FLOOR_PATH.traverse()
                    if (Players.local().distanceTo(Constants.LUMBY_STAIRS_TILE).toInt() > 8 ||
                        !Condition.wait({ !Players.local().inMotion() || Players.local()
                            .distanceTo(Constants.LUMBY_STAIRS_TILE).toInt() < 4 }, 50, 50))
                        return

                    val stairCase = Objects.stream().within(18).id(16671).nearest().first()
                    if (!stairCase.valid()) {
                        script.info("Failed to find the staircase in lumby castle.")
                        return
                    }

                    stairCase.bounds(-26, 26, -76, 24, -26, 26)
                    if (!stairCase.inViewport()) {
                        script.info("Stairs are not in view.")
                        Camera.turnTo(stairCase)
                        Condition.wait({ stairCase.inViewport() }, 50, 50)
                    }

                    if (!stairCase.interact("Climb-up")
                        || !Condition.wait({ Game.floor() == 1 }, 50, 90)) {
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
                    if (!secondStairCase.interact("Climb-up")
                        || !Condition.wait({ Game.floor() == 2 }, 50, 90)) {
                        script.info("Failed to walk up the stairs on first floor.")
                        return
                    }
                }
                if (Game.floor() == 2)
                    LUMBY_TOP_FLOOR_PATH.traverse()
            }
            else {
                script.info("Using back-up banking path.")
                Movement.moveToBank()
            }

            if (!Bank.inViewport()) {
                Camera.turnTo(Bank.nearest().tile())
                Condition.wait({ Bank.inViewport() }, 50, 50)
            }
        }
    }
}