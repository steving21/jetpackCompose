/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * This project and source code may use libraries or frameworks that are
 * released under various Open-Source licenses. Use of those libraries and
 * frameworks are governed by their own individual licenses.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.librarian.ui.readingList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animate
import androidx.compose.foundation.Icon
import androidx.compose.foundation.layout.ColumnScope.align
import androidx.compose.foundation.layout.RowScope.align
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raywenderlich.android.librarian.R
import com.raywenderlich.android.librarian.model.relations.ReadingListsWithBooks
import com.raywenderlich.android.librarian.ui.composeUi.DeleteDialog
import com.raywenderlich.android.librarian.ui.composeUi.LibrarianTheme
import com.raywenderlich.android.librarian.ui.composeUi.TopBar
import com.raywenderlich.android.librarian.ui.readingList.ui.AddReadingList
import com.raywenderlich.android.librarian.ui.readingList.ui.ReadingLists
import com.raywenderlich.android.librarian.ui.readingListDetails.ReadingListDetailsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReadingListFragment : Fragment() {

  private val readingListViewModel by viewModels<ReadingListViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).apply {
      setContent {
        LibrarianTheme {
          ReadingListContent()
        }
      }
    }
  }

  @Composable
  fun ReadingListContent() {
    Scaffold(
      topBar = { ReadingListTopBar() },
      floatingActionButton = { AddReadingListButton() }) {
      ReadingListContentWrapper()
    }
  }

  @Composable
  fun ReadingListContentWrapper() {
    val readingListsState by readingListViewModel.readingListsState.observeAsState(emptyList())
    val deleteListState by readingListViewModel.deleteReadingListState.observeAsState()

    val deleteList = deleteListState

    val isShowingAddList by readingListViewModel.isShowingAddReadingListState.observeAsState(false)

    Stack(
      modifier = Modifier
        .fillMaxSize()
        .align(Alignment.CenterVertically)
        .align(Alignment.CenterHorizontally)
    ) {
      ReadingLists(
        readingLists = readingListsState,
        onItemClick = { readingList -> onItemSelected(readingList) },
        onLongItemTap = { readingListViewModel.onDeleteReadingList(it) })

      if (isShowingAddList) {
        AddReadingList(
          onDismiss = { readingListViewModel.onDialogDismiss() },
          onAddList = { name ->
            readingListViewModel.addReadingList(name)
            readingListViewModel.onDialogDismiss()
          })
      }

      if (deleteList != null) {
        DeleteDialog(
          item = deleteList,
          message = stringResource(id = R.string.delete_message, deleteList.name),
          onDeleteItem = { readingList ->
            readingListViewModel.deleteReadingList(readingList)
            readingListViewModel.onDialogDismiss()
          },
          onDismiss = { readingListViewModel.onDialogDismiss() }
        )
      }
    }
  }

  @Composable
  fun AddReadingListButton() {
    val isShowingAddReadingList by readingListViewModel.isShowingAddReadingListState.observeAsState(false)
    val size = animate(target = if (isShowingAddReadingList) 0.dp else 56.dp)

    FloatingActionButton(modifier = Modifier.size(size),
      onClick = { readingListViewModel.onAddReadingListTapped() }
    ) {
      Icon(asset = Icons.Default.Add)
    }
  }

  @Composable
  fun ReadingListTopBar() {
    TopBar(title = stringResource(id = R.string.reading_lists_title))
  }

  private fun onItemSelected(readingList: ReadingListsWithBooks) {
    startActivity(ReadingListDetailsActivity.getIntent(requireContext(), readingList))
  }
}