package com.jay.chaostemple

object Variables {
    var oneTicking = false
    var stopAtLvl = 99
    var stopAfterMinutes = 0
    var lastKnownPrayerXp = 0
    var timeSinceLastXpDrop: Long = 0

    var protectItem = true
    var depositEquipment = false

    var boneType = "Dragon Bones"

    var altarTileMatrix = Constants.ALTAR_TILE.matrix()
    var suicideTileMatrix = Constants.SUICIDE_TILE.matrix()

    // antipk stuff
    var worldId = 0
    var timeUntilNextLogout: Long = 0
    var escapePker = false
}