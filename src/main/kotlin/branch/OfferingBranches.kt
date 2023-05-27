package com.jay.chaostemple.branch

import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.leaf.Chill
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager

class OfferingCheck(script: ChaosTemple) : Branch<ChaosTemple>(script, "Already offering?") {
    override val successComponent: TreeComponent<ChaosTemple> = Chill(script)
    override val failedComponent: TreeComponent<ChaosTemple> = SuicideCheck(script)

    override fun validate(): Boolean {
        if (!Variables.oneTicking && Variables.lastKnownPrayerXp != Skills.experience(Skill.Prayer)) {
            Variables.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
            // Player attacks interrupts us offering bones so 1-tick offer bones while suiciding.
            if (!Players.local().inCombat())
                Variables.timeSinceLastXpDrop = ScriptManager.getRuntime(true) + 2500
        }

        return Variables.timeSinceLastXpDrop > ScriptManager.getRuntime(true) || Skills.level(Skill.Hitpoints) == 0
    }
}