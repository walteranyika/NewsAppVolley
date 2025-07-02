package com.walter.volleyfetch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.JsonObjectRequest
import com.walter.volleyfetch.ui.theme.MyAppTheme
import androidx.core.net.toUri


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                NavigationBarMotherScreen()
            }
        }
    }
}

@Composable
fun NewsScreen() {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    val queue = remember { MySingleton.getInstance(context).requestQueue }
    var articlesList by remember { mutableStateOf(listOf<Article>()) }

    LaunchedEffect(Unit) {
        loading = true
        val url =
            "https://newsdata.io/api/1/latest?apikey=pub_417f9ce81b4b48ddae61417ec881b0fe&language=en"
        val newsRequest = JsonObjectRequest(url, { response ->
            loading = false
            Log.d("Response", "AllExpensesScreen: $response")
            val list = mutableListOf<Article>()
            val articles = response.getJSONArray("results")
            for (i in 0 until articles.length()) {
                val article = articles.getJSONObject(i)
                val title = article.getString("title")
                val link = article.getString("link")
                val description = article.getString("description")
                val pubDate = article.getString("pubDate")
                val imageUrl = article.getString("image_url")
                list.add(Article(title, link, description, pubDate, imageUrl))
            }
            articlesList = list
        }, { error ->
            loading = false
            Log.e("Error While Fetching", "AllExpensesScreen: ", error)
        })
        queue.add(newsRequest)
    }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (loading) {
                true -> Text("Loading")
                else -> LazyColumn(modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)) {
                    items(articlesList) { article ->
                        ArticleCard(article, context)
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleCard(article: Article, context: Context) {
    val body = when{
        article.description.length > 100 -> article.description.substring(0, 100) + "..."
        else -> article.description
    }
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        onClick = {
        val i = Intent(Intent.ACTION_VIEW)
        i.setData(article.link.toUri())
        context.startActivity(i)
    }) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(article.title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(10.dp))
            Text(body)
            Spacer(Modifier.height(6.dp))
            Text(article.pubDate)
        }
    }
}

@Composable
fun IncomeScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Income")
        }
    }
}

@Composable
fun GraphsScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Graphics")
        }
    }
}
