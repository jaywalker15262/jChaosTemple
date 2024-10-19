package com.jay.chaostemple

import com.google.common.eventbus.Subscribe
import com.jay.chaostemple.branch.IsLoggedIn
import org.powbot.api.Color
import org.powbot.api.Condition
import org.powbot.api.event.MessageEvent
import org.powbot.api.event.MessageType
import org.powbot.api.rt4.*
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
            "bone", "Bones to offer:",
            optionType = OptionType.STRING, defaultValue = "Dragon bones",
            allowedValues = arrayOf("Dragon bones","Lava dragon bones","Dagannoth bones","Wyvern bones",
                "Big bones", "Superior dragon bones","Bones","Hydra bones","Babydragon bones")
        ),
        ScriptConfiguration(
            "oneTick", "1-tick bone offering? (Recommended: 1-tap mode enabled when using this for better xp rates)",
            optionType = OptionType.BOOLEAN, defaultValue = "true"
        ),
        ScriptConfiguration(
            "notedMode", "Bring noted bones?",
            optionType = OptionType.BOOLEAN, defaultValue = "true"
        ),
        ScriptConfiguration(
            "notedAmount", "Amount of noted bones to bring?",
            optionType = OptionType.INTEGER, defaultValue = "100", visible = true
        ),
        ScriptConfiguration(
            "stopAtLvl", "Stop at lvl(values >99 or <1 means it will not stop based on lvl):",
            optionType = OptionType.INTEGER, defaultValue = "99"
        ),
        ScriptConfiguration(
            "stopAfterMinutes", "Stop after X minutes(0, for the bot to not stop based on time):",
            optionType = OptionType.INTEGER, defaultValue = "0"
        ),
        ScriptConfiguration(
            "protectItem", "Use Protect Item?",
            optionType = OptionType.BOOLEAN, defaultValue = "false"
        )
    ]
)

class ChaosTemple : TreeScript() {
    private val logoutInCombatErrorMessage = "You can't log out until 10 seconds after the end of combat."

    @ValueChanged("bone")
    fun boneTypeChanged(newValue: String) {
        if (Constants.BONE_TYPES.contains(newValue))
            Variables.boneType = newValue
    }

    @ValueChanged("oneTick")
    fun oneTickChanged(newValue: Boolean) {
        Variables.oneTicking = newValue
    }

    @ValueChanged("notedMode")
    fun notedModeChanged(newValue: Boolean) {
        Variables.notedMode = newValue
        updateVisibility("notedAmount", newValue)
    }

    @ValueChanged("notedAmount")
    fun notedAmountChanged(newValue: Int) {
        if (newValue < 1)
            Variables.notedMode = false
        else Variables.notedAmount = newValue
    }

    @ValueChanged("stopAtLvl")
    fun stopAtLevelChanged(newValue: Int) {
        Variables.stopAtLvl = newValue
    }

    @ValueChanged("stopAfterMinutes")
    fun stopAfterMinutesChanged(newValue: Int) {
        Variables.stopAfterMinutes = if (newValue > 0)
            newValue else 0
    }

    @ValueChanged("protectItem")
    fun protectItemChanged(newValue: Boolean) {
        Variables.protectItem = newValue
    }

    override val rootComponent: TreeComponent<*> by lazy {
        IsLoggedIn(this)
    }

    override fun onStart() {
        Condition.sleep(1000)
        // We cannot use superior dragon bones below lvl 70 prayer.
        if (Variables.boneType == "Superior dragon bones" && Skills.level(Skill.Prayer) < 70) {
            ScriptManager.stop()
            return
        }

        val p: Paint = PaintBuilder.newBuilder()
            .addString("Last Leaf:") { lastLeaf.name }
            .addString("Stop At Level: ") { Variables.stopAtLvl.toString() }
            .trackSkill(Skill.Prayer)
            .backgroundColor(Color.argb(255, 59,127,77))
            .build()
        addPaint(p)

        Variables.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
        if (Equipment.stream().isNotEmpty())
            Variables.depositEquipment = true

        if (Variables.notedMode)
            Variables.boneCount = 25

        Variables.worldId = Worlds.current().number
    }

    fun info(message: String) {
        log.info("JayLOGS: $message")
    }

    fun severe(message: String) {
        log.severe("JayLOGS: $message")
    }

    @Subscribe
    private fun message(messageEvent: MessageEvent) {
        if (messageEvent.messageType != MessageType.Game)
            return

        if (messageEvent.message == logoutInCombatErrorMessage)
            Variables.timeUntilNextLogout = ScriptManager.getRuntime(true) + 10000
    }
}

fun main(args: Array<String>) {
    ScriptUploader().uploadAndStart("jChaosTemple", "", "emulator-5570", true, false)
}