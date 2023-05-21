package com.jay.chaostemple.leaf

import com.jay.chaostemple.Script
import org.powbot.api.script.tree.Leaf

class Chill(script: Script) : Leaf<Script>(script, "Chillin") {
    override fun execute() {
        // No need to sleep here, poll() is on 50ms delay loop.
    }
}