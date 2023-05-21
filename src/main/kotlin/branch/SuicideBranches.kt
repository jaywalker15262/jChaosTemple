package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.OfferBones
import com.jay.chaostemple.leaf.suiciding.Suicide
import com.jay.chaostemple.leaf.TravelToAltar
import com.jay.chaostemple.leaf.suiciding.TravelToChaosFanatic
import com.jay.chaostemple.leaf.suiciding.WorldHop
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Npcs
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import org.powbot.mobile.script.ScriptManager

class SuicideCheck(script: jChaosTemple) : Branch<jChaosTemple>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<jChaosTemple> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<jChaosTemple> = OfferBones(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Variables.boneType).isEmpty()
    }
}

class SuicideCheckTwo(script: jChaosTemple) : Branch<jChaosTemple>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<jChaosTemple> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<jChaosTemple> = TravelToAltar(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Variables.boneType).isEmpty()
    }
}

class SuicideAreaCheck(script: jChaosTemple) : Branch<jChaosTemple>(script, "At suicide area?") {
    override val successComponent: TreeComponent<jChaosTemple> = WorldHopCheck(script)
    override val failedComponent: TreeComponent<jChaosTemple> = TravelToChaosFanatic(script)

    override fun validate(): Boolean {
        return Players.local().distanceTo(Constants.SUICIDE_TILE) < 6
    }
}

class WorldHopCheck(script: jChaosTemple) : Branch<jChaosTemple>(script, "World-hop?") {
    override val successComponent: TreeComponent<jChaosTemple> = WorldHop(script)
    override val failedComponent: TreeComponent<jChaosTemple> = Suicide(script)

    override fun validate(): Boolean {
        return !Players.local().inCombat() && ScriptManager.getRuntime(true) > Variables.timeUntilNextLogout &&
                Npcs.stream().within(13).name("Chaos fanatic").filtered {
            it.distanceTo(Players.local()).toInt() <= 11 && it.inViewport() }.isEmpty()
    }
}