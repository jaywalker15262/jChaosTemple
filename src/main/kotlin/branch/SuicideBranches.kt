package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.leaf.Chill
import com.jay.chaostemple.leaf.OfferBones
import com.jay.chaostemple.leaf.suiciding.Suicide
import com.jay.chaostemple.leaf.TravelToAltar
import com.jay.chaostemple.leaf.suiciding.TravelToChaosFanatic
import com.jay.chaostemple.leaf.suiciding.WorldHop
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Npcs
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager

class SuicideCheck(script: ChaosTemple) : Branch<ChaosTemple>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<ChaosTemple> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<ChaosTemple> = OfferBones(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Variables.boneType).isEmpty()
    }
}

class SuicideCheckTwo(script: ChaosTemple) : Branch<ChaosTemple>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<ChaosTemple> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<ChaosTemple> = TravelToAltar(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Variables.boneType).isEmpty()
    }
}

class SuicideAreaCheck(script: ChaosTemple) : Branch<ChaosTemple>(script, "At suicide area?") {
    override val successComponent: TreeComponent<ChaosTemple> = SuicideChillCheck(script)
    override val failedComponent: TreeComponent<ChaosTemple> = TravelToChaosFanatic(script)

    override fun validate(): Boolean {
        return Players.local().distanceTo(Constants.SUICIDE_TILE) < 6
    }
}

class SuicideChillCheck(script: ChaosTemple) : Branch<ChaosTemple>(script, "Chill at suicide area?") {
    override val successComponent: TreeComponent<ChaosTemple> = WorldHopCheck(script)
    override val failedComponent: TreeComponent<ChaosTemple> = Chill(script)

    override fun validate(): Boolean {
        return !Players.local().inCombat() && Skills.level(Skill.Hitpoints) > 0
    }
}

class WorldHopCheck(script: ChaosTemple) : Branch<ChaosTemple>(script, "World-hop?") {
    override val successComponent: TreeComponent<ChaosTemple> = WorldHop(script)
    override val failedComponent: TreeComponent<ChaosTemple> = Suicide(script)

    override fun validate(): Boolean {
        return ScriptManager.getRuntime(true) > Variables.timeUntilNextLogout
                && Skills.level(Skill.Hitpoints) > 0 && Npcs.stream().within(13).name("Chaos fanatic")
                .filtered { !it.healthBarVisible() && it.inViewport() }.isEmpty()
    }
}
