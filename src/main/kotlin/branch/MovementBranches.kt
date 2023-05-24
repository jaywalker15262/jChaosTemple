package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class AtAltar(script: ChaosTemple) : Branch<ChaosTemple>(script, "At the altar?") {
    override val successComponent: TreeComponent<ChaosTemple> = OfferingCheck(script)
    override val failedComponent: TreeComponent<ChaosTemple> = SuicideCheckTwo(script)

    override fun validate(): Boolean {
        return Constants.AREA_ALTAR.contains(Players.local()) && Inventory.stream().name(Variables.boneType).isNotEmpty()
    }
}