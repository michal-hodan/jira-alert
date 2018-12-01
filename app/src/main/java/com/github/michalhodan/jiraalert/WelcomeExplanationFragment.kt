package com.github.michalhodan.jiraalert

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import kotlinx.android.synthetic.main.fragment_welcome_explanation.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WelcomeExplanationFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WelcomeExplanationFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WelcomeExplanationFragment : Fragment() {

    private lateinit var onUrlSetup: (String) -> Unit

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater.inflate(R.layout.fragment_welcome_explanation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        url_input.setOnEditorActionListener { _, actionId, _ -> when (actionId) {
            EditorInfo.IME_ACTION_DONE -> {
                onUrlSubmit(url_input.text.toString())
                true
            }
            else -> false
        } }
    }

    private fun onUrlSubmit(url: String) {
        onUrlSetup(url_input.text.toString())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomeExplanationFragment.
         */
        @JvmStatic
        fun newInstance(onUrlSetup: (String) -> Unit) = WelcomeExplanationFragment().apply {
            this.onUrlSetup = onUrlSetup
        }
    }
}
