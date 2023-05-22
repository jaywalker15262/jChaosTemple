package com.jay.chaostemple.branch

import com.jay.chaostemple.jChaosTemple
import com.jay.chaostemple.leaf.antipk.LogIn
import com.jay.chaostemple.leaf.antipk.LogOut
import org.powbot.api.rt4.Combat
import org.powbot.api.rt4.Game
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
    override val successComponent: TreeComponent<jChaosTemple> = IsBankOpened(script)
    override val failedComponent: TreeComponent<jChaosTemple> = AntiPking(script)

    override fun validate(): Boolean {
        return Combat.wildernessLevel() == -1
    }
}

class AntiPking(script: jChaosTemple) : Branch<jChaosTemple>(script, "Anti-pk check") {
    override val successComponent: TreeComponent<jChaosTemple> = LogOut(script)
    override val failedComponent: TreeComponent<jChaosTemple> = AtAltar(script)

    override fun validate(): Boolean {
        return jChaosTemple.antiPkingCheck()
    }
}