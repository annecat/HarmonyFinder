/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.milleron.harmonyfinder.ui.harmonyfinderhome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import fr.milleron.harmonyfinder.data.HarmonyFinderModelRepository
import fr.milleron.harmonyfinder.ui.harmonyfinderhome.HarmonyFinderModelUiState.Error
import fr.milleron.harmonyfinder.ui.harmonyfinderhome.HarmonyFinderModelUiState.Loading
import fr.milleron.harmonyfinder.ui.harmonyfinderhome.HarmonyFinderModelUiState.Success
import javax.inject.Inject

@HiltViewModel
class HarmonyFinderModelViewModel @Inject constructor(
    private val harmonyFinderModelRepository: HarmonyFinderModelRepository
) : ViewModel() {

    val uiState: StateFlow<HarmonyFinderModelUiState> = harmonyFinderModelRepository
        .harmonyFinderModels.map<List<String>, HarmonyFinderModelUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addHarmonyFinderModel(name: String) {
        viewModelScope.launch {
            harmonyFinderModelRepository.add(name)
        }
    }
}

sealed interface HarmonyFinderModelUiState {
    object Loading : HarmonyFinderModelUiState
    data class Error(val throwable: Throwable) : HarmonyFinderModelUiState
    data class Success(val data: List<String>) : HarmonyFinderModelUiState
}
