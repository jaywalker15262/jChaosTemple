package com.jay.chaostemple.helpers

import org.powbot.api.*
import org.powbot.api.rt4.Game
import org.powbot.api.rt4.LoginScreenWorldSwitcher
import org.powbot.api.rt4.World
import org.powbot.api.rt4.Worlds
import java.util.logging.Logger
import kotlin.math.roundToInt

object WorldHopper {
    private val worldHopperLogger = Logger.getLogger(this.javaClass.simpleName)     // remove if added to api.
    private val START_WORLD_NUMBER = 301
    private val DIMENSIONS = Game.dimensions()
    private val CURRENT_RESOLUTION = DIMENSIONS.width.toDouble() to DIMENSIONS.height.toDouble()
    private val ORIGINAL_RESOLUTION = Pair(910.0, 512.0)
    private val COORDINATES = mutableListOf<Pair<Int, Int>>()

    private var clickingPoint = Point(150, 460)

    init {
        // Fetch our coordinates.
        calculateCoordinates()

        // Adjust the coordinates accordingly based on resolution.
        if (CURRENT_RESOLUTION != ORIGINAL_RESOLUTION) {
            clickingPoint = scalePoint(clickingPoint)
            COORDINATES.forEachIndexed { index, coordinate ->
                COORDINATES[index] = scaleCoordinates(coordinate)
            }
        }
    }

    private fun calculateCoordinates() {
        val startCoordinate = Pair(177, 44)
        val xIncrement = 93
        val yIncrement = 19

        for (col in 0 until 7) {        // Number of columns
            val x = startCoordinate.first + (col * xIncrement)

            for (row in 0 until 24) {   // Number of rows
                val y = startCoordinate.second + (row * yIncrement)
                COORDINATES.add(Pair(x, y))
            }
        }
    }

    private fun scaleCoordinates(coordinate: Pair<Int, Int>): Pair<Int, Int> {
        val xRatio = CURRENT_RESOLUTION.first / ORIGINAL_RESOLUTION.first
        val yRatio = CURRENT_RESOLUTION.second / ORIGINAL_RESOLUTION.second

        return Pair((coordinate.first * xRatio).roundToInt(), (coordinate.second * yRatio).roundToInt())
    }

    private fun scalePoint(coordinate: Point): Point {
        val xRatio = CURRENT_RESOLUTION.first / ORIGINAL_RESOLUTION.first
        val yRatio = CURRENT_RESOLUTION.second / ORIGINAL_RESOLUTION.second

        return Point((coordinate.x * xRatio).roundToInt(), (coordinate.y * yRatio).roundToInt())
    }

    private fun findMissingWorlds(sequence: List<Int>): List<Int> {
        return (START_WORLD_NUMBER until START_WORLD_NUMBER + sequence.size).filter { it !in sequence }
    }

    private fun LoginScreenWorldSwitcher.open(): Boolean {
        if (isOpen())
            return true

        val random = Random
        return Input.tap(Point(clickingPoint.x + random.nextInt(0, 3), clickingPoint.y + random.nextInt(0, 3)))
                && Condition.wait({ isOpen() }, 50, 60)
    }

    fun LoginScreenWorldSwitcher.switchToWorldExtended(world: World): Boolean {
        // Open it first if it is not open yet.
        if (!open())
            return false

        val worldNumbers = mutableListOf<Int>()
        Worlds.stream().forEach { worldNumbers.add(it.id()) }
        val missingWorlds = findMissingWorlds(worldNumbers)
        val lastWorldNumber = START_WORLD_NUMBER + COORDINATES.size - 1 + missingWorlds.size
        if (world.number > lastWorldNumber) {
            // This should just be changed to Logger.info() if ever added to the API, we cannot do that from here,
            // hence worldHopperLogger was created which otherwise would be redundant.
            worldHopperLogger.info("World number is higher than the highest visible world number on screen.")
            return false
        }

        val pair = COORDINATES[worldNumbers.indexOf(world.number)]

        // Calculate random offsets between 0 and 2 pixels for both X and Y
        val random = Random
        val adjustedX = pair.first + random.nextInt(0, 3)
        val adjustedY = pair.second + random.nextInt(0, 3)

        if (!Input.tap(adjustedX, adjustedY))
            return false

        Condition.sleep(Random.nextGaussian(170, 350, 200, 20.0))
        return Input.tap(adjustedX, adjustedY)
    }
}