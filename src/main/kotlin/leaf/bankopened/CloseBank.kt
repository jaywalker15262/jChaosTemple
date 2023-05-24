package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf

class CloseBank (script: ChaosTemple) : Leaf<ChaosTemple>(script, "Closing Bank") {
    override fun execute() {
        if (Bank.close())
            Condition.wait({ !Bank.opened() }, Random.nextGaussian(170, 250, 200, 20.0), 13)

        // Fixes a bug that causes us to not immediately offer bones upon arriving at the altar.
        Variables.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
    }
}