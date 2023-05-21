package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class AtAltar(script: jChaosTemple) : Branch<jChaosTemple>(script, "At the altar?") {
    override val successComponent: TreeComponent<jChaosTemple> = OfferingCheck(script)
    override val failedComponent: TreeComponent<jChaosTemple> = SuicideCheckTwo(script)

    override fun validate(): Boolean {
        return Constants.AREA_ALTAR.contains(Players.local()) && Inventory.stream().name(Constants.BONE_TYPE).isNotEmpty()
    }
}