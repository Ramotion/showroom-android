package com.ramotion.showroom.examples.dribbbleshots.domain.repository

import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleUser
import io.reactivex.Observable

interface DribbbleUserRepository {

  fun getDribbbleUser(): Observable<DribbbleUser>
}