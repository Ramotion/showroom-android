package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot

data class DribbbleShotsState(
    val loading: Boolean = false,
    val shots: List<ShotsListItem> = emptyList(),
    val error: Throwable? = null
)


sealed class DribbbleShotsIntent {
  class GetNextDribbbleShotsPage(val page: Int) : DribbbleShotsIntent()
}


sealed class DribbbleShotsStateChange {
  object Idle : DribbbleShotsStateChange()
  object StartLoading : DribbbleShotsStateChange()
  object StartLoadingNextPage : DribbbleShotsStateChange()
  class DribbbleShotsReceived(val shots: List<DribbbleShot>) : DribbbleShotsStateChange()
  class Error(val error: Throwable) : DribbbleShotsStateChange()
  object HideError : DribbbleShotsStateChange()
}


sealed class ShotsListItem {
  object ShotsLoadingItem : ShotsListItem()
  class DribbbleShotItem(val shot: DribbbleShot) : ShotsListItem()
  object DribbblePlaceholder : ShotsListItem()
}