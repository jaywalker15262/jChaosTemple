package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.script.tree.Leaf

class CloseBank (script: Script) : Leaf<Script>(script, "Closing Bank") {
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
    }
}