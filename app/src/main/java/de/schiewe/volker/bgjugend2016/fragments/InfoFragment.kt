package de.schiewe.volker.bgjugend2016.fragments

import androidx.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import de.schiewe.volker.bgjugend2016.R
import de.schiewe.volker.bgjugend2016.database.DatabaseHelper
import de.schiewe.volker.bgjugend2016.helper.Analytics
import de.schiewe.volker.bgjugend2016.layout.StaticGridLayoutManager
import de.schiewe.volker.bgjugend2016.layout.YouthWorkerRecyclerViewAdapter
import de.schiewe.volker.bgjugend2016.models.GeneralData
import kotlinx.android.synthetic.main.fragment_info.*

class InfoFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (toolbar as Toolbar).title = getString(R.string.title_informations)
        activity?.let {
            val databaseHelper = DatabaseHelper(it)

            val adapter = YouthWorkerRecyclerViewAdapter()
            with(list_youth_worker) {
                this.layoutManager = StaticGridLayoutManager(context, 2)

                this.adapter = adapter
                databaseHelper.getGeneralData().observe(viewLifecycleOwner, Observer { generalData: GeneralData? ->
                    if (generalData != null) {
                        (adapter).setYouthWorkers(generalData.professionals)
                        youth_team.text = context.getString(R.string.current_youth_team, generalData.youthTeam)
                    }
                })
            }
        }
    }
    override fun onAttach(context: Context) {
         activity?.let {
             Analytics.setScreen(it, javaClass.simpleName)
         }
        super.onAttach(context)
    }

    companion object {
        @JvmStatic
        fun newInstance() = InfoFragment()
    }
}
