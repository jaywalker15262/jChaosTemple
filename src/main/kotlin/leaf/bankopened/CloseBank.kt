package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Constants
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf

class CloseBank (script: jChaosTemple) : Leaf<jChaosTemple>(script, "Closing Bank") {
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

        // Fixes a bug that causes us to not immediately offer bones upon arriving at the altar.
        Constants.LAST_KNOWN_PRAYER_XP = Skills.experience(Skill.Prayer)
    }
}