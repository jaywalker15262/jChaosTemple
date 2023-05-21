package com.jay.chaostemple.leaf

import com.jay.chaostemple.jChaosTemple
import org.powbot.api.script.tree.Leaf

class Chill(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Chillin") {
    override fun execute() {
        // No need to sleep here, poll() is on 50ms delay loop.
    }
}