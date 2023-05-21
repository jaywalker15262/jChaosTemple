package com.jay.chaostemple.branch

import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.antipk.LogIn
import com.jay.chaostemple.leaf.antipk.LogOut
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

/**
 *  The root node which is executed by the script
 */
class IsLoggedIn(script: jChaosTemple) : Branch<jChaosTemple>(script, "Logged In?") {
    override val successComponent: TreeComponent<jChaosTemple> = IsInWildy(script)
    override val failedComponent: TreeComponent<jChaosTemple> = LogIn(script)

    override fun validate(): Boolean {
        return Game.loggedIn()
    }
}

class IsInWildy(script: jChaosTemple) : Branch<jChaosTemple>(script, "In Wildy?") {
    override val successComponent: TreeComponent<jChaosTemple> = AntiPking(script)
    override val failedComponent: TreeComponent<jChaosTemple> = IsBankOpened(script)
    companion object {
        var wildyLevel: Int = 0
        var yCoord = 0
    }

    override fun validate(): Boolean {
        wildyLevel = 1
        yCoord = Players.local().y()
        if (yCoord > 3527)
            wildyLevel = ((yCoord - 3528) / 8) + 2

        return wildyLevel > 1
    }
}

class AntiPking(script: jChaosTemple) : Branch<jChaosTemple>(script, "Anti-pk check") {
    override val successComponent: TreeComponent<jChaosTemple> = LogOut(script)
    override val failedComponent: TreeComponent<jChaosTemple> = AtAltar(script)

    override fun validate(): Boolean {
        return jChaosTemple.antiPkingCheck()
    }
}