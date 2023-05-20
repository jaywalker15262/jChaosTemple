package com.jay.chaostemple.leaf

import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Camera
import org.powbot.api.rt4.Movement
import org.powbot.api.script.tree.Leaf

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
            Movement.moveToBank()
            Camera.turnTo(Bank.nearest().tile())
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }
    }
}