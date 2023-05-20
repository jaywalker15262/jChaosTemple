package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import com.jay.chaostemple.leaf.Chill
import com.jay.chaostemple.leaf.OfferBones
import com.jay.chaostemple.leaf.Suicide
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager

class OfferingCheck(script: Script) : Branch<Script>(script, "Already offering?") {
    override val successComponent: TreeComponent<Script> = Chill(script)
    override val failedComponent: TreeComponent<Script> = SuicideCheck(script)

    override fun validate(): Boolean {
        if (Constants.lastKnownPrayerXp != Skills.experience(Skill.Prayer)) {
            Constants.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
            Constants.timeSinceLastXpDrop = ScriptManager.getRuntime(true) + 3000
        }

        return Inventory.stream().name(Constants.boneType).isNotEmpty() && Constants.timeSinceLastXpDrop > ScriptManager.getRuntime(true)
    }
}

class SuicideCheck(script: Script) : Branch<Script>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<Script> = Suicide(script)
    override val failedComponent: TreeComponent<Script> = OfferBones(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Constants.boneType).isEmpty()
    }
}