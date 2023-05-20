package com.jay.chaostemple

import org.powbot.api.Area
import org.powbot.api.Point
import org.powbot.api.Tile
import org.powbot.api.rt4.TilePath
import org.powbot.api.rt4.World

object Constants {
    var stopAtLvl = 99
    var emptySlotCountCheck = 27
    var timeSinceLastXpDrop: Long = 0
    var lastKnownPrayerXp = 0
    var protectItem = true
    var boneType = "Dragon Bones"
    val BONE_TYPES = arrayOf("Dragon bones","Lava dragon bones","Dagannoth bones","Wyvern bones",
        "Superior dragon bones","Bones","Hydra bones","Babydragon bones")
    val BURNING_AMULETS = arrayOf("Burning amulet(5)", "Burning amulet(4)", "Burning amulet(3)",
        "Burning amulet(2)", "Burning amulet(1)")

    val lavaMazeTile = Tile(3028, 3842, 0)
    val altarTile = Tile(2948, 3821, 0)
    val suicideTile = Tile(2979, 3850, 0)
    val suicideTileMatrix = suicideTile.matrix()
    val AREA_LUMBY = Area(Tile(3203, 3227, 0), Tile(3226, 3205, 0))
    val AREA_ALTAR = Area(Tile(2948, 3822, 0), Tile(2957, 3819, 0))
    val chaosAltarPath = TilePath(arrayOf(Tile(3028, 3842, 0), Tile(3027, 3842, 0),
        Tile(3026, 3841, 0), Tile(3025, 3840, 0), Tile(3024, 3840, 0), Tile(3023, 3839, 0),
        Tile(3022, 3839, 0), Tile(3021, 3839, 0), Tile(3020, 3839, 0),
        Tile(3019, 3839, 0), Tile(3018, 3839, 0), Tile(3017, 3839, 0),
        Tile(3016, 3839, 0), Tile(3015, 3839, 0), Tile(3014, 3839, 0),
        Tile(3013, 3839, 0), Tile(3012, 3839, 0), Tile(3011, 3839, 0),
        Tile(3010, 3839, 0), Tile(3009, 3839, 0), Tile(3008, 3839, 0),
        Tile(3007, 3839, 0), Tile(3006, 3839, 0), Tile(3005, 3839, 0),
        Tile(3004, 3839, 0), Tile(3003, 3839, 0), Tile(3002, 3839, 0),
        Tile(3001, 3839, 0), Tile(3000, 3839, 0), Tile(2999, 3839, 0),
        Tile(2998, 3838, 0), Tile(2997, 3838, 0), Tile(2996, 3837, 0),
        Tile(2995, 3837, 0), Tile(2994, 3837, 0), Tile(2993, 3836, 0),
        Tile(2992, 3836, 0), Tile(2991, 3835, 0), Tile(2990, 3835, 0),
        Tile(2989, 3835, 0), Tile(2988, 3834, 0), Tile(2987, 3834, 0),
        Tile(2986, 3834, 0), Tile(2985, 3833, 0), Tile(2984, 3833, 0),
        Tile(2983, 3833, 0), Tile(2982, 3833, 0), Tile(2981, 3833, 0),
        Tile(2980, 3833, 0), Tile(2979, 3833, 0), Tile(2978, 3833, 0),
        Tile(2977, 3832, 0), Tile(2976, 3831, 0), Tile(2975, 3830, 0),
        Tile(2974, 3829, 0), Tile(2973, 3828, 0), Tile(2972, 3827, 0),
        Tile(2971, 3826, 0), Tile(2970, 3826, 0), Tile(2969, 3826, 0),
        Tile(2968, 3825, 0), Tile(2967, 3824, 0), Tile(2966, 3824, 0),
        Tile(2965, 3824, 0), Tile(2964, 3824, 0), Tile(2963, 3824, 0),
        Tile(2962, 3824, 0), Tile(2961, 3824, 0), Tile(2960, 3823, 0),
        Tile(2959, 3822, 0), Tile(2958, 3821, 0)))
    val suicidePath = TilePath(arrayOf(Tile(2948, 3821, 0), Tile(2949, 3821, 0),
        Tile(2950, 3821, 0), Tile(2951, 3821, 0), Tile(2952, 3821, 0),
        Tile(2953, 3821, 0), Tile(2954, 3821, 0), Tile(2955, 3821, 0),
        Tile(2956, 3821, 0), Tile(2957, 3821, 0), Tile(2958, 3821, 0),
        Tile(2958, 3822, 0), Tile(2959, 3823, 0), Tile(2960, 3824, 0),
        Tile(2961, 3825, 0), Tile(2962, 3826, 0), Tile(2963, 3827, 0),
        Tile(2964, 3828, 0), Tile(2965, 3828, 0), Tile(2966, 3829, 0),
        Tile(2967, 3830, 0), Tile(2968, 3831, 0), Tile(2969, 3832, 0),
        Tile(2970, 3833, 0), Tile(2971, 3834, 0), Tile(2972, 3835, 0),
        Tile(2973, 3836, 0), Tile(2974, 3837, 0), Tile(2975, 3838, 0),
        Tile(2976, 3839, 0), Tile(2977, 3840, 0), Tile(2978, 3841, 0),
        Tile(2978, 3842, 0), Tile(2978, 3843, 0), Tile(2978, 3844, 0),
        Tile(2979, 3845, 0), Tile(2979, 3846, 0), Tile(2979, 3847, 0),
        Tile(2979, 3848, 0), Tile(2979, 3849, 0), Tile(2979, 3850, 0)))

    // antipk stuff
    var worldId = 0
    var escapePker = false
    val loginScreenWorldHopperPoint = Point(150, 460)
    val worldSpecialtyFilter = arrayOf(World.Specialty.BOUNTY_HUNTER, World.Specialty.TARGET_WORLD,
        World.Specialty.FRESH_START, World.Specialty.HIGH_RISK, World.Specialty.BETA, World.Specialty.DEAD_MAN,
        World.Specialty.LEAGUE, World.Specialty.PVP_ARENA, World.Specialty.SKILL_REQUIREMENT,
        World.Specialty.SPEEDRUNNING, World.Specialty.TWISTED_LEAGUE)
    var timeUntilNextLogOut: Long = 0
}