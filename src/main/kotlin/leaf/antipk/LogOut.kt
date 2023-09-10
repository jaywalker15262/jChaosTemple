package com.jay.chaostemple.leaf.antipk

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.helpers.WorldHopper.switchToWorldExtended
import org.powbot.api.Condition
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.SettingsManager
import org.powbot.mobile.ToggleId
import org.powbot.mobile.script.ScriptManager

class LogOut(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Logging Out") {
    override fun execute() {
        // We are not in combat so let's attempt to log out instantly.
        SettingsManager.set(ToggleId.AutoLogin, false)
        Variables.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
        if (!tryLogOut()) {
            // Sleep a very briefly before attempting to press the logout button again.
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            return
        }

        val worlds = Worlds.stream().filtered { it.number < 400 && it.number != Variables.worldId && it.type == World.Type.MEMBERS
                && !Constants.WORLD_SPECIALITY_FILTER.contains(it.specialty) && it.population > 0 && it.population < 990 }
        if (worlds.isEmpty()) {
            script.info("Failed to find a list of worlds to hop to.")
            return
        }

        val newWorld = worlds.list().random()
        if (!newWorld.valid()) {
            script.info("Failed to find a valid world in our list.")
            return
        }

        if (!LoginScreenWorldSwitcher.switchToWorldExtended(newWorld)) {
            Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
            if (!LoginScreenWorldSwitcher.switchToWorldExtended(newWorld)) {
                script.info("Failed to select our new world(" + newWorld.number.toString()
                        + ") to hop to in the login-screen worldhopper.")

                if (!Condition.wait({ !LoginScreenWorldSwitcher.isOpen() }, 50, 100)) {
                    if (!LoginScreenWorldSwitcher.close()) {
                        script.info("Failed to close the login-screen worldhopper.")
                        return
                    }

                    if (!Condition.wait({ !LoginScreenWorldSwitcher.isOpen() }, 50, 100)) {
                        script.info("Failed to close the login-screen worldhopper.")
                        return
                    }

                    // Wait for 3(+5 from the condition wait) sec to make sure pker no longer being there.
                    Condition.sleep(3000)
                }
                else Condition.sleep(8000)     // Wait for 8 sec to make sure pker no longer being there.
            }
            else Variables.worldId = newWorld.number
        }
        else Variables.worldId = newWorld.number
    }

    private fun tryLogOut(): Boolean {
        if (ScriptManager.getRuntime(true) >= Variables.timeUntilNextLogout && Game.logout()) {
            if (!Condition.wait({ !Game.loggedIn() || Players.local().inCombat()
                        || Variables.timeUntilNextLogout > ScriptManager.getRuntime(true) }, 50, 60)) {
                script.info("Failed to find that we logged out.")
                return false
            }

            if (Players.local().inCombat() || Variables.timeUntilNextLogout > ScriptManager.getRuntime(true))
                return false

            return true
        }

        return false
    }
}