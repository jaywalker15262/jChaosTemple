package com.jay.chaostemple.branch

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import com.jay.chaostemple.leaf.antipk.LogIn
import com.jay.chaostemple.leaf.antipk.LogOut
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.script.tree.Branch
import org.powbot.api.script.tree.TreeComponent
import java.awt.geom.NoninvertibleTransformException

/**
 *  The root node which is executed by the script
 */
class IsLoggedIn(script: Script) : Branch<Script>(script, "Logged In?") {
    override val successComponent: TreeComponent<Script> = IsInWildy(script)
    override val failedComponent: TreeComponent<Script> = LogIn(script)

    override fun validate(): Boolean {
        return Game.loggedIn()
    }
}

class IsInWildy(script: Script) : Branch<Script>(script, "In Wildy?") {
    override val successComponent: TreeComponent<Script> = AntiPking(script)
    override val failedComponent: TreeComponent<Script> = HaveInventory(script)
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

class AntiPking(script: Script) : Branch<Script>(script, "Anti-pk check") {
    override val successComponent: TreeComponent<Script> = LogOut(script)
    override val failedComponent: TreeComponent<Script> = AtAltar(script)

    override fun validate(): Boolean {
        return Script.antiPkingCheck()
    }
}