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
import java.util.logging.Logger

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
            "stopAtLvl", "Stop at lvl:",
            optionType = OptionType.INTEGER, defaultValue = "99"
        ),
        ScriptConfiguration(
            "bone", "Bones to offer:",
            optionType = OptionType.STRING, defaultValue = "Dragon bones",
            allowedValues = arrayOf("Dragon bones","Lava dragon bones","Dagannoth bones","Wyvern bones",
                "Big bones", "Superior dragon bones","Bones","Hydra bones","Babydragon bones")
        ),
        ScriptConfiguration(
            "protectItem", "Use Protect Item?",
            optionType = OptionType.BOOLEAN, defaultValue = "true"
        )
    ]
)

class Script : TreeScript() {
    private val logoutInCombatErrorMessage = "You can't log out until 10 seconds after the end of combat."

    @ValueChanged("stopAtLvl")
    fun stopAtLevelChanged(newValue: Int) {
        if (newValue > 0)
            Constants.stopAtLvl = newValue
    }

    @ValueChanged("bone")
    fun boneTypeChanged(newValue: String) {
        if (Constants.BONE_TYPES.contains(newValue))
            Constants.boneType = newValue
    }

    @ValueChanged("protectItem")
    fun protectItemChanged(newValue: Boolean) {
        Constants.protectItem = newValue
    }

    override val rootComponent: TreeComponent<*> by lazy {
        IsLoggedIn(this)
    }

    override fun onStart() {
        val p: Paint = PaintBuilder.newBuilder()
            .addString("Last Leaf:") { lastLeaf.name }
            .addString("Stop At Level: ") { Constants.stopAtLvl.toString() }
            .trackSkill(Skill.Prayer)
            .backgroundColor(Color.argb(255, 59,127,77))
            .build()
        addPaint(p)

        Constants.lastKnownPrayerXp = Skills.experience(Skill.Prayer)
        if (Equipment.stream().isNotEmpty())
            Constants.depositEquipment = true
    }

    companion object {
        fun antiPkingCheck(): Boolean {
            // Do not stop what we are doing if we are in combat, we cannot log out anyway.
            if (Inventory.stream().name(Constants.boneType).count() < 3
                || Players.local().inCombat() || Constants.timeUntilNextLogOut > ScriptManager.getRuntime(true))
                return false

            val playerCombatLevel = Players.local().combatLevel
            if (IsInWildy.yCoord > 3527)
                IsInWildy.wildyLevel = ((IsInWildy.yCoord - 3528) / 8) + 2

            Players.stream().notLocalPlayer().within(18).forEach {
                if ((it.combatLevel + IsInWildy.wildyLevel) >= playerCombatLevel) {
                    Constants.escapePker = true
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
            Constants.timeUntilNextLogOut = ScriptManager.getRuntime(true) + 10000
    }
}

object LoggingService {
    private val logger = Logger.getLogger(this.javaClass.name)
    fun info(message: String) {
        logger.info("JayLOGS: $message")
    }

    fun severe(message: String) {
        logger.severe("JayLOGS: $message")
    }
}

fun main(args: Array<String>) {
    ScriptUploader().uploadAndStart("jChaosTemple", "", "127.0.0.1:5565", true, false)
}