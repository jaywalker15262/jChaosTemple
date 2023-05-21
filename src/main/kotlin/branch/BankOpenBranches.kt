package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import com.jay.chaostemple.leaf.OpenBank
import com.jay.chaostemple.leaf.TravelToAltar
import com.jay.chaostemple.leaf.bankopened.CloseBank
import com.jay.chaostemple.leaf.bankopened.DepositInventory
import com.jay.chaostemple.leaf.bankopened.SetupInventory
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

class HaveInventory(script: Script) : Branch<Script>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<Script> = TravelToAltar(script)
    override val failedComponent: TreeComponent<Script> = OpenBank(script)

    override fun validate(): Boolean {
        Constants.ESCAPE_PKER = false
        return !Constants.DEPOSIT_EQUIPMENT && Inventory.stream().name(Constants.BONE_TYPE).count().toInt() == 27 &&
                Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class HaveInventoryTwo(script: Script) : Branch<Script>(script, "Have proper inventory?") {
    override val successComponent: TreeComponent<Script> = CloseBank(script)
    override val failedComponent: TreeComponent<Script> = IsInventoryEmpty(script)

    override fun validate(): Boolean {
        Constants.ESCAPE_PKER = false
        return !Constants.DEPOSIT_EQUIPMENT && Inventory.stream().name(Constants.BONE_TYPE).count().toInt() == 27 &&
                Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
    }
}

class IsBankOpened(script: Script) : Branch<Script>(script, "Bank open?") {
    override val successComponent: TreeComponent<Script> = HaveInventoryTwo(script)
    override val failedComponent: TreeComponent<Script> = HaveInventory(script)

    override fun validate(): Boolean {
        return Bank.opened()
    }
}

class IsInventoryEmpty(script: Script) : Branch<Script>(script, "Empty Inventory?") {
    override val successComponent: TreeComponent<Script> = SetupInventory(script)
    override val failedComponent: TreeComponent<Script> = DepositInventory(script)

    override fun validate(): Boolean {
        return Inventory.isEmpty() || (Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 1
                && Inventory.occupiedSlotCount() == 1)
    }
}