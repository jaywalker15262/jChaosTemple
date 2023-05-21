package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class AtAltar(script: Script) : Branch<Script>(script, "At the altar?") {
    override val successComponent: TreeComponent<Script> = OfferingCheck(script)
    override val failedComponent: TreeComponent<Script> = SuicideCheckTwo(script)

    override fun validate(): Boolean {
        return Constants.AREA_ALTAR.contains(Players.local()) && Inventory.stream().name(Constants.boneType).isNotEmpty()
    }
}