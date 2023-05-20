package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import com.jay.chaostemple.leaf.Suicide
import com.jay.chaostemple.leaf.TravelToAltar
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class AtAltar(script: Script) : Branch<Script>(script, "At the altar?") {
    override val successComponent: TreeComponent<Script> = OfferingCheck(script)
    override val failedComponent: TreeComponent<Script> = SuicideCheckTwo(script)

    override fun validate(): Boolean {
        return Constants.AREA_ALTAR.contains(Players.local()) && Inventory.emptySlotCount() != Constants.emptySlotCountCheck
    }
}

class SuicideCheckTwo(script: Script) : Branch<Script>(script, "Time to suicide?") {
    override val successComponent: TreeComponent<Script> = Suicide(script)
    override val failedComponent: TreeComponent<Script> = TravelToAltar(script)

    override fun validate(): Boolean {
        return Inventory.emptySlotCount() == Constants.emptySlotCountCheck
    }
}