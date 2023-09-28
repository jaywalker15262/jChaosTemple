package com.jay.chaostemple.helpers

import org.powbot.api.Condition
import org.powbot.api.Interactable
import org.powbot.api.Nameable
import org.powbot.api.Random
import org.powbot.api.rt4.Inventory
import org.powbot.api.rt4.Item
import java.util.logging.Logger

object ItemHelper {
    val logger = Logger.getLogger(this.javaClass.simpleName)

    fun <T> Item.useOnExtended(interactable: T, useMenu: Boolean? = null, useMenu2: Boolean? = null)
            : Boolean where T: Interactable, T: Nameable {
        if (name().isBlank())
            return false

        val selectedItem = Inventory.selectedItem()
        if (!selectedItem.valid() || selectedItem.name() != name()) {
            logger.info("JayLOGS: No selected item found or wrong selected item found.")
            if (selectedItem.valid()) {
                logger.info("JayLOGS: Wrong selected item found, attempting to de-select it.")
                if (!Inventory.selectedItem().click()
                    || !Condition.wait({ !Inventory.selectedItem().valid() }, 50, 20))
                    return false
            }

            if (useMenu != null) {
                if (!interact("Use", useMenu))
                    return false
            }
            else if (!interact("Use"))
                return false

            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
        }

        if (useMenu2 != null) {
            if (!interactable.interact("Use", "${name()} -> ${interactable.name()}", useMenu2)) {
                Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
                return false
            }

            return true
        }


        if (!interactable.interact("Use", "${name()} -> ${interactable.name()}")) {
            Condition.sleep(Random.nextGaussian(170, 250, 200, 20.0))
            return false
        }

        return true
    }
}