package com.jay.chaostemple.leaf.antipk

import com.jay.chaostemple.Variables
import com.jay.chaostemple.ChaosTemple
import org.powbot.api.Condition
import org.powbot.api.rt4.LoginScreenWorldSwitcher
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.SettingsManager
import org.powbot.mobile.ToggleId

class LogIn(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Logging In") {
    override fun execute() {
        if (LoginScreenWorldSwitcher.isOpen() && (!LoginScreenWorldSwitcher.close()
                    || !Condition.wait({ !LoginScreenWorldSwitcher.isOpen() }, 50, 100))) {
            script.info("Failed to close the login-screen worldhopper.")
            return
        }

        Variables.escapePker = false
        if (!SettingsManager.enabled(ToggleId.AutoLogin))
            SettingsManager.set(ToggleId.AutoLogin, true)
    }
}