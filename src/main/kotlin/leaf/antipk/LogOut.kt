package com.jay.chaostemple.leaf.antipk

import com.jay.chaostemple.Constants
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.Input
import org.powbot.api.Random
import org.powbot.api.rt4.*
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.SettingsManager
import org.powbot.mobile.ToggleId
import org.powbot.mobile.script.ScriptManager

class LogOut(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Logging Out") {
    override fun execute() {
        // We are in combat so we cannot log out.
        if (Players.local().inCombat() || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true)) {
            Condition.sleep(50)
            return
        }

        // We are not in combat so let's attempt to log out instantly.
        SettingsManager.set(ToggleId.AutoLogin, false)
        if (!tryLogOut()) {
            if (ScriptManager.isStopping())
                return
            else if (Players.local().inCombat() || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true)) {
                Condition.wait{ Constants.AREA_LUMBY.contains(Players.local())
                        || ScriptManager.getRuntime(true) > Constants.TIME_UNTIL_NEXT_LOGOUT }
                return
            }

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            if (Game.loggedIn() && !Game.logout() && !Condition.wait({ !Game.loggedIn() || Players.local().inCombat()
                        || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true) }, 50, 60)) {
                script.severe("Failed to log out to avoid getting pked.")
                ScriptManager.stop()
                return
            }
            else if (Game.loggedIn()) {
                Condition.wait{ Constants.AREA_LUMBY.contains(Players.local())
                        || ScriptManager.getRuntime(true) > Constants.TIME_UNTIL_NEXT_LOGOUT }
                return
            }
        }

        var worlds = Worlds.stream().filtered { it.number < 400 && it.number != Constants.WORLD_ID && it.type == World.Type.MEMBERS
                && !Constants.WORLD_SPECIALITY_FILTER.contains(it.specialty) && it.population > 0 && it.population < 990 }
        for (n in 1..10) {
            if (worlds.isNotEmpty())
                break

            Condition.sleep(50)
            worlds = Worlds.stream().filtered { it.number < 400 && it.number != Constants.WORLD_ID && it.type == World.Type.MEMBERS
                    && !Constants.WORLD_SPECIALITY_FILTER.contains(it.specialty) && it.population > 0 && it.population < 990 }
        }

        if (worlds.isEmpty()) {
            script.info("Failed to find a list of worlds to hop to.")
            ScriptManager.stop()
            return
        }

        val newWorld = worlds.list().random()
        if (!newWorld.valid()) {
            script.info("Failed to find a valid world in our list.")
            ScriptManager.stop()
            return
        }

        Condition.sleep(Random.nextGaussian(1380, 2180, 1580, 80.0))
        Input.tap(Constants.LOGIN_SCREEN_WORLDHOPPER_POINT)
        if (Condition.wait({ LoginScreenWorldSwitcher.isOpen() }, 50 ,200)) {
            Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
            if (!LoginScreenWorldSwitcher.switchToWorld(newWorld)) {
                Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
                if (!LoginScreenWorldSwitcher.isOpen() || !LoginScreenWorldSwitcher.switchToWorld(newWorld)) {
                    script.severe("Failed to select our new world(" + newWorld.number.toString() + ") to hop to in the login-screen worldhopper.")
                    ScriptManager.stop()
                    return
                }
            }

            if (!Condition.wait({ !LoginScreenWorldSwitcher.isOpen() }, 50, 100)) {
                if (!LoginScreenWorldSwitcher.close()) {
                    Condition.sleep(Random.nextGaussian(570, 700, 650, 20.0))
                    if (!LoginScreenWorldSwitcher.close()) {
                        script.severe("Failed to close the login-screen worldhopper.")
                        ScriptManager.stop()
                        return
                    }
                }

                if (!Condition.wait({ !LoginScreenWorldSwitcher.isOpen() }, 50, 100)) {
                    script.severe("Failed to close the login-screen worldhopper.")
                    ScriptManager.stop()
                    return
                }

                Condition.sleep(25000)  // Wait for 25 sec to make sure pker no longer being there.
            }
            else Constants.WORLD_ID = newWorld.number
        }
        else {
            script.info("Failed to open up the login-screen worldhopper.")
            Condition.sleep(30000)     // Wait for 30 sec to make sure pker no longer being there.
        }

        Constants.ESCAPE_PKER = false
    }

    private fun tryLogOut(): Boolean {
        if (ScriptManager.getRuntime(true) >= Constants.TIME_UNTIL_NEXT_LOGOUT && Game.logout()) {
            if (!Condition.wait({ !Game.loggedIn() || Players.local().inCombat()
                        || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true) }, 50, 60)) {
                script.severe("Failed to find that we logged out.")
                ScriptManager.stop()
                return false
            }
            else if (Players.local().inCombat() || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true))
                return false

            return true
        }

        return false
    }
}