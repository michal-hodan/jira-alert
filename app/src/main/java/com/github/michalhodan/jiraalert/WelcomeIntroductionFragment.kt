package com.github.michalhodan.jiraalert

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [WelcomeIntroductionFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [WelcomeIntroductionFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class WelcomeIntroductionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_introduction, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment WelcomeIntroductionFragment.
         */
        @JvmStatic
        fun newInstance() = WelcomeIntroductionFragment()
    }
}
