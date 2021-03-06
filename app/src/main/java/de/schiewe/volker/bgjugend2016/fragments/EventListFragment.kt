package de.schiewe.volker.bgjugend2016.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.schiewe.volker.bgjugend2016.R
import de.schiewe.volker.bgjugend2016.database.DatabaseHelper
import de.schiewe.volker.bgjugend2016.interfaces.OnListItemSelectedListener
import de.schiewe.volker.bgjugend2016.layout.EventRecyclerViewAdapter
import de.schiewe.volker.bgjugend2016.models.BaseEvent
import de.schiewe.volker.bgjugend2016.views.SharedViewModel
import kotlinx.android.synthetic.main.fragment_event_list.*


class EventListFragment : Fragment() {
    private var itemSelectedListener: OnListItemSelectedListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_event_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar = toolbar as Toolbar
        activity?.let {
            (it as AppCompatActivity).setSupportActionBar(toolbar)
            toolbar.title = getString(R.string.title_events)
            val sharedViewModel = ViewModelProvider(it).get(SharedViewModel::class.java)
            event_list_progress.visibility = View.VISIBLE
            val adapter = EventRecyclerViewAdapter(itemSelectedListener, sharedViewModel, it)

            filter_button.setOnClickListener { itemSelectedListener?.onFilterButtonClicked() }
            // Set the adapter
            val databaseHelper = DatabaseHelper(it)
            with(list) {
                this.layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
                this.addItemDecoration(DividerItemDecoration(it, DividerItemDecoration.VERTICAL))
                databaseHelper.getEvents().observe(viewLifecycleOwner, Observer<List<BaseEvent>> { events ->
                    if (events != null) {
                        adapter.setEvents(events)
                        event_list_progress.visibility = View.GONE
                    }
                })
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListItemSelectedListener) {
            itemSelectedListener = context
        } else {
            throw RuntimeException(context::class.java.toString() + " must implement OnListItemSelectedListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        itemSelectedListener = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): EventListFragment = EventListFragment()
    }
}
