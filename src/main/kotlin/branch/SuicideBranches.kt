package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
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

class SuicideCheck(script: Script) : Branch<Script>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<Script> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<Script> = OfferBones(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Constants.boneType).isEmpty()
    }
}

class SuicideCheckTwo(script: Script) : Branch<Script>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<Script> = SuicideAreaCheck(script)
    override val failedComponent: TreeComponent<Script> = TravelToAltar(script)

    override fun validate(): Boolean {
        return Inventory.stream().name(Constants.boneType).isEmpty()
    }
}

class SuicideAreaCheck(script: Script) : Branch<Script>(script, "At suicide area?") {
    override val successComponent: TreeComponent<Script> = WorldHopCheck(script)
    override val failedComponent: TreeComponent<Script> = TravelToChaosFanatic(script)

    override fun validate(): Boolean {
        return Players.local().distanceTo(Constants.suicideTile) < 6
    }
}

class WorldHopCheck(script: Script) : Branch<Script>(script, "World-hop?") {
    override val successComponent: TreeComponent<Script> = WorldHop(script)
    override val failedComponent: TreeComponent<Script> = Suicide(script)

    override fun validate(): Boolean {
        return !Players.local().inCombat() && ScriptManager.getRuntime(true) > Constants.timeUntilNextLogOut &&
                Npcs.stream().within(13).name("Chaos fanatic").filtered {
            it.distanceTo(Players.local()).toInt() <= 11 && it.inViewport() }.isEmpty()
    }
}