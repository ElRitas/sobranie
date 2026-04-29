package com.example.ui.screens.formScreen.editEntity

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
import com.example.ui.mapping.UserMapper
import com.example.ui.network.ApuApi
import com.example.ui.network.AuthorApi
import com.example.ui.network.BbkApi
import com.example.ui.network.BookApi
import com.example.ui.network.PublisherApi
import com.example.ui.network.UserApi
import com.example.ui.screens.formScreen.editEntity.editFormScreen.EditEntityType
import com.example.ui.screens.formScreen.form.ApuForm
import com.example.ui.screens.formScreen.form.AuthorForm
import com.example.ui.screens.formScreen.form.BbkForm
import com.example.ui.screens.formScreen.form.BookForm
import com.example.ui.screens.formScreen.form.PublisherForm
import com.example.ui.screens.formScreen.form.UserForm
import com.example.ui.screens.formScreen.form.ValidatableForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditEntityViewModel @Inject constructor(
    private val bookApi: BookApi,
    private val authorApi: AuthorApi,
    private val publisherApi: PublisherApi,
    private val bbkApi: BbkApi,
    private val apuApi: ApuApi,
    private val userApi: UserApi,
    private val bookMapper: BookMapper,
    private val authorMapper: AuthorMapper,
    private val publisherMapper: PublisherMapper,
    private val bbkMapper: BbkMapper,
    private val apuMapper: ApuMapper,
    private val userMapper: UserMapper
) : ViewModel() {
    private val _state = MutableStateFlow<EditEntityState>(EditEntityState.Empty)
    val state: StateFlow<EditEntityState> = _state

    var selectedType by mutableStateOf(EditEntityType.BOOK)
    var submitted by mutableStateOf(false)
    var buttonVisibility by mutableStateOf(false)


    var bookForm by mutableStateOf(BookForm())
    var authorForm by mutableStateOf(AuthorForm())
    var publisherForm by mutableStateOf(PublisherForm())
    var apuForm by mutableStateOf(ApuForm())
    var bbkForm by mutableStateOf(BbkForm())
    var userForm by mutableStateOf(UserForm())

    fun editEntity() {
        viewModelScope.launch {
            _state.value = EditEntityState.Loading
            val form = getSelectedForm()

            if (!form.isValid()) {
                _state.value = EditEntityState.Error("Форма содержит ошибки")
                submitted = true
                return@launch
            }

            try {
                editEntity(form)
                _state.value = EditEntityState.Success
            } catch (e: Exception) {
                submitted = true
                _state.value = EditEntityState.Error(e.message.orEmpty())
            }
        }
    }

    private fun getSelectedForm(): ValidatableForm = when (selectedType) {
        EditEntityType.AUTHOR -> authorForm
        EditEntityType.BOOK -> bookForm
        EditEntityType.PUBLISHER -> publisherForm
        EditEntityType.BBK -> bbkForm
        EditEntityType.APU -> apuForm
        EditEntityType.USER -> userForm
    }

    fun deleteEntity() {
        viewModelScope.launch {
            _state.value = EditEntityState.Loading
            try {
                when (selectedType) {
                    EditEntityType.AUTHOR -> {
                        authorApi.deleteAuthor(authorForm.id)
                    }

                    EditEntityType.BOOK -> {
                        bookApi.deleteBook(bookForm.id)
                    }

                    EditEntityType.PUBLISHER -> {
                        publisherApi.deletePublisher(publisherForm.id)
                    }

                    EditEntityType.BBK -> {
                        bbkApi.deleteBbk(bbkForm.id)

                    }

                    EditEntityType.APU -> {
                        apuApi.deleteApu(apuForm.id)
                    }

                    EditEntityType.USER -> {
                        userApi.deleteUser(userForm.id)
                    }
                }
                _state.value = EditEntityState.Success
            } catch (e: Exception) {
                _state.value = EditEntityState.Error(e.message.toString())
            }
        }
    }

    fun changeEntityType() {
        submitted = false
        _state.value = EditEntityState.Empty
    }

    private suspend fun editEntity(form: ValidatableForm) {
        when (form) {
            is BookForm -> editBook(form)
            is AuthorForm -> editAuthor(form)
            is PublisherForm -> editPublisher(form)
            is BbkForm -> editBbk(form)
            is ApuForm -> editApu(form)
            is UserForm -> editUser(form)
        }
    }


    // Update
    private suspend fun editBook(bookForm: BookForm) {
        val bookModel = com.example.ui.screens.formScreen.mapping.BookMapper.toModel(bookForm)
        bookApi.updateBook(bookMapper.toDto(bookModel))
    }

    private suspend fun editAuthor(authorForm: AuthorForm) {
        val authorModel =
            com.example.ui.screens.formScreen.mapping.AuthorMapper.toModel(authorForm)
        authorApi.updateAuthor(authorMapper.toDto(authorModel))
    }

    private suspend fun editPublisher(publisherForm: PublisherForm) {
        val publisherModel =
            com.example.ui.screens.formScreen.mapping.PublisherMapper.toModel(publisherForm)
        publisherApi.updatePublisher(publisherMapper.toDto(publisherModel))
    }

    private suspend fun editApu(apuForm: ApuForm) {
        val apuModel = com.example.ui.screens.formScreen.mapping.ApuMapper.toModel(apuForm)
        apuApi.updateApu(apuMapper.toDto(apuModel))
    }

    private suspend fun editBbk(bbkForm: BbkForm) {
        val bbkModel = com.example.ui.screens.formScreen.mapping.BbkMapper.toModel(bbkForm)
        bbkApi.updateBbk(bbkMapper.toDto(bbkModel))
    }

    private suspend fun editUser(userForm: UserForm) {
        val userModel = com.example.ui.screens.formScreen.mapping.UserMapper.toModel(userForm)
        userApi.updateUser(userMapper.toDto(userModel))
    }
}
