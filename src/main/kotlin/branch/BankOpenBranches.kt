package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.OpenBank
import com.jay.chaostemple.leaf.TravelToAltar
import com.jay.chaostemple.leaf.bankopened.CloseBank
import com.jay.chaostemple.leaf.bankopened.DepositInventory
import com.jay.chaostemple.leaf.bankopened.SetupInventory
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class HaveInventory(script: jChaosTemple) : Branch<jChaosTemple>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<jChaosTemple> = TravelToAltar(script)
    override val failedComponent: TreeComponent<jChaosTemple> = OpenBank(script)

    override fun validate(): Boolean {
        return !Variables.depositEquipment && Inventory.stream().name(Variables.boneType).count().toInt() == 27 &&
                Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class HaveInventoryTwo(script: jChaosTemple) : Branch<jChaosTemple>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<jChaosTemple> = CloseBank(script)
    override val failedComponent: TreeComponent<jChaosTemple> = IsInventoryEmpty(script)

    override fun validate(): Boolean {
        return !Variables.depositEquipment && Inventory.stream().name(Variables.boneType).count().toInt() == 27 &&
                Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class IsBankOpened(script: jChaosTemple) : Branch<jChaosTemple>(script, "Bank open?") {
    override val successComponent: TreeComponent<jChaosTemple> = HaveInventoryTwo(script)
    override val failedComponent: TreeComponent<jChaosTemple> = HaveInventory(script)

    override fun validate(): Boolean {
        return Bank.opened()
    }
}

class IsInventoryEmpty(script: jChaosTemple) : Branch<jChaosTemple>(script, "Empty Inventory?") {
    override val successComponent: TreeComponent<jChaosTemple> = SetupInventory(script)
    override val failedComponent: TreeComponent<jChaosTemple> = DepositInventory(script)

    override fun validate(): Boolean {
        return Inventory.isEmpty() || (Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
                && Inventory.occupiedSlotCount() == 1)
    }
}