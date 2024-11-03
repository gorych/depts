package com.gorych.debts.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.gorych.debts.R

private const val ARG_TITLE = "title"
private const val ARG_ICON_RESOURCE_ID = "iconResourceId"
private const val ARG_ICON_CONTENT_DESCRIPTION = "iconContentDescription"

class TopBarFragment : Fragment() {

    private var title: String? = null
    private var rightIconResourceId: Int = -1
    private var rightIconContentDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            rightIconResourceId = it.getInt(ARG_ICON_RESOURCE_ID)
            rightIconContentDescription = it.getString(ARG_ICON_CONTENT_DESCRIPTION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top_bar, container, false)

        val tvTitle = view.findViewById<TextView>(R.id.top_bar_fragment_title)
        tvTitle.text = title

        val ivRightIcon = view.findViewById<ImageView>(R.id.top_bar_fragment_right_icon)
        ivRightIcon.setImageResource(rightIconResourceId)
        ivRightIcon.contentDescription = rightIconContentDescription

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(
            title: String,
            iconResourceId: Int,
            iconContentDescription: String = title
        ) =
            TopBarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putInt(ARG_ICON_RESOURCE_ID, iconResourceId)
                }
            }
    }
}