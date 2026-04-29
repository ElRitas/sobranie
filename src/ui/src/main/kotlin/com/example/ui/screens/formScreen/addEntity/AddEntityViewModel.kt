package com.example.ui.screens.formScreen.addEntity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ui.mapping.ApuMapper
import com.example.ui.mapping.AuthorMapper
import com.example.ui.mapping.BbkMapper
import com.example.ui.mapping.BookMapper
import com.example.ui.mapping.PublisherMapper
import com.example.ui.network.ApuApi
import com.example.ui.network.AuthorApi
import com.example.ui.network.BbkApi
import com.example.ui.network.BookApi
import com.example.ui.network.PublisherApi
import com.example.ui.screens.formScreen.addEntity.addFormScreen.AddEntityType
import com.example.ui.screens.formScreen.form.ApuForm
import com.example.ui.screens.formScreen.form.AuthorForm
import com.example.ui.screens.formScreen.form.BbkForm
import com.example.ui.screens.formScreen.form.BookForm
import com.example.ui.screens.formScreen.form.PublisherForm
import com.example.ui.screens.formScreen.form.ValidatableForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEntityViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val authorApi: AuthorApi,
    private val publisherApi: PublisherApi,
    private val bbkApi: BbkApi,
    private val apuApi: ApuApi,
    private val bookMapper: BookMapper,
    private val authorMapper: AuthorMapper,
    private val publisherMapper: PublisherMapper,
    private val bbkMapper: BbkMapper,
    private val apuMapper: ApuMapper
) : ViewModel() {
    private val _state = MutableStateFlow<AddEntityState>(AddEntityState.Empty)
    val state: StateFlow<AddEntityState> = _state

    var selectedType by mutableStateOf(AddEntityType.AUTHOR)
    var submitted by mutableStateOf(false)


    var bookForm by mutableStateOf(BookForm())
    var authorForm by mutableStateOf(AuthorForm())
    var publisherForm by mutableStateOf(PublisherForm())
    var apuForm by mutableStateOf(ApuForm())
    var bbkForm by mutableStateOf(BbkForm())

    fun addEntity() {
        viewModelScope.launch {
            _state.value = AddEntityState.Loading
            try {
                val form = when (selectedType) {
                    AddEntityType.AUTHOR -> authorForm
                    AddEntityType.BOOK -> bookForm
                    AddEntityType.PUBLISHER -> publisherForm
                    AddEntityType.BBK -> bbkForm
                    AddEntityType.APU -> apuForm
                }
                if (form.isValid()) {
                    addEntity(form)
                    _state.value = AddEntityState.Success
                    clearFields()
                } else {
                    submitted = true
                    _state.value = AddEntityState.Error("Форма содержит ошибки")
                }
            } catch (e: Exception) {
                _state.value = AddEntityState.Error(e.message.toString())
            }
        }
    }

    fun changeEntityType() {
        submitted = false
        _state.value = AddEntityState.Empty
    }

    private fun clearFields() {
        bookForm = BookForm()
        authorForm = AuthorForm()
        publisherForm = PublisherForm()
        apuForm = ApuForm()
        bbkForm = BbkForm()
    }

    private suspend fun addEntity(form: ValidatableForm) {
        when (form) {
            is BookForm -> addBook(form)
            is AuthorForm -> addAuthor(form)
            is PublisherForm -> addPublisher(form)
            is BbkForm -> addBbk(form)
            is ApuForm -> addApu(form)
        }
    }

    private suspend fun addBook(bookForm: BookForm) {
        val bookModel = com.example.ui.screens.formScreen.mapping.BookMapper.toModel(bookForm)
        bookApi.createBook(bookMapper.toDto(bookModel))
    }

    private suspend fun addAuthor(authorForm: AuthorForm) {
        val authorModel =
            com.example.ui.screens.formScreen.mapping.AuthorMapper.toModel(authorForm)
        authorApi.createAuthor(authorMapper.toDto(authorModel))
    }

    private suspend fun addPublisher(publisherForm: PublisherForm) {
        val publisherModel =
            com.example.ui.screens.formScreen.mapping.PublisherMapper.toModel(publisherForm)
        publisherApi.createPublisher(publisherMapper.toDto(publisherModel))
    }

    private suspend fun addApu(apuForm: ApuForm) {
        val apuModel = com.example.ui.screens.formScreen.mapping.ApuMapper.toModel(apuForm)
        apuApi.createApu(apuMapper.toDto(apuModel))
    }

    private suspend fun addBbk(bbkForm: BbkForm) {
        val bbkModel = com.example.ui.screens.formScreen.mapping.BbkMapper.toModel(bbkForm)
        bbkApi.createBbk(bbkMapper.toDto(bbkModel))
    }
}
