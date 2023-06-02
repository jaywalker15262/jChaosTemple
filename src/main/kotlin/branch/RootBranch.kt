package com.jay.chaostemple.branch

import com.jay.chaostemple.ChaosTemple
import com.jay.chaostemple.helpers.CombatHelper
import com.jay.chaostemple.leaf.antipk.LogIn
import com.jay.chaostemple.leaf.antipk.LogOut
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent

/**
 *  The root node which is executed by the script
 */
class IsLoggedIn(script: ChaosTemple) : Branch<ChaosTemple>(script, "Logged in?") {
    override val successComponent: TreeComponent<ChaosTemple> = IsInWildy(script)
    override val failedComponent: TreeComponent<ChaosTemple> = LogIn(script)

    override fun validate(): Boolean {
        return Game.loggedIn()
    }
}

class IsInWildy(script: ChaosTemple) : Branch<ChaosTemple>(script, "In wildy?") {
    override val successComponent: TreeComponent<ChaosTemple> = AntiPking(script)
    override val failedComponent: TreeComponent<ChaosTemple> = IsBankOpened(script)

    override fun validate(): Boolean {
        return Players.local().y() >= 3523
    }
}

class AntiPking(script: ChaosTemple) : Branch<ChaosTemple>(script, "Anti-pk check") {
    override val successComponent: TreeComponent<ChaosTemple> = LogOut(script)
    override val failedComponent: TreeComponent<ChaosTemple> = AtAltar(script)

    override fun validate(): Boolean {
        return CombatHelper.antiPkingCheck()
    }
}