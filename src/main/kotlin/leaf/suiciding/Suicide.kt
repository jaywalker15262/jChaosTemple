package com.jay.chaostemple.leaf.suiciding

import com.jay.chaostemple.Constants
import com.jay.chaostemple.jChaosTemple
import org.powbot.api.Condition
import org.powbot.api.rt4.*
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.tree.Leaf
import org.powbot.mobile.script.ScriptManager

class Suicide(script: jChaosTemple) : Leaf<jChaosTemple>(script, "Suiciding") {
    override fun execute() {
        /* Chaos Fanatic might not always be alive and is sometimes killed or in combat with other players/bots.
        So as counter measure to make sure we die, make sure we wait for him to spawn and to attack him instead of
        waiting for us to timeout and the script ending*/
        var chaosFanatic = Npcs.stream().within(13).name("Chaos fanatic").filtered {
            it.distanceTo(Players.local()).toInt() <= 11 && it.inViewport() }.first()
        // Some users might use accounts with high stats that are really tanky, and so we should wait at least a full minute.
        for (n in 1..1200) {
            // Let's afk until Chaos Fanatic is there, so that we do not time out.
            while (Game.loggedIn() && Skills.level(Skill.Hitpoints) > 0
                && !Constants.AREA_LUMBY.contains(Players.local())  && Players.stream().interactingWithMe().isEmpty() &&
                (!chaosFanatic.valid() || !chaosFanatic.healthBarVisible())) {
                chaosFanatic = Npcs.stream().within(13).name("Chaos Fanatic").filtered {
                    it.distanceTo(Players.local()).toInt() <= 11 && it.inViewport() }.first()
                if (chaosFanatic.valid() && !chaosFanatic.healthBarVisible()) {
                    chaosFanatic.bounds(-16, 16, -16, -16, -16, 16)
                    if (chaosFanatic.click())
                        Condition.wait({ Players.local().healthBarVisible() && chaosFanatic.healthBarVisible() }, 50, 90)
                }

                Condition.sleep(100)
            }
            // We need to return if Chaos Fanatic is there, but we cannot attack it, so that we can worldhop.
            if (!Game.loggedIn() || Constants.AREA_LUMBY.contains(Players.local()) || Skills.level(Skill.Hitpoints) == 1
                || (chaosFanatic.valid() && Players.stream().interactingWithMe().isEmpty() &&
                        !Players.local().healthBarVisible()))
                return

            Condition.sleep(50)
        }

        // Some users might use accounts with high stats that are really tanky, and so we should wait at least a full minute.
        if (!Constants.AREA_LUMBY.contains(Players.local())) {
            script.severe("Failed to suicide.")
            ScriptManager.stop()
        }
    }
}