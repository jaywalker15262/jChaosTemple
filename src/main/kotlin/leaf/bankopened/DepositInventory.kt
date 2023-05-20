package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.script.tree.Leaf

class DepositInventory(script: Script) : Leaf<Script>(script, "Depositing Inventory") {
    override fun execute() {
        for (n in 1..10) {
            if (Inventory.isEmpty())
                break
            else if (Bank.depositInventory()) {
                if (Condition.wait({ Inventory.isEmpty() }, 50, 50)) {
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                    break
                }
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        // We only do this once upon script start
        if (Constants.depositEquipment) {
            for (n in 1..10) {
                if (Equipment.stream().isEmpty())
                    break
                else if (Bank.depositEquipment()) {
                    if (Condition.wait({ Equipment.stream().isEmpty() }, 50, 50)) {
                        Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                        break
                    }
                }

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }

            Constants.depositEquipment = false
        }
    }
}