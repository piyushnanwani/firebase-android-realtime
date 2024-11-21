package com.piyushnanwani.mynovemberapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val TAG = "LoginScreen"

    private lateinit var firebaseTextView: TextView

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var authorEditText: EditText

    private lateinit var submitBtn: Button
    private lateinit var updateBtn: Button

    private lateinit var articleIdEditText: EditText
    private lateinit var newTitleEditText: EditText


    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = Firebase.auth

        titleEditText = findViewById(R.id.titleTv)
        contentEditText = findViewById(R.id.contentTV)
        authorEditText = findViewById(R.id.authorTV)

        articleIdEditText = findViewById(R.id.articleIDTV)
        newTitleEditText = findViewById(R.id.newTitleTV)

        submitBtn = findViewById(R.id.submitBtn)
        updateBtn = findViewById(R.id.updateBtn)

        submitBtn.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()
            val author = authorEditText.text.toString()

            val newsArticle = NewsArticle(title, content, author)
            saveNewsArticle(newsArticle)
        }

        updateBtn .setOnClickListener {
            val articleId = articleIdEditText.text.toString()
            val newTitle = newTitleEditText.text.toString()

            val content = contentEditText.text.toString()
            val author = authorEditText.text.toString()

            val newsArticle = NewsArticle(newTitle, content, author)
            updateNewsArticleById(articleId, newsArticle)
        }


        // Read from the database
//        myRef.addValueEventListener(object: ValueEventListener {
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = snapshot.getValue<String>()
//                Log.d(TAG, "Value is: " + value)
//
//                firebaseTextView.text = value
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.w(TAG, "Failed to read value.", error.toException())
//            }
//
//        })
    }

    fun signUp(email: String, password: String)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }

    fun signIn(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
//                    updateUI(null)
                }
            }
    }

    fun saveNewsArticle(article: NewsArticle) {
        val database = FirebaseDatabase.getInstance()

        val articlesRef = database.getReference("articles")

        val newArticleRef = articlesRef.push()

        newArticleRef.setValue(article)
            .addOnSuccessListener {
                Log.d(TAG, "News article saved!")
            }
            .addOnFailureListener{ err ->
                Log.d(TAG, "Failed to add News article & error ${err.message}")
            }
    }

    fun updateNewsArticleById(id: String, updatedArticle: NewsArticle) {
        val database = FirebaseDatabase.getInstance()
        val articleRef = database.getReference("articles").child(id)

        articleRef.setValue(updatedArticle)
            .addOnSuccessListener {
                Log.d(TAG, "News article was updated!")
            }
            .addOnFailureListener{ err ->
                Log.d(TAG, "Failed to updated News article & error ${err.message}")
            }
    }



}