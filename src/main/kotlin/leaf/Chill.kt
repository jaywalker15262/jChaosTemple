package com.jay.chaostemple.leaf

import com.jay.chaostemple.ChaosTemple
import org.powbot.api.script.tree.Leaf

class Chill(script: ChaosTemple) : Leaf<ChaosTemple>(script, "Chillin") {
    override fun execute() {
        // No need to sleep here, poll() is on 50ms delay loop.
    }
}