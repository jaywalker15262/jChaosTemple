package com.jay.chaostemple.branch

import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.Chill
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager

class OfferingCheck(script: jChaosTemple) : Branch<jChaosTemple>(script, "Already offering?") {
    override val successComponent: TreeComponent<jChaosTemple> = Chill(script)
    override val failedComponent: TreeComponent<jChaosTemple> = SuicideCheck(script)

    override fun validate(): Boolean {
        if (Variables.lastKnownPrayerXp != Skills.experience(Skill.Prayer)) {
            Variables.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
            Variables.timeSinceLastXpDrop = if (!Players.local().inCombat())
                ScriptManager.getRuntime(true) + 3000
            // Player attacks interrupts us offering bones so check if we need to reattempt more often while suiciding.
            else ScriptManager.getRuntime(true) + 600
        }

        return Inventory.stream().name(Variables.boneType).isNotEmpty()
                && Variables.timeSinceLastXpDrop > ScriptManager.getRuntime(true)
    }
}