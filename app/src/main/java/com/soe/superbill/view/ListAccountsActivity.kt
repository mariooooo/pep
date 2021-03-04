package com.soe.superbill.view


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.soe.superbill.App
import com.soe.superbill.Globals
import com.soe.superbill.R
import com.soe.superbill.mvvm.Status
import com.soe.superbill.mvvm.viewModel.ListAccountViewModel
import com.soe.superbill.view.adapter.ShortListAdapter
import kotlinx.android.synthetic.main.account_list.*
import java.lang.NumberFormatException

class ListAccountsActivity : BaseActivity()  {
    private lateinit var listAccountViewModel: ListAccountViewModel
    private val shortListAdapter = ShortListAdapter(this, arrayListOf())
    lateinit var menuRefresh:MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_list)
        initToolbar(getString(R.string.toolbar_title_main),false, true)


        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    val token = task.result?.token

                    println("token: $token")
                })

        FirebaseMessaging.getInstance().subscribeToTopic("test")
                .addOnCompleteListener { task ->
                    var msg = "ok"
                    if (!task.isSuccessful) {
                        msg = "fail"
                    }
                }

        val pref = this.getSharedPreferences(Globals.APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)
        val login: String = pref.getString(Globals.APP_PREFERENCES_LOGIN, "log")!!
        FirebaseCrashlytics.getInstance().setUserId(login)
        swipeRefreshLayout.setColorSchemeColors(resources.getColor(R.color.blue))
        swipeRefreshLayout.setOnRefreshListener(OnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            onOptionsItemSelected(menuRefresh)
        })


        account_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = shortListAdapter
        }

        listAccountViewModel = ViewModelProvider(this).get(ListAccountViewModel::class.java)
        observeGetPosts()
        listAccountViewModel.getList()

    }

    private fun observeGetPosts() {
        listAccountViewModel.simpleLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    load.visibility = View.VISIBLE
                    account_list.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    shortListAdapter.updateShortList(it.data!!.lst_ls)
                    load.visibility = View.GONE
                    account_list.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
                Status.ERROR -> {
                    println("fav")
                    load.visibility = View.GONE
                    account_list.visibility = View.VISIBLE
                    swipeRefreshLayout.isRefreshing = false
                }
            }
        })
    }
    private fun observeDelete() {
        listAccountViewModel.deleteLiveData.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    load.visibility = View.VISIBLE
                    account_list.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    Toast.makeText(applicationContext, "${it.data!!.message}", Toast.LENGTH_LONG).show()
                    listAccountViewModel.getList()
                    load.visibility = View.GONE
                    account_list.visibility = View.VISIBLE
                }
                Status.ERROR -> Toast.makeText(applicationContext, "${it.error!!.err}", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Globals.GENERAL_INFO) {
            // Make sure the request was successful
            if (resultCode == Globals.REMOVE_ACCOUNT) {
                val a = data!!.getStringExtra(Globals.ARGUMENT_A)
                listAccountViewModel.delete(a!!)
                observeDelete()
            }
        }
        if(requestCode == Globals.ADD_ACCOUNT){
            if (resultCode == Globals.ADD_ACCOUNT_SUCCESS) {
                listAccountViewModel.getList()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        menuRefresh = menu.findItem(R.id.refresh)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        if (id == R.id.refresh) {
            listAccountViewModel.getList()
        }

        if (id == R.id.exit) {
            val editor = pref.edit()
            editor.putBoolean(Globals.APP_PREFERENCES_IS_LOGIN, false)
            editor.apply()
            finish()
            startActivity(Intent(this, ChoiceActivity::class.java))
        }

        return super.onOptionsItemSelected(item)

    }
    var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true
        val toast = Toast.makeText(this, resources.getString(R.string.double_click_back), Toast.LENGTH_SHORT)
        toast.show()
        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }
}