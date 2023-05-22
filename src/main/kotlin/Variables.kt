package com.jay.chaostemple

object Variables {
    var stopAtLvl = 99
    var stopAfterMinutes = 0

    var protectItem = true
    var depositEquipment = false

    var boneType = "Dragon Bones"

    var ALTAR_TILE_MATRIX = Constants.ALTAR_TILE.matrix()
    var SUICIDE_TILE_MATRIX = Constants.SUICIDE_TILE.matrix()

    // antipk stuff
    var worldId = 0
    var timeUntilNextLogout: Long = 0
    var escapePker = false
}