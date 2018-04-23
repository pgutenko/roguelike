package de.gutenko.roguelike.habittracker.data.habits

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Completable
import io.reactivex.Observable

class MemoryHabitRepository : HabitRepository {
    private var nextHabitId = 0
    private val habitsRelay =
        BehaviorRelay.createDefault(emptyList<Habit>())

    override fun observeUserHabits(userId: String): Observable<List<Habit>> {
        return habitsRelay.map { it.filter { it.userId == userId } }
    }

    override fun addHabit(userId: String, habitData: HabitData): Completable {
        return Completable.defer {
            val habit = Habit(
                nextHabitId.toString(),
                userId,
                habitData.name,
                habitData.playerUpdate,
                habitData.timeOfDay,
                0
            )

            habitsRelay.accept(habitsRelay.value + habit)

            nextHabitId++

            Completable.complete()
        }
    }
}