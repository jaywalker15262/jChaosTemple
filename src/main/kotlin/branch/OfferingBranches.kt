package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.Chill
import org.powbot.api.Production
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class OfferingCheck(script: jChaosTemple) : Branch<jChaosTemple>(script, "Already offering?") {
    override val successComponent: TreeComponent<jChaosTemple> = Chill(script)
    override val failedComponent: TreeComponent<jChaosTemple> = SuicideCheck(script)

    override fun validate(): Boolean {
        var stoppedOffering = Production.stoppedUsing(Constants.BONE_IDS[Constants.BONE_TYPES.indexOf(Variables.boneType)], 2500)
        if (Players.local().inCombat())
            stoppedOffering = Production.stoppedUsing(Constants.BONE_IDS[Constants.BONE_TYPES.indexOf(Variables.boneType)], 600)

        return Inventory.stream().name(Variables.boneType).isNotEmpty() && !stoppedOffering

    }
}