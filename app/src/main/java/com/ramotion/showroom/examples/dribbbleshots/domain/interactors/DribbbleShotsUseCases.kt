package com.ramotion.showroom.examples.dribbbleshots.domain.interactors

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import com.ramotion.showroom.examples.dribbbleshots.domain.repository.DribbbleShotsRepository

class ObserveDribbbleShotsUseCase(private val dribbbleShotsRepository: DribbbleShotsRepository) {
  fun execute() = dribbbleShotsRepository.observeDribbbleShots()
}


class LoadNextDribbbleShotsPageUseCase(private val dribbbleShotsRepository: DribbbleShotsRepository) {
  fun execute(page: Int, perPage: Int = 20) = dribbbleShotsRepository.loadNextDribbbleShotsPage(page, perPage)
}


class GetDribbbleShotUseCase(private val dribbbleShotsRepository: DribbbleShotsRepository) {
  fun execute(id: Int) = dribbbleShotsRepository.getDribbbleShot(id)
}


class SaveDribbbleShotUseCase(private val dribbbleShotsRepository: DribbbleShotsRepository) {
  fun execute(dribbbleShot: DribbbleShot) = dribbbleShotsRepository.saveDribbbleShot(dribbbleShot)
}