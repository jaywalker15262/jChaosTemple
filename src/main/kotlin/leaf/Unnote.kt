package com.jay.chaostemple.leaf

import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.helpers.CombatHelper
import com.jay.chaostemple.helpers.ItemHelper.useOnExtended
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf

class Unnote(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Unnoting bones") {
    override fun execute() {
        // Door handling.
        if (Constants.AREA_ALTAR.contains(Players.local())) {
            val altarDoor = Objects.stream().within(20).id(1524, 1525).first()
            if (!altarDoor.valid())
                return

            if (altarDoor.id() == 1524) {
                if (!altarDoor.inViewport()) {
                    Camera.turnTo(altarDoor.tile)
                    Condition.wait({ altarDoor.inViewport() }, 50, 50)
                }

                altarDoor.bounds(-54, -46, -160, -16, -32, 32)
                // Short sleep between attempts of opening the door.
                if (!altarDoor.interact("Open") || !Condition.wait({ !altarDoor.valid() }, 50, 50))
                    return
            }
        }

        val elderChaosDruid = Npcs.stream().within(15).name("Elder Chaos druid").first()
        if (!elderChaosDruid.valid()) {
            script.info("Failed to find the Elder Chaos druid to unnote our bones for us.")
            return
        }

        val bones = Inventory.stream().name(Variables.boneType).firstOrNull { it.noted() }
        if (bones == null || !bones.valid()) {
            script.info("Failed to find our noted bones to have them unnoted.")
            return
        }

        val bonesStackSize = bones.stackSize()
        if (bonesStackSize < 0) {
            script.info("Something went wrong with fetching the stacksize of our noted bones.")
            return
        }

        val unnotingAmt = if (bonesStackSize > 25) 25 else bonesStackSize
        var coins = Inventory.stream().name("Coins").first()
        if (!coins.valid()) {
            script.info("Failed to find any coins to unnote our bones.")
            return
        }

        val coinsStackSize = coins.stackSize()
        if (coinsStackSize < 0) {
            script.info("Something went wrong with fetching the stacksize of our coins.")
            return
        }

        if (coinsStackSize < (50 * unnotingAmt)) {
            script.info("Failed to find that we enough coins to unnote our bones.")
            return
        }

        if (!bones.useOnExtended(elderChaosDruid, useMenu = true, useMenu2 =true)
            || !Condition.wait({ Chat.chatting() || CombatHelper.antiPkingCheck() },
                Random.nextGaussian(270, 450, 300, 30.0), 17)) {
            script.info("Failed to find that we are chatting after using the bones on the Elder Chaos druid.")
            return
        }

        if (Variables.escapePker)
            return

        val cost = if (unnotingAmt < 25) 50 * unnotingAmt else 1250
        if (!Chat.completeChat("Exchange All: $cost coins")) {
            script.info("Failed to complete the conversation after talking to the Elder Chaos druid.")
            return
        }

        for (n in 1..17) {
            coins = Inventory.stream().name("Coins").first()
            if (!coins.valid())
                break

            val tempStackSize = coins.stackSize()
            if (tempStackSize < 0)
                continue

            if (tempStackSize < coinsStackSize || CombatHelper.antiPkingCheck())
                break

            Condition.sleep(Random.nextGaussian(270, 450, 300, 30.0))
        }
    }
}