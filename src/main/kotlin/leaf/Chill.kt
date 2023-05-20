package com.jay.chaostemple.leaf

import com.jay.chaostemple.Script
import org.powbot.api.Condition
import org.powbot.api.script.tree.Leaf

class Chill(script: Script) : Leaf<Script>(script, "Chillin") {
    override fun execute() {
        Condition.sleep(50)
    }
}