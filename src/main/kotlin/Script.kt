package com.jay.chaostemple

import com.google.common.eventbus.Subscribe
import com.jay.chaostemple.branch.IsInWildy
import com.jay.chaostemple.branch.IsLoggedIn
import org.powbot.api.Color
import org.powbot.api.event.MessageEvent
import org.powbot.api.rt4.Equipment
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Players
import org.powbot.api.rt4.Skills
import org.powbot.api.rt4.walking.model.Skill
import org.powbot.api.script.*
import org.powbot.api.script.paint.Paint
import org.powbot.api.script.paint.PaintBuilder
import org.powbot.api.script.tree.TreeComponent
import org.powbot.api.script.tree.TreeScript
import org.powbot.mobile.script.ScriptManager
import org.powbot.mobile.service.ScriptUploader

@ScriptManifest(
    name = "jChaosTemple",
    description = "Offers bones at the Chaos Temple in wildy.",
    version = "1.0.0",
    category = ScriptCategory.Prayer,
    author = "Jaywalker"
)
@ScriptConfiguration.List(
    [
        ScriptConfiguration(
            "stopAtLvl", "Stop at lvl(values >99 or <1 means it will not stop based on lvl):",
            optionType = OptionType.INTEGER, defaultValue = "99"
        ),
        ScriptConfiguration(
            "stopAfterMinutes", "Stop after X minutes(0, for the bot to not stop based on time):",
            optionType = OptionType.INTEGER, defaultValue = "0"
        ),
        ScriptConfiguration(
            "bone", "Bones to offer:",
            optionType = OptionType.STRING, defaultValue = "Dragon bones",
            allowedValues = arrayOf("Dragon bones","Lava dragon bones","Dagannoth bones","Wyvern bones",
                "Big bones", "Superior dragon bones","Bones","Hydra bones","Babydragon bones")
        ),
        ScriptConfiguration(
            "protectItem", "Use Protect Item?",
            optionType = OptionType.BOOLEAN, defaultValue = "false"
        )
    ]
)

class Script : TreeScript() {
    private val logoutInCombatErrorMessage = "You can't log out until 10 seconds after the end of combat."

    @ValueChanged("stopAtLvl")
    fun stopAtLevelChanged(newValue: Int) {
        Constants.STOP_AT_LEVEL = newValue
    }

    @ValueChanged("stopAfterMinutes")
    fun stopAfterMinutesChanged(newValue: Int) {
        Constants.STOP_AFTER_MINUTES = if (newValue > 0)
            newValue else 0
    }

    @ValueChanged("bone")
    fun boneTypeChanged(newValue: String) {
        if (Constants.BONE_TYPES.contains(newValue))
            Constants.BONE_TYPE = newValue
    }

    @ValueChanged("protectItem")
    fun protectItemChanged(newValue: Boolean) {
        Constants.PROTECT_ITEM = newValue
    }

    override val rootComponent: TreeComponent<*> by lazy {
        IsLoggedIn(this)
    }

    override fun onStart() {
        val p: Paint = PaintBuilder.newBuilder()
            .addString("Last Leaf:") { lastLeaf.name }
            .addString("Stop At Level: ") { Constants.STOP_AT_LEVEL.toString() }
            .trackSkill(Skill.Prayer)
            .backgroundColor(Color.argb(255, 59,127,77))
            .build()
        addPaint(p)

        Constants.LAST_KNOWN_PRAYER_XP = Skills.experience(Skill.Prayer)
        if (Equipment.stream().isNotEmpty())
            Constants.DEPOSIT_EQUIPMENT = true
    }

    fun info(message: String) {
        log.info("JayLOGS: $message")
    }

    fun severe(message: String) {
        log.severe("JayLOGS: $message")
    }

    companion object {
        fun antiPkingCheck(): Boolean {
            // Do not stop what we are doing if we are in combat, we cannot log out anyway.
            if (Inventory.stream().name(Constants.BONE_TYPE).count() < 3
                || Players.local().inCombat() || Constants.TIME_UNTIL_NEXT_LOGOUT > ScriptManager.getRuntime(true))
                return false

            val playerCombatLevel = Players.local().combatLevel
            if (IsInWildy.yCoord > 3527)
                IsInWildy.wildyLevel = ((IsInWildy.yCoord - 3528) / 8) + 2

            Players.stream().notLocalPlayer().within(18).forEach {
                if ((it.combatLevel + IsInWildy.wildyLevel) >= playerCombatLevel) {
                    Constants.ESCAPE_PKER = true
                    return true
                }
            }

            return false
        }
    }

    @Subscribe
    private fun message(messageEvent: MessageEvent) {
        // Ensure it's a game message not a player trying to mess it up
        if (messageEvent.sender.isNotEmpty())
            return
        else if (messageEvent.message == logoutInCombatErrorMessage)
            Constants.TIME_UNTIL_NEXT_LOGOUT = ScriptManager.getRuntime(true) + 10000
    }
}

fun main(args: Array<String>) {
    ScriptUploader().uploadAndStart("jChaosTemple", "", "127.0.0.1:5565", true, false)
}