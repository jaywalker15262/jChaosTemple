package com.jay.chaostemple

import org.powbot.api.Area
import org.powbot.api.Point
import org.powbot.api.Tile
import org.powbot.api.rt4.TilePath
import org.powbot.api.rt4.World

object Constants {
    val BONE_TYPES = arrayOf("Dragon bones","Lava dragon bones","Dagannoth bones","Wyvern bones",
        "Big bones", "Superior dragon bones","Bones","Hydra bones","Babydragon bones")
    val BURNING_AMULETS = arrayOf("Burning amulet(5)", "Burning amulet(4)", "Burning amulet(3)",
        "Burning amulet(2)", "Burning amulet(1)")

    val LAVA_MAZE_TILE = Tile(3028, 3842, 0)
    val ALTAR_TILE = Tile(2948, 3821, 0)
    val SUICIDE_TILE = Tile(2982, 3848, 0)
    val LUMBY_STAIRS_TILE = Tile(3206, 3228, 0)

    val AREA_LUMBY = Area(Tile(3203, 3231, 0), Tile(3226, 3205, 0))
    val AREA_ALTAR = Area(Tile(2948, 3822, 0), Tile(2957, 3819, 0))

    val CHAOS_ALTAR_PATH = TilePath(arrayOf(Tile(3028, 3842, 0), Tile(3027, 3842, 0),
        Tile(3026, 3841, 0), Tile(3025, 3840, 0), Tile(3024, 3840, 0), Tile(3023, 3839, 0),
        Tile(3022, 3839, 0), Tile(3021, 3839, 0), Tile(3020, 3839, 0),
        Tile(3019, 3839, 0), Tile(3018, 3839, 0), Tile(3017, 3839, 0),
        Tile(3016, 3838, 0), Tile(3016, 3837, 0), Tile(3016, 3836, 0),
        Tile(3016, 3835, 0), Tile(3016, 3834, 0), Tile(3016, 3833, 0),
        Tile(3016, 3832, 0), Tile(3016, 3831, 0), Tile(3016, 3830, 0),
        Tile(3016, 3829, 0), Tile(3015, 3829, 0), Tile(3014, 3829, 0),
        Tile(3013, 3829, 0), Tile(3012, 3829, 0), Tile(3011, 3829, 0),
        Tile(3010, 3829, 0), Tile(3009, 3829, 0), Tile(3008, 3829, 0),
        Tile(3007, 3829, 0), Tile(3006, 3829, 0), Tile(3005, 3829, 0),
        Tile(3004, 3829, 0), Tile(3003, 3829, 0), Tile(3002, 3829, 0),
        Tile(3001, 3829, 0), Tile(3000, 3829, 0), Tile(2999, 3829, 0),
        Tile(2998, 3829, 0), Tile(2997, 3829, 0), Tile(2996, 3829, 0),
        Tile(2995, 3829, 0), Tile(2994, 3829, 0), Tile(2993, 3829, 0),
        Tile(2992, 3829, 0), Tile(2991, 3829, 0), Tile(2990, 3829, 0),
        Tile(2989, 3829, 0), Tile(2988, 3829, 0), Tile(2987, 3829, 0),
        Tile(2986, 3829, 0), Tile(2985, 3829, 0), Tile(2983, 3829, 0),
        Tile(2983, 3829, 0), Tile(2982, 3829, 0), Tile(2981, 3829, 0),
        Tile(2980, 3829, 0), Tile(2979, 3829, 0), Tile(2978, 3829, 0),
        Tile(2977, 3829, 0), Tile(2976, 3831, 0), Tile(2975, 3830, 0),
        Tile(2974, 3829, 0), Tile(2973, 3828, 0), Tile(2972, 3827, 0),
        Tile(2971, 3826, 0), Tile(2970, 3826, 0), Tile(2969, 3826, 0),
        Tile(2968, 3825, 0), Tile(2967, 3824, 0), Tile(2966, 3824, 0),
        Tile(2965, 3824, 0), Tile(2964, 3824, 0), Tile(2963, 3824, 0),
        Tile(2962, 3824, 0), Tile(2961, 3824, 0), Tile(2960, 3823, 0),
        Tile(2959, 3822, 0), Tile(2958, 3821, 0)))
    val SUICIDE_PATH = TilePath(arrayOf(Tile(2948, 3821, 0), Tile(2949, 3821, 0),
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
        Tile(2978, 3842, 0), Tile(2979, 3843, 0), Tile(2980, 3844, 0),
        Tile(2981, 3845, 0), Tile(2982, 3846, 0), Tile(2982, 3847, 0),
        Tile(2982, 3848, 0)))
    val LUMBY_BOTTOM_FLOOR_PATH = TilePath(arrayOf(Tile(3226, 3219, 0), Tile(3225, 3219, 0),
        Tile(3224, 3219, 0), Tile(3223, 3219, 0), Tile(3222, 3219, 0),
        Tile(3221, 3219, 0), Tile(3220, 3219, 0), Tile(3219, 3219, 0),
        Tile(3218, 3219, 0), Tile(3217, 3219, 0), Tile(3216, 3219, 0),
        Tile(3215, 3219, 0), Tile(3215, 3220, 0), Tile(3215, 3221, 0),
        Tile(3215, 3222, 0), Tile(3215, 3223, 0), Tile(3215, 3224, 0),
        Tile(3215, 3225, 0), Tile(3215, 3226, 0), Tile(3214, 3227, 0),
        Tile(3213, 3228, 0), Tile(3212, 3228, 0), Tile(3211, 3228, 0),
        Tile(3210, 3228, 0), Tile(3209, 3228, 0), Tile(3208, 3228, 0),
        Tile(3207, 3228, 0), Tile(3206, 3228, 0)))
    val LUMBY_TOP_FLOOR_PATH = TilePath(arrayOf(Tile(3206, 3229, 2), Tile(3206, 3228, 2),
        Tile(3206, 3227, 2), Tile(3206, 3226, 2), Tile(3206, 3225, 2),
        Tile(3206, 3224, 2), Tile(3206, 3223, 2), Tile(3206, 3222, 2),
        Tile(3206, 3221, 2), Tile(3206, 3220, 2), Tile(3206, 3219, 2),
        Tile(3207, 3219, 2), Tile(3208, 3219, 2), Tile(3208, 3220, 2)))

    // antipk stuff
    val LOGIN_SCREEN_WORLDHOPPER_POINT = Point(150, 460)
    val WORLD_SPECIALITY_FILTER = arrayOf(World.Specialty.BOUNTY_HUNTER, World.Specialty.TARGET_WORLD,
        World.Specialty.FRESH_START, World.Specialty.HIGH_RISK, World.Specialty.BETA, World.Specialty.DEAD_MAN,
        World.Specialty.LEAGUE, World.Specialty.PVP_ARENA, World.Specialty.SKILL_REQUIREMENT,
        World.Specialty.SPEEDRUNNING, World.Specialty.TWISTED_LEAGUE)
}