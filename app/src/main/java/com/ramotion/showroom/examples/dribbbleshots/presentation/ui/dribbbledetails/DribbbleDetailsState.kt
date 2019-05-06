package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot

data class DribbbleDetailsState(
    val loading: Boolean = false,
    val saveLoading: Boolean = false,
    val shotSaved: Boolean = false,
    val shot: DribbbleShot = DribbbleShot.EMPTY,
    val error: Throwable? = null
)


sealed class DribbbleDetailsIntent {
  class GetDribbbleShot(val id: Int) : DribbbleDetailsIntent()
  class SaveDribbbleShot(val shot: DribbbleShot) : DribbbleDetailsIntent()
}


sealed class DribbbleDetailsStateChange {
  object StartLoading : DribbbleDetailsStateChange()
  object StartSaveLoading : DribbbleDetailsStateChange()
  class DribbbleShotReceived(val shot: DribbbleShot) : DribbbleDetailsStateChange()
  object DribbbleShotSaved : DribbbleDetailsStateChange()
  class Error(val error: Throwable) : DribbbleDetailsStateChange()
  object HideError : DribbbleDetailsStateChange()
}