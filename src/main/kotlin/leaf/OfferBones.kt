package com.jay.chaostemple.leaf

import com.jay.chaostemple.Variables
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf

class OfferBones(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Offering Bones") {
    override fun execute() {
        val bone = Inventory.stream().name(Variables.boneType).first()
        if (!bone.valid())
            return

        val altar = Objects.stream().within(20).name("Chaos altar").first()
        if (!altar.valid()) {
            script.info("Failed to find the Chaos Temple altar.")
            return
        }
        // Protect item support
        else if (Variables.protectItem && Skills.realLevel(Skill.Prayer) >= 25 && Skills.level(Skill.Prayer) > 5
            && !Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)) {
            for (n in 1..10) {
                if (jChaosTemple.antiPkingCheck())
                    return
                else if (Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)
                    || (Prayer.prayer(Prayer.Effect.PROTECT_ITEM, true)
                            && Condition.wait({ Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM)
                            || jChaosTemple.antiPkingCheck() }, 50, 50)))
                    break

                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            }

            if (!Prayer.prayerActive(Prayer.Effect.PROTECT_ITEM))
                script.info("Failed to turn on Protect Item.")
        }

        for (n in 1..10) {
            if (jChaosTemple.antiPkingCheck())
                return
            if (bone.interact("Use")) {
                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                if (jChaosTemple.antiPkingCheck())
                    return

                break
            }

            Condition.sleep(50)
        }

        altar.bounds(-32, 32, -64, 0, -32, 32)
        val prayerXp = Skills.experience(Skill.Prayer)
        for (n in 1..5) {
            if (jChaosTemple.antiPkingCheck())
                return
            else if (!altar.inViewport())
                Camera.turnTo(altar)
            else if (prayerXp != Skills.experience(Skill.Prayer) || (altar.interact("Use")
                        && Condition.wait({ prayerXp != Skills.experience(Skill.Prayer) || jChaosTemple.antiPkingCheck() }, 50, 60))) {
                break
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (Variables.escapePker)
            return
        else for (n in 1..3) {
            if (jChaosTemple.antiPkingCheck())
                return
            else if (Game.tab(Game.Tab.LOGOUT))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (!Condition.wait({ Game.tab() == Game.Tab.LOGOUT || jChaosTemple.antiPkingCheck() }, 50, 50))
            script.severe("We were unable to open up the logout tab after starting to offer bones at he altar.")
    }
}