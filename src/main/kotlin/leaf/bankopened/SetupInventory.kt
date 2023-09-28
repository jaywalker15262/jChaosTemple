package com.jay.chaostemple.leaf.bankopened

import com.jay.chaostemple.Variables
import com.jay.chaostemple.Constants
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.rt4.Bank
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class SetupInventory(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Setting Up Inventory") {
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

        if (Inventory.stream().name(*Constants.BURNING_AMULETS).count().toInt() == 0) {
            val bankAmulet = Bank.stream().name(*Constants.BURNING_AMULETS).first()
            if (!bankAmulet.valid()) {
                script.severe("Could not find any Burning amulets in the bank.")
                ScriptManager.stop()
                return
            }

            // Support for noted bones.
            if (Variables.notedMode && Bank.withdrawModeNoted() && (!Bank.withdrawModeNoted(false)
                        || !Condition.wait({ !Bank.withdrawModeNoted() }, 50, 80))) {
                script.info("Failed to turn off withdraw-noted-mode.")
                return
            }

            if (!Bank.withdraw(bankAmulet, 1)) {
                script.info("Failed to withdraw a Burning amulet.")
                return
            }

            if (!Condition.wait({ Inventory.stream().name(bankAmulet.name()).count().toInt() != 0 }, 50, 80)) {
                script.info("Failed to find a Burning amulet in our inventory.")
                return
            }
        }

        if (Inventory.stream().name(Variables.boneType).count().toInt() != Variables.boneCount + 1) {
            val bankBones = Bank.stream().name(Variables.boneType).first()
            if (!bankBones.valid()) {
                script.severe("Could not find any bones in the bank.")
                ScriptManager.stop()
                return
            }

            // Support for noted bones.
            val invCoins = Inventory.stream().name("Coins").first()
            var invCoinAmount = 0
            if (invCoins.valid())
                invCoinAmount = invCoins.stackSize()

            if (Variables.notedMode && (invCoinAmount < (Variables.notedAmount * 50)
                        || Inventory.stream().name(bankBones.name())
                    .firstOrNull { it.stackSize() == Variables.notedAmount } != null)) {
                if (bankBones.stackSize() < Variables.notedAmount + 25) {
                    script.severe("Could not find enough bones in the bank for another trip.")
                    ScriptManager.stop()
                    return
                }

                if (invCoinAmount < (Variables.notedAmount * 50)) {
                    val coins = Bank.stream().name("Coins").first()
                    if (!coins.valid() || coins.stackSize() < ((Variables.notedAmount * 50) - invCoinAmount)) {
                        script.severe("Could not find enough money in the bank for our noted bones.")
                        ScriptManager.stop()
                        return
                    }

                    if (!Bank.withdraw(coins, (Variables.notedAmount * 50) - invCoinAmount)) {
                        script.info("Failed to withdraw coins.")
                        return
                    }

                    if (!Condition.wait({ Inventory.stream().name(coins.name()).first()
                        .stackSize() == ((Variables.notedAmount * 50) - invCoinAmount) }, 50, 80)) {
                        script.info("Failed to find enough coins in our inventory for another trip.")
                        return
                    }
                }

                if (!Bank.withdrawModeNoted() && (!Bank.withdrawModeNoted(true)
                            || !Condition.wait({ Bank.withdrawModeNoted() }, 50, 80))) {
                    script.info("Failed to turn on withdraw-noted-mode.")
                    return
                }

                if (!Bank.withdraw(bankBones, Variables.notedAmount)) {
                    script.info("Failed to withdraw bones.")
                    return
                }

                if (!Condition.wait({ Inventory.stream().name(bankBones.name())
                    .firstOrNull { it.stackSize() == Variables.notedAmount } != null }, 50, 80)) {
                    script.info("Failed to find enough noted bones in our inventory for another trip.")
                    return
                }
            }
            else if (bankBones.stackSize() < 27) {
                script.severe("Could not find enough bones in the bank for another trip.")
                ScriptManager.stop()
                return
            }

            // Support for noted bones.
            if (Variables.notedMode && Bank.withdrawModeNoted() && (!Bank.withdrawModeNoted(false)
                        || !Condition.wait({ !Bank.withdrawModeNoted() }, 50, 80))) {
                script.info("Failed to turn off withdraw-noted-mode.")
                return
            }

            if (!Bank.withdraw(bankBones, Variables.boneCount)) {
                script.info("Failed to withdraw bones.")
                return
            }

            if (!Condition.wait({ Inventory.stream().name(
                    bankBones.name()).count().toInt() == Variables.boneCount + 1 }, 50, 80)) {
                script.info("Failed to find enough bones in our inventory for another trip.")
                return
            }
        }
    }
}