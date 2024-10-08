package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class DepositInventory(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Depositing Inventory") {
    override fun execute() {
        if (Bank.depositInventory())
            Condition.wait({ Inventory.isEmpty() }, Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 13)

        // We only do this once upon script start
        if (Variables.depositEquipment && (Equipment.stream().isEmpty() || (Bank.depositEquipment()
            && Condition.wait({ Equipment.stream().isEmpty() },
                Condition.sleep(Random.nextGaussian(170, 250, 200, 50.0)), 13))))
            Variables.depositEquipment = false
    }
}