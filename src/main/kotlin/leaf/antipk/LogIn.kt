package com.jay.chaostemple.leaf.antipk

import com.jay.chaostemple.Constants
import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.SettingsManager
import org.powbot.mobile.ToggleId

class LogIn(script: Script) : Leaf<Script>(script, "Logging In") {
    override fun execute() {
        SettingsManager.set(ToggleId.AutoLogin, true)
        while (!Game.loggedIn())
            Condition.sleep(50)
        Constants.LAST_KNOWN_PRAYER_XP = Skills.experience(Skill.Prayer)
    }
}