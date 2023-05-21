package com.jay.chaostemple.leaf.antipk

import com.jay.chaostemple.jChaosTemple
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.SettingsManager
import org.powbot.mobile.ToggleId

class LogIn(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Logging In") {
    override fun execute() {
        if (!SettingsManager.enabled(ToggleId.AutoLogin))
            SettingsManager.set(ToggleId.AutoLogin, true)
    }
}