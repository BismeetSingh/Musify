/*
 * Copyright 2017 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.app.bissudroid.musify.interfaces

import android.support.annotation.IntDef

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy



abstract class PlaybackInfoListener {

    @IntDef(State.INVALID, State.PLAYING, State.PAUSED, State.RESET, State.COMPLETED)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class State {
        companion object {

           const val INVALID = -1
            const val PLAYING = 0
            const val PAUSED = 1
            const val RESET = 2
            const val COMPLETED = 3
        }
    }

    internal fun onLogUpdated(formattedMessage: String) {}

    internal fun onDurationChanged(duration: Int) {}

    internal fun onPositionChanged(position: Int) {}

    internal fun onStateChanged(@State state: Int) {}

    internal fun onPlaybackCompleted() {}

    companion object {

        fun convertStateToString(@State state: Int): String {
            val stateString: String
            when (state) {
                State.COMPLETED -> stateString = "COMPLETED"
                State.INVALID -> stateString = "INVALID"
                State.PAUSED -> stateString = "PAUSED"
                State.PLAYING -> stateString = "PLAYING"
                State.RESET -> stateString = "RESET"
                else -> stateString = "N/A"
            }
            return stateString
        }
    }
}