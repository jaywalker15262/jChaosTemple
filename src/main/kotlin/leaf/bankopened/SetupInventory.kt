package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Variables
import com.jay.chaostemple.Constants
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class SetupInventory(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Setting Up Inventory") {
    override fun execute() {
        Variables.escapePker = false
        if (Variables.stopAfterMinutes > 0) {
            val minutes: Int = (ScriptManager.getRuntime(true) / 60000).toInt()
            if (minutes >= Variables.stopAfterMinutes) {
                script.info("Script stopping due to runtime goal reached.")
                ScriptManager.stop()
                return
            }
        }
        if (Skills.realLevel(Skill.Prayer) >= Variables.stopAtLvl) {
            script.info("Script stopping due to prayer level goal reached.")
            ScriptManager.stop()
            return
        }
        else if (Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 0) {
            var bankAmulet = Bank.stream().name(*Constants.BURNING_AMULETS).first()
            for (n in 1..10) {
                if (bankAmulet.valid())
                    break

                Condition.sleep(50)
                bankAmulet = Bank.stream().name(*Constants.BURNING_AMULETS).first()
            }

            if (!bankAmulet.valid()) {
                script.severe("Could not find any Burning amulets in the bank.")
                ScriptManager.stop()
                return
            }
            else for (n in 1..10) {
                if (!bankAmulet.valid() || Inventory.stream().name(bankAmulet.name()).count().toInt() != 0
                    || (Bank.withdraw(bankAmulet, 1) && Condition.wait({
                        Inventory.stream()
                            .name(bankAmulet.name()).count().toInt() != 0
                    }, 50, 50))
                )
                    break

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }

            if (Inventory.stream().name(bankAmulet.name()).count().toInt() == 0) {
                script.severe("Failed to find a Burning amulet in our inventory.")
                ScriptManager.stop()
                return
            }
        }

        var bankBones = Bank.stream().name(Variables.boneType).first()
        for (n in 1..10) {
            if (bankBones.valid())
                break

            Condition.sleep(50)
            bankBones = Bank.stream().name(Variables.boneType).first()
        }

        if (!bankBones.valid()) {
            script.severe("Could not find any bones in the bank.")
            ScriptManager.stop()
            return
        }
        else if (bankBones.stackSize() < 27) {
            script.severe("Could not find enough bones in the bank for another trip.")
            ScriptManager.stop()
            return
        }
        else for (n in 1..10) {
            if (!bankBones.valid() || Inventory.stream().name(bankBones.name()).count().toInt() != 0
                || (Bank.withdraw(bankBones, 27) && Condition.wait({ Inventory.stream()
                    .name(bankBones.name()).count().toInt() == 27 }, 50, 50)))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (Inventory.stream().name(bankBones.name()).count().toInt() != 27) {
            script.severe("Failed to find enough bones in our inventory for another trip.")
            ScriptManager.stop()
            return
        }
    }
}