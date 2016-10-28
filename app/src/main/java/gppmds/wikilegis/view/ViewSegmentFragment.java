package gppmds.wikilegis.view;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gppmds.wikilegis.R;
import gppmds.wikilegis.controller.BillController;
import gppmds.wikilegis.controller.DataDownloadController;
import gppmds.wikilegis.controller.SegmentController;
import gppmds.wikilegis.exception.BillException;
import gppmds.wikilegis.exception.SegmentException;
import gppmds.wikilegis.exception.VotesException;
import gppmds.wikilegis.model.Segment;

public class ViewSegmentFragment extends Fragment {
    private static Integer segmentId;
    private static Integer billId;
    private TextView likes;
    private TextView dislikes;
    private TextView segmentText;
    private TextView billText;
    private List<Segment> segmentList;
    private SegmentController segmentController;
    private List<Segment> segmentListAux= new ArrayList<>();
    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        segmentId = getArguments().getInt("segmentId");
        billId = getArguments().getInt("billId");

        setView(inflater, container);

        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        segmentController = SegmentController.getInstance(getContext());
        segmentList = SegmentController.getAllSegments();

        settingText();

        TabLayout tabs = (TabLayout) getActivity().findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        segmentListAux= SegmentController.getProposalsOfSegment(segmentList, segmentId);
        RecyclerViewAdapterContent content = new RecyclerViewAdapterContent(segmentListAux);
        Log.d("TAMANHO2", segmentListAux.size() + "");
        recyclerView.setAdapter(content);

        return view;
    }

    private void setView(final LayoutInflater inflater, final ViewGroup container) {
        DataDownloadController dataDownloadController =
                DataDownloadController.getInstance(getContext());

        final int WIFI = 0;
        final int MOBILE_3G = 1;
        final int NO_NETWORK = 2;

        int connectionType = dataDownloadController.connectionType();

        if(connectionType == WIFI || connectionType == MOBILE_3G) {
            view = inflater.inflate(R.layout.fragment_view_segment, container, false);
            likes = (TextView) view.findViewById(R.id.textViewNumberLike);
            dislikes = (TextView) view.findViewById(R.id.textViewNumberDislike);
            billText = (TextView) view.findViewById(R.id.titleBill);
            segmentText = (TextView) view.findViewById(R.id.contentSegment);
        } else if (connectionType == NO_NETWORK){
            view = inflater.inflate(R.layout.fragment_view_segment_offline, container, false);
            billText = (TextView) view.findViewById(R.id.titleBillOffline);
            segmentText = (TextView) view.findViewById(R.id.contentSegmentOffline);
        }

        recyclerView= (RecyclerView) view.findViewById(R.id.recycler_viewSegment);
    }

    private void settingText() {
        DataDownloadController dataDownloadController =
                DataDownloadController.getInstance(getContext());

        final int WIFI = 0;
        final int MOBILE_3G = 1;
        final int NO_NETWORK = 2;

        int connectionType = dataDownloadController.connectionType();

        try {
            //FIXME
            dislikes.setText("7");
            likes.setText("13");
            segmentText.setText(SegmentController.getSegmentById(segmentId, getContext()).getContent());
            billText.setText(BillController.getBillById(billId).getTitle());
        } catch (SegmentException e) {
            e.printStackTrace();
        } catch (BillException e) {
            e.printStackTrace();
        }
    }
}
