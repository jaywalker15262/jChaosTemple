package com.jay.chaostemple

import com.google.common.eventbus.Subscribe
import com.jay.chaostemple.branch.IsLoggedIn
import org.powbot.api.Color
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

class ChaosTemple : TreeScript() {
    private val logoutInCombatErrorMessage = "You can't log out until 10 seconds after the end of combat."

    @ValueChanged("stopAtLvl")
    fun stopAtLevelChanged(newValue: Int) {
        Variables.stopAtLvl = newValue
    }

    @ValueChanged("stopAfterMinutes")
    fun stopAfterMinutesChanged(newValue: Int) {
        Variables.stopAfterMinutes = if (newValue > 0)
            newValue else 0
    }

    @ValueChanged("bone")
    fun boneTypeChanged(newValue: String) {
        if (Constants.BONE_TYPES.contains(newValue))
            Variables.boneType = newValue
    }

    @ValueChanged("protectItem")
    fun protectItemChanged(newValue: Boolean) {
        Variables.protectItem = newValue
    }

    override val rootComponent: TreeComponent<*> by lazy {
        IsLoggedIn(this)
    }

    override fun onStart() {
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
    ScriptUploader().uploadAndStart("jChaosTemple", "", "127.0.0.1:5565", true, false)
}