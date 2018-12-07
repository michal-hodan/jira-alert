package com.github.michalhodan.jiraalert

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.github.michalhodan.jira.sdk.Credentials
import com.github.michalhodan.jira.sdk.JIRA
import com.github.michalhodan.jira.sdk.http.HttpException
import kotlinx.android.synthetic.main.fragment_welcome_login.*
import kotlinx.android.synthetic.main.nav_header_user.*
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WelcomeLoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WelcomeLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WelcomeLoginFragment : Fragment() {

    private lateinit var onLogin: (String, String) -> Unit

    private lateinit var _url: () -> String

    private val url: String
        get() = _url()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment_welcome_login, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        password_input.setOnEditorActionListener { _, actionId, _ -> when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                    login(username_input.text.toString(), password_input.text.toString())
                true
            }
            else -> false
        } }
    }

    private fun login(username: String, password: String) {
        val credentials = Credentials.HttpAuth(url, username, password)

        val progress = ProgressDialog.show(context, "Authentication", "Securely logging you in", true, false)

        GlobalScope.launch(Dispatchers.IO) {

            try {
                JIRA.rest(credentials).myself.get()
            } catch (exception: HttpException.Unauthorized) {
                Toast.makeText(context, "Invalid credentials", Toast.LENGTH_SHORT).show()
                progress.dismiss()
                return@launch
            } catch (exception: HttpException.Forbidden) {
                Toast.makeText(context, "Manual login via web page is required", Toast.LENGTH_SHORT).show()
                progress.dismiss()
                return@launch
            }

           activity!!.runOnUiThread {
                progress.setMessage("Initiating")

                activity?.getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE)!!.edit().apply {
                    putString("url", credentials.url)
                    putString("authToken", credentials.authToken)
                    putString("username", username)
                    apply()
                }

                progress.dismiss()
                startActivity(Intent(activity, UserActivity::class.java))
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(url: () -> String, onLogin: (String, String) -> Unit) = WelcomeLoginFragment(). apply {
            _url = url
            this.onLogin = onLogin
        }
    }
}
