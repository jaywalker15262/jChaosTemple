package com.jay.chaostemple.helpers

import com.jay.chaostemple.Variables
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.mobile.script.ScriptManager

object CombatHelper{
    fun antiPkingCheck(): Boolean {
        // Do not stop what we are doing if we are in combat, we cannot log out anyway.
        if (Inventory.stream().name(Variables.boneType).count() < 3
            || Players.local().inCombat() || Variables.timeUntilNextLogout > ScriptManager.getRuntime(true))
            return false

        val playerCombatLevel = Players.local().combatLevel
        var wildyLevel = 1
        val yCoord = Players.local().y()
        if (yCoord > 3527)
            wildyLevel = ((yCoord - 3528) / 8) + 2

        if(Players.stream().notLocalPlayer().within(18).filtered {
                (it.combatLevel + wildyLevel) >= playerCombatLevel && (it.combatLevel - wildyLevel) <= playerCombatLevel
            }.isNotEmpty()) {
            Variables.escapePker = true
            return true
        }

        return false
    }
}