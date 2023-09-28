package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.leaf.OpenBank
import com.jay.chaostemple.leaf.TravelToAltar
import com.jay.chaostemple.leaf.bankopened.CloseBank
import com.jay.chaostemple.leaf.bankopened.DepositInventory
import com.jay.chaostemple.leaf.bankopened.SetupInventory
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class HaveInventory(script: ChaosTemple) : Branch<ChaosTemple>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<ChaosTemple> = TravelToAltar(script)
    override val failedComponent: TreeComponent<ChaosTemple> = OpenBank(script)

    override fun validate(): Boolean {
        return !Variables.depositEquipment && ((Variables.notedMode && Inventory.stream().name(Variables.boneType)
            .firstOrNull { it.stackSize() == Variables.notedAmount } != null
            && Inventory.stream().name(Variables.boneType).count().toInt() == 26)
            || !Variables.notedMode && Inventory.stream().name(Variables.boneType).count().toInt() == 27)
            && Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class HaveInventoryTwo(script: ChaosTemple) : Branch<ChaosTemple>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<ChaosTemple> = CloseBank(script)
    override val failedComponent: TreeComponent<ChaosTemple> = IsInventoryEmpty(script)

    override fun validate(): Boolean {
        return !Variables.depositEquipment && ((Variables.notedMode && Inventory.stream().name(Variables.boneType)
                    .firstOrNull { it.stackSize() == Variables.notedAmount } != null
                && Inventory.stream().name(Variables.boneType).count().toInt() == 26)
                || !Variables.notedMode && Inventory.stream().name(Variables.boneType).count().toInt() == 27)
                && Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class IsBankOpened(script: ChaosTemple) : Branch<ChaosTemple>(script, "Bank open?") {
    override val successComponent: TreeComponent<ChaosTemple> = HaveInventoryTwo(script)
    override val failedComponent: TreeComponent<ChaosTemple> = HaveInventory(script)

    override fun validate(): Boolean {
        return Bank.opened()
    }
}

class IsInventoryEmpty(script: ChaosTemple) : Branch<ChaosTemple>(script, "Empty Inventory?") {
    override val successComponent: TreeComponent<ChaosTemple> = SetupInventory(script)
    override val failedComponent: TreeComponent<ChaosTemple> = DepositInventory(script)

    override fun validate(): Boolean {
        return Inventory.isEmpty() || (Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
                && Inventory.occupiedSlotCount() == 1)
    }
}