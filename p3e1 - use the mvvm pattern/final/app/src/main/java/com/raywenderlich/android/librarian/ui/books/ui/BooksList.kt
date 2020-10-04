package com.raywenderlich.android.librarian.ui.books.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raywenderlich.android.librarian.model.relations.BookAndGenre

@Composable
fun BooksList(
  books: List<BookAndGenre> = emptyList(),
  onItemLongTap: (BookAndGenre) -> Unit = {}
) {

  LazyColumnFor(
    items = books,
    modifier = Modifier.padding(top = 16.dp)) { bookAndGenre ->
    BookListItem(bookAndGenre, onItemLongTap)
    Spacer(modifier = Modifier.size(2.dp))
  }
}

@Composable
fun BookListItem(
  bookAndGenre: BookAndGenre,
  onItemLongTap: (BookAndGenre) -> Unit
) {
  Card(
    modifier = Modifier
      .wrapContentHeight()
      .fillMaxWidth()
      .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
      .clickable(onClick = {}, indication = null, onLongClick = { onItemLongTap(bookAndGenre) }),
    elevation = 8.dp,
    border = BorderStroke(1.dp, MaterialTheme.colors.primary),
    shape = RoundedCornerShape(16.dp)) {

    Row(modifier = Modifier.fillMaxSize()) {
      Spacer(modifier = Modifier.width(16.dp))

      Column {
        Text(text = bookAndGenre.book.name,
          modifier = Modifier.padding(top = 16.dp),
          fontSize = 18.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colors.primary)

        Spacer(modifier = Modifier.height(2.dp))

        Text(
          fontSize = 16.sp,
          text = bookAndGenre.genre.name,
          fontStyle = FontStyle.Italic,
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = bookAndGenre.book.description,
          fontStyle = FontStyle.Italic,
          overflow = TextOverflow.Ellipsis,
          fontSize = 12.sp,
          modifier = Modifier.fillMaxHeight().padding(end = 16.dp),
          color = MaterialTheme.colors.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))
      }
    }
  }
}