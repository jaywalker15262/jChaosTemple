package com.jay.chaostemple.leaf

import com.jay.chaostemple.Constants
import com.jay.chaostemple.LoggingService
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Objects
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class OfferBones(script: Script) : Leaf<Script>(script, "Offering Bones") {
    override fun execute() {
        var bone = Inventory.stream().name(Constants.boneType).first()
        for (n in 1..10) {
            if (Script.antiPkingCheck())
                return
            else if (bone.valid())
                break

            Condition.sleep(50)
            bone = Inventory.stream().name(Constants.boneType).first()
        }

        if (!bone.valid())
            return

        var altar = Objects.stream().within(20).name("Chaos altar").first()
        for (n in 1..10) {
            if (Script.antiPkingCheck())
                return
            else if (bone.valid())
                break

            Condition.sleep(50)
            altar = Objects.stream().within(20).name("Chaos altar").first()
        }

        if (!altar.valid())
            return
        else for (n in 1..10) {
            if (Script.antiPkingCheck())
                return
            if (bone.interact("Use")) {
                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                if (Script.antiPkingCheck())
                    return

                break
            }

            Condition.sleep(50)
        }

        val prayerXp = Skills.experience(Skill.Prayer)
        for (n in 1..5) {
            if (Script.antiPkingCheck())
                return
            else if (prayerXp != Skills.experience(Skill.Prayer) || (altar.interact("Use")
                        && Condition.wait({ prayerXp != Skills.experience(Skill.Prayer) || Script.antiPkingCheck() }, 50, 60))) {
                Constants.timeSinceLastXpDrop = ScriptManager.getRuntime(true) + 3000
                break
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (Constants.escapePker)
            return

        for (n in 1..3) {
            if (Script.antiPkingCheck())
                return
            else if (Game.tab(Game.Tab.LOGOUT))
                break

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (!Condition.wait({ Game.tab() == Game.Tab.LOGOUT || Script.antiPkingCheck() }, 50, 50))
            LoggingService.severe("We were unable to open up the logout tab after starting to offer bones at he altar.")
        else if (Constants.escapePker)
            return
    }
}