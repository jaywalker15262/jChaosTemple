package com.jay.chaostemple.leaf

import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.helpers.CombatHelper
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class OfferBones(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Offering Bones") {
    override fun execute() {
        val bone = Inventory.stream().name(Variables.boneType).last()
        if (!bone.valid()) {
            script.info("Failed to find any bones to offer in our inventory.")
            return
        }

        val altar = Objects.stream().within(20).name("Chaos altar").first()
        if (!altar.valid()) {
            script.info("Failed to find the Chaos Temple altar.")
            return
        }

        // Protect item support
        if (Variables.protectItem && Skills.realLevel(Skill.Prayer) >= 25 && Skills.level(Skill.Prayer) > 5
            && !Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
            if (Prayer.prayer(Prayer.Effect.PROTECT_ITEM, true))
                Condition.wait({ Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM) || CombatHelper.antiPkingCheck() },
                    Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0)), 13)

            if (Variables.escapePker)
                return

            if (!Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM))
                script.info("Failed to turn on Protect Item.")
        }

        if (!bone.interact("Use", true)) {
            script.info("Failed to select the bone.")
            return
        }

        if (CombatHelper.antiPkingCheck())
            return

        // Short sleep after interaction.
        if (!Variables.oneTicking)
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))

        if (CombatHelper.antiPkingCheck())
            return

        altar.bounds(-32, 32, -64, 0, -32, 32)
        if (!altar.inViewport()) {
            Camera.turnTo(altar)
            Condition.wait({ altar.inViewport() }, 50, 50)
        }

        if (!Variables.oneTicking) {
            if (!Inventory.selectedItem().valid()) {
                script.info("Failed to select the bone.")
                return
            }
            if (altar.interact("Use")) {
                script.info("Failed to use the bone on the altar.")
                return
            }
        }
        else if (!altar.click())
            script.info("Failed to use the bone on the altar.")

        if (!Variables.oneTicking) {
            val prayerXp = Skills.experience(Skill.Prayer)
            if (!Condition.wait({ prayerXp != Skills.experience(Skill.Prayer)
                        || CombatHelper.antiPkingCheck() }, 50, 60)) {
                script.info("Failed to find that bone was used on altar.")
                return
            }

            Variables.timeSinceLastXpDrop = ScriptManager.getRuntime(true) + 2500
            if (Variables.escapePker)
                return

            // We just offered a bone, wait very briefly before trying to open the logout tab.
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            if (CombatHelper.antiPkingCheck())
                return

            for (n in 1..3) {
                if (Game.tab(Game.Tab.LOGOUT))
                    break

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                if (CombatHelper.antiPkingCheck())
                    return
            }

            if (!Condition.wait({ CombatHelper.antiPkingCheck() || Game.tab() == Game.Tab.LOGOUT }, 50, 50))
                script.info("We were unable to open up the logout tab after starting to offer bones at he altar.")
        }
        else Variables.timeSinceLastXpDrop = ScriptManager.getRuntime(true)
    }
}